package dflibrary.middleware;

import dflibrary.library.CardType;

/**
 * 
 * @author Francisco Rodríguez Algarra
 *
 */
public interface ComManager {

	/**
	 * 
	 */
	public void scan();
	
	/**
	 * 
	 * @return
	 */
	public String[] listReaders();
	
	/**
	 * 
	 * @param readerName
	 */
	public void select(String readerName);
	
	/**
	 * 
	 */
	public void deselect();
	
	/**
	 * 
	 * @return
	 */
	public boolean isCardPresent();
	
	/**
	 * 
	 * @param readerName
	 * @return
	 */
	public boolean isCardPresent(String readerName);
	
	/**
	 * 
	 */
	public void waitForCard();
	
	/**
	 * 
	 * @param readerName
	 */
	public void waitForCard(String readerName);
	
	/**
	 * 
	 */
	public void waitCardExtraction();
	
	/**
	 * 
	 * @param readerName
	 */
	public void waitCardExtraction(String readerName);
	
	
	/**
	 * 
	 */
	public void connect();
	
	/**
	 * 
	 * @param readerName
	 */
	public void connect(String readerName);
	
	
	/**
	 * 
	 * @param command
	 * @return
	 */
	public byte[] send(byte[] command);
	
	/**
	 * 
	 */
	public void reconnect();
	
	/**
	 * 
	 */
	public void disconnect();
	
	/**
	 * 
	 */
	public void release();
	
	/**
	 * 
	 * @return
	 */
	public CardType getCardType();
	

	
}
