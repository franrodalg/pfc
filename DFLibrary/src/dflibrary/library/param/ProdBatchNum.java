package dflibrary.library.param;

import dflibrary.utils.ba.*;

/**
 * 
 * @author frankie
 *
 */
public class ProdBatchNum {

	/**
	 * 
	 * @param number
	 */
	public ProdBatchNum(byte[] number){
		
		if(number == null) throw new NullPointerException();
		if(number.length != 5) throw new IllegalArgumentException();
		
		this.number = number;
		
	}
	
	/**
	 * 
	 * @return
	 */
	public byte[] toBA(){
		
		return this.number;
		
	}
	
	/**
	 * 
	 */
	public String toString(){
		
		return BAUtils.toString(this.toBA());
		
	}
	
	private byte[] number;
	
}
