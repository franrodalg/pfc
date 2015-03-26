package dflibrary.library.param;

import dflibrary.utils.ba.*;

/**
 * 
 * @author Francisco Rodríguez Algarra
 *
 */
public class UID {

	/**
	 * 
	 */
	public UID(){
		
		this.uid = new byte[7];
		
	}
	
	/**
	 * 
	 * @param uid
	 */
	public UID(byte[] uid){
		
		if(uid == null) throw new NullPointerException();
		if(uid.length != 7) throw new IllegalArgumentException();
		
		this.uid = uid;
		
	}
	
	/**
	 * 
	 * @param uid
	 */
	public UID(String uid) {
		
		this(BAUtils.toBA(uid));
		
	}

	/**
	 * 
	 * @return
	 */
	public byte[] toBA(){
		
		return this.uid;
		
	}
	
	/**
	 * 
	 */
	public String toString(){
		
		return BAUtils.toString(this.toBA());
		
	}
	
	private byte[] uid;
	
}
