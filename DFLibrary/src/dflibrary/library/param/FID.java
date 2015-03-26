package dflibrary.library.param;

import dflibrary.utils.ba.BAUtils;


/**
 * 
 * @author Francisco Rodríguez Algarra
 *
 */
public class FID {

	/**
	 * 
	 */
	public FID(){
		
		this.fid = new byte[1];
		
	}
	
	/**
	 * 
	 * @param fid
	 */
	public FID(int fid){
		
		this.fid = BAUtils.toBA(fid, 1);
		
	}
	
	/**
	 * 
	 * @param fid
	 */
	public FID(byte[] fid){
		
		if(fid == null) throw new NullPointerException();
		if(fid.length != 1) throw new IllegalArgumentException();
		
		this.fid = fid;
		
	}
	
	/**
	 * 
	 * @return
	 */
	public int toInt(){ return BAUtils.toInt(this.fid); }
	
	/**
	 * 
	 * @return
	 */
	public byte[] toBA(){ return this.fid; }
	
	/**
	 * 
	 */
	public String toString(){
		
		return "0x" + BAUtils.toString(toBA());		
	}
	
	
	private byte[] fid;
	
}
