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
			throw new DFLException(e);
		}

	}

	@Override
	/**
	 * 
	 */
	public String[] listReaders() {

		if(context == null) throw new DFLException(ExType.CONTEXT_NOT_INITIALIZED);
		
		String[] readers = null;
		
		try{
			readers = context.ListReaders();
		}catch(PCSCException e){
			throw new DFLException(e);
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
			throw new DFLException(e);
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
			throw new DFLException(e);
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
			throw new DFLException(e);
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
			throw new DFLException(e);
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
			throw new DFLException(e);
		}
		
		this.card = null;

	}

	@Override
	/**
	 * 
	 */
	public void release() {
		
		if(context == null) throw new DFLException(ExType.CONTEXT_NOT_INITIALIZED);
		
		try{
			context.ReleaseContext();
		}catch(PCSCException e){
			throw new DFLException(e);
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
	 * @return
	 */
	public String getReader(){
		return this.reader;
	}
	
	private Context context;
	private Card card;
	
	private String reader;
	
}
