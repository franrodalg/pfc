package dflibrary.utils.security;

import dflibrary.library.ComCode;
import dflibrary.library.DFLException;
import dflibrary.library.SC;
import dflibrary.library.DFLException.ExType;
import dflibrary.utils.ba.BAUtils;


/**
 * 
 * @author Francisco Rodríguez Algarra
 *
 */
public class MAC {

	/**
	 * 
	 * @param M
	 * @param keyData
	 * @param iv
	 * @param alg
	 * @return
	 */
	public static byte[] cmac(byte[] M, byte[] keyData, byte[] iv, CipAlg  alg){
		
		return cmac(M, keyData, iv, alg, alg.getBlockLength());
		
	}
	
	/**
	 * 
	 * @param M
	 * @param keyData
	 * @param iv
	 * @param alg
	 * @param padLength
	 * @return
	 */
	public static byte[] cmac(byte[] M, byte[] keyData, byte[] iv, CipAlg  alg, int padLength){
		
		
		byte[] T = new byte[0];
		
		boolean pad;
		byte[] M2;
		
		int blockLength = alg.getBlockLength();
		int res;
		
		if(M.length == 0){
			
			pad = true;
			M2 = BAUtils.concatenateBAs(BAUtils.toBA("80"), new byte[padLength - 1]);
			
		}
		else{
			
			if((res = M.length % padLength) != 0){

				pad = true;
				M2 = BAUtils.concatenateBAs(M, BAUtils.toBA("80"), new byte[padLength - res -1]);
				
			}
			else{
				
				pad = false;
				M2 = BAUtils.extractSubBA(M, 0, M.length);
				
			}
						
			
		}
		
		byte[] L;
		
		try{
			L = Crypto.encode(new byte[blockLength], keyData, new byte[blockLength], ChainMode.CBCSendISO, alg, PaddingMode.ZEROPadding);
		}catch(Exception e){
			throw new DFLException(ExType.SECURITY_EXCEPTION);
		}
			
		byte[] sk;
		
		if(pad){
			sk = getSubK(getSubK(L));
		}
		else sk = getSubK(L);
		
		byte[] M31 = BAUtils.extractSubBA(M2, 0, M2.length - blockLength);
		byte[] M32 = BAUtils.xor(BAUtils.extractSubBA(M2, M2.length - blockLength, blockLength), sk);
		
		byte[] M3 = BAUtils.concatenateBAs(M31, M32);
		
		byte [] cip;
		
		try{
			cip = Crypto.encode(M3, keyData, iv, ChainMode.CBCSendISO, alg, PaddingMode.ZEROPadding);
		}catch(Exception e){
			throw new DFLException(ExType.SECURITY_EXCEPTION);
		}
		
		T = BAUtils.extractSubBA(cip, cip.length - blockLength , blockLength);
		
		return T;
		
		
		
	}
	
	/**
	 * 
	 * @param L
	 * @return
	 */
	private static byte[] getSubK(byte[] L){
		
		byte[] k;
		
		byte[] msb = BAUtils.extractSubBA(L, 0, 1);
		
		boolean b = BAUtils.compareBAs(BAUtils.and(msb, BAUtils.toBA("80")), new byte[1]);
		
		if(b) k = lShift(L);
		else {
			byte[] Rb;
			if(L.length == 8) Rb = BAUtils.concatenateBAs(new byte[7], BAUtils.toBA("1B"));
			else Rb = BAUtils.concatenateBAs(new byte[15], BAUtils.toBA("87"));
			k = BAUtils.xor(lShift(L), Rb);
		}
		
		return k;
		
	}
	
	/**
	 * 
	 * @param ba
	 * @return
	 */
	private static byte[] lShift(byte[] ba){
    	
    	boolean msb = false;
    	byte[] aux;
    	byte[] res = BAUtils.extractSubBA(ba, 0, ba.length);
   
    	
    	for(int i = ba.length - 1; i >= 0; i--){
    		
    		res[i] = (byte) ((int) res[i] << 1);
    		
    		if(msb) res[i] = (byte) ((int)res[i] | (byte)1);
    		
    		aux = BAUtils.and(BAUtils.extractSubBA(ba, i, 1), BAUtils.toBA("80"));
    		if(BAUtils.compareBAs(aux, new byte[1])) msb = false;
    		else msb = true;
    		
    	}
    	
    	return res;
    	
    }
	
	/**
	 * 
	 * @param data
	 * @param keyData
	 * @param alg
	 * @return
	 */
	public static byte[] mac(byte[] data, byte[] keyData, CipAlg alg){

		try{
		
			byte[] enc = Crypto.encode(data, keyData, ChainMode.CBCSendISO, alg);
			return BAUtils.extractSubBA(enc, enc.length - 8, 4);
		
		}catch(Exception e){
			throw new DFLException(ExType.SECURITY_EXCEPTION);
		}

	}

	public static void main(String[] args){
		
		byte[] keyData = BAUtils.toBA("B234E7173EC52E16B234E7173EC52E16");
		
		byte[] com;
		byte[] res;
		byte[] rcmac, chcmac;
		
		
		
		com = BAUtils.concatenateBAs(ComCode.FORMAT_PICC.toBA());
		rcmac = cmac(com, keyData, new byte[8], CipAlg.TDEA2);
		System.out.println(BAUtils.toString(rcmac));
		
		
		
		res = BAUtils.concatenateBAs(SC.OPERATION_OK.toBA());
		rcmac = cmac(res, keyData, rcmac, CipAlg.TDEA2);
		System.out.println(BAUtils.toString(rcmac));
		
		chcmac = BAUtils.toBA("56252592EDD68174");
		
		if(BAUtils.compareBAs(chcmac, rcmac)) System.out.println("OKKKKKKKKKKKKKKK!!!!!!!");
		else System.out.println("KO :(");
		
		
		/*
		
		com = BAUtils.toBA("FC");
		rcmac = cmac(com, keyData, rcmac, CipAlg.TDEA2);
		System.out.println(BAUtils.toString(rcmac));
		
		res = BAUtils.toBA("00");
		rcmac = cmac(res, keyData, rcmac, CipAlg.TDEA2);
		System.out.println(BAUtils.toString(rcmac));
		
		chcmac = BAUtils.toBA("9255823EC36E1D05");
		
		if(BAUtils.compareBAs(chcmac, rcmac)) System.out.println("OKKKKKKKKKKKKKKK!!!!!!!");
		else System.out.println("KO :(");
	*/
	
	}
	
	/*
	
	public static void main(String[] args){
		
		byte[] keyData;
		byte[] M;
		CipAlg alg;
		
		System.out.println("D1- AES 128\n");
		
		keyData= BAUtils.toBA("2B7E151628AED2A6ABF7158809CF4F3C");
		alg = CipAlg.AES;
		
		System.out.println("D1.1\n");
		
		M = new byte[0];
		System.out.println("M: " + BAUtils.toString(M));
		System.out.println("T:" + BAUtils.toString(cmac(M, keyData, new byte[alg.getBlockLength()], alg)));		
		System.out.println();
		
		System.out.println("D1.2\n");
		
		M = BAUtils.toBA("6BC1BEE22E409F96E93D7E117393172A");
		System.out.println("M:" + BAUtils.toString(M));
		System.out.println("T:" + BAUtils.toString(cmac(M, keyData, new byte[alg.getBlockLength()], alg)));
		System.out.println();
		
		System.out.println("D1.3\n");
		
		M = BAUtils.toBA("6BC1BEE22E409F96E93D7E117393172AAE2D8A571E03AC9C9EB76FAC45AF8E5130C81C46A35CE411");
		System.out.println("M:" + BAUtils.toString(M));
		System.out.println("T:" + BAUtils.toString(cmac(M, keyData, new byte[alg.getBlockLength()], alg)));
		System.out.println();

		System.out.println("D1.4\n");
		
		M = BAUtils.toBA("6BC1BEE22E409F96E93D7E117393172AAE2D8A571E03AC9C9EB76FAC45AF8E5130C81C46A35CE411E5FBC1191A0A52EFF69F2445DF4F9B17AD2B417BE66C3710");
		System.out.println("M:" + BAUtils.toString(M));
		System.out.println("T:" + BAUtils.toString(cmac(M, keyData, new byte[alg.getBlockLength()], alg)));
		System.out.println();
		
		System.out.println("D4.- Three Key TDEA\n");
		
		keyData = BAUtils.toBA("8AA83BF8CBDA10620BC1BF19FBB6CD58BC313D4A371CA8B5");
		alg = CipAlg.TDEA3;
		
		System.out.println("D4.1\n");
		
		M = new byte[0];
		System.out.println("M: " + BAUtils.toString(M));
		System.out.println("T:" + BAUtils.toString(cmac(M, keyData, new byte[alg.getBlockLength()], alg)));		
		System.out.println();
		
		System.out.println("D4.2\n");
		
		M = BAUtils.toBA("6BC1BEE22E409F96");
		System.out.println("M: " + BAUtils.toString(M));
		System.out.println("T:" + BAUtils.toString(cmac(M, keyData, new byte[alg.getBlockLength()], alg)));		
		System.out.println();
		
		System.out.println("D4.3\n");
		
		M = BAUtils.toBA("6BC1BEE22E409F96E93D7E117393172AAE2D8A57");
		System.out.println("M: " + BAUtils.toString(M));
		System.out.println("T:" + BAUtils.toString(cmac(M, keyData, new byte[alg.getBlockLength()], alg)));		
		System.out.println();
		
		System.out.println("D4.4\n");
		
		M = BAUtils.toBA("6BC1BEE22E409F96E93D7E117393172AAE2D8A571E03AC9C9EB76FAC45AF8E51");
		System.out.println("M: " + BAUtils.toString(M));
		System.out.println("T:" + BAUtils.toString(cmac(M, keyData, new byte[alg.getBlockLength()], alg)));		
		System.out.println();
		
		System.out.println("D5.- Two Key TDEA\n");
		
		keyData = BAUtils.toBA("4CF15134A2850DD58A3D10BA80570D38");
		alg = CipAlg.TDEA2;
		
		System.out.println("D5.1\n");
		
		M = new byte[0];
		System.out.println("M: " + BAUtils.toString(M));
		System.out.println("T:" + BAUtils.toString(cmac(M, keyData, new byte[alg.getBlockLength()], alg)));		
		System.out.println();
		
		System.out.println("D5.2\n");
		
		M = BAUtils.toBA("6BC1BEE22E409F96");
		System.out.println("M: " + BAUtils.toString(M));
		System.out.println("T:" + BAUtils.toString(cmac(M, keyData, new byte[alg.getBlockLength()], alg)));		
		System.out.println();
		
		System.out.println("D5.3\n");
		
		M = BAUtils.toBA("6BC1BEE22E409F96E93D7E117393172AAE2D8A57");
		System.out.println("M: " + BAUtils.toString(M));
		System.out.println("T:" + BAUtils.toString(cmac(M, keyData, new byte[alg.getBlockLength()], alg)));		
		System.out.println();
		
		System.out.println("D5.4\n");
		
		M = BAUtils.toBA("6BC1BEE22E409F96E93D7E117393172AAE2D8A571E03AC9C9EB76FAC45AF8E51");
		System.out.println("M: " + BAUtils.toString(M));
		System.out.println("T:" + BAUtils.toString(cmac(M, keyData, new byte[alg.getBlockLength()], alg)));		
		System.out.println();
		

		
		System.out.println("Ejemplos AN10922");
		
		keyData = BAUtils.toBA("00112233445566778899AABBCCDDEEFF");
		alg = CipAlg.AES;
		
		M = BAUtils.toBA("0104782E21801D803042F54E585020416275");
		
		System.out.println("T:" + BAUtils.toString(cmac(M, keyData, new byte[alg.getBlockLength()], alg)));
		
		System.out.println();
		
		keyData = BAUtils.toBA("00112233445566778899AABBCCDDEEFF");
		alg = CipAlg.TDEA2;
		
		M = BAUtils.toBA("2104782E21801D803042F54E58502041");
		
		System.out.println("T:" + BAUtils.toString(cmac(M, keyData, new byte[alg.getBlockLength()], alg)));
		
		System.out.println();
		
		M = BAUtils.toBA("2204782E21801D803042F54E58502041");
		
		System.out.println("T:" + BAUtils.toString(cmac(M, keyData, new byte[alg.getBlockLength()], alg)));
		
		System.out.println();
		
		keyData = BAUtils.toBA("00112233445566778899AABBCCDDEEFF0102030405060708");
		alg = CipAlg.TDEA3;
		
		M = BAUtils.toBA("3104782E21801D803042F54E5850");
		
		System.out.println("T:" + BAUtils.toString(cmac(M, keyData, new byte[alg.getBlockLength()], alg)));
		
		System.out.println();
		
		M = BAUtils.toBA("3204782E21801D803042F54E5850");
		
		System.out.println("T:" + BAUtils.toString(cmac(M, keyData, new byte[alg.getBlockLength()], alg)));
		
		System.out.println();
		
		M = BAUtils.toBA("3304782E21801D803042F54E5850");
		
		System.out.println("T:" + BAUtils.toString(cmac(M, keyData, new byte[alg.getBlockLength()], alg)));
		
		System.out.println();
	
	}
	
	*/
}
