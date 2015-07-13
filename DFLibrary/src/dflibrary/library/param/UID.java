package dflibrary.library.param;

import dflibrary.utils.ba.*;

/**
 * Provides an encapsulation of a card's unique identifier
 * @author Francisco Rodriguez Algarra
 */
public class UID {

	/**
	 * Creates a default instance of class <code>UID</code>
	 */
	public UID(){
		
		this.uid = new byte[7];
		
	}
	
	/**
	 * Creates an instance of class <code>UID</code>
	 * @param uid a byte array containing the card's unique identifier
	 */
	public UID(byte[] uid){
		
		if(uid == null) throw new NullPointerException();
		if(uid.length != 7) throw new IllegalArgumentException();
		
		this.uid = uid;
		
	}

	/**
	 * @return the byte array representation of the card's unique identifier
	 */
	public byte[] toBA(){
		
		return this.uid;
		
	}
	
	@Override
	public String toString(){
		
		return BAUtils.toString(this.toBA());
		
	}
	
	private byte[] uid;
	
}
