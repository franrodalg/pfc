package dflibrary.library.param;

import dflibrary.utils.ba.*;
import dflibrary.utils.ba.DigitUtils.SignMode;

/**
 * Provides an encapsulation of a number to write in or read from a
 * <code>Value</code> file
 * @author Francisco Rodriguez Algarra
 *
 */
public class Value {

	/**
	 * Creates a default instance of class <code>Value</code>
	 */
	public Value(){
		
		this.value = 0;
	
	}
	
	/**
	 * Creates an instance of class <code>Value</code>
	 * @param value an int indicating the number to store
	 */
	public Value(int value){
		
		this.value = value;
		
	}
	
	/**
	 * Creates an instance of class <code>Value</code>
	 * @param value an byte array indicating the number to store
	 */
	public Value(byte[] value){
		
		if(value == null) throw new NullPointerException();
		if(value.length != 4) throw new IllegalArgumentException();
		
		this.value = BAUtils.toInt(value, SignMode.SIGNED);
		
	}
	
	/**
	 * @return an int indicating the stored number
	 */
	public int getValue(){ return this.value; }
	
	/**
	 * @return an byte array representing the stored number
	 */
	public byte[] toBA(){ return BAUtils.toBA(
			this.value, 4, SignMode.SIGNED); }
	
	@Override
	public String toString(){
		
		String s = "" + this.value;
		
		return s;
		
	}
	
	private int value;
	
}
