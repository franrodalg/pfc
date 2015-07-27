package dflibrary.middleware;

import java.util.List;

import dflibrary.library.CardType;
import dflibrary.library.DFLException;
import dflibrary.library.DFLException.ExType;
import dflibrary.utils.ba.BAUtils;

import javax.smartcardio.*;

public class JSCIOComManager implements ComManager {

	/** {@inheritDoc}
	 * 
	 */
	@Override
	public void scan() {
		
		this.context = TerminalFactory.getDefault();
		
	}

	/** {@inheritDoc}
	 * 
	 */
	@Override
	public String[] listReaders() {
		
		if(context == null) 
			throw new DFLException(ExType.CONTEXT_NOT_INITIALIZED);
		
		String[] readers = null;
		
		try{
			List<CardTerminal> readersList = context.terminals().list();
			readers = new String[readersList.size()];
			for(int i = 0; i < readers.length; i ++){
				readers[i] = readersList.get(i).getName();
			}
		}catch(CardException e){
			throw convertException(e);
		}
		
		if((readers == null) || (readers.length == 0))
			throw new DFLException(ExType.NO_READERS_FOUND);
		
		return readers;
		
	}

	/** {@inheritDoc}
	 * 
	 */
	@Override
	public void select(String readerName) {
		
		if(readerName == null) throw new NullPointerException();
		
		this.reader = this.context.terminals().getTerminal(readerName);
		
		if(this.reader == null)
			throw new DFLException(ExType.READER_NOT_FOUND);
	}

	/** {@inheritDoc}
	 * 
	 */
	@Override
	public void deselect() {
		
		this.reader = null;
	
	}

	/** {@inheritDoc}
	 * 
	 */
	@Override
	public boolean isCardPresent() {
		
		checkSetUp();
		
		try{
			return this.reader.isCardPresent();
		}
		catch(CardException e){
			throw convertException(e);
		}
	
	}

	/** {@inheritDoc}
	 * 
	 */
	@Override
	public void waitCardInsertion() {
		// TODO Auto-generated method stub

	}

	/** {@inheritDoc}
	 * 
	 */
	@Override
	public void waitCardExtraction() {
		// TODO Auto-generated method stub

	}

	/** {@inheritDoc}
	 * 
	 */
	@Override
	public void connect() {
		
		checkSetUp();
		
		try {
			this.card = this.reader.connect("*");
		} catch (CardException e) {
			throw convertException(e);
		}

	}


	/** {@inheritDoc}
	 * 
	 */
	@Override
	public byte[] send(byte[] command) {
		
		if(command == null) throw new NullPointerException();
		
		if(this.card == null)
			throw new DFLException(ExType.CARD_NOT_CONNECTED);
		
		CardChannel channel = card.getBasicChannel();
		
		try {
			CommandAPDU c = new CommandAPDU(command);
			ResponseAPDU r = channel.transmit(c);
			return r.getBytes();
		} catch (CardException e) {
			throw convertException(e);
		}
		
	}

	/** {@inheritDoc}
	 * 
	 */
	@Override
	public void reconnect() {
		// TODO Auto-generated method stub
	}

	/** {@inheritDoc}
	 * 
	 */
	@Override
	public void disconnect() {
		
		if(this.card == null)
			throw new DFLException(ExType.CARD_NOT_CONNECTED);
		
		try{
			this.card.disconnect(false);
		}catch(CardException e){
			throw convertException(e);
		}
		
		this.card = null;

	}

	/** {@inheritDoc}
	 * 
	 */
	@Override
	public void release() {
		
		this.context = null;

	}

	/** {@inheritDoc}
	 * 
	 */
	@Override
	public CardType getCardType() {
		
		if(this.card == null)
			throw new DFLException(ExType.CARD_NOT_CONNECTED);
		
		String atr = BAUtils.toString(this.card.getATR().getBytes());
		
		return CardType.getCardType(atr);
		
	}
	
	/**
	 * 
	 */
	private void checkSetUp(){
		
		if(this.context == null)
			throw new DFLException(ExType.CONTEXT_NOT_INITIALIZED);
		
		if(this.reader == null) 
			throw new DFLException(ExType.NO_READER_SELECTED);
		
	}
	
	protected static DFLException convertException(Exception e){
		
		String m = e.getCause().getMessage();
		ExType type;

		if(m == "SCARD_E_CANCELLED")
			type = ExType.CANCELLED;
		else if(m == "SCARD_E_CANT_DISPOSE")
			type = ExType.CANT_DISPOSE;
		else if(m == "SCARD_E_CANT_DISPOSE")
			type = ExType.CANT_DISPOSE;
		else if(m == "SCARD_E_INVALID_ATR")
			type = ExType.INVALID_ATR;
		else if(m == "SCARD_E_INVALID_HANDLE")
			type = ExType.INVALID_HANDLE;
		else if(m == "SCARD_E_INVALID_PARAMETER")
			type = ExType.INVALID_PARAMETER;
		else if(m == "SCARD_E_INVALID_TARGET")
			type = ExType.INVALID_TARGET;
		else if(m == "SCARD_E_INVALID_VALUE")
			type = ExType.INVALID_VALUE;
		else if(m == "SCARD_E_NO_MEMORY")
			type = ExType.NO_MEMORY;
		else if(m == "SCARD_E_UNKNOWN_READER")
			type = ExType.UNKNOWN_READER;
		else if(m == "SCARD_E_TIMEOUT")
			type = ExType.TIMEOUT;
		else if(m == "SCARD_E_SHARING_VIOLATION")
			type = ExType.SHARING_VIOLATION;
		else if(m == "SCARD_E_NO_SMARTCARD")
			type = ExType.NO_SMARTCARD;
		else if(m == "SCARD_E_UNKNOWN_CARD")
			type = ExType.UNKNOWN_CARD;
		else if(m == "SCARD_E_PROTO_MISMATCH")
			type = ExType.PROTO_MISMATCH;
		else if(m == "SCARD_E_NOT_READY")
			type = ExType.NOT_READY;
		else if(m == "SCARD_E_SYSTEM_CANCELLED")
			type = ExType.SYSTEM_CANCELLED;
		else if(m == "SCARD_E_NOT_TRANSACTED")
			type = ExType.NOT_TRANSACTED;
		else if(m == "SCARD_E_READER_UNAVAILABLE")
			type = ExType.READER_UNAVAILABLE;
		else if(m == "SCARD_E_UNSUPPORTED_FEATURE")
			type = ExType.UNSUPPORTED_FEATURE;
		else if(m == "SCARD_E_PCI_TOO_SMALL")
			type = ExType.PCI_TOO_SMALL;
		else if(m == "SCARD_E_READER_UNSUPPORTED")
			type = ExType.READER_UNSUPPORTED;
		else if(m == "SCARD_E_DUPPLICATE_READER")
			type = ExType.DUPLICATE_READER;
		else if(m == "SCARD_E_CARD_UNSUPPORTED")
			type = ExType.CARD_UNSUPPORTED;
		else if(m == "SCARD_E_NO_SERVICE")
			type = ExType.NO_SERVICE;
		else if(m == "SCARD_E_SERVICE_STOPPED")
			type = ExType.SERVICE_STOPPED;
		else if(m == "SCARD_E_NO_READERS_AVAILABLE")
			type = ExType.NO_READERS_FOUND;
		else if(m == "SCARD_W_UNSUPPORTED_CARD")
			type = ExType.CARD_UNSUPPORTED;
		else if(m == "SCARD_W_UNRESPONSIVE_CARD")
			type = ExType.CARD_UNAVAILABLE;
		else if(m == "SCARD_W_UNPOWERED_CARD")
			type = ExType.CARD_UNAVAILABLE;
		else if(m == "SCARD_F_COMM_ERROR")
			type = ExType.COMM_ERROR;
		else if(m == "SCARD_F_INTERNAL_ERROR")
			type = ExType.INTERNAL_ERROR;
		else if(m == "SCARD_F_WAITED_TOO_LONG")
			type = ExType.WAITED_TOO_LONG;
		else
			type = ExType.UNKNOWN_ERROR;
		
		return new DFLException(type);
		
	}

	private TerminalFactory context;
	private CardTerminal reader;
	private Card card;
	
}
