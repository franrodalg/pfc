package dflibrary.library.param;

import dflibrary.library.security.DFCrypto;
import dflibrary.utils.ba.*;
import dflibrary.utils.security.CipAlg;

/**
 * Provides an encapsulation of the structure retrieved from the execution of
 * the <code>getKeySettings</code> command
 * @author Francisco Rodriguez Algarra
 */
public class KeySettingsRes {

	/**
	 * Creates an instance of class <code>KeySettingsRes</code>
	 * @param keySettings an instance of class <code>KeySettings</code>
	 * representing the current key settings
	 * @param maxNumOfKeys an int indicating the maximum number of keys
	 * allowed in the application
	 */
    public KeySettingsRes(KeySettings keySettings, CipAlg alg, int maxNumOfKeys){

    	if((keySettings == null) || (alg == null)) throw new NullPointerException();
    	
        setKeySettings(keySettings);
        this.alg = alg;
        setMaxNumOfKeys(maxNumOfKeys);

    }

    /**
     * Creates an instance of class <code>KeySettingsRes</code>
     * @param field a byte array containing the card response to a
     * <code>GetKeySettings</code> command
     */
    public KeySettingsRes(byte[] field){
    	
    	if(field == null) throw new NullPointerException();
    	if(field.length != 2) throw new IllegalArgumentException();

        setKeySettings(BAUtils.extractSubBA(field, 0, 1));
        setMaxNumOfKeysAlg(BAUtils.extractSubBA(field, 1, 1));

    }
    
    /**
     * @return an instance of class <code>KeySettings</code>
	 * representing the current key settings
     */
    public KeySettings getKeySettings(){

        return this.keySettings;

    }

    /**
     * @return an int indicating the maximum number of keys
	 * allowed in the application
     */
    public int getMaxNumOfKeys(){

        return this.maxNumOfKeys;

    }
    
    /**
     * @return an instance of class <code>CipAlg</code> representing the
     * currently set criptographic algorithm for the application
     */
    public CipAlg getAlg(){
    	
    	return this.alg;
    	
    }
    
    /**
     * @param ks a byte array representing the new key settings
     */
    public void setKeySettings(byte[] ks){
    	
    	if(ks == null) throw new NullPointerException();
    	if(ks.length != 1) throw new IllegalArgumentException();
    	
    	setKeySettings(new KeySettings(ks));
    	
    }
    
    /**
	 * @param ks an instance of class <code>KeySettings</code>
	 * representing the new key settings
     */
    public void setKeySettings(KeySettings ks){
    	
    	if(ks == null) throw new NullPointerException();
    	
    	this.keySettings = ks;
    	
    }
    
    /**
     * @param maxNumOfKeysAlg an byte array containing the maximum number 
     * of keys allowed in the application
     */
    public void setMaxNumOfKeysAlg(byte[] maxNumOfKeysAlg){
    	
    	if(maxNumOfKeysAlg == null) 
    		throw new NullPointerException();
    	if(maxNumOfKeysAlg.length != 1) 
    		throw new IllegalArgumentException();
    	
    	byte[] maxNumOfKeys = BAUtils.and(maxNumOfKeysAlg, BAUtils.toBA("0F"));
    	
    	byte[] algBA = BAUtils.and(maxNumOfKeysAlg, BAUtils.toBA("C0"));
    	
    	this.alg = DFCrypto.getAlg(algBA);
    	
    	setMaxNumOfKeys(BAUtils.toInt(maxNumOfKeys));
    	
    }
    
    /**
     * @param maxNumOfKeys an int indicating the maximum number of keys
	 * allowed in the application
     */
    public void setMaxNumOfKeys(int maxNumOfKeys){
    	
    	if((maxNumOfKeys < 1) || (maxNumOfKeys > 14)) 
    		throw new IllegalArgumentException();
    	
    	this.maxNumOfKeys = maxNumOfKeys;
    	
    }

    @Override
    public String toString(){

    	String s = "";
    	
    	s = s + "Key Settings:\n " + 
    	        this.getKeySettings().toString() + "\n";
    	
        s = s + "Application Crypto Algorithm: " + 
                this.getAlg().toString() + "\n" +
        		"Maximum number of keys: " + 
                this.getMaxNumOfKeys();

        return s;
        
    }
    
    private KeySettings keySettings;
    private CipAlg alg;
    private int maxNumOfKeys;
	
}
