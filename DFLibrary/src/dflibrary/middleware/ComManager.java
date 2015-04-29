package dflibrary.middleware;

import dflibrary.library.CardType;

/**
 * 
 * Provides method definitions for ensuring an homogeneous management of the
 * communications with smart cards, independently of the lower level layers
 * employed. Implementations of this interface will allow the usage of
 * particular device controllers for standard functionalities.
 * 
 * @author Francisco Rodríguez Algarra
 *
 */
public interface ComManager {

	/**
	 * Initializes the context structure representation if needed and determines 
	 * which devices can be currently identified in the system.
	 */
	public void scan();
	
	/**
	 * Obtains the names of the supported devices currently connected to the system.
	 * @return A String array containing the connected devices names
	 */
	public String[] listReaders();
	
	/**
	 * Marks a device identified by readerName as selected for future operations
	 * only if the given name coincides with an entry within the response to a
	 * listReaders() call.
	 * @param readerName the device name, as stored in the listReaders() response
	 */
	public void select(String readerName);
	
	/**
	 * Removes the previous device selection mark
	 */
	public void deselect();
	
	/**
	 * Returns <code>true</code> if a card is already present in the selected 
	 * device.
	 * @return <code>true</code> if a card is already present in the selected 
	 * device; <code>false</false> otherwise
	 */
	public boolean isCardPresent();
	
	/**
	 * Returns <code>true</code> if a card is already present in the device 
	 * identified by readerName.
	 * @param readerName the device name, as stored in the listReaders() response
	 * @return <code>true</code> if a card is already present in the device
	 * identified by readerName; <code>false</false> otherwise
	 */
	public boolean isCardPresent(String readerName);
	
	/**
	 * Halts the program execution until a card insertion event is detected
	 * in the selected device.
	 */
	public void waitCardInsertion();
	
	/**
	 * Halts the program execution until a card insertion event is detected
	 * in the device identified by readerName.
	 * @param readerName the device name, as stored in the listReaders() response
	 */
	public void waitCardInsertion(String readerName);
	
	/**
	 * Halts the program execution until a card extraction event is detected
	 * in the selected device.
	 */
	public void waitCardExtraction();
	
	/**
	 * Halts the program execution until a card extraction event is detected
	 * in the device identified by readerName.
	 * @param readerName the device name, as stored in the listReaders() response
	 */
	public void waitCardExtraction(String readerName);
	
	/**
	 * Establishes a communication channel with a card currently present in the
	 * selected device.
	 */
	public void connect();
	
	/**
	 * Establishes a communication channel with a card currently present in the
	 * device identified by readerName.
	 * @param readerName the device name, as stored in the listReaders() response
	 */
	public void connect(String readerName);
	
	/**
	 * Transmits a byte frame to the currently connected card and obtains the
	 * corresponding response.
	 * @param command a byte array to be transmitted
	 * @return a byte array representing the obtained response
	 */
	public byte[] send(byte[] command);
	
	/**
	 * Re-starts the current communication channel.
	 */
	public void reconnect();
	
	/**
	 * Closes the current communication channel.
	 */
	public void disconnect();
	
	/**
	 * Frees the current context representation if needed.
	 */
	public void release();
	
	/**
	 * Obtains a representation of the model of the currently connected card,
	 * usually via its ATR, if known.
	 * @return an instance of class CardType representing the model of the
	 * currently connected card.
	 */
	public CardType getCardType();

}
