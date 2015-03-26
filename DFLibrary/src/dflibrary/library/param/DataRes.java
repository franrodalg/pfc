package dflibrary.library.param;

/**
 * 
 * @author Francisco Rodríguez Algarra
 *
 */
public class DataRes {
	
	/**
	 * 
	 * @param data
	 * @param checked
	 */
	public DataRes(byte[] data, boolean checked){
		
		if(data == null) throw new NullPointerException();
		
		this.data = new Data(data);
		this.isChecked = checked;
		
	}
	
	/**
	 * 
	 * @return
	 */
	public Data getData(){ return this.data; }
	
	/**
	 * 
	 * @return
	 */
	public boolean isChecked(){ return this.isChecked; }
	
	/**
	 * 
	 */
	public String toString(){
		
		String s =  "Data: " + this.data;
		if(!isChecked) s = s + "\nCaution: this Data hasn't passed an integrity test";
		return s;
		
	}
	
	
	private boolean isChecked;
	private Data data;
	
}
