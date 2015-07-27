package dflibrary.utils.security;

import dflibrary.utils.ba.*;

import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;

/**
 * Provides methods for the computation of cryptographic operations 
 * @author Francisco Rodriguez Algarra
 *
 */
public class Crypto {

    /**
     * Encodes <code>data</code>
     * @param data a byte array
     * @param keyData a byte array containing the value of the key to use for
     * encoding
     * @param chain an instance of class <code>ChainMode</code> indicating
     * how the differenc cryptographic blocks should be chained
     * @param alg an instance of class <code>CipAlg</code> indicating
     * the cryptographic algorithm to use in the encoding
     * @return the encoded version of <code>data</code>
     */
    public static byte[] encode(byte[] data, byte[] keyData, 
            ChainMode chain, CipAlg alg) 
        throws GeneralSecurityException{		
		
	return encode(data, keyData, new byte[alg.getBlockLength()], 
                chain, alg, PaddingMode.ZEROPadding);
		
    }
	
    /**
     * Encodes <code>data</code>
     * @param data a byte array
     * @param keyData a byte array containing the value of the key to use for
     * encoding
     * @param iv a byte array containing the initial vector to use in
     * the encoding operation
     * @param chain an instance of class <code>ChainMode</code> indicating
     * how the differenc cryptographic blocks should be chained
     * @param alg an instance of class <code>CipAlg</code> indicating
     * the cryptographic algorithm to use in the encoding
     * @param pad an instance of class <code>PaddingMode</code> indicating
     * how blocks should be padded if needed
     * @return the encoded version of <code>data</code>
     */
    public static byte[] encode(byte[] data, byte[] keyData, 
            byte[] iv, ChainMode chain, CipAlg alg, PaddingMode pad)
        throws GeneralSecurityException{
		
	if((data == null) || (keyData == null) || (iv == null) ||
                (chain == null) || (alg == null) || (pad == null))
            throw new NullPointerException();
        
        if(!checkKeyLength(keyData, alg)) 
            throw new InvalidKeyException();
        
        byte[] paddedData = padding(data, alg.getBlockLength(), pad);
		
	return chainedCipher(paddedData, keyData, iv, chain, alg);
    	
    }
    
    private static byte[] chainedCipher(byte[] data, byte[] keyData, 
            byte[] iv, ChainMode chain, CipAlg alg) 
        throws GeneralSecurityException{
		
        if((data == null) || (keyData == null) || (iv == null))
            throw new NullPointerException();
		
	int blen = alg.getBlockLength();

	int numOfBlocks = data.length/blen;
		
	CipMode mode;
		
	if(chain == ChainMode.CBCSendISO) mode = CipMode.ENC;
	else mode = CipMode.DEC;
		
	byte[] pt, ct;
	byte[] out = new byte[0];
		
	if((chain == ChainMode.CBCSendISO) ||
                (chain == ChainMode.CBCSendDF)){
			
	    for(int i = 0; i < numOfBlocks; i++){
				
	        pt = BAUtils.xor(
                        BAUtils.extractSubBA(data,  i*blen, blen), iv);
	        ct = blockCipher(pt, keyData, alg, mode);
	        iv = ct;
	        out = BAUtils.concatenateBAs(out, ct);				
				
	    }
	    	
	}
	else{
			
	    for(int i = 0; i < numOfBlocks; i++){
                
                ct = BAUtils.extractSubBA(data, i*blen, blen);
		pt = BAUtils.xor(blockCipher(ct, keyData, alg, mode), iv);
		iv = ct;
	        out = BAUtils.concatenateBAs(out, pt);				
				
	    }
			
	}
		
	return out;
		
		
    }
	
    private static byte[] blockCipher(byte[] block, byte[] keyData,
            CipAlg alg, CipMode mode)
        throws GeneralSecurityException{
		
        if((alg == CipAlg.DES) || (alg == CipAlg.AES))
            return cipher(block, keyData, alg, mode);
	else{
			
	    byte[] k1, k2, k3;
			
	    k1 = BAUtils.extractSubBA(keyData, 0, 8);
	    k2 = BAUtils.extractSubBA(keyData, 8, 8);
			
	    if(alg == CipAlg.TDEA2){
		k3 = new byte[8];
		System.arraycopy(k1, 0, k3, 0, 8);
	    }
	    else k3 = BAUtils.extractSubBA(keyData, 16, 8);
			
	    byte[] ct;
			
	    if(mode == CipMode.DEC){

		ct = cipher(block, k3, alg, CipMode.DEC);
		ct = cipher(ct, k2, alg, CipMode.ENC);
	        ct = cipher(ct, k1, alg, CipMode.DEC);
				
	    }
	    else{

		ct = cipher(block, k1, alg, CipMode.ENC);
		ct = cipher(ct, k2, alg, CipMode.DEC);
		ct = cipher(ct, k3, alg, CipMode.ENC);
			
            }
			
	    return ct;
	}
    	
    }
	
    private static byte[] cipher(byte[] block, byte[] keyData,
            CipAlg alg, CipMode mode)
        throws GeneralSecurityException{
		
	String cipAlg;
		
	if(alg == CipAlg.AES) cipAlg = "AES";
	else cipAlg = "DES";
		
	Cipher cipher = Cipher.getInstance(cipAlg + "/CBC/NoPadding");
		
	byte[] IV = new byte[block.length];
	IvParameterSpec iv = new IvParameterSpec(IV);
		
	int keyLength;
		
	if(alg == CipAlg.AES) keyLength = 16;
	else keyLength = 8;
		
	SecretKeySpec keySpec = new SecretKeySpec(
                BAUtils.extractSubBA(keyData, 0, keyLength), cipAlg);
		
	if(mode == CipMode.ENC)
            cipher.init(Cipher.ENCRYPT_MODE,keySpec, iv);
	else cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);
		
	return cipher.doFinal(block, 0, block.length);
    }
	
    /**
     * Checks whether the length of a key is the appropriate for a
     * given cryptographic algorithm
     * @param keyData a byte array containing the value of a key
     * @param alg a instance of class <code>CipAlg</code> indicating
     * the cryptographic algorithm to use
     * @return <code>true</code> if the length of <code>keyData</code>
     * is appropriate for the algorithm indicated by <code>alg</code>;
     * <code>false</code> otherwise
     */
    public static boolean checkKeyLength(byte[] keyData, CipAlg alg){
		
    	int len = keyData.length;
		
	if(alg == CipAlg.AES){
	    if(len == 16) return true;
	    return false;
	}
	else if(alg == CipAlg.TDEA3){
	    if(len == 24) return true;
	    return false;
	}
        else{
			
	    byte[] k1, k2, k3;
			
	    if(len == 24){
				
	        k1 = BAUtils.extractSubBA(keyData, 0, 8);
		k3 = BAUtils.extractSubBA(keyData, 16, 8);
				
		boolean k1k3 = BAUtils.compareBAs(k1, k3);
				
	        if(!k1k3) return false;
				
		if(alg == CipAlg.TDEA2) return true;
		
		k2 = BAUtils.extractSubBA(keyData, 8, 8);
				
		return BAUtils.compareBAs(k1, k2);			
				
	    }
	    else if(len == 16){
				
		if(alg == CipAlg.TDEA2) return true;
				
		k1 = BAUtils.extractSubBA(keyData, 0, 8);
		k2 = BAUtils.extractSubBA(keyData, 8, 8);
				
		return BAUtils.compareBAs(k1, k2);	
				
	    }
	    else if(len == 8){
				
		if(alg == CipAlg.DES) return true;
		else return false;
				
	    }
	    else return false;
			
	}
    
    }
	
    //Padding
	
    /**
     * Fills a byte array according to the indicated padding mode
     * @param data a byte array to be padded
     * @param blockLength an int indicating the desired length of the blocks
     * @param mode an instance of class <code>PaddingMode</code> indicating
     * how the extra bytes should be filled
     * @return a padded version of <code>data</code>
     */
    public static byte[] padding(byte[] data, int blockLength,
            PaddingMode mode){
		
	int res = data.length % blockLength;
		
	if(res == 0) return data;
		
	byte[] pad = new byte[0];
		
	if(mode == PaddingMode.ZEROPadding){
		
	    pad = new byte[blockLength - res];
		
	}
	else if(mode == PaddingMode.EIGHTPadding){
			
	    pad = BAUtils.concatenateBAs(BAUtils.toBA("80"), 
                    new byte[blockLength - res - 1]);
			
    	}
		
	return BAUtils.concatenateBAs(data, pad);
	
    }

}
