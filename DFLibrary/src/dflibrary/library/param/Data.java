package dflibrary.library.param;

import dflibrary.utils.ba.BAUtils;


/**
 * 
 * @author Francisco Rodríguez Algarra
 *
 */
public class Data {
	
	/**
	 * 
	 * @param data
	 */
	public Data(byte[] data){
		
		if(data == null) throw new NullPointerException();
		
		this.data = data;	
		
	}
	
	/**
	 * 
	 * @return
	 */
	public byte[] toBA(){
		
		return this.data;
		
	}
	
	/**
	 * 
	 * @return
	 */
	public Size getLength(){
		
		return new Size(this.data.length);
		
	}
	
	/**
	 * 
	 * @param data
	 */
	public void setData(byte[] data){
		
		if(data == null) throw new NullPointerException();
		
		this.data = data;
		
	}
	
	/**
	 * 
	 */
	public String toString(){
		
		String s =  "0x" + BAUtils.toString(this.toBA());
		return s;
		
	}
	
	private byte[] data;
	
}
