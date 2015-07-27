package dflibrary.utils.security;

import dflibrary.library.DFLException;
import dflibrary.library.DFLException.ExType;
import dflibrary.utils.ba.BAUtils;

/**
 * Provides methods to compute and manage Message Authentication Codes
 * @author Francisco Rodriguez Algarra
 */
public class MAC {

    /**
     * Computes the CMAC of the message <code>M</code>
     * @param M a byte array
     * @param keyData a byte array containing the data of the key
     * @param iv a byte array representing the CMAC initial vector
     * @param alg an instance of class <code>CipAlg</code> representing the
     * cryptographic algorithm to use
     * @return a byte array representing the CMAC
     * of the message <code>M</code>
     */
    public static byte[] cmac(byte[] M, byte[] keyData, byte[] iv, 
            CipAlg  alg){
		
	return cmac(M, keyData, iv, alg, alg.getBlockLength());
		
    }
	
    /**
     * Computes the CMAC of the message <code>M</code>
     * @param M a byte array
     * @param keyData a byte array containing the data of the key
     * @param iv a byte array representing the CMAC initial vector
     * @param alg an instance of class <code>CipAlg</code> representing the
     * cryptographic algorithm to use
     * @param padLength the length of the padding needed
     * @return a byte array representing the CMAC
     * of the message <code>M</code>
     */
    public static byte[] cmac(byte[] M, byte[] keyData, byte[] iv, 
            CipAlg  alg, int padLength){
			
	byte[] T = new byte[0];
		
	boolean pad;
    	byte[] M2;
		
	int blockLength = alg.getBlockLength();
	int res;
		
	if(M.length == 0){
			
	    pad = true;
	    M2 = BAUtils.concatenateBAs(BAUtils.toBA("80"),
                    new byte[padLength - 1]);
			
	}
	else{
			
	    if((res = M.length % padLength) != 0){

	        pad = true;
		M2 = BAUtils.concatenateBAs(M, BAUtils.toBA("80"), 
                        new byte[padLength - res -1]);
				
	    }
	    else{
				
		pad = false;
		M2 = BAUtils.extractSubBA(M, 0, M.length);
				
	    }
								
	}
		
	byte[] L;
		
	try{
	    L = Crypto.encode(new byte[blockLength], keyData, 
                    new byte[blockLength], ChainMode.CBCSendISO, 
                    alg, PaddingMode.ZEROPadding);
	    }catch(Exception e){
		throw new DFLException(ExType.SECURITY_EXCEPTION);
	    }
			
	byte[] sk;
		
	if(pad){
	    sk = getSubK(getSubK(L));
	}
	else sk = getSubK(L);
		
	byte[] M31 = BAUtils.extractSubBA(M2, 0, M2.length - blockLength);
	byte[] M32 = BAUtils.xor(
                BAUtils.extractSubBA(M2, M2.length - blockLength,
                    blockLength), sk);
		
	byte[] M3 = BAUtils.concatenateBAs(M31, M32);
		
	byte [] cip;
		
	try{
	    cip = Crypto.encode(M3, keyData, iv, ChainMode.CBCSendISO,
                    alg, PaddingMode.ZEROPadding);
	}catch(Exception e){
	    throw new DFLException(ExType.SECURITY_EXCEPTION);
	}
		
	T = BAUtils.extractSubBA(cip, cip.length - blockLength , blockLength);
		
	return T;	
			
    }
	
    private static byte[] getSubK(byte[] L){
		
	byte[] k;
		
	byte[] msb = BAUtils.extractSubBA(L, 0, 1);
		
	boolean b = BAUtils.compareBAs(
                BAUtils.and(msb, BAUtils.toBA("80")), 
                new byte[1]);
		
	if(b) k = lShift(L);
	else {
	    byte[] Rb;
	    if(L.length == 8) 
                Rb = BAUtils.concatenateBAs(new byte[7], BAUtils.toBA("1B"));
	    else 
                Rb = BAUtils.concatenateBAs(new byte[15], BAUtils.toBA("87"));
	    k = BAUtils.xor(lShift(L), Rb);
	}
		
	return k;	
	
    }

    private static byte[] lShift(byte[] ba){
    	
        boolean msb = false;
    	byte[] aux;
    	byte[] res = BAUtils.extractSubBA(ba, 0, ba.length);
   
    	for(int i = ba.length - 1; i >= 0; i--){
    		
            res[i] = (byte) ((int) res[i] << 1);
    		
    	    if(msb) res[i] = (byte) ((int)res[i] | (byte)1);
    		
    	    aux = BAUtils.and(BAUtils.extractSubBA(ba, i, 1), 
                    BAUtils.toBA("80"));
    	    if(BAUtils.compareBAs(aux, new byte[1])) msb = false;
    	    else msb = true;
    		
    	}
    	
    	return res;
    	
    }
	
    /**
     * Computes the MAC of the byte array <code>data</code> 
     * @param data a byte array
     * @param keyData a byte array containing the data of the key
     * @param alg an instance of class <code>CipAlg</code> representing the
     * cryptographic algorithm to use     
     * @return a byte array representing the MAC of <code>data</code>
     */
    public static byte[] mac(byte[] data, byte[] keyData, CipAlg alg){

        try{
		
	    byte[] enc = Crypto.encode(data, keyData, 
                    ChainMode.CBCSendISO, alg);
	    return BAUtils.extractSubBA(enc, enc.length - 8, 4);
		
	}catch(Exception e){
	    throw new DFLException(ExType.SECURITY_EXCEPTION);
	}

    }

}
