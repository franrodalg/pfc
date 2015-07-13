package dflibrary.library.param;

import dflibrary.utils.ba.*;

/**
 * Provides an encapsulation of the production batch number retrieved
 * with the PICC version information
 * @author Francisco Rodriguez Algarra
 */
public class ProdBatchNum {

	/**
	 * Creates an instance of class <code>ProdBatchNum</code>
	 * @param number a byte array representing the card's production 
	 * batch number
	 */
	public ProdBatchNum(byte[] number){
		
		if(number == null) throw new NullPointerException();
		if(number.length != 5) throw new IllegalArgumentException();
		
		this.number = number;
		
	}
	
	/**
	 * @return the byte array representation of the card's production
	 * batch number
	 */
	public byte[] toBA(){
		
		return this.number;
		
	}
	
	@Override
	public String toString(){
		
		return BAUtils.toString(this.toBA());
		
	}
	
	private byte[] number;
	
}
