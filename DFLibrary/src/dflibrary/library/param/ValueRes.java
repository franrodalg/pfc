package dflibrary.library.param;

/**
 * 
 * @author Francisco Rodríguez Algarra
 *
 */
public class ValueRes {
	
	/**
	 * 
	 * @param value
	 * @param checked
	 */
	public ValueRes(byte[] value, boolean checked){
		
		if(value == null) throw new NullPointerException();
		if(value.length != 4) throw new IllegalArgumentException();
		
		this.value = new Value(value);
		this.isChecked = checked;
		
	}
	
	/**
	 * 
	 * @return
	 */
	public Value getValue(){ return this.value; }
	
	/**
	 * 
	 * @return
	 */
	public boolean isChecked(){ return this.isChecked; }
	
	/**
	 * 
	 */
	public String toString(){
		
		String s =  "Value: " + this.value;
		if(!isChecked) s = s + "\nCaution: this Value hasn't passed an integrity test";
		return s;
		
	}
	
	
	private boolean isChecked;
	private Value value;
	
}
