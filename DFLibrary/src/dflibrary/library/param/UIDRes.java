package dflibrary.library.param;

/**
 * 
 * @author Francisco Rodríguez Algarra
 *
 */
public class UIDRes {
	
	/**
	 * 
	 * @param uid
	 * @param checked
	 */
	public UIDRes(byte[] uid, boolean checked){
		
		if(uid == null) throw new NullPointerException();
		if(uid.length != 7) throw new IllegalArgumentException();
		
		this.uid = new UID(uid);
		this.isChecked = checked;
		
	}
	
	/**
	 * 
	 * @return
	 */
	public UID getUID(){ return this.uid; }
	
	/**
	 * 
	 * @return
	 */
	public boolean isChecked(){ return this.isChecked; }
	
	/**
	 * 
	 */
	public String toString(){
		
		String s =  "UID: " + this.uid;
		if(!isChecked) s = s + "\nCaution: this UID hasn't passed an integrity test";
		return s;
		
	}
	
	
	private boolean isChecked;
	private UID uid;
	
}
