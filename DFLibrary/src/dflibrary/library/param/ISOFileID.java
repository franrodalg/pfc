package dflibrary.library.param;

import dflibrary.utils.ba.BAUtils;

/**
 * 
 * @author Francisco Rodríguez Algarra
 *
 */
public class ISOFileID {

	/**
	 * 
	 */
	public ISOFileID(){
		
		this.fid = new byte[2];
		
	}
	
	/**
	 * 
	 * @param fid
	 */
	public ISOFileID(byte[] fid){
		
		if(fid == null) throw new NullPointerException();
		if(fid.length != 2) throw new IllegalArgumentException();
		
		
		this.fid = fid;
		
	}

	/**
	 * 
	 * @param fid
	 */
	public ISOFileID(int fid){
	
		this.fid = BAUtils.toBA(fid, 2);
	
	}
	
	/**
	 * 
	 * @return
	 */
	public byte[] toBA(){
		
		return this.fid;
		
	}
	
	/**
	 * 
	 * @return
	 */
	public int toInt(){
		
		return BAUtils.toInt(this.fid);
		
	}
	
	/**
	 * 
	 */
	public String toString(){
		
		return "0x" + BAUtils.toString(this.fid);
		
	}
	
	byte[] fid;
	
}
