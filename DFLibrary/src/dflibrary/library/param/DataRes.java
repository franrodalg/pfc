package dflibrary.library.param;

/**
 * Provides an encapsulation of the structure retrieved from
 * an execution of the <code>readData</code> command
 * @author Francisco Rodriguez Algarra
 *
 */
public class DataRes {
	
	/**
	 * Creates an instance of class <code>DataRes</code>
	 * @param data a byte array containing the retrieved data frame
	 * @param checked a boolean indicating whether the retrieved
	 * data frame has successfully passed an integrity check or not
	 */
	public DataRes(byte[] data, boolean checked){
		
		if(data == null) throw new NullPointerException();
		
		this.data = new Data(data);
		this.isChecked = checked;
		
	}
	
	/**
	 * @return an instance of class <code>Data</code> representing
	 * the retrieved data frame
	 */
	public Data getData(){ return this.data; }
	
	/**
	 * @return <code>true</code> if the data frame had successfully 
	 * passed an integrity check; <code>false</code>otherwise
	 */
	public boolean isChecked(){ return this.isChecked; }
	
	@Override
	public String toString(){
		
		String s =  "Data: " + this.data;
		if(!isChecked) s = s + "\nCaution: this Data hasn't passed an integrity test";
		return s;
		
	}
	
	private boolean isChecked;
	private Data data;
	
}
