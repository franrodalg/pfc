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
public class BankKeyProvider {

	/**
	 * 
	 * @param keyVersion
	 * @param alg
	 * @param bankID
	 * @return
	 */
	public static DFKey getKey(int keyVersion, CipAlg alg, int bankID){		
		
		if(keyVersion == 0) return getKey(keyVersion, alg);
		
		return bankKeyDiv(getKey(keyVersion, alg), bankID);
		
		
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
	 * @return
	 */
	private static DFKey bankKeyDiv(DFKey key, int bankID){
		
		byte[] CT = BAUtils.toBA("88");
		byte[] bankIDBA= BAUtils.toBA(bankID, 2);
		
		byte[] divInput = BAUtils.concatenateBAs(CT, new byte[5], bankIDBA);
		
		byte[] keyData = key.getKeyBytes();
		
		try{
			byte[] divKey = Crypto.encode(keyData, keyData, divInput, 
					ChainMode.CBCSendISO, key.getAlg(), PaddingMode.ZEROPadding);
			return new DFKey(divKey, key.getAlg(), key.getKeyVersion());
		
		}catch(Exception e){
			throw new DFLException(ExType.SECURITY_EXCEPTION);
		}
		
	}
	
	
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
