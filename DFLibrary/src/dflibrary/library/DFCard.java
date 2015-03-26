package dflibrary.library;


import dflibrary.library.param.*;
import dflibrary.library.param.fileset.*;
import dflibrary.library.security.*;
import dflibrary.middleware.*;
import dflibrary.utils.ba.*;
import dflibrary.utils.security.*;

/**
 * 
 * @author Francisco Rodríguez Algarra
 *
 */
public class DFCard {
	
	/**
	 * 
	 * @param cm
	 */
	public DFCard(ComManager cm){
		
		this.cm = cm;
		this.session = new DFSession();
		isoSelect();
				
	}
	
	//SECURITY RELATED COMMANDS
	
	//Authenticate	
	
	/**
	 * 
	 * @param keyNum
	 * @param key
	 * @return
	 */
	public DFResponse authenticate(int keyNum, DFKey key){
		
		if(key == null) throw new NullPointerException();
		
		CipAlg alg = key.getAlg();
		
		if((alg != CipAlg.DES) && (alg != CipAlg.TDEA2)) throw new IllegalArgumentException();
		
		return authenticate(keyNum, key.getKeyBytes());
		
	}
	
	/**
	 * 
	 * @param keyNum
	 * @param keyData
	 * @return
	 */
	public DFResponse authenticate(int keyNum, byte[] keyData){
		
		if(keyData == null) throw new NullPointerException();
	
		byte[] res = Authenticate(BAUtils.toBA(keyNum, 1), keyData);
		
		SC sc = SC.toSC(BAUtils.extractSubBA(res, 0, 1));
		
		if(!SC.isOk(res)){
			resetSession();
			return new DFResponse(sc);
		}
		byte[] sessionKey = BAUtils.extractSubBA(res, 1, 16);
		boolean PICCAuth = BAUtils.toBoolean((BAUtils.extractSubBA(res, 17, 1)));
		
		setSession(AuthType.TDEA_NATIVE, keyNum, sessionKey, PICCAuth);
		
		return new DFResponse(sc);
		
	}
	
	
	/**
	 * 
	 * @param keyNum
	 * @param keyData
	 * @param alg
	 * @return
	 */
	public byte[] Authenticate(byte[] keyNum, byte[] keyData){
		
		if((keyNum == null) || (keyData == null)) throw new NullPointerException();
		
		CipAlg alg = DFCrypto.getDESAlg(keyData);
		
		if((alg != CipAlg.DES) && (alg != CipAlg.TDEA2)) throw new IllegalArgumentException();
		
		byte[] res = send(ComCode.AUTHENTICATE.toBA(), keyNum);
		
		if(!SC.isAF(res)) return res;
		
		byte[] RndA = DFCrypto.getRndA(alg);
		
		byte[] RndB = DFCrypto.getRndB(BAUtils.extractSubBA(res, 1, 8), keyData, alg);
		
		byte[] dkRndARndBp = DFCrypto.getDKRndARndBp(RndA, RndB, keyData, alg);
		
		res = send(SC.ADDITIONAL_FRAME.toBA(), dkRndARndBp);
		
		if(!SC.isOk(res)) return res;
		
		byte[] isPICCAuth = BAUtils.toBA(DFCrypto.getPICCAuth(RndA, BAUtils.extractSubBA(res, 1, 8), keyData, alg));
		
		byte[] sessionKey = DFCrypto.getSessionKey(RndA, RndB, alg);
		
		return BAUtils.concatenateBAs(BAUtils.extractSubBA(res, 0, 1), sessionKey, isPICCAuth);
		
	}
		
	
	/**
	 * 
	 * @param keyNum
	 * @param key
	 * @return
	 */
	public DFResponse authenticateISO(int keyNum, DFKey key){
		
		if(key == null) throw new NullPointerException();
		
		CipAlg alg = key.getAlg();
		
		if(alg == CipAlg.AES) throw new IllegalArgumentException();
		
		return authenticateISO(keyNum, key.getKeyBytes());
		
	}
	
	/**
	 * 
	 * @param keyNum
	 * @param keyData
	 * @return
	 */
	public DFResponse authenticateISO(int keyNum, byte[] keyData){
		
		if(keyData == null) throw new NullPointerException();
		
		byte[] res = AuthenticateISO(BAUtils.toBA(keyNum, 1), keyData);
		
		SC sc = SC.toSC(BAUtils.extractSubBA(res, 0, 1));
		
		if(!SC.isOk(res)){
			resetSession();
			return new DFResponse(sc);
		}
		
		byte[] sessionKey = BAUtils.extractSubBA(res, 1, res.length-2);
		boolean PICCAuth = BAUtils.toBoolean((BAUtils.extractSubBA(res, res.length-1, 1)));
		
		CipAlg alg = DFCrypto.getDESAlg(keyData);
		if(alg == CipAlg.TDEA3) setSession(AuthType.TDEA3, keyNum, sessionKey, PICCAuth);
		else setSession(AuthType.TDEA_STANDARD, keyNum, sessionKey, PICCAuth);
		
		return new DFResponse(sc);
			
	}
	
	/**
	 * 
	 * @param keyNum
	 * @param keyData
	 * @param alg
	 * @return
	 */
	public byte[] AuthenticateISO(byte[] keyNum, byte[] keyData){
			
		if((keyNum == null) || (keyData == null)) throw new NullPointerException();
		
		CipAlg alg = DFCrypto.getDESAlg(keyData);

		byte[] res = send(ComCode.AUTHENTICATE_ISO.toBA(), keyNum);
		
		if(!SC.isAF(res)) return res;
		
		byte[] RndA = DFCrypto.getRndA(alg);
		
		byte[] ekRndB = BAUtils.extractSubBA(res, 1, res.length - 1);		
		
		byte[] RndB = DFCrypto.getRndB(ekRndB, keyData, alg);
	
		byte[] iv = BAUtils.extractSubBA(ekRndB, ekRndB.length - 8, 8);
		
		byte[] ekRndARndBp = DFCrypto.getEKRndARndBp(RndA, RndB, keyData, iv, alg);

		res = send(SC.ADDITIONAL_FRAME.toBA(), ekRndARndBp);
		
		if(!SC.isOk(res)) return res;
		
		iv = BAUtils.extractSubBA(ekRndARndBp, ekRndARndBp.length - 8, 8);
		
		byte[] ekRndAp = BAUtils.extractSubBA(res, 1, res.length - 1);
		
		byte[] isPICCAuth = BAUtils.toBA(DFCrypto.getPICCAuth(RndA, ekRndAp, keyData, iv, alg));
		
		byte[] sessionKey = DFCrypto.getSessionKey(RndA, RndB, alg);
		
		return BAUtils.concatenateBAs(BAUtils.extractSubBA(res, 0, 1), sessionKey, isPICCAuth);

	}
	
	/**
	 * 
	 * @param keyNum
	 * @param key
	 * @return
	 */
	public DFResponse authenticateAES(int keyNum, DFKey key){
		
		if(key == null) throw new NullPointerException();
		
		CipAlg alg = key.getAlg();
		
		if(alg != CipAlg.AES) throw new IllegalArgumentException();
		
		return authenticateAES(keyNum, key.getKeyBytes());
		
	}
	
	/**
	 * 
	 * @param keyNum
	 * @param keyData
	 * @return
	 */
	public DFResponse authenticateAES(int keyNum, byte[] keyData){
		
		if(keyData == null) throw new NullPointerException();
		
		byte[] res = AuthenticateAES(BAUtils.toBA(keyNum, 1), keyData);
		
		SC sc = SC.toSC(BAUtils.extractSubBA(res, 0, 1));
		
		if(!SC.isOk(res)){
			resetSession();
			return new DFResponse(sc);
		}
		
		byte[] sessionKey = BAUtils.extractSubBA(res, 1, 16);
		boolean PICCAuth = BAUtils.toBoolean((BAUtils.extractSubBA(res, 17, 1)));
		setSession(AuthType.AES, keyNum, sessionKey, PICCAuth);
		
		return new DFResponse(sc);
			
	}
	
	/**
	 * 
	 * @param keyNum
	 * @param keyData
	 * @return
	 */
	public byte[] AuthenticateAES(byte[] keyNum, byte[] keyData){
				
		if((keyNum == null) || (keyData == null)) throw new NullPointerException();
		
		byte[] res = send(ComCode.AUTHENTICATE_AES.toBA(), keyNum);
		 
		if(!SC.isAF(res)) return res;
		 
		CipAlg alg = CipAlg.AES;
		 
		byte[] RndA = DFCrypto.getRndA(alg);
		 
		byte[] ekRndB = BAUtils.extractSubBA(res, 1, res.length - 1);
		
		byte[] RndB = DFCrypto.getRndB(ekRndB, keyData, alg);
		
		byte[] iv = BAUtils.extractSubBA(ekRndB, ekRndB.length - 16, 16);
		
		byte[] ekRndARndBp = DFCrypto.getEKRndARndBp(RndA, RndB, keyData, iv, alg);
		
		res = send(SC.ADDITIONAL_FRAME.toBA(), ekRndARndBp);
		
		if(!SC.isOk(res)) return res;
		
		iv = BAUtils.extractSubBA(ekRndARndBp, ekRndARndBp.length - 16, 16);
		 
		byte[] ekRndAp = BAUtils.extractSubBA(res, 1, res.length - 1);
		
		byte[] isPICCAuth = BAUtils.toBA(DFCrypto.getPICCAuth(RndA, ekRndAp, keyData, iv, alg));
		
		byte[] sessionKey = DFCrypto.getSessionKey(RndA, RndB, alg);
		
		return BAUtils.concatenateBAs(BAUtils.extractSubBA(res, 0, 1), sessionKey, isPICCAuth);
		
		
	}	
	
	//Change Key Settings
	
	/**
	 * 
	 * @param ks
	 * @return
	 */
	public DFResponse changeKeySettings(KeySettings ks){
		
		if(ks == null) throw new NullPointerException();
		
		byte[] ksBA = ks.toBA();
		
		byte[] crc = DFCrypto.CRC(ComCode.CHANGE_KEY_SETTINGS, ksBA, getSession().getAuthType());
		
		byte[] cipKeySettings = DFCrypto.encode(BAUtils.concatenateBAs(ksBA, crc), getSession());
		
		byte[] res = ChangeKeySettings(cipKeySettings);
		
		if(!SC.isOk(res)){
			session.resetAuth();
			return new DFResponse(res);
		}
		
		DFCrypto.checkCMAC(res, session);
		
		return new DFResponse(BAUtils.extractSubBA(res, 0, 1));
	}
	
	
	/**
	 * 
	 * @param cipKeySettings
	 * @return
	 */
	public byte[] ChangeKeySettings(byte[] cipKeySettings){
	
		if(cipKeySettings == null) throw new NullPointerException();
		
		byte[] com = BAUtils.concatenateBAs(ComCode.CHANGE_KEY_SETTINGS.toBA(), cipKeySettings);
		
		return send(com);
	
	}
	
	
	//Set Configuration
	
	
	/**
	 * 
	 * @param opt
	 * @return
	 */
	public DFResponse setConfiguration(ConfigOption opt){
		
		if(opt == null) throw new NullPointerException();
		
		byte[] optBA = opt.getOpt().toBA();
		byte[] encOptData = DFCrypto.getEncOptData(opt, getSession());
		
		byte[] res = SetConfiguration(optBA, encOptData);
		
		if(!SC.isOk(res)){
			session.resetAuth();
			return new DFResponse(res);
		}
		
		DFCrypto.checkCMAC(res, session);
		
		
		return new DFResponse(SC.OPERATION_OK);
		
	}
	
	
		
	/**
	 * 
	 * @param option
	 * @param encData
	 * @return
	 */
	public byte[] SetConfiguration(byte[] option, byte[] encData){
	
		if((option == null) || (encData == null)) throw new NullPointerException();
		
		byte[] com = BAUtils.concatenateBAs(ComCode.SET_CONFIGURATION.toBA(), option, encData);
	
		return send(com);
		
	} 
	
	
	//Change Key
	
	/**
	 * 
	 * @param keyNum
	 * @param newKey
	 * @param oldKey
	 * @return
	 */
	public DFResponse changeKey(int keyNum, DFKey newKey, DFKey oldKey){
		
		if(keyNum < 0) throw new IllegalArgumentException();
		if((newKey == null) || (oldKey == null)) throw new NullPointerException();
		
		DFSession session = getSession();
		AuthType auth = session.getAuthType();
	
		if(auth == AuthType.NO_AUTH) return new DFResponse(SC.AUTHENTICATION_ERROR);
		
		byte[] keyNumBA;
		byte[] data;
		
		if(session.getSelectedAID().isMaster()) keyNumBA = DFCrypto.getKeyNumBA(keyNum, newKey.getAlg());
		else keyNumBA = BAUtils.toBA(keyNum, 1);
		
		int authKeyNum = session.getAuthKeyNum();
		
		if(authKeyNum != keyNum) data = BAUtils.xor(newKey.getKeyBytes(), oldKey.getKeyBytes());
		else data = BAUtils.extractSubBA(newKey.getKeyBytes(), 0, newKey.getKeyBytes().length);
		
		if(newKey.getAlg() == CipAlg.AES) data = BAUtils.concatenateBAs(data, BAUtils.toBA(newKey.getKeyVersion(), 1));		
		
		data = BAUtils.concatenateBAs(data, DFCrypto.CRC(ComCode.CHANGE_KEY, keyNumBA, data, auth));
		
		if(authKeyNum != keyNum) 
			data = BAUtils.concatenateBAs(data, DFCrypto.CRC(newKey.getKeyBytes(), auth));
		
		data = DFCrypto.padding(data, auth);
		
		data = DFCrypto.encode(data, session);
		
		byte[] res = ChangeKey(keyNumBA, data);
		
		if((!SC.isOk(res)) || (authKeyNum == keyNum)){
			
			session.resetAuth();
			return new DFResponse(res);
			
		}
		
		
		DFCrypto.checkCMAC(res, session);
		
		return new DFResponse(BAUtils.extractSubBA(res, 0, 1));
		
	}

	/**
	 * 
	 * @param keyNum
	 * @param encKeyData
	 * @return
	 */
	public byte[] ChangeKey(byte[] keyNum, byte[] encKeyData){
		
		if((keyNum == null) || (encKeyData == null)) throw new NullPointerException();
		
		byte[] com = BAUtils.concatenateBAs(ComCode.CHANGE_KEY.toBA(), keyNum, encKeyData);
		
		return send(com);
		
	}
	
	
	//Get Key Version

	
	/**
	 * 
	 * @param keyNum
	 * @return
	 */
	public DFResponse getKeyVersion(int keyNum){
		
		if((keyNum <0) && (keyNum > 14))  throw new IllegalArgumentException();
		
		byte[] keyNumBA = BAUtils.toBA(keyNum, 1);
		
		byte[] res = GetKeyVersion(keyNumBA);
		
		if(!SC.isOk(res)){
			session.resetAuth();
			return new DFResponse(res);
		}
		
		DFCrypto.updateCmacIV(ComCode.GET_KEY_VERSION, keyNumBA, getSession());
		
		DFCrypto.checkCMAC(res, getSession());
		
		KeyVersion kv = new KeyVersion(BAUtils.extractSubBA(res, 1, 1));
		
		return new DFResponse(SC.OPERATION_OK, kv);
		
	}
	
	/**
	 * 
	 * @param keyNum
	 * @return
	 */
	public byte[] GetKeyVersion(byte[] keyNum){
		
		if(keyNum == null) throw new NullPointerException();
		
		byte[] com = BAUtils.concatenateBAs(ComCode.GET_KEY_VERSION.toBA(), keyNum);
		
		return send(com);
		
	}

	
	
	//PICC LEVEL COMMANDS
	
	//Create Application
	
	/**
	 * 
	 * @param aid
	 * @param ks
	 * @param numOfKeys
	 * @return
	 */
	public DFResponse createApplication(AID aid, KeySettings ks, int numOfKeys){
		
		if((aid == null) || (ks == null))
			throw new NullPointerException();
		
		if((numOfKeys < 1) || (numOfKeys > 14)) throw new IllegalArgumentException();
		
		
		byte[] aidBA = aid.toBA();
		byte[] ksBA = ks.toBA();
		byte[] numOfKeysBA = BAUtils.toBA(numOfKeys, 1);
		
		byte[] res = CreateApplication(aidBA, ksBA, numOfKeysBA);
		
		if(!SC.isOk(res)){
			session.resetAuth();
			return new DFResponse(res);
		}
		
		byte[] data = BAUtils.concatenateBAs(aidBA, ksBA, numOfKeysBA);
		
		DFCrypto.updateCmacIV(ComCode.CREATE_APPLICATION, data, getSession());
		
		DFCrypto.checkCMAC(res, getSession());
		
		return new DFResponse(SC.OPERATION_OK);
		
	}
	
	/**
	 * 
	 * @param aid
	 * @param ks
	 * @param numOfKeys
	 * @return
	 */
	public DFResponse createApplication(AID aid, KeySettings ks, int numOfKeys, boolean ISOFidAllow, CipAlg alg){
		
		if((aid == null) || (ks == null) || (alg == null))
			throw new NullPointerException();
		
		if((numOfKeys < 1) || (numOfKeys > 14)) throw new IllegalArgumentException();
		
		byte[] aidBA = aid.toBA();
		byte[] ksBA = ks.toBA();

		byte[] ks2 = DFCrypto.getKeySettings2(numOfKeys, ISOFidAllow, alg);
		
		byte[] res = CreateApplication(aidBA, ksBA, ks2);
		
		if(!SC.isOk(res)){
			session.resetAuth();
			return new DFResponse(res);
		}
		
		byte[] data = BAUtils.concatenateBAs(aidBA, ksBA, ks2);
		
		DFCrypto.updateCmacIV(ComCode.CREATE_APPLICATION, data, getSession());
		
		DFCrypto.checkCMAC(res, getSession());
		
		return new DFResponse(SC.OPERATION_OK);
		
	}
	
	/**
	 * 
	 * @param aid
	 * @param ks
	 * @param numOfKeys
	 * @param ISOFidAllow
	 * @param alg
	 * @param fid
	 * @param name
	 * @return
	 */
	public DFResponse createApplication(AID aid, KeySettings ks, int numOfKeys, boolean ISOFidAllow, CipAlg alg, ISOFileID fid, DFName name){
		
		if((aid == null) || (ks == null) || (alg == null) || (fid == null) || (name == null))
			throw new NullPointerException();
		
		if((numOfKeys < 1) || (numOfKeys > 14)) throw new IllegalArgumentException();
		
		byte[] aidBA = aid.toBA();
		byte[] ksBA = ks.toBA();

		byte[] ks2 = DFCrypto.getKeySettings2(numOfKeys, ISOFidAllow, alg);
		
		byte[] fidBA = fid.toBA();
		byte[] nameBA = name.getDFName();
		
		byte[] res = CreateApplication(aidBA, ksBA, ks2, fidBA, nameBA);
		
		if(!SC.isOk(res)){
			session.resetAuth();
			return new DFResponse(res);
		}
		
		byte[] data = BAUtils.concatenateBAs(aidBA, ksBA, ks2, fidBA, nameBA);
		
		DFCrypto.updateCmacIV(ComCode.CREATE_APPLICATION, data, getSession());
		
		DFCrypto.checkCMAC(res, getSession());
		
		return new DFResponse(SC.OPERATION_OK);
		
	}
	
	/**
	 * 
	 * @param aid
	 * @param keySettings
	 * @param numOfKeys
	 * @return
	 */
	public byte[] CreateApplication(byte[] aid, byte[] keySettings, byte[] numOfKeys){
		
		if((aid == null) || (keySettings == null) || (numOfKeys == null)) 
			throw new NullPointerException();
		
		byte[] com = BAUtils.concatenateBAs(ComCode.CREATE_APPLICATION.toBA(), aid, keySettings, numOfKeys);
		
		return send(com);
		
	}
	
	/**
	 * 
	 * @param aid
	 * @param keySettings
	 * @param keySettings2
	 * @param ISOFid
	 * @param DFName
	 * @return
	 */
	public byte[] CreateApplication(byte[] aid, byte[] keySettings, byte[] keySettings2, byte[] ISOFid, byte[] DFName){
		
		if((aid == null) || (keySettings == null) || (keySettings2 == null) || (ISOFid == null) || (DFName == null)) 
			throw new NullPointerException();
		
		byte[] com = BAUtils.concatenateBAs(ComCode.CREATE_APPLICATION.toBA(), aid, keySettings, keySettings2, ISOFid, DFName);
		
		return send(com);
		
	}
	
	//Delete Application
	
	/**
	 * 
	 * @param aid
	 * @return
	 */
	public DFResponse deleteApplication(AID aid){
		
		if(aid == null) throw new NullPointerException();
		
		byte[] aidBA = aid.toBA();
		
		byte[] res = DeleteApplication(aidBA);
		
		if(!SC.isOk(res) || (session.getSelectedAID().toInt() == aid.toInt())){
			session.resetAuth();
			return new DFResponse(res);
		}
		
		
		
		DFCrypto.updateCmacIV(ComCode.DELETE_APPLICATION, aidBA, getSession());
		
		DFCrypto.checkCMAC(res, getSession());
		
		return new DFResponse(SC.OPERATION_OK);
		
	}
	
	/**
	 * 
	 * @param aid
	 * @return
	 */
	public byte[] DeleteApplication(byte[] aid){
	
		if(aid == null) throw new NullPointerException();
		if(aid.length != 3) throw new IllegalArgumentException();
		
		byte[] com = BAUtils.concatenateBAs(ComCode.DELETE_APPLICATION.toBA(), aid);
	
		return send(com);
		
	}
		
	//Get Application IDs
	
	/**
	 * 
	 * @return
	 */
	public DFResponse getApplicationIDs(){
		
		byte[] res = GetApplicationIDs();
		
		if(!SC.isOk(res)){
			session.resetAuth();
			return new DFResponse(res);
		}
		
		DFCrypto.updateCmacIV(ComCode.GET_APPLICATION_IDS, getSession());
		
		DFCrypto.checkCMAC(res, getSession());
		
		byte[] aids = DFCrypto.getData(res, session);
		
		return new DFResponse(SC.OPERATION_OK, new AIDS(aids));
		
	}
	
	
	
	/**
	 * 
	 * @return
	 */
	public byte[] GetApplicationIDs(){
		
		byte[] rres;
		byte[] res = new byte[0];
		byte[] com = ComCode.GET_APPLICATION_IDS.toBA();
		
		do{
		
			rres = send(com);
			
			if(rres.length > 1)
				res = BAUtils.concatenateBAs(res, BAUtils.extractSubBA(rres, 1, rres.length-1));
			
			com = SC.ADDITIONAL_FRAME.toBA();
			
		}while(SC.isAF(rres));
		
		byte[] sc = BAUtils.extractSubBA(rres, 0, 1);
		
		if(SC.isOk(rres)) return BAUtils.concatenateBAs(sc, res);
		
		else return sc;
		
	}
	
	//Get Free Memory
	
	public DFResponse freeMemory(){
		
		byte[] res = FreeMemory();
		
		if(!SC.isOk(res)){
			
			session.resetAuth();
			return new DFResponse(res);
			
		}
		
		DFCrypto.updateCmacIV(ComCode.FREE_MEMORY, getSession());
		
		DFCrypto.checkCMAC(res, getSession());
		
		byte[] freeMem = DFCrypto.getData(res, getSession());
		
		return new DFResponse(SC.OPERATION_OK, new Size(freeMem));
		
		
	}
	
	/**
	 * 
	 * @return
	 */
	public byte[] FreeMemory(){
		
		return send(ComCode.FREE_MEMORY.toBA());
		
	}
	
	//Get DF Names

	/**
	 * 
	 * @return
	 */
	public DFResponse getDFNames(){
		
		byte[][] res = GetDFNames();

		if(!SC.isOk(res[0])){
			session.resetAuth();
			return new DFResponse(res[0]);
			
		}
		
		byte[] cmacData = SC.OPERATION_OK.toBA();
		byte[][] dfNames = new byte[res.length - 1][];
		
		for(int i = 1; i < res.length - 1; i ++){
			
			cmacData = BAUtils.concatenateBAs(cmacData, res[i]);
			dfNames[i-1] = res[i];
			
		}
		
		if(res.length > 1) {
						
			cmacData = BAUtils.concatenateBAs(cmacData, res[res.length - 1]); 
			
			byte[] lastFrame = DFCrypto.getData(BAUtils.concatenateBAs(new byte[1], res[res.length - 1]), session);
			if(lastFrame.length == 0){
				
				byte[][] aux = new byte[dfNames.length - 1][];
				System.arraycopy(dfNames, 0, aux, 0, dfNames.length-1);
				dfNames = aux;
				
			}
			else dfNames[dfNames.length - 1] = lastFrame;
		}
			
		DFCrypto.updateCmacIV(ComCode.GET_DF_NAMES, getSession());
		
		DFCrypto.checkCMAC(cmacData, getSession());
		
		return new DFResponse(SC.OPERATION_OK, new DFNamesRes(dfNames));
		
		
	}

	
	/**
	 * 
	 * @return
	 */
	public byte[][] GetDFNames(){
		
		byte[] rres;
		byte[][] res = new byte[0][];
		byte[] com = ComCode.GET_DF_NAMES.toBA();
		
		do{
			
			rres = send(com);
		
			if(rres.length > 1)
				res = BAUtils.create2dBA(res, BAUtils.extractSubBA(rres, 1, rres.length-1));
				
			com = SC.ADDITIONAL_FRAME.toBA();
		
		}while(SC.isAF(rres));
		
		byte[][] sc = BAUtils.create2dBA(BAUtils.extractSubBA(rres, 0, 1));
		
		if(SC.isOk(rres)) return BAUtils.join2dBAs(sc, res);
		
		else return sc;
		
	}

	//Get Key Settings
	
	/**
	 * 
	 * @return
	 */
	public DFResponse getKeySettings(){
				
		byte[] res = GetKeySettings();
		
		if(!SC.isOk(res)){
			
			session.resetAuth();
			return new DFResponse(res);
			
		}
		
		DFCrypto.updateCmacIV(ComCode.GET_KEY_SETTINGS, getSession());
		
		DFCrypto.checkCMAC(res, getSession());
		
		
		KeySettingsRes ksr = new KeySettingsRes(BAUtils.extractSubBA(res, 1, 2));
		
		return new DFResponse(SC.OPERATION_OK, ksr);
		
	}
	
	/**
	 * 
	 * @return
	 */
	public byte[] GetKeySettings(){
	
		byte[] com = ComCode.GET_KEY_SETTINGS.toBA();
		
		return send(com);
	
	}
	
	
	//Select Application
	
	/**
	 * 
	 * @param aid
	 * @return
	 */
	public DFResponse selectApplication(AID aid){
		
		if(aid == null) throw new NullPointerException();
		
		byte[] aidBA = aid.toBA();
		
		byte[] res = SelectApplication(aidBA);
		
		if(!SC.isOk(res)){
			
			session.resetAuth();
			return new DFResponse(res);
			
		}

		getSession().setSelectedAID(aid);
		
		getSession().cancelAuth();
		
		return new DFResponse(SC.OPERATION_OK);
		
	}
	
	
	/**
	 * 
	 * @param aid
	 * @return
	 */
	public byte[] SelectApplication(byte[] aid){
		
		if(aid == null) throw new NullPointerException();
		
		byte[] com = BAUtils.concatenateBAs(ComCode.SELECT_APPLICATION.toBA(), aid);
		
		return send(com);
		
	}
		
	//Format PICC
	
	/**
	 * 
	 * @return
	 */
	public DFResponse formatPICC(){
		
		byte[] res = FormatPICC();
		
		if(!SC.isOk(res)){
			
			session.resetAuth();
			return new DFResponse(res);
			
		}
		
		DFCrypto.updateCmacIV(ComCode.FORMAT_PICC, getSession());
		
		DFCrypto.checkCMAC(res, getSession());
		
		return new DFResponse(SC.OPERATION_OK);
		
	}
	
	
	/**
	 * 
	 * @return
	 */
	public byte[] FormatPICC(){
		
		byte[] com = ComCode.FORMAT_PICC.toBA();
		
		return send(com);
		
	}
	
	//Get Version
	
	/**
	 * 
	 * @return
	 */
	public DFResponse getVersion(){
		
		byte[] res = GetVersion();
		
		if(!SC.isOk(res)){
			
			session.resetAuth();
			return new DFResponse(res);
			
		}
		
		DFCrypto.updateCmacIV(ComCode.GET_VERSION, getSession());
		
		DFCrypto.checkCMAC(res, getSession());
		
		byte[] piccVersionBA = DFCrypto.getData(res, getSession());
		
		PICCVersion piccVersion = new PICCVersion(piccVersionBA);
		
		this.setCardType(piccVersion);
		
		return new DFResponse(SC.OPERATION_OK, piccVersion);
				
	}
	
	/**
	 * 
	 * @return
	 */
	public byte[] GetVersion(){
		
		byte[] rres;
		byte[] res = new byte[0];
		byte[] com = ComCode.GET_VERSION.toBA();
		
		do{
		
			rres = send(com);
			
			if(rres.length > 1)
				res = BAUtils.concatenateBAs(res, BAUtils.extractSubBA(rres, 1, rres.length-1));
			
			com = SC.ADDITIONAL_FRAME.toBA();
			
		}while(SC.isAF(rres));
		
		byte[] sc = BAUtils.extractSubBA(rres, 0, 1);
		
		if(SC.isOk(rres)) return BAUtils.concatenateBAs(sc, res);
		
		else return sc;
			
	}

	//Get Card UID
	
	/**
	 * 
	 * @return
	 */
	public DFResponse getCardUID(){
	
		byte[] res = GetCardUID();
		
		if(!SC.isOk(res)){
			
			session.resetAuth();
			return new DFResponse(res);
			
		}		
		
		DFCrypto.updateCmacIV(ComCode.GET_CARD_UID, getSession());
		
		byte[] dec = DFCrypto.decode(BAUtils.extractSubBA(res, 1, 16), session);
		
		byte[] uid = BAUtils.extractSubBA(dec, 0, 7);
		byte[] crcPad = BAUtils.extractSubBA(dec, 7, dec.length);
		
		boolean checked = DFCrypto.checkCRC(dec, SC.OPERATION_OK, session, crcPad);
		
		return new DFResponse(SC.OPERATION_OK, new UIDRes(uid, checked));
		
	}	
	
	/**
	 * 
	 * @return
	 */
	public byte[] GetCardUID(){
		
		byte[] com = ComCode.GET_CARD_UID.toBA();
		
		return send(com);
		
	}

	//APPLICATION LEVEL COMMANDS
	
	//Get File IDs
	
	/**
	 * 
	 * @return
	 */
	public DFResponse getFileIDs(){	
		
		byte[] res = GetFileIDs();
		
		if(!SC.isOk(res)){
			
			session.resetAuth();
			return new DFResponse(res);
			
		}
		
		DFCrypto.updateCmacIV(ComCode.GET_FILE_IDS, getSession());
		
		DFCrypto.checkCMAC(res, getSession());
		
		byte[] fids = DFCrypto.getData(res, getSession());
		
		return new DFResponse(SC.OPERATION_OK, new FIDS(fids));
		
		
		
	}
	
	
	/**
	 * 
	 * @return
	 */
	public byte[] GetFileIDs(){
		
		byte[] com = ComCode.GET_FILE_IDS.toBA();
		
		return send(com);
		
	}

	//Get ISO File IDs
	
	
	public DFResponse getISOFileIDs(){	
		
		byte[] res = GetISOFileIDs();
		
		if(!SC.isOk(res)){
			
			session.resetAuth();
			return new DFResponse(res);
			
		}
		
		DFCrypto.updateCmacIV(ComCode.GET_ISO_FILE_IDS, getSession());
		
		DFCrypto.checkCMAC(res, getSession());
		
		byte[] fids = DFCrypto.getData(res, getSession());
		
		return new DFResponse(SC.OPERATION_OK, new ISOFileIDS(fids));
		
		
		
	}
	
	
	
	/**
	 * 
	 * @return
	 */
	public byte[] GetISOFileIDs(){
		
		byte[] rres;
		byte[] res = new byte[0];
		byte[] com = ComCode.GET_ISO_FILE_IDS.toBA();
		
		do{
		
			rres = send(com);
			
			if(rres.length > 1)
				res = BAUtils.concatenateBAs(res, BAUtils.extractSubBA(rres, 1, rres.length-1));
			
			com = SC.ADDITIONAL_FRAME.toBA();
			
		}while(SC.isAF(rres));
		
		byte[] sc = BAUtils.extractSubBA(rres, 0, 1);
		
		if(SC.isOk(rres)) return BAUtils.concatenateBAs(sc, res);
		
		else return sc;
		
	}
		
	
	//Get File Settings
	
	/**
	 * 
	 * @param fid
	 * @return
	 */
	public DFResponse getFileSettings(FID fid){
		
		if(fid == null) throw new NullPointerException();
		
		byte[] fidBA = fid.toBA();
		
		byte[] res = GetFileSettings(fidBA);
		
		if(!SC.isOk(res)){
			
			getSession().resetAuth();
			return new DFResponse(res);
			
		}	
		
		DFCrypto.updateCmacIV(ComCode.GET_FILE_SETTINGS, fidBA, getSession());
		
		DFCrypto.checkCMAC(res, getSession());
		
		byte[] fileSetBA = DFCrypto.getData(res, getSession());
		
		System.out.println(BAUtils.toString(fileSetBA));

		FileType ft = FileType.toFileType(BAUtils.extractSubBA(fileSetBA, 0, 1));
		
		FileSettings fs;
		
		if((ft == FileType.STANDARD_DATA)||(ft == FileType.BACKUP_DATA)) fs = new DataFileSettings(fileSetBA);
		else if(ft == FileType.VALUE) fs = new ValueFileSettings(fileSetBA);
		else fs = new RecordFileSettings(fileSetBA);
		
		return new DFResponse(SC.OPERATION_OK, fs);
		
		
	}
	
	
	/**
	 * 
	 * @param fid
	 * @return
	 */
	public byte[] GetFileSettings(byte[] fid){
		
		if(fid == null) throw new NullPointerException();
		
		byte[] com = BAUtils.concatenateBAs(ComCode.GET_FILE_SETTINGS.toBA(), fid);
		
		return send(com);
				
	}
	
	//Change File Settings
	
	/**
	 * 
	 * @param fid
	 * @param comSet
	 * @param newAR
	 * @param oldAR
	 * @return
	 */
	public DFResponse changeFileSettings(FID fid, ComSet comSet, AccessRights newAR, AccessRights oldAR){
		
		if((fid == null) || (comSet == null) || (newAR == null) || (oldAR == null)) throw new NullPointerException();
		
		byte[] fidBA = fid.toBA();
		byte[] comSetBA = comSet.toBA();
		byte[] arBA = newAR.toBA();

		byte[] res;
		
		if(oldAR.getChangeAccessRights() == AccessRights.FREE){
			
			res = ChangeFileSettings(fidBA, comSetBA, arBA);
			
		}
		else{
			
			if(getSession().getAuthType() == AuthType.NO_AUTH) return new DFResponse(SC.AUTHENTICATION_ERROR);
			
			byte[] fsBA = BAUtils.concatenateBAs(comSetBA, arBA);
			
			byte[] crc = DFCrypto.CRC(ComCode.CHANGE_FILE_SETTINGS, fidBA, fsBA, getSession().getAuthType());
			
			byte[] cipFileSettings = DFCrypto.encode(BAUtils.concatenateBAs(fsBA, crc), getSession());
			
			res = ChangeFileSettings(fidBA, cipFileSettings);		
			
			
		}
		
		
		if(!SC.isOk(res)){
			session.resetAuth();
			return new DFResponse(res);
		}
		
		DFCrypto.checkCMAC(res, session);
		
		return new DFResponse(BAUtils.extractSubBA(res, 0, 1));
		
		
	}
	
	
	/**
	 * 
	 * @param fid
	 * @param comSet
	 * @param accessRights
	 * @return
	 */
	public byte[] ChangeFileSettings(byte[] fid, byte[] comSet, byte[] accessRights){
		
		if((fid == null) || (comSet == null) || (accessRights == null)) throw new NullPointerException();
		
		byte[] com = BAUtils.concatenateBAs(ComCode.CHANGE_FILE_SETTINGS.toBA(), fid, comSet, accessRights);
		
		return send(com);
		
	}
	
	/**
	 * 
	 * @param fid
	 * @param encFileSettings
	 * @return
	 */
	public byte[] ChangeFileSettings(byte[] fid, byte[] encFileSettings){		
		
		if((fid == null) || (encFileSettings == null)) throw new NullPointerException();
		
		byte[] com = BAUtils.concatenateBAs(ComCode.CHANGE_FILE_SETTINGS.toBA(), fid, encFileSettings);

		return send(com);
		
	}


	//Create Standard Data File
	
	/**
	 * 
	 * @param fid
	 * @param isoFid
	 * @param comSet
	 * @param ar
	 * @param fileSize
	 * @return
	 */
	public DFResponse createStdDataFile(FID fid, ComSet comSet, AccessRights ar, Size fileSize){
		
		if((fid == null) || (comSet == null) || (ar == null) || (fileSize == null))
			throw new NullPointerException();
				
		byte[] fidBA = fid.toBA();
		byte[] comSetBA = comSet.toBA();
		byte[] arBA = ar.toBA();
		byte[] sizeBA = fileSize.toBA();
		
		byte[] res = CreateStdDataFile(fidBA, comSetBA, arBA, sizeBA);
		
		if(!SC.isOk(res)){
			session.resetAuth();
			return new DFResponse(res);
		}
		
		byte[] data = BAUtils.concatenateBAs(fidBA, comSetBA, arBA, sizeBA);
		
		DFCrypto.updateCmacIV(ComCode.CREATE_STD_DATA_FILE, data, getSession());
		
		DFCrypto.checkCMAC(res, getSession());
		
		return new DFResponse(SC.OPERATION_OK);
		
	}
	
	/**
	 * 
	 * @param fid
	 * @param isoFid
	 * @param comSet
	 * @param ar
	 * @param fileSize
	 * @return
	 */
	public DFResponse createStdDataFile(FID fid, ISOFileID isoFid, ComSet comSet, AccessRights ar, Size fileSize){
		
		if((fid == null) || (isoFid == null) || (comSet == null) || (ar == null) || (fileSize == null))
			throw new NullPointerException();
				
		byte[] fidBA = fid.toBA();
		byte[] isoFidBA = isoFid.toBA();
		byte[] comSetBA = comSet.toBA();
		byte[] arBA = ar.toBA();
		byte[] sizeBA = fileSize.toBA();
		
		byte[] res = CreateStdDataFile(fidBA, isoFidBA, comSetBA, arBA, sizeBA);
		
		if(!SC.isOk(res)){
			session.resetAuth();
			return new DFResponse(res);
		}
		
		byte[] data = BAUtils.concatenateBAs(fidBA, isoFidBA, comSetBA, arBA, sizeBA);
		
		DFCrypto.updateCmacIV(ComCode.CREATE_STD_DATA_FILE, data, getSession());
		
		DFCrypto.checkCMAC(res, getSession());
		
		return new DFResponse(SC.OPERATION_OK);	
		
	}
	
	
	/**
	 * 
	 * @param fid
	 * @param comSet
	 * @param accessRights
	 * @param fileSize
	 * @return
	 */
	public byte[] CreateStdDataFile(byte[] fid, byte[] comSet, byte[] accessRights, byte[] fileSize){
		
		if((fid == null) || (comSet == null) || (accessRights == null) || (fileSize == null))
			throw new NullPointerException();
		
		byte[] com = BAUtils.concatenateBAs(ComCode.CREATE_STD_DATA_FILE.toBA(), fid, comSet, accessRights, fileSize);
		
		return send(com);
		
	}
	
	/**
	 * 
	 * @param fid
	 * @param ISOfid
	 * @param comSet
	 * @param accessRights
	 * @param fileSize
	 * @return
	 */
	public byte[] CreateStdDataFile(byte[] fid, byte[] ISOfid, byte[] comSet, byte[] accessRights, byte[] fileSize){
		
		if((fid == null) || (ISOfid == null) || (comSet == null) || (accessRights == null) || (fileSize == null))
			throw new NullPointerException();
		
		byte[] com = BAUtils.concatenateBAs(ComCode.CREATE_STD_DATA_FILE.toBA(), fid, ISOfid, comSet, accessRights, fileSize);
		
		return send(com);
		
	}
	
	
	//Create Backup Data File
	
	/**
	 * 
	 * @param fid
	 * @param isoFid
	 * @param comSet
	 * @param ar
	 * @param fileSize
	 * @return
	 */
	public DFResponse createBackupDataFile(FID fid, ComSet comSet, AccessRights ar, Size fileSize){
		
		if((fid == null) || (comSet == null) || (ar == null) || (fileSize == null))
			throw new NullPointerException();
				
		byte[] fidBA = fid.toBA();
		byte[] comSetBA = comSet.toBA();
		byte[] arBA = ar.toBA();
		byte[] sizeBA = fileSize.toBA();
		
		byte[] res = CreateBackupDataFile(fidBA, comSetBA, arBA, sizeBA);
		
		if(!SC.isOk(res)){
			session.resetAuth();
			return new DFResponse(res);
		}
		
		byte[] data = BAUtils.concatenateBAs(fidBA, comSetBA, arBA, sizeBA);
		
		DFCrypto.updateCmacIV(ComCode.CREATE_BACKUP_DATA_FILE, data, getSession());
		
		DFCrypto.checkCMAC(res, getSession());
		
		return new DFResponse(SC.OPERATION_OK);
		
	}
	
	/**
	 * 
	 * @param fid
	 * @param isoFid
	 * @param comSet
	 * @param ar
	 * @param fileSize
	 * @return
	 */
	public DFResponse createBackupDataFile(FID fid, ISOFileID isoFid, ComSet comSet, AccessRights ar, Size fileSize){
		
		if((fid == null) || (isoFid == null) || (comSet == null) || (ar == null) || (fileSize == null))
			throw new NullPointerException();
				
		byte[] fidBA = fid.toBA();
		byte[] isoFidBA = isoFid.toBA();
		byte[] comSetBA = comSet.toBA();
		byte[] arBA = ar.toBA();
		byte[] sizeBA = fileSize.toBA();
		
		byte[] res = CreateBackupDataFile(fidBA, isoFidBA, comSetBA, arBA, sizeBA);
		
		if(!SC.isOk(res)){
			session.resetAuth();
			return new DFResponse(res);
		}
		
		byte[] data = BAUtils.concatenateBAs(fidBA, isoFidBA, comSetBA, arBA, sizeBA);
		
		DFCrypto.updateCmacIV(ComCode.CREATE_BACKUP_DATA_FILE, data, getSession());
		
		DFCrypto.checkCMAC(res, getSession());
		
		return new DFResponse(SC.OPERATION_OK);	
		
	}
	
	
	/**
	 * 
	 * @param fid
	 * @param comSet
	 * @param accessRights
	 * @param fileSize
	 * @return
	 */
	public byte[] CreateBackupDataFile(byte[] fid, byte[] comSet, byte[] accessRights, byte[] fileSize){
		
		if((fid == null) || (comSet == null) || (accessRights == null) || (fileSize == null))
			throw new NullPointerException();
		
		byte[] com = BAUtils.concatenateBAs(ComCode.CREATE_BACKUP_DATA_FILE.toBA(), fid, comSet, accessRights, fileSize);
		
		return send(com);
		
	}
	
	/**
	 * 
	 * @param fid
	 * @param ISOfid
	 * @param comSet
	 * @param accessRights
	 * @param fileSize
	 * @return
	 */
	public byte[] CreateBackupDataFile(byte[] fid, byte[] ISOfid, byte[] comSet, byte[] accessRights, byte[] fileSize){
		
		if((fid == null) || (ISOfid == null) || (comSet == null) || (accessRights == null) || (fileSize == null))
			throw new NullPointerException();
		
		byte[] com = BAUtils.concatenateBAs(ComCode.CREATE_BACKUP_DATA_FILE.toBA(), fid, ISOfid, comSet, accessRights, fileSize);
		
		return send(com);
		
	}
	
	//Create Value File
	
	/**
	 * 
	 * @param fid
	 * @param comSet
	 * @param ar
	 * @param lowerLimit
	 * @param upperLimit
	 * @param value
	 * @param limCredEn
	 * @return
	 */
	public DFResponse createValueFile(FID fid, ComSet comSet, AccessRights ar, Value lowerLimit, Value upperLimit, Value value, boolean limCredEn){
		
		if((fid == null) || (comSet == null) || (ar == null) || (lowerLimit == null) || (upperLimit == null) || (value == null)) 
			throw new NullPointerException();
			
		byte[] fidBA = fid.toBA();
		byte[] comSetBA = comSet.toBA();
		byte[] arBA = ar.toBA();
		byte[] lowBA = lowerLimit.toBA();
		byte[] upBA = upperLimit.toBA();
		byte[] valueBA = value.toBA();
		byte[] limBA = BAUtils.toBA(limCredEn);
		
		byte[] res = CreateValueFile(fidBA, comSetBA, arBA, lowBA, upBA, valueBA, limBA);
		
		if(!SC.isOk(res)){
			session.resetAuth();
			return new DFResponse(res);
		}
		
		byte[] data = BAUtils.concatenateBAs(fidBA, comSetBA, arBA, lowBA, upBA, valueBA, limBA);
		
		DFCrypto.updateCmacIV(ComCode.CREATE_VALUE_FILE, data, getSession());
		
		DFCrypto.checkCMAC(res, getSession());
		
		return new DFResponse(SC.OPERATION_OK);
		
	}
	
	/**
	 * 
	 * @param fid
	 * @param comSet
	 * @param ar
	 * @param lowerLimit
	 * @param upperLimit
	 * @param value
	 * @param limCredEn
	 * @return
	 */
	public DFResponse createValueFile(FID fid, ComSet comSet, AccessRights ar, Value lowerLimit, Value upperLimit, Value value, boolean limCredEn, boolean freeGetVal){
		
		if((fid == null) || (comSet == null) || (ar == null) || (lowerLimit == null) || (upperLimit == null) || (value == null)) 
			throw new NullPointerException();
				
		byte[] fidBA = fid.toBA();
		byte[] comSetBA = comSet.toBA();
		byte[] arBA = ar.toBA();
		byte[] lowBA = lowerLimit.toBA();
		byte[] upBA = upperLimit.toBA();
		byte[] valueBA = value.toBA();
		byte[] limBA = BAUtils.toBA(limCredEn);
		
		if(freeGetVal) limBA = BAUtils.xor(limBA, BAUtils.toBA("02"));
		
		byte[] res = CreateValueFile(fidBA, comSetBA, arBA, lowBA, upBA, valueBA, limBA);
		
		if(!SC.isOk(res)){
			session.resetAuth();
			return new DFResponse(res);
		}
		
		byte[] data = BAUtils.concatenateBAs(fidBA, comSetBA, arBA, lowBA, upBA, valueBA, limBA);
		
		DFCrypto.updateCmacIV(ComCode.CREATE_VALUE_FILE, data, getSession());
		
		DFCrypto.checkCMAC(res, getSession());
		
		return new DFResponse(SC.OPERATION_OK);
		

		
	}
		
	/**
	 * 
	 * @param fid
	 * @param comSet
	 * @param accessRights
	 * @param lowerLimit
	 * @param upperLimit
	 * @param value
	 * @param limCredEnFreeGetVal
	 * @return
	 */
	public byte[] CreateValueFile(byte[] fid, byte[] comSet, byte[] accessRights, byte[] lowerLimit, byte[] upperLimit, byte[] value,
			byte[] limCredEnFreeGetVal){
		
		
		if((fid == null)|| (comSet == null) || (accessRights == null) || (lowerLimit == null) || (upperLimit == null) || (value == null)
				|| (limCredEnFreeGetVal == null))
			throw new NullPointerException();
		
		byte[] com = BAUtils.concatenateBAs(ComCode.CREATE_VALUE_FILE.toBA(), fid, comSet, accessRights, lowerLimit, upperLimit, value,
				limCredEnFreeGetVal);
		
		return send(com);
		
	}
	
	//Create Linear Record File
	
	/**
	 * 
	 * @param fid
	 * @param comSet
	 * @param ar
	 * @param recSize
	 * @param maxNumOfRecords
	 * @return
	 */
	public DFResponse createLinearRecordFile(FID fid, ComSet comSet, AccessRights ar, Size recSize, int maxNumOfRecords){
				
		if((fid == null) || (comSet == null) || (ar == null) || (recSize == null))
			throw new NullPointerException();
		
		byte[] fidBA = fid.toBA();
		byte[] comSetBA = comSet.toBA();
		byte[] arBA = ar.toBA();
		byte[] recSizeBA = recSize.toBA();
		byte[] maxBA = BAUtils.toBA(maxNumOfRecords, 3);
		
		byte[] res = CreateLinearRecordFile(fidBA, comSetBA, arBA, recSizeBA, maxBA);
		
		if(!SC.isOk(res)){
			session.resetAuth();
			return new DFResponse(res);
		}
		
		byte[] data = BAUtils.concatenateBAs(fidBA, comSetBA, arBA, recSizeBA, maxBA);
		
		DFCrypto.updateCmacIV(ComCode.CREATE_LINEAR_RECORD_FILE, data, getSession());
		
		DFCrypto.checkCMAC(res, getSession());
		
		return new DFResponse(SC.OPERATION_OK);	
		
		
	}
	
	/**
	 * 
	 * @param fid
	 * @param isoFid
	 * @param comSet
	 * @param ar
	 * @param recSize
	 * @param maxNumOfRecords
	 * @return
	 */
	public DFResponse createLinearRecordFile(FID fid, ISOFileID isoFid, ComSet comSet, AccessRights ar, Size recSize, int maxNumOfRecords){
				
		if((fid == null) || (isoFid == null) || (comSet == null) || (ar == null) || (recSize == null))
			throw new NullPointerException();
		
		byte[] fidBA = fid.toBA();
		byte[] isoFidBA = isoFid.toBA();
		byte[] comSetBA = comSet.toBA();
		byte[] arBA = ar.toBA();
		byte[] recSizeBA = recSize.toBA();
		byte[] maxBA = BAUtils.toBA(maxNumOfRecords, 3);
		
		byte[] res = CreateLinearRecordFile(fidBA, isoFidBA, comSetBA, arBA, recSizeBA, maxBA);
		
		if(!SC.isOk(res)){
			session.resetAuth();
			return new DFResponse(res);
		}
		
		byte[] data = BAUtils.concatenateBAs(fidBA, isoFidBA, comSetBA, arBA, recSizeBA, maxBA);
		
		DFCrypto.updateCmacIV(ComCode.CREATE_LINEAR_RECORD_FILE, data, getSession());
		
		DFCrypto.checkCMAC(res, getSession());
		
		return new DFResponse(SC.OPERATION_OK);	
		
		
	}
	
	
	/**
	 * 
	 * @param fid
	 * @param comSet
	 * @param accessRights
	 * @param recordSize
	 * @param maxNumOfRecords
	 * @return
	 */
	public byte[] CreateLinearRecordFile(byte[] fid, byte[] comSet, byte[] accessRights, byte[] recordSize, byte[] maxNumOfRecords){		
		
		if((fid == null)|| (comSet == null) || (accessRights == null) || (recordSize == null) || (maxNumOfRecords == null))
			throw new NullPointerException();
		
		byte[] com = BAUtils.concatenateBAs(ComCode.CREATE_LINEAR_RECORD_FILE.toBA(), fid, comSet, accessRights, recordSize, maxNumOfRecords);
		
		return send(com);
		
	}
	
	/**
	 * 
	 * @param fid
	 * @param ISOFid
	 * @param comSet
	 * @param accessRights
	 * @param recordSize
	 * @param maxNumOfRecords
	 * @return
	 */
	public byte[] CreateLinearRecordFile(byte[] fid, byte[] ISOFid, byte[] comSet, byte[] accessRights, byte[] recordSize, byte[] maxNumOfRecords){		
		
		if((fid == null)|| (ISOFid == null) || (comSet == null) || (accessRights == null) || (recordSize == null) || (maxNumOfRecords == null))
			throw new NullPointerException();
		
		byte[] com = BAUtils.concatenateBAs(ComCode.CREATE_LINEAR_RECORD_FILE.toBA(), fid, ISOFid, comSet, accessRights, recordSize, maxNumOfRecords);
		
		return send(com);
		
	}
	
	//Create Cyclic Record File
	
	/**
	 * 
	 * @param fid
	 * @param comSet
	 * @param ar
	 * @param recSize
	 * @param maxNumOfRecords
	 * @return
	 */
	public DFResponse createCyclicRecordFile(FID fid, ComSet comSet, AccessRights ar, Size recSize, int maxNumOfRecords){
				
		if((fid == null) || (comSet == null) || (ar == null) || (recSize == null))
			throw new NullPointerException();
		
		byte[] fidBA = fid.toBA();
		byte[] comSetBA = comSet.toBA();
		byte[] arBA = ar.toBA();
		byte[] recSizeBA = recSize.toBA();
		byte[] maxBA = BAUtils.toBA(maxNumOfRecords, 3);
		
		byte[] res = CreateCyclicRecordFile(fidBA, comSetBA, arBA, recSizeBA, maxBA);
		
		if(!SC.isOk(res)){
			session.resetAuth();
			return new DFResponse(res);
		}
		
		byte[] data = BAUtils.concatenateBAs(fidBA, comSetBA, arBA, recSizeBA, maxBA);
		
		DFCrypto.updateCmacIV(ComCode.CREATE_CYCLIC_RECORD_FILE, data, getSession());
		
		DFCrypto.checkCMAC(res, getSession());
		
		return new DFResponse(SC.OPERATION_OK);	
		
		
	}
	
	/**
	 * 
	 * @param fid
	 * @param isoFid
	 * @param comSet
	 * @param ar
	 * @param recSize
	 * @param maxNumOfRecords
	 * @return
	 */
	public DFResponse createCyclicRecordFile(FID fid, ISOFileID isoFid, ComSet comSet, AccessRights ar, Size recSize, int maxNumOfRecords){
				
		if((fid == null) || (isoFid == null) || (comSet == null) || (ar == null) || (recSize == null))
			throw new NullPointerException();
		
		byte[] fidBA = fid.toBA();
		byte[] isoFidBA = isoFid.toBA();
		byte[] comSetBA = comSet.toBA();
		byte[] arBA = ar.toBA();
		byte[] recSizeBA = recSize.toBA();
		byte[] maxBA = BAUtils.toBA(maxNumOfRecords, 3);
		
		byte[] res = CreateCyclicRecordFile(fidBA, isoFidBA, comSetBA, arBA, recSizeBA, maxBA);
		
		if(!SC.isOk(res)){
			session.resetAuth();
			return new DFResponse(res);
		}
		
		byte[] data = BAUtils.concatenateBAs(fidBA, isoFidBA, comSetBA, arBA, recSizeBA, maxBA);
		
		DFCrypto.updateCmacIV(ComCode.CREATE_CYCLIC_RECORD_FILE, data, getSession());
		
		DFCrypto.checkCMAC(res, getSession());
		
		return new DFResponse(SC.OPERATION_OK);	
		
		
	}
	
	
	/**
	 * 
	 * @param fid
	 * @param comSet
	 * @param accessRights
	 * @param recordSize
	 * @param maxNumOfRecords
	 * @return
	 */
	public byte[] CreateCyclicRecordFile(byte[] fid, byte[] comSet, byte[] accessRights, byte[] recordSize, byte[] maxNumOfRecords){
				
		if((fid == null)|| (comSet == null) || (accessRights == null) || (recordSize == null) || (maxNumOfRecords == null))
			throw new NullPointerException();
		
		byte[] com = BAUtils.concatenateBAs(ComCode.CREATE_CYCLIC_RECORD_FILE.toBA(), fid, comSet, accessRights, recordSize, maxNumOfRecords);
		
		return send(com);
		
	}
	
	/**
	 * 
	 * @param fid
	 * @param ISOFid
	 * @param comSet
	 * @param accessRights
	 * @param recordSize
	 * @param maxNumOfRecords
	 * @return
	 */
	public byte[] CreateCyclicRecordFile(byte[] fid, byte[] ISOFid, byte[] comSet, byte[] accessRights, byte[] recordSize, byte[] maxNumOfRecords){		
		
		if((fid == null)|| (ISOFid == null) || (comSet == null) || (accessRights == null) || (recordSize == null) || (maxNumOfRecords == null))
			throw new NullPointerException();
		
		byte[] com = BAUtils.concatenateBAs(ComCode.CREATE_CYCLIC_RECORD_FILE.toBA(), fid, ISOFid, comSet, accessRights, recordSize, maxNumOfRecords);
		
		return send(com);
		
	}
	
	//Delete File
	
	/**
	 * 
	 * @param fid
	 * @return
	 */
	public DFResponse deleteFile(FID fid){
		
		if(fid == null) throw new NullPointerException();
		
		byte[] fidBA = fid.toBA();
		
		byte[] res = DeleteFile(fidBA);
		
		if(!SC.isOk(res)){
			session.resetAuth();
			return new DFResponse(res);
		}
		
		DFCrypto.updateCmacIV(ComCode.DELETE_FILE, fidBA, getSession());
		
		DFCrypto.checkCMAC(res, getSession());
		
		return new DFResponse(SC.OPERATION_OK);
		
	}
	
	/**
	 * 
	 * @param fid
	 * @return
	 */
	public byte[] DeleteFile(byte[] fid){
		
		if(fid == null) throw new NullPointerException();
		
		byte[] com = BAUtils.concatenateBAs(ComCode.DELETE_FILE.toBA(), fid);
		
		return send(com);
		
	}
	
	//DATA MANIPULATION COMMANDS
	
	//Write Data
	
	/**
	 * 
	 * @param fid
	 * @param offset
	 * @param data
	 * @param fileSet
	 * @return
	 */
	public DFResponse writeData(FID fid, Size offset, Data data, DataFileSettings fileSet){
		
		if((fid == null) || (offset == null) || (data == null) || (fileSet == null)) throw new NullPointerException();
		
		return writeData(fid, offset, data, fileSet.getComSet(), fileSet.getAccessRights());
			
	}
	
	/**
	 * 
	 * @param fid
	 * @param offset
	 * @param data
	 * @param comSet
	 * @param ar
	 * @return
	 */
	public DFResponse writeData(FID fid, Size offset, Data data, ComSet comSet, AccessRights ar){
		
		if((fid == null) || (offset == null) || (data == null) || (comSet == null) || (ar == null)) 
			throw new NullPointerException();
		
		byte[] sendData = DFCrypto.prepareSendData(fid, offset, data, comSet, ar, ComCode.WRITE_DATA, getSession());
		
		byte[] fidBA = fid.toBA();
		byte[] offsetBA = offset.toBA();
		byte[] lengthBA = data.getLength().toBA();
		
		byte[] res = WriteData(fidBA, offsetBA, lengthBA, sendData);
		
		
		if(!SC.isOk(res)){
			session.resetAuth();
			return new DFResponse(res);
		}
		
		DFCrypto.checkCMAC(res, session);
		
		return new DFResponse(BAUtils.extractSubBA(res, 0, 1));
		
	}
	
	
	/**
	 * 
	 * @param fid
	 * @param offset
	 * @param length
	 * @param data
	 * @return
	 */
	public byte[] WriteData(byte[] fid, byte[] offset, byte[] length, byte[] data){
		
		if((fid == null) || (offset == null) || (length == null) || (data == null))
			throw new NullPointerException();
			
		byte[][] dataFrames = getDataFrames(data);
		
		byte[] com = BAUtils.concatenateBAs(ComCode.WRITE_DATA.toBA(), fid, offset, length, dataFrames[0]);
		
		byte[] res = send(com);
		
		if((!SC.isOk(res))&&(!SC.isAF(res))) return BAUtils.extractSubBA(res, 0, 1);
		
		int i = 1;
		
		while((i < dataFrames.length)&&(!SC.isOk(res))){
			
			com = BAUtils.concatenateBAs(SC.ADDITIONAL_FRAME.toBA(), dataFrames[i]);			
			res = send(com);			
			if((!SC.isOk(res))&&(!SC.isAF(res))) return BAUtils.extractSubBA(res, 0, 1);			
			i++;
		}
		
		return res;
	}
	
	//Read Data
	
	/**
	 * 
	 * @param fid
	 * @param offset
	 * @param length
	 * @param fileSet
	 * @return
	 */
	public DFResponse readData(FID fid, Size offset, Size length, DataFileSettings fileSet){
		
		if((fid == null) || (offset == null) || (length == null) || (fileSet == null)) 
			throw new NullPointerException();
		
		return readData(fid, offset, length, fileSet.getComSet(), fileSet.getAccessRights());
			
	}	
		
	/**
	 * 	
	 * @param fid
	 * @param offset
	 * @param length
	 * @param comSet
	 * @param ar
	 * @return
	 */
	public DFResponse readData(FID fid, Size offset, Size length, ComSet comSet, AccessRights ar){
		
		if((fid == null) || (offset == null) || (length == null) || (comSet == null) || (ar == null)) 
			throw new NullPointerException();	
		
		byte[] fidBA = fid.toBA();
		byte[] offsetBA = offset.toBA();
		byte[] lengthBA = length.toBA();
		
		byte[] res = ReadData(fidBA, offsetBA, lengthBA);
		
		if(!SC.isOk(res)){
			session.resetAuth();
			return new DFResponse(res);
		}
		
		DFCrypto.updateCmacIV(ComCode.READ_DATA, BAUtils.concatenateBAs(fidBA, offsetBA, lengthBA), getSession());
		
		DataRes dataRes = DFCrypto.getDataRes(res, length, comSet, ar, getSession());
		
		return new DFResponse(SC.OPERATION_OK, dataRes);
		
	}	
	
	
		/**
		 * 
		 * @param fid
		 * @param offset
		 * @param length
		 * @return
		 */
		public byte[] ReadData(byte[] fid, byte[] offset, byte[] length){
			
			if((fid == null) || (offset == null) || (length == null))
				throw new NullPointerException();
			
			byte[] rres;
			byte[] res = new byte[0];
			byte[] com = BAUtils.concatenateBAs(ComCode.READ_DATA.toBA(), fid, offset, length);
			
			do{
			
				rres = send(com);
				
				if(rres.length > 1)
					res = BAUtils.concatenateBAs(res, BAUtils.extractSubBA(rres, 1, rres.length-1));
				
				com = SC.ADDITIONAL_FRAME.toBA();
				
			}while(SC.isAF(rres));
			
			byte[] sc = BAUtils.extractSubBA(rres, 0, 1);
			
			if(SC.isOk(rres)) return BAUtils.concatenateBAs(sc, res);
			
			else return sc;
					
		}
	
	
	//Get Value
	
	/**
	 * 
	 * @param fid
	 * @param fileSet
	 * @return
	 */
	public DFResponse getValue(FID fid, ValueFileSettings fileSet){
		
		if((fid == null) || (fileSet == null)) throw new NullPointerException();
		
		return getValue(fid, fileSet.getComSet(), fileSet.getAccessRights(), fileSet.getFreeValueEnabled());
			
	}	
		
		/**
		 * 
		 * @param fid
		 * @param comSet
		 * @param ar
		 * @param getFreeValue
		 * @return
		 */
	public DFResponse getValue(FID fid, ComSet comSet, AccessRights ar, boolean getFreeValue){
		
		if((fid == null) || (comSet == null) || (ar == null)) throw new NullPointerException();	
		
		byte[] fidBA = fid.toBA();
		
		byte[] res = GetValue(fidBA);
		
		if(!SC.isOk(res)){
			session.resetAuth();
			return new DFResponse(res);
		}
		
		DFCrypto.updateCmacIV(ComCode.GET_VALUE, fidBA, getSession());
		
		ValueRes valueRes = DFCrypto.getValueRes(res, comSet, ar, getFreeValue, getSession());
		
		return new DFResponse(SC.OPERATION_OK, valueRes);
		
	}	
		
	/**
	 * 
	 * @param fid
	 * @return
	 */
	public byte[] GetValue(byte[] fid){
		
		if(fid == null) throw new NullPointerException();
		
		byte[] com = BAUtils.concatenateBAs(ComCode.GET_VALUE.toBA(), fid);
		
		return send(com);
		
	}
	
	//Credit
	
	/**
	 * 
	 * @param fid
	 * @param value
	 * @param fileSet
	 * @return
	 */
	public DFResponse credit(FID fid, Value value, ValueFileSettings fileSet){
		
		if((fid == null) || (value == null) || (fileSet == null)) throw new NullPointerException();
		
		return credit(fid, value, fileSet.getComSet(), fileSet.getAccessRights());
			
	}
	
	/**
	 * 
	 * @param fid
	 * @param comSet
	 * @param newAR
	 * @param oldAR
	 * @return
	 */
	public DFResponse credit(FID fid, Value value, ComSet comSet, AccessRights ar){
		
		if((fid == null) || (value == null) || (comSet == null) || (ar == null)) throw new NullPointerException();
		
		byte[] fidBA = fid.toBA();

		byte[] valueData = DFCrypto.prepareSendValueData(fid, value, comSet, ar, ComCode.CREDIT, getSession());
		
		byte[] res = Credit(fidBA, valueData);
		
		
		if(!SC.isOk(res)){
			session.resetAuth();
			return new DFResponse(res);
		}
		
		DFCrypto.checkCMAC(res, session);
		
		return new DFResponse(BAUtils.extractSubBA(res, 0, 1));
		
		
	}
	
	
	/**
	 * 
	 * @param fid
	 * @param data
	 * @return
	 */
	public byte[] Credit(byte[] fid, byte[] data){
		
		if((fid == null) || (data == null)) throw new NullPointerException();
		
		byte[] com = BAUtils.concatenateBAs(ComCode.CREDIT.toBA(), fid, data);
		
		return send(com);
		
	}
	
	//Debit
	
	/**
	 * 
	 * @param fid
	 * @param value
	 * @param fileSet
	 * @return
	 */
	public DFResponse debit(FID fid, Value value, ValueFileSettings fileSet){
		
		if((fid == null) || (value == null) || (fileSet == null)) throw new NullPointerException();
		
		return debit(fid, value, fileSet.getComSet(), fileSet.getAccessRights());
			
	}
	
	/**
	 * 
	 * @param fid
	 * @param comSet
	 * @param newAR
	 * @param oldAR
	 * @return
	 */
	public DFResponse debit(FID fid, Value value, ComSet comSet, AccessRights ar){
		
		if((fid == null) || (value == null) || (comSet == null) || (ar == null)) throw new NullPointerException();
		
		byte[] fidBA = fid.toBA();

		byte[] valueData = DFCrypto.prepareSendValueData(fid, value, comSet, ar, ComCode.DEBIT, getSession());
		
		byte[] res = Debit(fidBA, valueData);
			
		if(!SC.isOk(res)){
			session.resetAuth();
			return new DFResponse(res);
		}
		
		DFCrypto.checkCMAC(res, session);
		
		return new DFResponse(BAUtils.extractSubBA(res, 0, 1));
		
		
	}
	
	/**
	 * 
	 * @param fid
	 * @param data
	 * @return
	 */
	public byte[] Debit(byte[] fid, byte[] data){
		
		if((fid == null) || (data == null)) throw new NullPointerException();
		
		byte[] com = BAUtils.concatenateBAs(ComCode.DEBIT.toBA(), fid, data);
		
		return send(com);
		
	}
	
	//Limited Credit
	
	/**
	 * 
	 * @param fid
	 * @param value
	 * @param fileSet
	 * @return
	 */
	public DFResponse limitedCredit(FID fid, Value value, ValueFileSettings fileSet){
		
		if((fid == null) || (value == null) || (fileSet == null)) throw new NullPointerException();
		
		return debit(fid, value, fileSet.getComSet(), fileSet.getAccessRights());
			
	}
	
	/**
	 * 
	 * @param fid
	 * @param comSet
	 * @param newAR
	 * @param oldAR
	 * @return
	 */
	public DFResponse limitedCredit(FID fid, Value value, ComSet comSet, AccessRights ar){
		
		if((fid == null) || (value == null) || (comSet == null) || (ar == null)) throw new NullPointerException();
		
		byte[] fidBA = fid.toBA();

		byte[] valueData = DFCrypto.prepareSendValueData(fid, value, comSet, ar, ComCode.LIMITED_CREDIT, getSession());
		
		byte[] res = LimitedCredit(fidBA, valueData);
				
		if(!SC.isOk(res)){
			session.resetAuth();
			return new DFResponse(res);
		}
		
		DFCrypto.checkCMAC(res, session);
		
		return new DFResponse(BAUtils.extractSubBA(res, 0, 1));
				
	}
	
	/**
	 * 
	 * @param fid
	 * @param data
	 * @return
	 */
	public byte[] LimitedCredit(byte[] fid, byte[] data){
		
		if((fid == null) || (data == null))
			throw new NullPointerException();
		
		byte[] com = BAUtils.concatenateBAs(ComCode.LIMITED_CREDIT.toBA(), fid, data);
		
		return send(com);
		
	}
	
	//Write Record
	
	/**
	 * 
	 * @param fid
	 * @param offset
	 * @param data
	 * @param fileSet
	 * @return
	 */
	public DFResponse writeRecord(FID fid, Size offset, Data data, RecordFileSettings fileSet){
		
		if((fid == null) || (offset == null) || (data == null) || (fileSet == null)) throw new NullPointerException();
		
		return writeRecord(fid, offset, data, fileSet.getComSet(), fileSet.getAccessRights());
			
	}
	
	/**
	 * 
	 * @param fid
	 * @param offset
	 * @param data
	 * @param comSet
	 * @param ar
	 * @return
	 */
	public DFResponse writeRecord(FID fid, Size offset, Data data, ComSet comSet, AccessRights ar){
		
		if((fid == null) || (offset == null) || (data == null) || (comSet == null) || (ar == null)) 
			throw new NullPointerException();
		
		byte[] sendData = DFCrypto.prepareSendData(fid, offset, data, comSet, ar, ComCode.WRITE_RECORD, getSession());
		
		byte[] fidBA = fid.toBA();
		byte[] offsetBA = offset.toBA();
		byte[] lengthBA = data.getLength().toBA();
		
		byte[] res = WriteRecord(fidBA, offsetBA, lengthBA, sendData);
				
		if(!SC.isOk(res)){
			session.resetAuth();
			return new DFResponse(res);
		}
		
		DFCrypto.checkCMAC(res, session);
		
		return new DFResponse(BAUtils.extractSubBA(res, 0, 1));
		
		
		
	}
	
	/**
	 * 
	 * @param fid
	 * @param offset
	 * @param length
	 * @param data
	 * @return
	 */
	public byte[] WriteRecord(byte[] fid, byte[] offset, byte[] length, byte[] data){
		
		if((fid == null) || (offset == null) || (length == null) || (data == null))
			throw new NullPointerException();
			
		byte[][] dataFrames = getDataFrames(data);
		
		byte[] com = BAUtils.concatenateBAs(ComCode.WRITE_RECORD.toBA(), fid, offset, length, dataFrames[0]);
		
		byte[] res = send(com);
		
		if((!SC.isOk(res))&&(!SC.isAF(res))) return BAUtils.extractSubBA(res, 0, 1);
		
		int i = 1;
		
		while((i < dataFrames.length)&&(!SC.isOk(res))){
			
			com = BAUtils.concatenateBAs(SC.ADDITIONAL_FRAME.toBA(), dataFrames[i]);			
			res = send(com);			
			if((!SC.isOk(res))&&(!SC.isAF(res))) return BAUtils.extractSubBA(res, 0, 1);			
			i++;
		}
		
		return res;
	}
	
	//Read Records
	
	/**
	 * 
	 * @param fid
	 * @param offset
	 * @param length
	 * @param fileSet
	 * @return
	 */
	public DFResponse readRecords(FID fid, Size offset, Size length, RecordFileSettings fileSet){
		
		if((fid == null) || (offset == null) || (length == null) || (fileSet == null)) 
			throw new NullPointerException();
		
		return readRecords(fid, offset, length, fileSet.getRecordSize(), fileSet.getCurrentNumberOfRecords(), fileSet.getComSet(), fileSet.getAccessRights());
			
	}	
		
	/**
	 * 	
	 * @param fid
	 * @param offset
	 * @param length
	 * @param comSet
	 * @param ar
	 * @return
	 */
	public DFResponse readRecords(FID fid, Size offset, Size length, Size recSize, int currNumOfRecords, ComSet comSet, AccessRights ar){
		
		if((fid == null) || (offset == null) || (length == null) || (recSize == null) || (comSet == null) || (ar == null)) 
			throw new NullPointerException();	
		
		byte[] fidBA = fid.toBA();
		byte[] offsetBA = offset.toBA();
		byte[] lengthBA = length.toBA();
		
		byte[] res = ReadRecords(fidBA, offsetBA, lengthBA);
		
		if(!SC.isOk(res)){
			session.resetAuth();
			return new DFResponse(res);
		}
		
		DFCrypto.updateCmacIV(ComCode.READ_RECORDS, BAUtils.concatenateBAs(fidBA, offsetBA, lengthBA), getSession());
		
		RecordsRes recordsRes = DFCrypto.getRecordsRes(res, length, recSize, comSet, ar, getSession());
		
		return new DFResponse(SC.OPERATION_OK, recordsRes);
		
	}	
	
	/**
	 * 
	 * @param fid
	 * @param offset
	 * @param length
	 * @return
	 */
	public byte[] ReadRecords(byte[] fid, byte[] offset, byte[] length){
		
		if((fid == null) || (offset == null) || (length == null))
			throw new NullPointerException();
		
		byte[] rres;
		byte[] res = new byte[0];
		byte[] com = BAUtils.concatenateBAs(ComCode.READ_RECORDS.toBA(), fid, offset, length);
		
		do{
		
			rres = send(com);
			
			if(rres.length > 1)
				res = BAUtils.concatenateBAs(res, BAUtils.extractSubBA(rres, 1, rres.length-1));
			
			com = SC.ADDITIONAL_FRAME.toBA();
			
		}while(SC.isAF(rres));
		
		byte[] sc = BAUtils.extractSubBA(rres, 0, 1);
		
		if(SC.isOk(rres)) return BAUtils.concatenateBAs(sc, res);
		
		else return sc;
				
	}
	
	//Clear Record File
	
	/**
	 * 
	 * @param fid
	 * @return
	 */
	public DFResponse clearRecordFile(FID fid){
		
		if(fid == null) throw new NullPointerException();
		
		byte[] fidBA = fid.toBA();
		
		byte[] res = ClearRecordFile(fidBA);
		
		if(!SC.isOk(res)){
			session.resetAuth();
			return new DFResponse(res);
		}
		
		DFCrypto.updateCmacIV(ComCode.CLEAR_RECORD_FILE, fidBA, getSession());
		
		DFCrypto.checkCMAC(res, getSession());
		
		return new DFResponse(SC.OPERATION_OK);
		
	}
	
	
	/**
	 * 
	 * @param fid
	 * @return
	 */
	public byte[] ClearRecordFile(byte[] fid){
		
		if(fid == null) throw new NullPointerException();
		
		byte[] com = BAUtils.concatenateBAs(ComCode.CLEAR_RECORD_FILE.toBA(), fid);
		
		return send(com);
		
	}
	
	//Commit Transaction
	
	public DFResponse commitTransaction(){
		
		byte[] res = CommitTransaction();
		
		if(!SC.isOk(res)){
			
			session.resetAuth();
			return new DFResponse(res);
			
		}
		
		DFCrypto.updateCmacIV(ComCode.COMMIT_TRANSACTION, getSession());
		
		DFCrypto.checkCMAC(res, getSession());
		
		return new DFResponse(SC.OPERATION_OK);
		
		
	}
	
	
	/**
	 * 
	 * @return
	 */
	public byte[] CommitTransaction(){
		
		byte[] com = ComCode.COMMIT_TRANSACTION.toBA();
		
		return send(com);
		
	}
	
	//Abort Transaction
	
	/**
	 * 
	 * @return
	 */
	public DFResponse abortTransaction(){
		
		byte[] res = AbortTransaction();
		
		if(!SC.isOk(res)){
			
			session.resetAuth();
			return new DFResponse(res);
			
		}
		
		DFCrypto.updateCmacIV(ComCode.ABORT_TRANSACTION, getSession());
		
		DFCrypto.checkCMAC(res, getSession());
		
		return new DFResponse(SC.OPERATION_OK);
		
			
	}
	
	/**
	 * 
	 * @return
	 */
	public byte[] AbortTransaction(){
		
		byte[] com = ComCode.ABORT_TRANSACTION.toBA();
		
		return send(com);
		
	}
	
	
	
	public DFResponse isoSelect(){
		
		byte[] res = ISOSelect();
		
		if(BAUtils.compareBAs(BAUtils.extractSubBA(res, 0, 1), BAUtils.toBA("90"))) wrap = true;
		
		return new DFResponse(SC.OPERATION_OK);
		
		
	}
	
	public byte[] ISOSelect(){
		
		byte[] com = BAUtils.toBA("00A40000");
		
		return send(com);
		
	}
	
	
	//Auxiliary methods
	
	/**
	 * 
	 * @param data
	 * @return
	 */
	private byte[][] getDataFrames(byte[] data){
		
		if(data == null) throw new NullPointerException();
		
		int numOfFrames = getNumOfFrames(data);
		int maxFrameSize, frameSize;
		int pos = 0;
		
		int first = (wrap)? 42 : 52;
		int others = (wrap)? 49 : 59;
		
		byte[][] dataFrames = new byte[numOfFrames][];
		
		for(int i = 0; i < numOfFrames; i++){
			
			if(i == 0) maxFrameSize = first;
			else maxFrameSize = others;
			
			frameSize = Math.min(data.length - pos, maxFrameSize);
			
			dataFrames[i] = BAUtils.extractSubBA(data, pos, frameSize);
			
			pos = pos + frameSize;
			
		}		
		
		return dataFrames;
				
	}
	
	/**
	 * 
	 * @param data
	 * @return
	 */
	private int getNumOfFrames(byte[] data){
		
		if(data == null) throw new NullPointerException();
		
		int first = (wrap)? 42 : 52;
		int others = (wrap)? 49 : 59;
		
		int num = 0;
		int len = data.length;
		
		if(len > 0){
			num++;
			len -= first;
			while(len > 0){
				num++;
				len -= others;
			}
		}
		
		return num;
		
	}
	
	/**
	 * 
	 * @param command
	 * @return
	 */
	
	private byte[] send(byte[] command){
		
		if(command == null) throw new NullPointerException();
		
		if(wrap) return sendWrap(command);
		
		return this.cm.send(command);
	}
	
	/**
	 * 
	 * @param bas
	 * @return
	 */
	private byte[] send(byte[]...bas){
		
		if(bas == null) throw new NullPointerException();
		
		byte[] com = BAUtils.concatenateBAs(bas);
		
		return send(com);
		
	}
	
	/**
	 * 
	 * @param command
	 * @return
	 */
	private byte[] sendWrap(byte[] command){
	
		if(command == null) throw new NullPointerException();
		if(command.length < 1) throw new IllegalArgumentException();
		
		byte[] wrappedCom = wrapCom(command);

		byte[] wrappedRes = this.cm.send(wrappedCom);
		
		return unwrapRes(wrappedRes);
		
	}
	
	/**
	 * 
	 * @param command
	 * @return
	 */
	private byte[] wrapCom(byte[] command){
		
		byte[] comCode = BAUtils.extractSubBA(command, 0, 1);
		byte[] CLA = BAUtils.toBA("90");
		byte[] zero = new byte[1];
		
		byte[] com = BAUtils.concatenateBAs(CLA, comCode, zero, zero);
		
		int dataLen = command.length - 1;
		
		byte[] len = BAUtils.toBA(dataLen, 1);
		
		com = BAUtils.concatenateBAs(com, len);
		
		if(dataLen > 0){
			byte[] data = BAUtils.extractSubBA(command, 1, dataLen);
			com = BAUtils.concatenateBAs(com, data);
		}
		else return com;
		
		com = BAUtils.concatenateBAs(com, zero);
		
		return com;
		
		
	}
	
	/**
	 * 
	 * @param res
	 * @return
	 */
	private byte[] unwrapRes(byte[] res){
		
		byte[] sc = BAUtils.extractSubBA(res, res.length - 1, 1);
		
		if(res.length > 2){
		
			byte[] data = BAUtils.extractSubBA(res, 0, res.length - 2);
			return BAUtils.concatenateBAs(sc, data);
			
		}
		
		return sc;
		
	}
	
	/**
	 * 
	 */
	public void resetSession(){
		
		this.session.resetAuth();
		
	}
	
	/**
	 * 
	 * @param auth
	 * @param sessionKey
	 * @param PICCAuth
	 */
	public void setSession(AuthType auth, int authKeyNum, byte[] sessionKey, boolean PICCAuth){
		
		if((auth == null) || (sessionKey == null)) throw new NullPointerException();
		
		this.session.setAuth(auth, authKeyNum, sessionKey, PICCAuth);
		
	}
	
	/**
	 * 
	 * @return
	 */
	public DFSession getSession(){ return this.session; }
	
	/**
	 * 
	 * @param ct
	 */
	public void setCardType(CardType ct){
		
		if(ct == null) throw new NullPointerException();
		
		this.ct = ct;
		
	}
	
	/**
	 * 
	 * @param version
	 */
	public void setCardType(PICCVersion version){
		
		int major = version.getHardwareInfo().getMajorVersionNumber();
		
		if(major == 0) setCardType(CardType.MIFARE_DESFIRE);
		else setCardType(CardType.MIFARE_DESFIRE_EV1);
		
	}
	
	
	/**
	 * 
	 * @return
	 */
	public CardType getCardType(){
		
		return this.ct;
		
	}
	
	private boolean wrap;
	
	private DFSession session;
	private CardType ct;
	private ComManager cm;
	
}
