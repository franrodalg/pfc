package dflibrary.library.param;

import dflibrary.utils.ba.*;

/**
 * 
 * @author Francisco Rodríguez Algarra
 *
 */
public class KeyVersion {

	/**
	 * 
	 * @param keyVersion
	 */
	public KeyVersion(int keyVersion){
		
		setKV(keyVersion);
		
	}
	
	/**
	 * 
	 * @param keyVersion
	 */
	public KeyVersion(byte[] keyVersion){
		
		if(keyVersion == null) throw new NullPointerException();
		if(keyVersion.length != 1) throw new IllegalArgumentException();
		
		int kv = BAUtils.toInt(keyVersion);
		if((kv < 0) || (kv > 255)) throw new IllegalArgumentException();
		
		this.keyVersion = kv;
	}

	/**
	 * 
	 * @param keyVersion
	 */
	public void setKV(int keyVersion){
		
		if((keyVersion < 0) || (keyVersion > 255)) throw new IllegalArgumentException();
		
		this.keyVersion = keyVersion;
		
	}
	
	/**
	 * 
	 * @return
	 */
	public int toInt(){
		
		return this.keyVersion;
		
	}
	
	/**
	 * 
	 * @return
	 */
	public byte[] toBA(){
		
		return BAUtils.toBA(this.toInt(), 1);
		
	}
	
	/**
	 * 
	 */
	public String toString(){
		
		return "Key Version: " + keyVersion;
		
	}
	
	private int keyVersion;
	
}
