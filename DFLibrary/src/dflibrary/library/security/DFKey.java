package dflibrary.library.security;

import dflibrary.library.DFLException;
import dflibrary.library.DFLException.ExType;
import dflibrary.utils.ba.BAUtils;
import dflibrary.utils.security.CipAlg;

/**
 * 
 * @author Francisco Rodríguez Algarra
 *
 */
public class DFKey {

	/**
	 * 
	 * @param keyBytes
	 * @param alg
	 */
	public DFKey(byte[] keyBytes, CipAlg alg){
		
		if((keyBytes == null) || (alg == null)) throw new NullPointerException();
		if(alg == CipAlg.AES) throw new IllegalArgumentException();
		
		this.keyBytes = keyBytes;
		this.keyVersion = DFCrypto.getKeyVersion(keyBytes);

		this.alg = alg;
	}
	
	/**
	 * 
	 * @param keyBytes
	 * @param alg
	 * @param keyVersion
	 */
	public DFKey(byte[] keyBytes, CipAlg alg, int keyVersion){
		
		if((keyBytes == null) || (alg == null)) throw new NullPointerException();
		
		if(alg != CipAlg.AES){
			setKeyVersion(keyBytes, keyVersion);
		}
		else{
			this.keyBytes = keyBytes;
			this.keyVersion = keyVersion;
		}
		
		this.alg = alg;
	}
	
	/**
	 * 
	 * @param keyBytes
	 * @param keyVersion
	 */
	private void setKeyVersion(byte[] keyBytes, int keyVersion){
		
		if(keyBytes == null) throw new NullPointerException();
		if(keyBytes.length % 8 != 0) throw new DFLException(ExType.SECURITY_EXCEPTION);
		if((keyVersion < 0) && (keyVersion > 255)) throw new DFLException(ExType.SECURITY_EXCEPTION);
		
		int count = keyBytes.length / 8;
		
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
        	
        	aux = BAUtils.extractSubBA(keyBytes, 8*i, 8);
        	aux = BAUtils.and(aux, mask2);
        	aux = BAUtils.xor(aux, ver);
        	verKey = BAUtils.concatenateBAs(verKey, aux);
        }

        this.keyBytes = new byte[verKey.length];
        
        System.arraycopy(verKey, 0,  this.keyBytes, 0, verKey.length);
		this.keyVersion = keyVersion;
		
	}
	
	/**
	 * 
	 * @return
	 */
	public byte[] getKeyBytes(){ return this.keyBytes; }
	/**
	 * 
	 * @return
	 */
	public int getKeyVersion(){ return this.keyVersion; }
	/**
	 * 
	 * @return
	 */
	public CipAlg getAlg(){ return this.alg; }
	
	/**
	 * 
	 */
	public String toString(){
		
		String s = "";
		
		s = s + "Key Data: 0x" + BAUtils.toString(this.keyBytes) + "\n";
		s = s + "Key Version number " + this.keyVersion + "\n";
		s = s + "Key crypto algorithm: " + this.alg.toString();
		
		
		return s;		
				
	}
	
	private byte[] keyBytes;
	private CipAlg alg;
	private int keyVersion;
	
}
