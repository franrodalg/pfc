package dflibrary.library.param;

import dflibrary.utils.ba.BAUtils;

/**
 * Provides an encapsulation for the data to be write to or read from
 * Standard Data and Backup Data files 
 * @author Francisco Rodriguez Algarra
 *
 */
public class Data {
	
	/**
	 * Creates an instance of class <code>Data</code>
	 * @param data a byte array containing the data frame
	 */
	public Data(byte[] data){
		
		if(data == null) throw new NullPointerException();
		
		this.data = data;	
		
	}
	
	/**
	 * @return the byte array representation of the data frame
	 */
	public byte[] toBA(){
		
		return this.data;
		
	}
	
	/**
	 * @return an instance of class <code>Size</code> representing
	 * the length of the data
	 */
	public Size getLength(){
		
		return new Size(this.data.length);
		
	}
	
	/**
	 * @param data a byte array representing the data frame to be set
	 */
	public void setData(byte[] data){
		
		if(data == null) throw new NullPointerException();
		
		this.data = data;
		
	}

	@Override
	public String toString(){
		
		String s =  "0x" + BAUtils.toString(this.toBA());
		return s;
		
	}
	
	private byte[] data;
	
}
