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
		
		this.ctx = new Context();
		
		try{
			ctx.EstablishContext(PCSC.SCOPE_SYSTEM, null, null);
		}catch(PCSCException e){
			throw new DFLException(e);
		}

	}

	@Override
	/**
	 * 
	 */
	public String[] listReaders() {

		if(ctx == null) throw new DFLException(ExType.CONTEXT_NOT_INITIALIZED);
		
		String[] readers = null;
		
		try{
			readers = ctx.ListReaders();
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
	public void select(String reader) {
		
		if(reader == null) throw new NullPointerException();
		
		String[] readers = listReaders();
		
		if(!findReader(reader, readers)) 
			throw new DFLException(ExType.READER_NOT_FOUND);
		
		this.rn = reader;
		

	}

	/**
	 * 
	 * @param reader
	 * @param readers
	 * @return
	 */
	private boolean findReader(String reader, String[] readers){
		
		if((reader == null) || (readers == null)) throw new NullPointerException();
		
		for(int i = 0; i < readers.length; i++)
		{
			if(readers[i].equals(reader)) return true;
		}
		return false;
	}
	
	@Override
	/**
	 * 
	 */
	public void deselect() {
		
		this.rn = null;
		

	}

	@Override
	/**
	 * 
	 */
	public boolean isCardPresent() {
		
		if(this.rn == null) 
			throw new DFLException(ExType.NO_READER_SELECTED);
		
		if(this.ctx == null)
			throw new DFLException(ExType.CONTEXT_NOT_INITIALIZED);
		
		try{
		
			State state = new State(this.rn);
			State[] states = new State[1];
			states[0] = state;
			
			ctx.GetStatusChange(0, states);
			
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
	public boolean isCardPresent(String reader) {
		
		if(reader == null) throw new NullPointerException();
		
		select(reader);
		
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
	public void waitForCard(String reader) {
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
	public void waitCardExtraction(String reader) {
		// TODO Auto-generated method stub

	}

	@Override
	/**
	 * 
	 */
	public void connect() {
		
		if(this.rn == null)
			throw new DFLException(ExType.NO_READER_SELECTED);
		
		if(this.ctx == null)
			throw new DFLException(ExType.CONTEXT_NOT_INITIALIZED);
		
		try{
			this.card = ctx.Connect(this.rn, PCSC.SHARE_SHARED, PCSC.PROTOCOL_T0 | PCSC.PROTOCOL_T1);	
			
		}catch(PCSCException e){
			throw new DFLException(e);
		}

		//TODO: Call to reconnect needed to avoid "6700" response in DESFire EV1
		
		reconnect();
	}
	
	/**
	 * 
	 */
	public void connect(String reader){
		
		if(reader == null) throw new NullPointerException();
		
		select(reader);
		
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
		
		if(this.rn == null)
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
		
		if(ctx == null) throw new DFLException(ExType.CONTEXT_NOT_INITIALIZED);
		
		try{
			ctx.ReleaseContext();
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
		
		String atr = BAUtils.toString(state.rgbAtr);
		
		if(atr.equals(DESFIRE_ATR)) return CardType.MIFARE_DESFIRE;
		else if(atr.equals(MIFARE_CLASSIC_1K_ATR)) return CardType.MIFARE_CLASSIC_1K;
		else if(atr.equals(MIFARE_CLASSIC_4K_ATR)) return CardType.MIFARE_CLASSIC_4K;
		else if(atr.equals(MIFARE_ULTRALIGHT_ATR)) return CardType.MIFARE_ULTRALIGHT;
		else if(atr.equals(MIFARE_PLUS_ATR)) return CardType.MIFARE_PLUS;
		else throw new DFLException(ExType.UNKNOWN_CARD_TYPE);
		
		
		
	}
	
	/**
	 * 
	 * @return
	 */
	public String getReader(){
		return this.rn;
	}
	
	private Context ctx;
	private Card card;
	
	private String rn;
	
	
	public static final String DESFIRE_ATR = "3B8180018080";
	public static final String MIFARE_CLASSIC_1K_ATR = "3B8F8001804F0CA000000306030001000000006A";
	public static final String MIFARE_CLASSIC_4K_ATR = "3B8F8001804F0CA0000003060300020000000069";
	public static final String MIFARE_ULTRALIGHT_ATR = "3B8F8001804F0CA0000003060300030000000068";
	public static final String MIFARE_PLUS_ATR = "3B878001C1052F2F01BCD6A9";
	
	
}
