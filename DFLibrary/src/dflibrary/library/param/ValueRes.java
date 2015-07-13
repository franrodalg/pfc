package dflibrary.library.param;

/**
 * Provides an encapsulation of the structure retrieved from
 * an execution of the <code>getValue</code> command
 * @author Francisco Rodriguez Algarra
 */
public class ValueRes {
	
	/**
	 * Creates an instance of class <code>ValueRes</code>
	 * @param value a byte array representing the number stored in
	 * the file
	 * @param checked a boolean indicating whether the retrieved
	 * number has successfully passed an integrity check or not
	 */
	public ValueRes(byte[] value, boolean checked){
		
		if(value == null) throw new NullPointerException();
		if(value.length != 4) throw new IllegalArgumentException();
		
		this.value = new Value(value);
		this.isChecked = checked;
		
	}
	
	/**
	 * @return an instance of class <code>Value</code> representing 
	 * the number currently stored
	 */
	public Value getValue(){ return this.value; }
	
	/**
	 * 
	 * @return <code>true</code> if the number currently stored has
	 * successfully passed an integrity check;
	 * <code>false</code> otherwise
	 */
	public boolean isChecked(){ return this.isChecked; }
	
	@Override
	public String toString(){
		
		String s =  "Value: " + this.value;
		if(!isChecked) s = s + "\nCaution: this Value hasn't passed an integrity test";
		return s;
		
	}
		
	private boolean isChecked;
	private Value value;
	
}
