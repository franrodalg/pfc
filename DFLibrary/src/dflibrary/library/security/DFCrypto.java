package dflibrary.library.security;

import dflibrary.library.*;
import dflibrary.library.param.*;
import dflibrary.library.param.fileset.*;
import dflibrary.library.DFLException.ExType;
import dflibrary.utils.ba.*;
import dflibrary.utils.security.*;

public class DFCrypto {
	
	

	/**
	 * 
	 * @param alg
	 * @return
	 */
	public static byte[] getRndA(CipAlg alg){
		
		if(alg == null) throw new NullPointerException();
		
		int length = 0;
		
		if((alg == CipAlg.DES) || (alg == CipAlg.TDEA2))
			length = 8;
		else if((alg == CipAlg.TDEA3) || (alg == CipAlg.AES))
			length = 16;
		
		return BAUtils.getRandomBA(length);
		
	}
	
	/**
	 * 
	 * @param ekRndB
	 * @param keyData
	 * @param alg
	 * @return
	 */
	public static byte[] getRndB(byte[] ekRndB, byte[] keyData, CipAlg alg){
		
		if((ekRndB == null) || (keyData == null) || (alg == null))
			throw new NullPointerException();
		
		try{
		
			return Crypto.encode(ekRndB, keyData, ChainMode.CBCReceiveISO, alg);
	
		}catch(Exception e){
			throw new DFLException(ExType.SECURITY_EXCEPTION);
		}
		
	}
	
	/**
	 * 
	 * @param RndA
	 * @param RndB
	 * @param keyData
	 * @param alg
	 * @return
	 */
	public static byte[] getDKRndARndBp(byte[] RndA, byte[] RndB, byte[] keyData, CipAlg alg){
		
		if((RndA == null) || (RndB == null) || (keyData == null) || (alg == null))
			throw new NullPointerException();
		
		byte[] RndBp = BAUtils.rotateBA(RndB, 1, BAUtils.Direction.LEFT);
		
		byte[] RndARndBp = BAUtils.concatenateBAs(RndA, RndBp);
		
		try{
		
			return Crypto.encode(RndARndBp, keyData, ChainMode.CBCSendDF, alg);
		
		}catch(Exception e){
			throw new DFLException(ExType.SECURITY_EXCEPTION);
		}
	
	}
	
	
	/**
	 * 
	 * @param RndA
	 * @param RndB
	 * @param keyData
	 * @param iv
	 * @param alg
	 * @return
	 */
	public static byte[] getEKRndARndBp(byte[] RndA, byte[] RndB, byte[] keyData, byte[] iv, CipAlg alg){
		
		if((RndA == null) || (RndB == null) || (keyData == null) || (iv == null) || (alg == null))
			throw new NullPointerException();
		
		byte[] RndBp = BAUtils.rotateBA(RndB, 1, BAUtils.Direction.LEFT);
		
		byte[] RndARndBp = BAUtils.concatenateBAs(RndA, RndBp);
		
		try{
		
			return Crypto.encode(RndARndBp, keyData, iv, ChainMode.CBCSendISO, alg, PaddingMode.ZEROPadding);
		
		}catch(Exception e){
			throw new DFLException(ExType.SECURITY_EXCEPTION);
		}
	
	}
	
	/**
	 * 
	 * @param RndA
	 * @param ekRndAp
	 * @param keyData
	 * @param alg
	 * @return
	 */
	public static boolean getPICCAuth(byte[] RndA, byte[] ekRndAp, byte[] keyData, CipAlg alg){
		
		if((RndA == null) || (ekRndAp == null) || (keyData == null) || (alg == null))
			throw new NullPointerException();
	
		try{
			
			byte[] RndAp = Crypto.encode(ekRndAp, keyData, ChainMode.CBCReceiveDF, alg);
		
			return BAUtils.compareBAs(BAUtils.rotateBA(RndA, 1, BAUtils.Direction.LEFT), RndAp);
			
		}catch(Exception e){
			throw new DFLException(ExType.SECURITY_EXCEPTION);
		}		
		
	}
	
	/**
	 * 
	 * @param RndA
	 * @param ekRndAp
	 * @param keyData
	 * @param iv
	 * @param alg
	 * @return
	 */
	public static boolean getPICCAuth(byte[] RndA, byte[] ekRndAp, byte[] keyData, byte[] iv, CipAlg alg){
		
		if((RndA == null) || (ekRndAp == null) || (keyData == null) || (iv == null) || (alg == null))
			throw new NullPointerException();
	
		try{
			
			byte[] RndAp = Crypto.encode(ekRndAp, keyData, iv, ChainMode.CBCReceiveISO, alg, PaddingMode.ZEROPadding);
			
			return BAUtils.compareBAs(BAUtils.rotateBA(RndA, 1, BAUtils.Direction.LEFT), RndAp);
			
		}catch(Exception e){
			throw new DFLException(ExType.SECURITY_EXCEPTION);
		}
		
		
	}
	
	/**
	 * 
	 * @param RndA
	 * @param RndB
	 * @param alg
	 * @return
	 */
	public static byte[] getSessionKey(byte[] RndA, byte[] RndB, CipAlg alg){
		
		if((RndA == null) || (RndB == null) || (alg == null))
			throw new NullPointerException();
		
		int lenA = RndA.length;
		int lenB = RndB.length;
		
		if((lenA < 8) || (lenB < 8) || (lenA % 8 != 0) || (lenB % 8 != 0)) throw new IllegalArgumentException();
		
		byte[] k1 = BAUtils.concatenateBAs(BAUtils.extractSubBA(RndA, 0, 4), BAUtils.extractSubBA(RndB, 0, 4));
		
		if(alg == CipAlg.DES) return BAUtils.concatenateBAs(k1, k1);
		
		byte[] k2 = BAUtils.concatenateBAs(BAUtils.extractSubBA(RndA, 4, 4), BAUtils.extractSubBA(RndB, 4, 4));
		
		if(alg == CipAlg.TDEA2) return BAUtils.concatenateBAs(k1, k2);
		
		if((lenA != 16) || (lenB != 16)) throw new IllegalArgumentException();
		
		byte[] k3 = BAUtils.concatenateBAs(BAUtils.extractSubBA(RndA, 6, 4), BAUtils.extractSubBA(RndB, 6, 4));
		byte[] k4 = BAUtils.concatenateBAs(BAUtils.extractSubBA(RndA, 12, 4), BAUtils.extractSubBA(RndB, 12, 4));
		
		if(alg == CipAlg.TDEA3) return BAUtils.concatenateBAs(k1, k3, k4);
		
		return BAUtils.concatenateBAs(k1, k4);
		
	}
	
	/**
	 * 
	 * @param keyNum
	 * @param alg
	 * @return
	 */
	public static byte[] getKeyNumBA(int keyNum, CipAlg alg){
		
		if(alg == null) throw new NullPointerException();
		
		byte[] keyNumBA = BAUtils.toBA(keyNum, 1);
		
		if(alg == CipAlg.TDEA3) return BAUtils.xor(keyNumBA, BAUtils.toBA("40"));
		if(alg == CipAlg.AES) return BAUtils.xor(keyNumBA, BAUtils.toBA("80"));
		
		return keyNumBA;

		
	}
	
	/**
	 * 
	 * @param res
	 * @param session
	 * @return
	 */
	public static byte[] getData(byte[] res, DFSession session){
		
		if((res == null) || (session == null)) throw new NullPointerException();
		
		AuthType auth = session.getAuthType();
		
		if((auth == AuthType.NO_AUTH)|| (auth == AuthType.TDEA_NATIVE)){
			
			return BAUtils.extractSubBA(res, 1, res.length-1);
			
		}
		
		int len;
		
		if(auth == AuthType.AES) len = 16;
		else len = 8;
		
		return BAUtils.extractSubBA(res, 1, res.length - 1 - len);
		
		
	}
	
	/**
	 * 
	 * @param numOfKeys
	 * @param ISOFidAllow
	 * @param alg
	 * @return
	 */
	public static byte[] getKeySettings2(int numOfKeys, boolean ISOFidAllow, CipAlg alg){
		
		if(alg == null) throw new NullPointerException();
		
		byte[] numOfKeysBA = BAUtils.toBA(numOfKeys, 1);
		
		byte[] ISOFidAllowBA = (ISOFidAllow) ? BAUtils.toBA("20") : BAUtils.toBA("00");
		
		byte[] algBA;
		
		if((alg == CipAlg.TDEA3)) algBA = BAUtils.toBA("40");
		else if((alg == CipAlg.AES)) algBA = BAUtils.toBA("80");
		else algBA = BAUtils.toBA("00");
		
		return BAUtils.xor(numOfKeysBA, ISOFidAllowBA, algBA);
		
	}
	
	
	/**
	 * 
	 * @param opt
	 * @param session
	 * @return
	 */
	public static byte[] getEncOptData(ConfigOption opt, DFSession session){
		
		if((opt == null) || (session == null)) throw new NullPointerException();

		byte[] optData = opt.getDataBA();
		
		byte[] crc = DFCrypto.CRC(ComCode.SET_CONFIGURATION,  opt.getOpt().toBA(), optData, session.getAuthType());
		
		byte[] optDataCrc = BAUtils.concatenateBAs(optData, crc);
		
		byte[] padOptDataCrc;
		
		if(opt.getOpt() == ConfigOptionType.ATS)
			padOptDataCrc = padding(optDataCrc, session.getAuthType(), PaddingMode.EIGHTPadding);
		else padOptDataCrc = padding(optDataCrc, session.getAuthType(), PaddingMode.ZEROPadding);
		
		byte[] encOptData = DFCrypto.encode(padOptDataCrc, session);

		return encOptData;
		
	}
	
	/**
	 * 
	 * @param fid
	 * @param offset
	 * @param data
	 * @param comSet
	 * @param ar
	 * @param com
	 * @param session
	 * @return
	 */
	public static byte[] prepareSendData(FID fid, Size offset, Data data, ComSet comSet, AccessRights ar, ComCode com, DFSession session){
		
		if((fid == null) || (offset == null) || (data == null) || (comSet == null) || (ar == null) || (com == null) || (session == null))
			throw new NullPointerException();
		
		ComSet effComSet = getSendDataEffComSet(comSet, ar, session);
		
		AuthType auth = session.getAuthType();
		
		byte[] sendData;
		
		if(effComSet == ComSet.PLAIN){
			sendData = data.toBA();
			updateCmacIV(com, BAUtils.concatenateBAs(fid.toBA(), offset.toBA(), data.getLength().toBA(), sendData), session);
		}
		else if(effComSet == ComSet.MAC){
			
			byte[] mac;
			if(auth == AuthType.TDEA_NATIVE) mac = MAC.mac(data.toBA(), session.getSessionKey(), CipAlg.TDEA2);
			else{
				updateCmacIV(com, BAUtils.concatenateBAs(fid.toBA(), offset.toBA(), data.getLength().toBA(), data.toBA()), session);
				mac = BAUtils.extractSubBA(session.getCmacIV(), 0, 8);
			}
			
			sendData = BAUtils.concatenateBAs(data.toBA(), mac);
		}
		else{
			
			byte[] crc = CRC(com, BAUtils.concatenateBAs(fid.toBA(), offset.toBA(), data.getLength().toBA()), data.toBA(), session.getAuthType());
			
			sendData = encode(BAUtils.concatenateBAs(data.toBA(), crc), session);
			
		}
		
		return sendData;
		
		
		
	}
	
	/**
	 * 
	 * @param comSet
	 * @param ar
	 * @param session
	 * @return
	 */
	public static ComSet getSendDataEffComSet(ComSet comSet, AccessRights ar, DFSession session){		
		
		if((comSet == null) || (ar == null) || (session == null))
			throw new NullPointerException();
		
		AuthType auth = session.getAuthType();
		int authKey = session.getAuthKeyNum();
		
		int rw = ar.getReadWriteAccess();
		int w = ar.getWriteAccess();
		
		
		if(auth == AuthType.NO_AUTH) return ComSet.PLAIN;

		else if((authKey == rw) || (authKey == w)){			
				return comSet;			
			}
		else return ComSet.PLAIN;					

	}
	
	/**
	 * 
	 * @param res
	 * @param comSet
	 * @param ar
	 * @param getFreeValue
	 * @param session
	 * @return
	 */
	public static DataRes getDataRes(byte[] res, Size length, ComSet comSet, AccessRights ar, DFSession session){
		
		if((res == null) || (length == null) || (comSet == null) || (ar == null) || (session == null))
			throw new NullPointerException();
		
		ComSet effComSet = getDataEffComSet(comSet, ar, session);
		
		return getDataRes(res, length, effComSet, session);
		
	}
	
	/**
	 * 
	 * @param res
	 * @param effComSet
	 * @param session
	 * @return
	 */
	private static DataRes getDataRes(byte[] res, Size length, ComSet effComSet, DFSession session){
		
		if((res == null) || (length == null) || (effComSet == null) || session == null)
			throw new NullPointerException();
		
		byte[] data;
		boolean checked;
		int len;
		
		if(length.getSize() == 0){
			
			if(effComSet == ComSet.PLAIN) len = res.length - 1;
			else if(effComSet == ComSet.MAC) len = res.length - 1 - getMacLength(session);
			else len = 0;
		}
		
		else len = length.getSize();
		
		AuthType auth = session.getAuthType();
		
		if(effComSet == ComSet.PLAIN){ 
			data = BAUtils.extractSubBA(res, 1, len);
			checked = false;
		}
		else if(effComSet == ComSet.MAC){
			
			data = BAUtils.extractSubBA(res, 1, len);
			
			if(auth == AuthType.TDEA_NATIVE){
				checked = checkMAC(res, session);		
			}
			else{
				checkCMAC(res, session);
				checked = session.getCmacOK();
			}
			
		}		
		else{
		
			byte[] plain = decode(BAUtils.extractSubBA(res, 1, res.length-1), session);
			
			if(len != 0){
				data = BAUtils.extractSubBA(plain, 0, len);
				checked = checkCRC(data, SC.OPERATION_OK, session, BAUtils.extractSubBA(plain, len, plain.length - len));			
			}
			else{
				byte[] dataCRC = extractDataCRC(plain);
				int crcLen = getCRCLength(session);
				data = BAUtils.extractSubBA(dataCRC, 0, dataCRC.length - crcLen);
				byte[] crc = BAUtils.extractSubBA(dataCRC, dataCRC.length - crcLen, crcLen);
				checked = checkCRC(data, SC.OPERATION_OK, session, crc);
				
			}
		}
		
		return new DataRes(data, checked);
		
	}
	
	
	/**
	 * 
	 * @param comSet
	 * @param ar
	 * @param getFreeValue
	 * @param session
	 * @return
	 */
	public static ComSet getDataEffComSet(ComSet comSet, AccessRights ar, DFSession session){
		
		if((comSet == null) || (ar == null) || (session == null))
			throw new NullPointerException();
		
		AuthType auth = session.getAuthType();
		int authKey = session.getAuthKeyNum();
		
		if(auth == AuthType.NO_AUTH) return ComSet.PLAIN;
		
		int r = ar.getReadAccess();
		int rw = ar.getReadWriteAccess();
		
		
		if((r == authKey) || (rw == authKey)){
			
			if((auth != AuthType.TDEA_NATIVE) && (comSet == ComSet.PLAIN)) return ComSet.MAC;
			else return comSet;
			
		}
		else{
			
			if(auth != AuthType.TDEA_NATIVE) return ComSet.MAC;
			else return ComSet.PLAIN;
			
		}

		
	}
	
	/**
	 * 
	 * @param res
	 * @return
	 */
	private static byte[] extractDataCRC(byte[] plainRes){
		
		if(plainRes == null) throw new NullPointerException();
		
		int pos = getDataCRCEnd(plainRes);
		
		return BAUtils.extractSubBA(plainRes, 0, pos+1);
		
		
	}
	
	/**
	 * 
	 * @param plainRes
	 * @return
	 */
	private static int getDataCRCEnd(byte[] plainRes){
		
		if(plainRes == null) throw new NullPointerException();
		
		for(int i = plainRes.length - 1; i > 0; i--){
			
			if(BAUtils.compareBAs(BAUtils.extractSubBA(plainRes, i, 1), BAUtils.toBA("80")))
				return i-1;
			
		}
		
		return 0;
	}
	
	
	/**
	 * 
	 * @param res
	 * @param comSet
	 * @param ar
	 * @param getFreeValue
	 * @param session
	 * @return
	 */
	public static ValueRes getValueRes(byte[] res, ComSet comSet, AccessRights ar, boolean getFreeValue, DFSession session){
		
		if((res == null) || (comSet == null) || (ar == null)|| (session == null))
			throw new NullPointerException();
		
		ComSet effComSet = getGetValueEffComSet(comSet, ar, getFreeValue, session);
		
		return getValueRes(res, effComSet, session);
		
	}
	
	/**
	 * 
	 * @param res
	 * @param effComSet
	 * @param session
	 * @return
	 */
	private static ValueRes getValueRes(byte[] res, ComSet effComSet, DFSession session){
		
		if((res == null) || (effComSet == null) || (session == null))
			throw new NullPointerException();
		
		byte[] value;
		boolean checked;
		
		AuthType auth = session.getAuthType();
		
		if(effComSet == ComSet.PLAIN){ 
			value = BAUtils.extractSubBA(res, 1, 4);
			checked = false;
		}
		else if(effComSet == ComSet.MAC){
			
			value = BAUtils.extractSubBA(res, 1, 4);
			
			if(auth == AuthType.TDEA_NATIVE){
				checked = checkMAC(res, session);		
			}
			else{
				checkCMAC(res, session);
				checked = session.getCmacOK();
			}
			
		}		
		else{
		
			byte[] plain = decode(BAUtils.extractSubBA(res, 1, res.length-1), session);
			value = BAUtils.extractSubBA(plain, 0, 4);
			checked = checkCRC(value, SC.OPERATION_OK, session, BAUtils.extractSubBA(plain, 4, plain.length - 4));
			
			
		}
		
		return new ValueRes(value, checked);
		
	}
	
	
	/**
	 * 
	 * @param comSet
	 * @param ar
	 * @param getFreeValue
	 * @param session
	 * @return
	 */
	public static ComSet getGetValueEffComSet(ComSet comSet, AccessRights ar, boolean getFreeValue, DFSession session){
		
		AuthType auth = session.getAuthType();
		
		if(auth == AuthType.NO_AUTH) return ComSet.PLAIN;
		
		if(getFreeValue){
			
			if(auth == AuthType.TDEA_NATIVE) return ComSet.PLAIN;
			else return ComSet.MAC;
						
		}
		
		int authKey = session.getAuthKeyNum();
		int r = ar.getReadAccess();
		int rw = ar.getReadWriteAccess();
		int w = ar.getWriteAccess();
		
		
		if((authKey == w) || (r == authKey) || (rw == authKey)){
			
			if((auth != AuthType.TDEA_NATIVE) && (comSet == ComSet.PLAIN)) return ComSet.MAC;
			else return comSet;
			
		}
		else{
			
			if(auth != AuthType.TDEA_NATIVE) return ComSet.MAC;
			else return ComSet.PLAIN;
			
		}
		
	}
	
	/**
	 * 
	 * @param value
	 * @param comSet
	 * @param ar
	 * @param com
	 * @param session
	 * @return
	 */
	public static byte[] prepareSendValueData(FID fid, Value value, ComSet comSet, AccessRights ar, ComCode com, DFSession session){
		
		if((fid == null) || (value == null) || (comSet == null) || (ar == null) || (com == null) || (session == null))
			throw new NullPointerException();
		
		ComSet effComSet = getSendValueEffComSet(comSet, ar, com, session);
		
		AuthType auth = session.getAuthType();
		
		byte[] valueData;
		
		if(effComSet == ComSet.PLAIN){
			valueData = value.toBA();
			updateCmacIV(com, BAUtils.concatenateBAs(fid.toBA(), value.toBA()), session);
		}
		else if(effComSet == ComSet.MAC){
			
			byte[] mac;
			if(auth == AuthType.TDEA_NATIVE) mac = MAC.mac(value.toBA(), session.getSessionKey(), CipAlg.TDEA2);
			else{
				updateCmacIV(com, BAUtils.concatenateBAs(fid.toBA(), value.toBA()), session);
				mac = BAUtils.extractSubBA(session.getCmacIV(), 0, 8);
			}
			
			valueData = BAUtils.concatenateBAs(value.toBA(), mac);
		}
		else{
			
			byte[] crc = CRC(com, fid.toBA(), value.toBA(), session.getAuthType());
			
			valueData = encode(BAUtils.concatenateBAs(value.toBA(), crc), session);
			
		}
		
		return valueData;
		
	}
	
	/**
	 * 
	 * @param comSet
	 * @param ar
	 * @param com
	 * @param session
	 * @return
	 */
	public static ComSet getSendValueEffComSet(ComSet comSet, AccessRights ar, ComCode com, DFSession session){
		
		if((com == null) || (comSet == null) || (ar == null) || (session == null))
			throw new NullPointerException();
		
		ComSet effComSet;
		
		AuthType auth = session.getAuthType();
		int authKey = session.getAuthKeyNum();
		
		int r = ar.getReadAccess();
		int rw = ar.getReadWriteAccess();
		int w = ar.getWriteAccess();
		
		
		if(auth == AuthType.NO_AUTH) effComSet = ComSet.PLAIN;
		
		else if(com == ComCode.CREDIT){
			if(authKey != r){			
				effComSet = comSet;			
			}
			else effComSet = ComSet.PLAIN;			
		}
		else if(com == ComCode.DEBIT){
			if((authKey == r)||(authKey == rw) || (authKey == w)){			
				effComSet = comSet;			
			}
			else effComSet = ComSet.PLAIN;					
		}
		else if(com == ComCode.LIMITED_CREDIT){
			if((authKey == rw) || (authKey == w)){			
				effComSet = comSet;			
			}
			else effComSet = ComSet.PLAIN;					
		}
		else throw new IllegalArgumentException();
		
		return effComSet;
		
	}
	
	/**
	 * 
	 * @param res
	 * @param length
	 * @param recSize
	 * @param comSet
	 * @param ar
	 * @param session
	 * @return
	 */
	public static RecordsRes getRecordsRes(byte[] res, Size length, Size recSize, ComSet comSet, AccessRights ar, DFSession session){
	
		if((res == null) || (length == null) || (recSize == null) || (comSet == null) || (ar == null) || (session == null))
			throw new NullPointerException();
		
		int numOfBytes = length.getSize() * recSize.getSize();
		
		DataRes dr = getDataRes(res, new Size(numOfBytes), comSet, ar, session);
		
		return new RecordsRes(dr.getData().toBA(), recSize, dr.isChecked());
		
		
	}
	
	
	/**
	 * 
	 * @param data
	 * @param auth
	 * @return
	 */
	public static byte[] padding(byte[] data, AuthType auth){
		
		if((data == null) || (auth == null)) throw new NullPointerException();
		
		return padding(data, auth, PaddingMode.ZEROPadding);
		
	}
	
	
	/**
	 * 
	 * @param data
	 * @param auth
	 * @param pad
	 * @return
	 */
	public static byte[] padding(byte[] data, AuthType auth, PaddingMode pad){
		
		if((data == null) || (auth == null) || (pad == null)) throw new NullPointerException();
		
		int bl = (auth == AuthType.AES) ? 16 : 8;
		
		return Crypto.padding(data, bl, pad);
	}
	
	/**
	 * 
	 * @param com
	 * @param plainParam
	 * @param data
	 * @param auth
	 * @return
	 */
	public static byte[] CRC(ComCode com, byte[] plainParam, byte[] data, AuthType auth){
		
		if((com == null) || (plainParam == null) || (data == null) || (auth == null))
			throw new NullPointerException();
		
		if(auth == AuthType.NO_AUTH) throw new DFLException(ExType.SECURITY_EXCEPTION);
		else if(auth == AuthType.TDEA_NATIVE) return CRC.CRC16(data);
		else return CRC.CRC32(BAUtils.concatenateBAs(com.toBA(), plainParam, data));
		
	}
	
	/**
	 * 
	 * @param com
	 * @param data
	 * @param auth
	 * @return
	 */
	public static byte[] CRC(ComCode com, byte[] data, AuthType auth){
		
		if((com == null) || (data == null) || (auth == null))
			throw new NullPointerException();
		
		if(auth == AuthType.NO_AUTH) throw new DFLException(ExType.SECURITY_EXCEPTION);
		else if(auth == AuthType.TDEA_NATIVE) return CRC.CRC16(data);
		else return CRC.CRC32(BAUtils.concatenateBAs(com.toBA(), data));
		
	}
	
	/**
	 * 
	 * @param data
	 * @param auth
	 * @return
	 */
	public static byte[] CRC(byte[] data, AuthType auth){
		
		if((data == null) || (auth == null))
			throw new NullPointerException();
		
		if(auth == AuthType.NO_AUTH) throw new DFLException(ExType.SECURITY_EXCEPTION);
		else if(auth == AuthType.TDEA_NATIVE) return CRC.CRC16(data);
		else return CRC.CRC32(data);
		
		
	}
	
	/**
	 * 
	 * @param data
	 * @param auth
	 * @return
	 */
	public static byte[] CRC(byte[] data, SC sc, AuthType auth){
		
		if((data == null) || (sc == null) || (auth == null))
			throw new NullPointerException();
		
		if(auth == AuthType.NO_AUTH) throw new DFLException(ExType.SECURITY_EXCEPTION);
		else if(auth == AuthType.TDEA_NATIVE) return CRC.CRC16(data);
		else return CRC.CRC32(BAUtils.concatenateBAs(data, sc.toBA()));
		
		
	}
	
	
	/**
	 * 
	 * @param data
	 * @param sc
	 * @param session
	 * @param crcPad
	 * @return
	 */
	public static boolean checkCRC(byte[] data, SC sc, DFSession session, byte[] crcPad){

		if((data == null) || (sc == null) || (session == null) || (crcPad == null))
			throw new NullPointerException();
		
		byte[] calcCRC = CRC(data, sc, session.getAuthType());
		
		int crcLen = getCRCLength(session);
		
		byte[] crc = BAUtils.extractSubBA(crcPad, 0, crcLen);
		
		return BAUtils.compareBAs(crc, calcCRC);
	
	
	}
	
	/**
	 * 
	 * @param session
	 * @return
	 */
	public static int getCRCLength(DFSession session){
		
		if(session == null) throw new NullPointerException();
		
		if(session.getAuthType() == AuthType.NO_AUTH) return 0;
		else if(session.getAuthType() == AuthType.TDEA_NATIVE) return 2;
		else return 4;
		
	}
	
	/**
	 * 
	 * @param com
	 * @param session
	 */
	public static void updateCmacIV(ComCode com, DFSession session){
		
		if((com == null) || (session == null)) throw new NullPointerException();
		
		updateCmacIV(com.toBA(), session);
		
	}
	
	/**
	 * 
	 * @param com
	 * @param data
	 * @param session
	 */
	public static void updateCmacIV(ComCode com, byte[] data, DFSession session){
		
		if((com == null) || (data == null) || (session == null)) throw new NullPointerException();
		
		updateCmacIV(BAUtils.concatenateBAs(com.toBA(), data), session);
		
	}
	
	/**
	 * 
	 * @param data
	 * @param session
	 */
	public static void updateCmacIV(byte[] data, DFSession session){
		
		if((data == null) || (session == null)) throw new NullPointerException();
		
		if((session.getAuthType() != AuthType.NO_AUTH) && (session.getAuthType() != AuthType.TDEA_NATIVE)){
			
			byte[] cmaciv = session.getCmacIV();
			byte[] sk = session.getSessionKey();
			CipAlg alg = getAlg(session);
			
			byte[] cmac = MAC.cmac(data, sk, cmaciv, alg);
			
			session.updateCmacIV(cmac);
			
		}
		
	}
	
	
	
	
	/**
	 * 
	 * @param res
	 * @param session
	 * @return
	 */
	public static void checkCMAC(byte[] res, DFSession session){
		
		if((res == null) || (session == null)) throw new NullPointerException();
		
		if((session.getAuthType() != AuthType.NO_AUTH) && (session.getAuthType() != AuthType.TDEA_NATIVE)){
			
			byte[] cmaciv = session.getCmacIV();
			int len = 8;
			byte[] sk = session.getSessionKey();
			CipAlg alg = getAlg(session);
			
			byte[] cmacData = BAUtils.concatenateBAs(BAUtils.extractSubBA(res, 1, res.length - len -1), BAUtils.extractSubBA(res, 0, 1));
			
			byte[] cmac = MAC.cmac(cmacData, sk, cmaciv, alg);
			
			boolean b = BAUtils.compareBAs(BAUtils.extractSubBA(cmac, 0, len), BAUtils.extractSubBA(res, res.length - len, len));
			
			session.updateCmacIV(cmac, b);
			
		}

	}
	
	/**
	 * 
	 * @param res
	 * @param session
	 * @return
	 */
	public static boolean checkMAC(byte[] res, DFSession session){
		
		if((res == null) || (session == null)) throw new NullPointerException();
		
		if(session.getAuthType() == AuthType.TDEA_NATIVE){
			
			byte[] rmac = BAUtils.extractSubBA(res, res.length - 4, 4);
			byte[] mac = MAC.mac(BAUtils.extractSubBA(res, 1, res.length - 5), session.getSessionKey(), CipAlg.TDEA2);
			
			return BAUtils.compareBAs(rmac, mac);
			
		}
		
		return false;
	}
	
	/**
	 * 
	 * @param session
	 * @return
	 */
	public static int getMacLength(DFSession session){
		
		if(session == null) throw new NullPointerException();
		
		if(session.getAuthType() == AuthType.NO_AUTH) return 0;
		else if(session.getAuthType() == AuthType.TDEA_NATIVE) return 4;
		else return 8;
		
	}
	
	
	/**
	 * 
	 * @param keyBytes
	 */
	public static int getKeyVersion(byte[] keyBytes){
		
		if(keyBytes == null) throw new NullPointerException();
		if((keyBytes.length % 8) != 0) throw new IllegalArgumentException();
		
		byte[] LSB = BAUtils.extractSubBA(keyBytes, 0, 8);
		
		int keyVersion = 0;
		
		for(int i = 0; i < 8; i++){
			
			if((LSB[i] & (byte) 0x01) != 0) keyVersion = keyVersion + (int)Math.pow(2, i);		
			
		}
		
		return keyVersion;
	}
	
	/**
	 * 
	 * @param data
	 * @param keyData
	 * @param auth
	 * @return
	 */
	public static byte[] encode(byte[] data, DFSession session){
		
		if((data == null) || (session == null)) throw new NullPointerException();
		
		return encode(data, session, PaddingMode.ZEROPadding);
		
		
	}
	
	/**
	 * 
	 * @param data
	 * @param session
	 * @param pad
	 * @return
	 */
	public static byte[] encode(byte[] data, DFSession session, PaddingMode pad){
		
		if((data == null) || (session == null) || (pad == null)) throw new NullPointerException();
		
		ChainMode chain;
		CipAlg alg;
		AuthType auth = session.getAuthType();
		byte[] keyData = session.getSessionKey();
		byte[] iv;
		
		alg = getAlg(session);
		
		if(auth == AuthType.TDEA_NATIVE){
			
			chain = ChainMode.CBCSendDF;
			iv = new byte[alg.getBlockLength()];
			
		}
		else{
			
			chain = ChainMode.CBCSendISO;			
			iv = session.getCmacIV();
			
		}
		try{
			byte[] cip = Crypto.encode(data, keyData, iv, chain, alg, pad);
			
			if(auth != AuthType.TDEA_NATIVE)
				session.updateCmacIV(BAUtils.extractSubBA(cip, cip.length - alg.getBlockLength(), alg.getBlockLength()));
			
			return cip;
		}catch(Exception e){
			throw new DFLException(ExType.SECURITY_EXCEPTION);
		}
		
		
	}

	
	/**
	 * 
	 * @param data
	 * @param session
	 * @return
	 */
	public static byte[] decode(byte[] data, DFSession session){
		
		if((data == null) || (session == null)) throw new NullPointerException();
		
		ChainMode chain;
		CipAlg alg;
		AuthType auth = session.getAuthType();
		byte[] keyData = session.getSessionKey();
		byte[] iv;
		
		
		
		alg = getAlg(session);
		
		if(auth == AuthType.TDEA_NATIVE){
			
			chain = ChainMode.CBCReceiveDF;
			iv = new byte[alg.getBlockLength()];
			
		}
		else{
			
			chain = ChainMode.CBCReceiveISO;			
			iv = session.getCmacIV();
			
		}
		try{
			byte[] cip = Crypto.encode(data, keyData, iv, chain, alg, PaddingMode.ZEROPadding);
			
			if(auth != AuthType.TDEA_NATIVE)
				session.updateCmacIV(BAUtils.extractSubBA(data, data.length - alg.getBlockLength(), alg.getBlockLength()));
			
			return cip;
		}catch(Exception e){
			throw new DFLException(ExType.SECURITY_EXCEPTION);
		}
		
		
	}
	
	/**
	 * 
	 * @param session
	 * @return
	 */
	public static CipAlg getAlg(DFSession session){
		
		if(session == null) throw new NullPointerException();
		
		AuthType auth = session.getAuthType();
		byte[] keyData = session.getSessionKey();	
		
		if(auth == AuthType.TDEA_NATIVE) return getDESAlg(keyData);
		if(auth == AuthType.TDEA_STANDARD) return CipAlg.TDEA2;
		else if(auth == AuthType.TDEA3) return CipAlg.TDEA3;
		else return CipAlg.AES;
		
		
	}
	
	/**
	 * 
	 * @param algBA
	 * @return
	 */
	public static CipAlg getAlg(byte[] algBA){
		
		if(algBA == null) throw new NullPointerException();
		
    	if(BAUtils.compareBAs(algBA, new byte[1])) return CipAlg.TDEA2;
    	else if(BAUtils.compareBAs(algBA, BAUtils.toBA("40"))) return CipAlg.TDEA3;
    	else if(BAUtils.compareBAs(algBA, BAUtils.toBA("80"))) return CipAlg.AES;
		
    	throw new IllegalArgumentException();
		
	}
	/**
	 * 
	 * @param keyData
	 * @return
	 */
	public static CipAlg getDESAlg(byte[] keyData){
		
		if(keyData == null) throw new NullPointerException();
		
		if(keyData.length == 8) return CipAlg.DES;
		else if(keyData.length == 24) return CipAlg.TDEA3;
		else if(keyData.length == 16){
			
			byte[] sk1 = BAUtils.extractSubBA(keyData, 0, 8);
			byte[] sk2 = BAUtils.extractSubBA(keyData, 8, 8);
			
			if(BAUtils.compareBAs(sk1, sk2)) return CipAlg.DES;
			else return CipAlg.TDEA2;
		}
		
		else throw new DFLException(ExType.SECURITY_EXCEPTION);
	}
	
}
