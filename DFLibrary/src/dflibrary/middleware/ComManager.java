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
	 * @param reader
	 */
	public void select(String reader);
	
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
	 * @param reader
	 * @return
	 */
	public boolean isCardPresent(String reader);
	
	/**
	 * 
	 */
	public void waitForCard();
	
	/**
	 * 
	 * @param reader
	 */
	public void waitForCard(String reader);
	
	/**
	 * 
	 */
	public void waitCardExtraction();
	
	/**
	 * 
	 * @param reader
	 */
	public void waitCardExtraction(String reader);
	
	
	/**
	 * 
	 */
	public void connect();
	
	/**
	 * 
	 * @param reader
	 */
	public void connect(String reader);
	
	
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
