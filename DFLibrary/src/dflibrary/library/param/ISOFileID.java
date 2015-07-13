package dflibrary.library.param;

import dflibrary.utils.ba.BAUtils;

/**
 * Provides an encapsulation of an ISO file identifier
 * @author Francisco Rodriguez Algarra
 */
public class ISOFileID {

	/**
	 * Creates a default ISO file identifier
	 */
	public ISOFileID(){
		
		this.fid = new byte[2];
		
	}
	
	/**
	 * Creates a file identifier 
	 * @param fid an int representing the ISO file identifier
	 */
	public ISOFileID(int fid){
	
		this.fid = BAUtils.toBA(fid, 2);
	
	}
	
	/**
	 * Creates an ISO file identifier 
	 * @param fid a byte array representing the ISO file identifier
	 */
	public ISOFileID(byte[] fid){
		
		if(fid == null) throw new NullPointerException();
		if(fid.length != 2) throw new IllegalArgumentException();
		
		this.fid = fid;
		
	}
	
	/**
	 * @return the int representation of the ISO file identifier
	 */
	public int toInt(){
		
		return BAUtils.toInt(this.fid);
		
	}
	
	/**
	 * @return the byte array representation of the ISO file identifier
	 */
	public byte[] toBA(){
		
		return this.fid;
		
	}

	@Override
	public String toString(){
		
		return "0x" + BAUtils.toString(this.fid);
		
	}
	
	byte[] fid;
	
}
