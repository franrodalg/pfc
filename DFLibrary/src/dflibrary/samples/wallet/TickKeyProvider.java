package dflibrary.samples.wallet;

import dflibrary.library.DFLException;
import dflibrary.library.DFLException.ExType;
import dflibrary.library.param.*;
import dflibrary.library.security.*;
import dflibrary.utils.security.*;
import dflibrary.utils.ba.BAUtils;

/**
 * 
 * @author Francisco Rodríguez Algarra
 *
 */
public class TickKeyProvider {

	public static DFKey getKey(int keyVersion, CipAlg alg, UID uid, int keyNum, AID aid, boolean legacy){
		
		if(aid.toInt() == WTModel.TICK_AID) return keyDiv(getKey(keyVersion, alg), uid, keyNum, aid, legacy);
		
		return getKey(keyVersion, alg);
				
	}		
	
	/**
	 * 
	 * @param keyVersion
	 * @param alg
	 * @return
	 */
	public static DFKey getKey(int keyVersion, CipAlg alg){
		
		byte[] keyBytes;
		
		switch (keyVersion){
			case 0: 
				keyBytes = new byte[24];
				break;
			case 1:
				keyBytes = BAUtils.toBA(ONE);
				break;
			case 2:
				keyBytes = BAUtils.toBA(TWO);
				break;
			case 3: 
				keyBytes = BAUtils.toBA(THREE);
				break;
			case 4:
				keyBytes = BAUtils.toBA(FOUR);
				break;
			case 5:
				keyBytes = BAUtils.toBA(FIVE);
				break;
			case 6: 
				keyBytes = BAUtils.toBA(SIX);
				break;
			case 7:
				keyBytes = BAUtils.toBA(SEVEN);
				break;
			case 8:
				keyBytes = BAUtils.toBA(EIGHT);
				break;
			case 9:
				keyBytes = BAUtils.toBA(NINE);
				break;
			default: return null;
		}
		
		int len = alg.getKeyLength();
		
		byte[] aux = BAUtils.extractSubBA(keyBytes, 0, len);
		
		if(len == 8){
			keyBytes = BAUtils.concatenateBAs(aux, aux);
			return new DFKey(keyBytes, alg, keyVersion);
		}
		else{
			return new DFKey(aux, alg, keyVersion);
		}
				
	}
	
	/**
	 * 
	 * @param key
	 * @param uid
	 * @param keyNum
	 * @param aid
	 * @param legacy
	 * @return
	 */
	private static DFKey keyDiv(DFKey key, UID uid, int keyNum, AID aid, boolean legacy){
		
		if(legacy) return uidKeyDiv(key, uid);
		else return cmacKeyDiv(key, uid, keyNum, aid);
		
	}
	
	/**
	 * 
	 * @param key
	 * @param uid
	 * @return
	 */
	private static DFKey uidKeyDiv(DFKey key, UID uid){
		
		byte[] CT = BAUtils.toBA("88");
		
		byte[] divInput = BAUtils.concatenateBAs(CT, uid.toBA());
		
		byte[] keyData = key.getKeyData();
		
		try{
			byte[] divKey = Crypto.encode(keyData, keyData, divInput, ChainMode.CBCSendISO, key.getAlg(), PaddingMode.ZEROPadding);
			return new DFKey(divKey, key.getAlg(), key.getKeyVersion());
		
		}catch(Exception e){
			throw new DFLException(ExType.SECURITY_EXCEPTION);
		}
		
	}
	
	/**
	 * 
	 * @param key
	 * @param uid
	 * @param keyNum
	 * @param aid
	 * @return
	 */
	private static DFKey cmacKeyDiv(DFKey key, UID uid, int keyNum, AID aid){
		
		byte[] divInput = BAUtils.concatenateBAs(uid.toBA(), BAUtils.toBA(keyNum, 1), aid.toBA());
		
		if(key.getAlg() == CipAlg.AES) return aesCmacKeyDiv(key, divInput);
		else if(key.getAlg() == CipAlg.TDEA3) return tdea3CmacKeyDiv(key, divInput);
		else return tdea2CmacKeyDiv(key, divInput);
				
	}
	
	/**
	 * 
	 * @param key
	 * @param divInput
	 * @return
	 */
	private static DFKey aesCmacKeyDiv(DFKey key, byte[] divInput){
		
		byte[] aux = BAUtils.concatenateBAs(AES_DIV_CONSTANT, divInput);
		
		byte[] divKey = MAC.cmac(aux, key.getKeyData(), new byte[16], key.getAlg(), 32);
		
		return new DFKey(divKey, key.getAlg(), key.getKeyVersion());
		
	}
	
	/**
	 * 
	 * @param key
	 * @param divInput
	 * @return
	 */
	private static DFKey tdea2CmacKeyDiv(DFKey key, byte[] divInput){
		
		byte[] aux1 = BAUtils.concatenateBAs(TDEA2_DIV_CONSTANT_1, divInput);
		byte[] aux2 = BAUtils.concatenateBAs(TDEA2_DIV_CONSTANT_2, divInput);
		
		byte[] divKey1 = MAC.cmac(aux1, key.getKeyData(), new byte[8], key.getAlg(), 16);
		byte[] divKey2 = MAC.cmac(aux2, key.getKeyData(), new byte[8], key.getAlg(), 16);
		
		return new DFKey(BAUtils.concatenateBAs(divKey1, divKey2), key.getAlg(), key.getKeyVersion());
		
	}
	
	/**
	 * 
	 * @param key
	 * @param divInput
	 * @return
	 */
	private static DFKey tdea3CmacKeyDiv(DFKey key, byte[] divInput){
		
		byte[] aux1 = BAUtils.concatenateBAs(TDEA3_DIV_CONSTANT_1, divInput);
		byte[] aux2 = BAUtils.concatenateBAs(TDEA3_DIV_CONSTANT_2, divInput);
		byte[] aux3 = BAUtils.concatenateBAs(TDEA3_DIV_CONSTANT_3, divInput);
		
		byte[] divKey1 = MAC.cmac(aux1, key.getKeyData(), new byte[8], key.getAlg(), 16);
		byte[] divKey2 = MAC.cmac(aux2, key.getKeyData(), new byte[8], key.getAlg(), 16);
		byte[] divKey3 = MAC.cmac(aux3, key.getKeyData(), new byte[8], key.getAlg(), 16);
		
		return new DFKey(BAUtils.concatenateBAs(divKey1, divKey2, divKey3), key.getAlg(), key.getKeyVersion());
		
	}
	
	private static byte[] AES_DIV_CONSTANT = BAUtils.toBA("01");
	private static byte[] TDEA2_DIV_CONSTANT_1 = BAUtils.toBA("21");
	private static byte[] TDEA2_DIV_CONSTANT_2 = BAUtils.toBA("22");
	private static byte[] TDEA3_DIV_CONSTANT_1 = BAUtils.toBA("31");
	private static byte[] TDEA3_DIV_CONSTANT_2 = BAUtils.toBA("32");
	private static byte[] TDEA3_DIV_CONSTANT_3 = BAUtils.toBA("33");
	
	private static final String ONE = "00112233445566778899AABBCCDDEEFFFFEEDDCCBBAA9988";
	private static final String TWO = "0022446688AACCEE1133557799BBDDFFFFDDBB9977553311";
	private static final String THREE = "000102030405060708090A0B0C0D0E0FF0E0D0C0B0A09080";
	private static final String FOUR = "00020406080A0C0E01030507090B0D0FF0D0B09070503010";
	private static final String FIVE = "FFEEDDCCBBAA998877665544332211000011223344556677";
	private static final String SIX = "FFDDBB9977553311EECCAA88664422000022446688AACCEE";
	private static final String SEVEN = "F0E0D0C0B0A0908070605040302010000001020304050607";
	private static final String EIGHT = "F0D0B09070503010E0C0A0806040200000020406080A0C0E";
	private static final String NINE = "00112233445566778899AABBCCDDEEFF0102030405060708";
	
}
