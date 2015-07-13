package dflibrary.library.security;

import dflibrary.library.DFLException;
import dflibrary.library.DFLException.ExType;
import dflibrary.utils.ba.BAUtils;
import dflibrary.utils.security.CipAlg;

/**
 * Provides an encapsulation for keys to be used with Mifare DESFire cards
 * @author Francisco Rodriguez Algarra
 */
public class DFKey {

	/**
	 * Creates an instance of class <code>DFKey</code>
	 * @param keyData the data bytes that form the key
	 * @param alg an instance of class <code>CipAlg</code> representing
	 * the current cryptographic algorithm to use
	 */
	public DFKey(byte[] keyData, CipAlg alg){
		
		if((keyData == null) || (alg == null)) 
			throw new NullPointerException();
		if(alg == CipAlg.AES) 
			throw new IllegalArgumentException();
		
		this.keyData = keyData;
		this.keyVersion = DFCrypto.getKeyVersion(keyData);

		this.alg = alg;
	}
	
	/**
	 * Creates an instance of class <code>DFKey</code>
	 * @param keyData the data bytes that form the key
	 * @param alg an instance of class <code>CipAlg</code> representing
	 * the current cryptographic algorithm to use
	 * @param keyVersion an int representing the current key version
	 */
	public DFKey(byte[] keyData, CipAlg alg, int keyVersion){
		
		if((keyData == null) || (alg == null)) 
			throw new NullPointerException();
		
		if(alg != CipAlg.AES){
			setKeyVersion(keyData, keyVersion);
		}
		else{
			this.keyData = keyData;
			this.keyVersion = keyVersion;
		}
		
		this.alg = alg;
	}
	
	/**
	 * Edits the bytes of a key to introduce its version
	 * @param keyData
	 * @param keyVersion
	 */
	private void setKeyVersion(byte[] keyData, int keyVersion){
		
		if(keyData == null) 
			throw new NullPointerException();
		if(keyData.length % 8 != 0) 
			throw new DFLException(ExType.SECURITY_EXCEPTION);
		if((keyVersion < 0) && (keyVersion > 255)) 
			throw new DFLException(ExType.SECURITY_EXCEPTION);
		
		int count = keyData.length / 8;
		
        byte[] ver = new byte[0];
        byte[] aux = new byte[1];
        byte mask = (byte)0x01;
        byte b = (byte)keyVersion;
        

        for(int i = 0; i<8; i++){
            if((b & mask) != 0) aux = BAUtils.toBA(1, 1);
            else aux = new byte[1];
            ver = BAUtils.concatenateBAs(aux, ver);
            mask = (byte)(mask << 1);
        }

        byte[] mask2 = BAUtils.toBA("FEFEFEFEFEFEFEFE");
        byte[] verKey = new byte[0];
        
        for(int i = 0; i<count; i++){
        	
        	aux = BAUtils.extractSubBA(keyData, 8*i, 8);
        	aux = BAUtils.and(aux, mask2);
        	aux = BAUtils.xor(aux, ver);
        	verKey = BAUtils.concatenateBAs(verKey, aux);
        }

        this.keyData = new byte[verKey.length];
        
        System.arraycopy(verKey, 0,  this.keyData, 0, verKey.length);
		this.keyVersion = keyVersion;
		
	}
	
	/**
	 * @return a byte array containing the current key data
	 */
	public byte[] getKeyData(){ return this.keyData; }
	
	/**
	 * @return an int corresponding to the current version of the key
	 */
	public int getKeyVersion(){ return this.keyVersion; }
	
	/**
	 * @return
	 */
	public CipAlg getAlg(){ return this.alg; }
	
	@Override
	public String toString(){
		
		String s = "";
		
		s = s + "Key Data: 0x" + BAUtils.toString(this.keyData) + "\n";
		s = s + "Key Version number " + this.keyVersion + "\n";
		s = s + "Key crypto algorithm: " + this.alg.toString();
		
		return s;		
				
	}
	
	private byte[] keyData;
	private CipAlg alg;
	private int keyVersion;
	
}
