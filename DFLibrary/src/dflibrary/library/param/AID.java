package dflibrary.library.param;

import dflibrary.utils.ba.BAUtils;


/**
 * 
 * @author Francisco Rodríguez Algarra
 *
 */
public class AID {

	/**
	 * 
	 */
	public AID(){
		
		this.aid = new byte[3];
		
	}
	
	/**
	 * 
	 * @param aid
	 */
	public AID(int aid){
		
		this.aid = BAUtils.toBA(aid, 3);
		
	}
	
	/**
	 * 
	 * @param aid
	 */
	public AID(byte[] aid){
		
		if(aid == null) throw new NullPointerException();
		if(aid.length != 3) throw new IllegalArgumentException();
		
		this.aid = aid;
		
	}
	
	/**
	 * 
	 * @param aid
	 */
	public AID(String aid){
		
		this(BAUtils.toBA(aid));
		
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isMaster(){
		
		return BAUtils.compareBAs(this.aid, new byte[3]);
		
	}
	
	
	/**
	 * 
	 * @return
	 */
	public int toInt(){ return BAUtils.toInt(this.aid); }
	
	/**
	 * 
	 * @return
	 */
	public byte[] toBA(){ return this.aid; }
	
	/**
	 * 
	 */
	public String toString(){
		
		if (isMaster()) return "PICC Master Application";
		
		return "0x" + BAUtils.toString(toBA());		
	}
	
	
	private byte[] aid;
	
}
