package dflibrary.samples.ticketing;

import dflibrary.library.*;
import dflibrary.library.param.*;
import dflibrary.library.param.fileset.*;
import dflibrary.library.security.DFKey;
import dflibrary.middleware.*;
import dflibrary.utils.security.CipAlg;

public class TicketingDFCard {
	
	/**
	 * 
	 * @param cm
	 */
	public TicketingDFCard(ComManager cm){
		
		this.df = new DFCard(cm);
		setLegacyUID();
		
	}
	
	/**
	 * 
	 * @param keyNum
	 * @return
	 */
	protected DFResponse authentication(int keyNum){
		
		DFResponse res;
		
		res = df.getKeyVersion(keyNum);
		
		if(!res.isOk()) return res;
		
		int keyVersion = res.getKeyVersion().toInt();
		
		DFKey key;	
		CipAlg alg;
		
		alg = getKeyAlg();
		
		//
		System.out.println("Algoritmo solicitado: " + alg);
	
		key = KeyProvider.getKey(keyVersion, alg);

		//
		System.out.println("Clave recuperada:  " + key);
		
		if(legacy) return df.authenticate(keyNum, key);
		else return df.authenticateISO(keyNum, key);
		
	}
	
	/**
	 * 
	 * @param keyNum
	 * @param newKeyVersion
	 * @return
	 */
	protected DFResponse changeKey(int keyNum, int newKV){
		
		DFResponse res;
		CipAlg alg;
		DFKey oldKey, newKey;
		
		res = df.getKeySettings();
		if(!res.isOk()) return res;

		int chKey = res.getKeySettings().getChangeKeyAccessRights();
	
		if(chKey == KeySettings.ALLKEYSFROZEN) return new DFResponse(SC.PERMISSION_DENIED);
		
		res = df.getKeyVersion(keyNum);
		if(!res.isOk()) return res;
				
		int oldKV = res.getKeyVersion().toInt();
		
		if(chKey == KeySettings.SAMEKEY) res = authentication(keyNum);
		else if((chKey == keyNum) || (keyNum == 0)) res = authentication(0);
		else res = authentication(chKey);
		
		if(!res.isOk()) return res;
		
		alg = getKeyAlg();
		oldKey = KeyProvider.getKey(oldKV, alg);
		newKey = KeyProvider.getKey(newKV, alg);
		
		return df.changeKey(keyNum, newKey, oldKey);
				
	}
	
	/**
	 * 
	 * @return
	 */
	protected DFResponse piccFormat(){
		
		DFResponse res;
		
		AID aid = new AID(TicketingModel.MASTER);
		
		res = df.selectApplication(aid);
		if(!res.isOk()) return res;
		
		res = authentication(TicketingModel.MASTER);
		if(!res.isOk()) return res;
		
		res = df.formatPICC();	
		
		return res;

		
	}
	
	/**
	 * 
	 * @return
	 */
	protected DFResponse piccFullFormat(){
		
		DFResponse res = piccFormat();
		
		if(!res.isOk()) return res;
		
		res = df.getKeyVersion(TicketingModel.MASTER);
		
		if(!res.isOk()) return res;
		
		if(res.getKeyVersion().toInt() != 0){
			
			res = changeKey(TicketingModel.MASTER, 0);
		
		}
		return res;
		
	}
	
	
	/**
	 * 
	 * @param aid
	 * @param numOfKeys
	 * @return
	 */
	protected DFResponse createApp(int aid, int numOfKeys){
	
		DFResponse res;
		
		if(legacy) res = df.createApplication(new AID(aid), new KeySettings(), numOfKeys);
		else res =  df.createApplication(new AID(aid), new KeySettings(), numOfKeys, false, CipAlg.TDEA3);
		if(!res.isOk()) return res;
		
		res = configApp();
		
		return res;
	}
	
	
	/**
	 * 
	 * @return
	 */
	private DFResponse configApp(){
		
		DFResponse res;
		
		res = selectApp(TicketingModel.AID);
		if(!res.isOk()) return res;
		
		
		//res = changeKey(TicketingModel.MASTER, 1);
		//if(!res.isOk()) return res;
		
		//res = df.getKeyVersion(TicketingModel.MASTER);
		
		//System.out.println("MASTER KEY VERSION: " + res.getKeyVersion().toInt());
		
		res = changeKey(TicketingModel.READ, 2);
		if(!res.isOk()) return res;
		
		res = df.getKeyVersion(TicketingModel.READ);
		System.out.println("READ KEY VERSION: " + res.getKeyVersion().toInt());
		
		//
		res = authentication(TicketingModel.READ);
		if(!res.isOk()) return res;
		
		
		res = changeKey(TicketingModel.WRITE, 3);
		if(!res.isOk()) return res;
		
		res = df.getKeyVersion(TicketingModel.WRITE);
		
		System.out.println("WRITE KEY VERSION: " + res.getKeyVersion().toInt());
		
		res = changeKey(TicketingModel.READWRITE, 4);
		if(!res.isOk()) return res;	
		
		res = df.getKeyVersion(TicketingModel.READWRITE);
		
		System.out.println("READ&WRITE KEY VERSION: " + res.getKeyVersion().toInt());
		
		return new DFResponse(SC.OPERATION_OK);
	}
	
	/**
	 * 
	 * @param fid
	 * @param comSet
	 * @param ar
	 * @param size
	 * @return
	 */
	protected DFResponse createDataFile(int fid, ComSet comSet, AccessRights ar, int size){
		
		return df.createStdDataFile(new FID(fid), comSet, ar, new Size(size));
		
	}
	
	/**
	 * 
	 * @param fid
	 * @return
	 */
	protected DFResponse getFileSettings(int fid){
		
		return df.getFileSettings(new FID(fid));
		
	}
	
	/**
	 * 
	 * @param aid
	 * @return
	 */
	protected DFResponse selectApp(int aid){
		
		return df.selectApplication(new AID(aid));
		
	}
	
	/**
	 * 
	 * @param aid
	 * @return
	 */
	protected boolean checkApp(int aid){		
		
		DFResponse res = selectApp(TicketingModel.MASTER);
		if(!res.isOk()) return false;
		res = df.getApplicationIDs();
		if(!res.isOk()) return false;
		for(int i = 0; i < res.getAIDs().getAids().length; i++){
			if(res.getAIDs().getAid(i).toInt() == aid) return true;
		}
		return false;
		
	}
	
	/**
	 * 
	 * @param fid
	 * @return
	 */
	protected boolean checkFile(int fid){
		
		DFResponse res = df.getFileIDs();
		if(!res.isOk()) return false;
		if(res.getFIDs() == null) return false;
		for(int i = 0; i < res.getFIDs().getFids().length; i++){
			if(res.getFIDs().getFid(i).toInt() == fid) return true;
		}
		return false;
		
	}
	
	
	/**
	 * 
	 * @param fid
	 * @param offset
	 * @param data
	 * @return
	 */
	protected DFResponse writeData(int fid, int offset, byte[] data){
		
		DFResponse res = df.getFileSettings(new FID(fid));
		if(!res.isOk()) return res;
		
		DataFileSettings fileSet = (DataFileSettings) res.getFileSettings();
		
		int keyNum = fileSet.getAccessRights().getWriteAccess();
		
		res = authentication(keyNum);
		if(!res.isOk()) return res;
		
		res = df.writeData(new FID(fid), new Size(offset), new Data(data), fileSet);
		
		return res;
		
	}
	
	/**
	 * 
	 * @param fid
	 * @param offset
	 * @param size
	 * @return
	 */
	protected DFResponse readData(int fid, int offset, int size){
		
		DFResponse res = df.getFileSettings(new FID(fid));
		if(!res.isOk()) return res;
		
		DataFileSettings fileSet = (DataFileSettings) res.getFileSettings();
		
		int keyNum = fileSet.getAccessRights().getReadAccess();
		
		res = authentication(keyNum);
		if(!res.isOk()) return res;
		
		res = df.readData(new FID(fid), new Size(offset), new Size(size), fileSet);
		
		return res;
	}
	
	
	private CipAlg getKeyAlg(){
		
		int aid = df.getSession().getSelectedAID().toInt();
		
		if(aid == 0) return CipAlg.TDEA2;
		else return (legacy) ? CipAlg.TDEA2 : CipAlg.TDEA3;
		
	}
	
	/**
	 * 
	 */
	private void setLegacyUID(){
		
		DFResponse res = df.getVersion();
		
		if(df.getCardType() == CardType.MIFARE_DESFIRE) legacy = true;
		else legacy = false;

		uid = res.getUID();
		
	}
	
	/**
	 * 
	 * @return
	 */
	public DFCard getDFCard(){ return this.df; }
	
	public UID getUID(){ return this.uid; }
	
	private DFCard df;
	private boolean legacy;
	private UID uid;
	

}
