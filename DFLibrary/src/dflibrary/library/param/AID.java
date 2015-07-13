package dflibrary.library.param;

import dflibrary.utils.ba.BAUtils;

/**
 * Provides an encapsulation of an application identifier
 * @author Francisco Rodriguez Algarra
 */
public class AID {

	/**
	 * Creates a default application identifier
	 */
	public AID(){
		
		this.aid = new byte[3];
		
	}
	
	/**
	 * Creates an application identifier 
	 * @param aid an int representing the application identifier
	 */
	public AID(int aid){
		
		this.aid = BAUtils.toBA(aid, 3);
		
	}
	
	/**
	 * Creates an application identifier 
	 * @param aid a byte array representing the application identifier
	 */
	public AID(byte[] aid){
		
		if(aid == null) throw new NullPointerException();
		if(aid.length != 3) throw new IllegalArgumentException();
		
		this.aid = aid;
		
	}
	
	/**
	 * Determines whether the application identifier represents
	 * the PICC Master Application or not
	 * @return <code>true</code> if the application identifier represents
	 * the PICC Master Application; <code>false</code> otherwise
	 */
	public boolean isMaster(){
		
		return BAUtils.compareBAs(this.aid, new byte[3]);
		
	}
	
	/**
	 * @return the int representation of the application identifier
	 */
	public int toInt(){ return BAUtils.toInt(this.aid); }
	
	/**
	 * @return the byte array representation of the application identifier
	 */
	public byte[] toBA(){ return this.aid; }
	
	@Override
	public String toString(){
		
		if (isMaster()) return "PICC Master Application";
		
		return "0x" + BAUtils.toString(toBA());		
	}
		
	private byte[] aid;
	
}
