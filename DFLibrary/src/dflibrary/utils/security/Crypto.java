package dflibrary.utils.security;

import dflibrary.utils.ba.*;

import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;

/**
 * 
 * @author Francisco Rodríguez Algarra
 *
 */
public class Crypto {

	/**
	 * 
	 * @param data
	 * @param keyData
	 * @param chain
	 * @param alg
	 * @return
	 */
	public static byte[] encode(byte[] data, byte[] keyData, ChainMode chain, CipAlg alg)
		throws GeneralSecurityException{		
		
		return encode(data, keyData, new byte[alg.getBlockLength()], chain, alg, PaddingMode.ZEROPadding);
		
	}
	
	/**
	 * 
	 * @param data
	 * @param keyData
	 * @param iv
	 * @param chain
	 * @param alg
	 * @param pad
	 * @return
	 */
	public static byte[] encode(byte[] data, byte[] keyData, byte[] iv, ChainMode chain, CipAlg alg, PaddingMode pad)
		throws GeneralSecurityException{
		
		if((data == null) || (keyData == null) || (iv == null) || (chain == null) || (alg == null) || (pad == null))
			throw new NullPointerException();
		
		if(!checkKeyLength(keyData, alg)) throw new InvalidKeyException();
		
		byte[] paddedData = padding(data, alg.getBlockLength(), pad);
		
		return chainedCipher(paddedData, keyData, iv, chain, alg);
		
	}
	
	/**
	 * 
	 * @param data
	 * @param keyData
	 * @param iv
	 * @param chain
	 * @param alg
	 * @return
	 */
	private static byte[] chainedCipher(byte[] data, byte[] keyData, byte[] iv, ChainMode chain, CipAlg alg)
			throws GeneralSecurityException{
		
		if((data == null) || (keyData == null) || (iv == null)) throw new NullPointerException();
		
		int blen = alg.getBlockLength();

		int numOfBlocks = data.length/blen;
		
		CipMode mode;
		
		if(chain == ChainMode.CBCSendISO) mode = CipMode.ENC;
		else mode = CipMode.DEC;
		
		byte[] pt, ct;
		byte[] out = new byte[0];
		
		if((chain == ChainMode.CBCSendISO) || (chain == ChainMode.CBCSendDF)){
			
			for(int i = 0; i < numOfBlocks; i++){
				
				pt = BAUtils.xor(BAUtils.extractSubBA(data,  i*blen, blen), iv);
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
	
	/**
	 * 
	 * @param block
	 * @param keyData
	 * @param alg
	 * @param mode
	 * @return
	 * @throws GeneralSecurityException
	 */
	private static byte[] blockCipher(byte[] block, byte[] keyData, CipAlg alg, CipMode mode)
		throws GeneralSecurityException{
		
		if((alg == CipAlg.DES) || (alg == CipAlg.AES)) return cipher(block, keyData, alg, mode);
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
	
	/**
	 * 
	 * @param block
	 * @param keyData
	 * @param alg
	 * @param mode
	 * @return
	 * @throws GeneralSecurityException
	 */
	private static byte[] cipher(byte[] block, byte[] keyData, CipAlg alg, CipMode mode)
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
		
		SecretKeySpec keySpec = new SecretKeySpec(BAUtils.extractSubBA(keyData, 0, keyLength), cipAlg);
		
		if(mode == CipMode.ENC) cipher.init(Cipher.ENCRYPT_MODE,keySpec, iv);
		else cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);
		
		return cipher.doFinal(block, 0, block.length);
	}
	
	/**
	 * 
	 * @param keyData
	 * @param alg
	 * @return
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
	 * 
	 * @param data
	 * @param blockLength
	 * @param mode
	 * @return
	 */
	public static byte[] padding(byte[] data, int blockLength, PaddingMode mode){
		
		int res = data.length % blockLength;
		
		if(res == 0) return data;
		
		byte[] pad = new byte[0];
		
		if(mode == PaddingMode.ZEROPadding){
		
			pad = new byte[blockLength - res];
		
		}
		else if(mode == PaddingMode.EIGHTPadding){
			
			pad = BAUtils.concatenateBAs(BAUtils.toBA("80"), new byte[blockLength - res - 1]);
			
		}
		
		return BAUtils.concatenateBAs(data, pad);
	}
	
	
}
