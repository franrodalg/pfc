package dflibrary.middleware;

import com.linuxnet.jpcsc.*;

import dflibrary.library.CardType;
import dflibrary.library.DFLException;
import dflibrary.library.DFLException.ExType;
import dflibrary.utils.ba.BAUtils;

/**
 * 
 * @author Francisco Rodríguez Algarra
 *
 */
public class JPCSCComManager implements ComManager {

	@Override
	/**
	 * 
	 */
	public void scan() {
		
		this.context = new Context();
		
		try{
			context.EstablishContext(PCSC.SCOPE_SYSTEM, null, null);
		}catch(PCSCException e){
			throw convertException(e);
		}

	}

	@Override
	/**
	 * 
	 */
	public String[] listReaders() {

		if(context == null) 
			throw new DFLException(ExType.CONTEXT_NOT_INITIALIZED);
		
		String[] readers = null;
		
		try{
			readers = context.ListReaders();
		}catch(PCSCException e){
			throw convertException(e);
		}
		
		if((readers == null) || (readers.length == 0))
			throw new DFLException(ExType.NO_READERS_FOUND);
		
		return readers;

	}

	@Override
	/**
	 * 
	 */
	public void select(String readerName) {
		
		if(readerName == null) throw new NullPointerException();
		
		String[] readers = listReaders();
		
		if(!findReader(readerName, readers)) 
			throw new DFLException(ExType.READER_NOT_FOUND);
		
		this.reader = readerName;
		

	}

	/**
	 * 
	 * @param readerName
	 * @param readers
	 * @return
	 */
	private boolean findReader(String readerName, String[] readers){
		
		if((readerName == null) || (readers == null)) throw new NullPointerException();
		
		for(int i = 0; i < readers.length; i++)
		{
			if(readers[i].equals(readerName)) return true;
		}
		return false;
	}
	
	@Override
	/**
	 * 
	 */
	public void deselect() {
		
		this.reader = null;
		

	}

	@Override
	/**
	 * 
	 */
	public boolean isCardPresent() {
		
		if(this.reader == null) 
			throw new DFLException(ExType.NO_READER_SELECTED);
		
		if(this.context == null)
			throw new DFLException(ExType.CONTEXT_NOT_INITIALIZED);
		
		try{
		
			State state = new State(this.reader);
			State[] states = new State[1];
			states[0] = state;
			
			context.GetStatusChange(0, states);
			
			if((state.dwEventState & PCSC.STATE_PRESENT) != 0){
				return true;
			}
			
			return false;
		
		}catch(PCSCException e){
			throw convertException(e);
		}
	}	

	@Override
	/**
	 * 
	 */
	public boolean isCardPresent(String readerName) {
		
		if(readerName == null) throw new NullPointerException();
		
		select(readerName);
		
		boolean b = isCardPresent();
		
		deselect();
		
		return b;
	}

	
	@Override
	/**
	 * 
	 */
	public void waitForCard() {
		// TODO Auto-generated method stub

	}

	@Override
	/**
	 * 
	 */
	public void waitForCard(String readerName) {
		// TODO Auto-generated method stub

	}

	@Override
	/**
	 * 
	 */
	public void waitCardExtraction() {
		// TODO Auto-generated method stub

	}

	@Override
	/**
	 * 
	 */
	public void waitCardExtraction(String readerName) {
		// TODO Auto-generated method stub

	}

	@Override
	/**
	 * 
	 */
	public void connect() {
		
		if(this.reader == null)
			throw new DFLException(ExType.NO_READER_SELECTED);
		
		if(this.context == null)
			throw new DFLException(ExType.CONTEXT_NOT_INITIALIZED);
		
		try{
			this.card = context.Connect(this.reader, PCSC.SHARE_SHARED, 
					PCSC.PROTOCOL_T0 | PCSC.PROTOCOL_T1);	
			
		}catch(PCSCException e){
			throw convertException(e);
		}

		//TODO: Call to reconnect needed to avoid "6700" response in DESFire EV1
		
		reconnect();
	}
	
	/**
	 * 
	 */
	public void connect(String readerName){
		
		if(readerName == null) throw new NullPointerException();
		
		select(readerName);
		
		connect();
		
		deselect();
		
	}


	@Override
	/**
	 * 
	 */
	public byte[] send(byte[] command) {
		
		if(command == null) throw new NullPointerException();
		
		if(this.card == null)
			throw new DFLException(ExType.CARD_NOT_CONNECTED);
		
		try{
			
			Apdu apdu = new Apdu(command);
			
			return card.Transmit(apdu);
			
		}catch(PCSCException e){
			throw convertException(e);
		}
		
	}

	@Override
	/**
	 * 
	 */
	public void reconnect() {
		
		if(this.card == null)
			throw new DFLException(ExType.CARD_NOT_CONNECTED);
		
		if(this.reader == null)
			throw new DFLException(ExType.NO_READER_SELECTED);
		
		try{
			
			card.Reconnect(PCSC.SHARE_SHARED, PCSC.PROTOCOL_T0 | PCSC.PROTOCOL_T1, PCSC.RESET_CARD);
			
		}catch(PCSCException e){
			throw convertException(e);
		}
		

	}

	@Override
	/**
	 * 
	 */
	public void disconnect() {
		
		if(this.card == null)
			throw new DFLException(ExType.CARD_NOT_CONNECTED);
		
		try{
			card.Disconnect();
		}catch(PCSCException e){
			throw convertException(e);
		}
		
		this.card = null;

	}

	@Override
	/**
	 * 
	 */
	public void release() {
		
		if(context == null) 
			throw new DFLException(ExType.CONTEXT_NOT_INITIALIZED);
		
		try{
			context.ReleaseContext();
		}catch(PCSCException e){
			throw convertException(e);
		}
	}

	@Override
	/**
	 * 
	 */
	public CardType getCardType(){
		
		if(this.card == null)
			throw new DFLException(ExType.CARD_NOT_CONNECTED);
		
		State state = card.Status();
		
		return CardType.getCardType(BAUtils.toString(state.rgbAtr));
		
	}

	/**
	 * 
	 * @param e
	 * @return
	 */
	protected static DFLException convertException(Exception e){
	
		ExType type;
		
		if (e instanceof PCSCException){
			
			int r = ((PCSCException) e).getReason();
			
			if (r == PCSC.E_CANCELLED){
				type = ExType.PCSC_CANCELLED;
			}
			else if(r == PCSC.E_CANT_DISPOSE){
				type = ExType.PCSC_CANT_DISPOSE;
			}
			else if(r == PCSC.E_CARD_UNSUPPORTED){
				type = ExType.PCSC_CARD_UNSUPPORTED;
			}
			else if(r == PCSC.E_DUPLICATE_READER){
				type = ExType.PCSC_DUPLICATE_READER;
			}
			else if(r == PCSC.E_INSUFFICIENT_BUFFER){
				type = ExType.PCSC_INSUFFICIENT_BUFFER;
			}
			else if(r == PCSC.E_INVALID_ATR){
				type = ExType.PCSC_INSUFFICIENT_BUFFER;
			}
			else if(r == PCSC.E_INVALID_HANDLE){
				type = ExType.PCSC_INVALID_HANDLE;
			}
			else if(r == PCSC.E_INVALID_PARAMETER){
				type = ExType.PCSC_INVALID_PARAMETER;
			}
			else if(r == PCSC.E_INVALID_TARGET){
				type = ExType.PCSC_INVALID_TARGET;
			}
			else if(r == PCSC.E_INVALID_VALUE){
				type = ExType.PCSC_INVALID_VALUE;
			}
			else if(r == PCSC.E_NO_MEMORY){
				type = ExType.PCSC_NO_MEMORY;
			}
			else if(r == PCSC.E_NO_SERVICE){
				type = ExType.PCSC_NO_SERVICE;
			}
			else if(r == PCSC.E_NO_SMARTCARD){
				type = ExType.PCSC_NO_SMARTCARD;
			}
			else if(r == PCSC.E_NOT_READY){
				type = ExType.PCSC_NOT_READY;
			}	
			else if(r == PCSC.E_NOT_TRANSACTED){
				type = ExType.PCSC_NOT_TRANSACTED;
			}
			else if(r == PCSC.E_PCI_TOO_SMALL){
				type = ExType.PCSC_PCI_TOO_SMALL;
			}
			else if(r == PCSC.E_PROTO_MISMATCH){
				type = ExType.PCSC_PROTO_MISMATCH;
			}
			else if(r == PCSC.E_READER_UNAVAILABLE){
				type = ExType.PCSC_READER_UNAVAILABLE;
			}
			else if(r == PCSC.E_READER_UNSUPPORTED){
				type = ExType.PCSC_READER_UNSUPPORTED;
			}
			else if(r == PCSC.E_SERVICE_STOPPED){
				type = ExType.PCSC_SERVICE_STOPPED;
			}
			else if(r == PCSC.E_SHARING_VIOLATION){
				type = ExType.PCSC_SHARING_VIOLATION;
			}
			else if(r == PCSC.E_SYSTEM_CANCELLED){
				type = ExType.PCSC_SYSTEM_CANCELLED;
			}
			else if(r == PCSC.E_TIMEOUT){
				type = ExType.PCSC_TIMEOUT;
			}
			else if(r == PCSC.E_UNKNOWN_CARD){
				type = ExType.PCSC_UNKNOWN_CARD;
			}
			else if(r == PCSC.E_UNKNOWN_READER){
				type = ExType.PCSC_UNKNOWN_READER;
			}
			else if(r == PCSC.F_COMM_ERROR){
				type = ExType.COMM_ERROR;
			}
			else if(r == PCSC.F_INTERNAL_ERROR){
				type = ExType.INTERNAL_ERROR;
			}
			else if(r == PCSC.F_WAITED_TOO_LONG){
				type = ExType.WAITED_TOO_LONG;
			}
			else type = ExType.UNKNOWN_ERROR;
		}
		else
			type = ExType.UNKNOWN_ERROR;
		
		return new DFLException(type);
		
	}
	
	private Context context;
	private Card card;
	
	private String reader;
	
}
