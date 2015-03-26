package dflibrary.library.param;

import dflibrary.library.security.DFCrypto;
import dflibrary.utils.ba.*;
import dflibrary.utils.security.CipAlg;

/**
 * 
 * @author Francisco Rodríguez Algarra
 *
 */
public class KeySettingsRes {

	/**
	 * 
	 * @param keySettings
	 * @param maxNumOfKeys
	 */
    public KeySettingsRes(KeySettings keySettings, CipAlg alg, int maxNumOfKeys){

    	if((keySettings == null) || (alg == null)) throw new NullPointerException();
    	
        setKeySettings(keySettings);
        this.alg = alg;
        setMaxNumOfKeys(maxNumOfKeys);

    }

    /**
     * 
     * @param field
     */
    public KeySettingsRes(byte[] field){
    	
    	if(field == null) throw new NullPointerException();
    	if(field.length != 2) throw new IllegalArgumentException();

        setKeySettings(BAUtils.extractSubBA(field, 0, 1));
        setMaxNumOfKeysAlg(BAUtils.extractSubBA(field, 1, 1));

    }
    
    /**
     * 
     * @return
     */
    public KeySettings getKeySettings(){

        return this.keySettings;

    }

    /**
     * 
     * @return
     */
    public int getMaxNumOfKeys(){

        return this.maxNumOfKeys;

    }
    
    /**
     * 
     * @return
     */
    public CipAlg getAlg(){
    	
    	return this.alg;
    	
    }
    /**
     * 
     * @param ks
     */
    public void setKeySettings(byte[] ks){
    	
    	if(ks == null) throw new NullPointerException();
    	if(ks.length != 1) throw new IllegalArgumentException();
    	
    	setKeySettings(new KeySettings(ks));
    	
    }
    
    /**
     * 
     * @param ks
     */
    public void setKeySettings(KeySettings ks){
    	
    	if(ks == null) throw new NullPointerException();
    	
    	this.keySettings = ks;
    	
    }
    
    /**
     * 
     * @param maxNumOfKeysAlg
     */
    public void setMaxNumOfKeysAlg(byte[] maxNumOfKeysAlg){
    	
    	if(maxNumOfKeysAlg == null) throw new NullPointerException();
    	if(maxNumOfKeysAlg.length != 1) throw new IllegalArgumentException();
    	
    	byte[] maxNumOfKeys = BAUtils.and(maxNumOfKeysAlg, BAUtils.toBA("0F"));
    	
    	byte[] algBA = BAUtils.and(maxNumOfKeysAlg, BAUtils.toBA("C0"));
    	
    	this.alg = DFCrypto.getAlg(algBA);
    	
    	setMaxNumOfKeys(BAUtils.toInt(maxNumOfKeys));
    	
    }
    
    /**
     * 
     * @param maxNumOfKeys
     */
    public void setMaxNumOfKeys(int maxNumOfKeys){
    	
    	if((maxNumOfKeys < 1) || (maxNumOfKeys > 14)) throw new IllegalArgumentException();
    	
    	this.maxNumOfKeys = maxNumOfKeys;
    	
    }

    /**
     * 
     */
    public String toString(){

    	String s = "";
    	
    	s = s + "Key Settings:\n " + this.getKeySettings().toString() + "\n";
    	
        s = s + "Application Crypto Algorithm: " + this.getAlg().toString() + "\n" +
        
        		"Maximum number of keys: " + this.getMaxNumOfKeys();

        return s;
        
    }
    
    private KeySettings keySettings;
    private CipAlg alg;
    private int maxNumOfKeys;
	
}
