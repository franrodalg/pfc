package dflibrary.middleware;

import java.io.*;
import java.util.*;

import dflibrary.library.CardType;
import dflibrary.utils.ba.BAUtils;

/**
 * Provides an implementation of the ComManager interface, mainly
 * addressed to the transmission of commands to and the reception of 
 * responses from a card located in a remote device.
 * @author Francisco Rodríguez Algarra
 *
 */
public class RemoteComManager implements ComManager {

	/**
	 * Creates an instance of RemoteComManager
	 * @param in a Scanner instance representing the input channel for 
	 * 			receiving responses
	 * @param out a PrintWriter instance representing the output channel
	 * 			for sending commands
	 */
	public RemoteComManager(Scanner in, PrintWriter out){
		
		this.in = in;
		this.out = out;
	}
	
	/** {@inheritDoc}
	 * 
	 */
	@Override
	public void scan() {}

	/** {@inheritDoc}
	 * 
	 */
	@Override
	public String[] listReaders() { return null; }

	/** {@inheritDoc}
	 * 
	 */
	@Override
	public void select(String readerName) {}

	/** {@inheritDoc}
	 * 
	 */
	@Override
	public void deselect() {}

	/** {@inheritDoc}
	 * 
	 */
	@Override
	public boolean isCardPresent() { return true; }

	/** {@inheritDoc}
	 * 
	 */
	@Override
	public void waitCardInsertion() {}

	/** {@inheritDoc}
	 * 
	 */
	@Override
	public void waitCardExtraction() {}

	/** {@inheritDoc}
	 * 
	 */
	@Override
	public void connect() {}

	/** {@inheritDoc}
	 * 
	 */
	@Override
	public byte[] send(byte[] command) {
		
		out.println(BAUtils.toString(command));	
		
		boolean ended = false;
		
		while((!ended) && (in.hasNextLine())){
			
			String line = in.nextLine();
			System.out.println(line);
			return BAUtils.toBA(line);
			
		}
		
		return null;
		
	}

	/** {@inheritDoc}
	 * 
	 */
	@Override
	public void reconnect() {}

	/** {@inheritDoc}
	 * 
	 */
	@Override
	public void disconnect() {}

	/** {@inheritDoc}
	 * 
	 */
	@Override
	public void release() {}

	/** {@inheritDoc}
	 * 
	 */
	@Override
	public CardType getCardType() { return null; }

	private Scanner in;
	private PrintWriter out;
	
}
