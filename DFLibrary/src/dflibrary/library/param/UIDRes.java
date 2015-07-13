package dflibrary.library.param;

/**
 * Provides an encapsulation of the structure retrieved from
 * an execution of the <code>getUID</code> command
 * @author Francisco Rodriguez Algarra
 */
public class UIDRes {
	
	/**
	 * Creates an instance of class <code>UIDRes</code>
	 * @param uid a byte array containing the card's unique identifier
	 * @param checked a boolean indicating whether the retrieved UID
	 * has successfully passed an integrity check or not
	 */
	public UIDRes(byte[] uid, boolean checked){
		
		if(uid == null) throw new NullPointerException();
		if(uid.length != 7) throw new IllegalArgumentException();
		
		this.uid = new UID(uid);
		this.isChecked = checked;
		
	}
	
	/**
	 * @return an instance of class <code>UID</code> representing the card's
	 * unique identifier
	 */
	public UID getUID(){ return this.uid; }
	
	/**
	 * @return <code>true</code> if the card's unique identifier has 
	 * successfully passed an integrity check;
	 * <code>false</code> otherwise
	 */
	public boolean isChecked(){ return this.isChecked; }
	
	@Override
	public String toString(){
		
		String s =  "UID: " + this.uid;
		if(!isChecked) 
			s = s + "\nCaution: this UID hasn't " + 
		    "passed an integrity test";
		return s;
		
	}
	
	
	private boolean isChecked;
	private UID uid;
	
}
