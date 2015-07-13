package dflibrary.library.param;

import dflibrary.utils.ba.BAUtils;


/**
 * Provides an encapsulation of a file identifier
 * @author Francisco Rodriguez Algarra
 */
public class FID {

	/**
	 * Creates a default file identifier
	 */
	public FID(){
		
		this.fid = new byte[1];
		
	}
	
	/**
	 * Creates a file identifier 
	 * @param aid an int representing the file identifier
	 */
	public FID(int fid){
		
		this.fid = BAUtils.toBA(fid, 1);
		
	}
	
	/**
	 * Creates a file identifier 
	 * @param aid a byte array representing the file identifier
	 */
	public FID(byte[] fid){
		
		if(fid == null) throw new NullPointerException();
		if(fid.length != 1) throw new IllegalArgumentException();
		
		this.fid = fid;
		
	}
	
	/**
	 * @return the int representation of the file identifier
	 */
	public int toInt(){ return BAUtils.toInt(this.fid); }
	
	/**
	 * @return the byte array representation of the file identifier
	 */
	public byte[] toBA(){ return this.fid; }
	
	@Override
	public String toString(){
		
		return "0x" + BAUtils.toString(toBA());		
	}
	
	
	private byte[] fid;
	
}
