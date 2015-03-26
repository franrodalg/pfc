package dflibrary.library.param;

import dflibrary.utils.ba.*;
import dflibrary.utils.ba.DigitUtils.SignMode;

/**
 * 
 * @author Francisco Rodríguez Algarra
 *
 */
public class Value {

	/**
	 * 
	 */
	public Value(){
		
		this.value = 0;
	
	}
	
	/**
	 * 
	 * @param value
	 */
	public Value(int value){
		
		this.value = value;
		
	}
	
	/**
	 * 
	 * @param value
	 */
	public Value(byte[] value){
		
		if(value == null) throw new NullPointerException();
		if(value.length != 4) throw new IllegalArgumentException();
		
		this.value = BAUtils.toInt(value, SignMode.SIGNED);
		
	}
	
	/**
	 * 
	 * @return
	 */
	public int getValue(){ return this.value; }
	
	/**
	 * 
	 * @return
	 */
	public byte[] toBA(){ return BAUtils.toBA(this.value, 4, SignMode.SIGNED); }
	
	
	public String toString(){
		
		String s = "" + this.value;
		
		return s;
		
	}
	
	private int value;
	
}
