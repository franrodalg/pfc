package dflibrary.samples.wallet;

import dflibrary.library.*;
import dflibrary.library.param.*;
import dflibrary.library.param.fileset.*;
import dflibrary.library.security.DFKey;
import dflibrary.middleware.*;
import dflibrary.utils.ba.BAUtils;
import dflibrary.utils.security.CipAlg;

public class WTDFCard {
	
	/**
	 * 
	 * @param cm
	 */
	public WTDFCard(ComManager cm){
		
		this.df = new DFCard(cm);
		setLegacyUID();
		
	}
		
	/**
	 * 
	 * @return
	 */
	protected DFResponse format(int bankID){
		
		DFResponse res;
		
		AID aid = new AID(WTModel.MASTER);
		
		res = df.selectApplication(aid);
		if(!res.isOk()) return res;
		
		res = authentication(WTModel.MASTER, bankID);
		if(!res.isOk()) return res;
		
		res = df.formatPICC();	
		
		return res;

	}

	/**
	 * 
	 * @param bankID
	 * @return
	 */
	protected DFResponse setDefaultCardMasterKey(int bankID){
		
		DFResponse res;
		
		AID aid = new AID(WTModel.MASTER);
		
		res = df.selectApplication(aid);
		if(!res.isOk()) return res;
		
		res = df.getKeyVersion(WTModel.MASTER);		
		if(!res.isOk()) return res;
		
		if(res.getKeyVersion().toInt() != 0){			
			res = changeKey(WTModel.MASTER, 0, bankID);
		}
		
		return authentication(WTModel.MASTER, bankID);
		
	}
	
	/**
	 * 
	 * @param aid
	 * @return
	 */
	protected DFResponse deleteApp(int aid){
		
		DFResponse res = selectApp(0);
		if(!res.isOk()) return res;
		
		res = selectApp(aid);
		if(!res.isOk()) return res;
		
		res = authentication(0);
		if(!res.isOk()) return res;
		
		res = df.deleteApplication(new AID(aid));
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
		else res =  df.createApplication(new AID(aid), new KeySettings(), numOfKeys, false, getKeyAlg(aid));
		return res;

	}
	
	/**
	 * 
	 * @return
	 */
	protected DFResponse configTickAppKeys(int aid){
		
		DFResponse res;
		
		if(aid == WTModel.TICK_AID){
			
			res = changeKey(0, 1);
			if(!res.isOk()) return res;
			
			res = changeKey(WTModel.TICK_R, 2);
			if(!res.isOk()) return res;
				
			res = changeKey(WTModel.TICK_W, 3);
			if(!res.isOk()) return res;
			
			res = changeKey(WTModel.TICK_RW, 4);
			if(!res.isOk()) return res;
			
		}
		else return new DFResponse(SC.APPLICATION_NOT_FOUND);
		
		return new DFResponse(SC.OPERATION_OK);
	}
	
	/**
	 * 
	 * @param aid
	 * @param bankID
	 * @param initialBalance
	 * @return
	 */
	protected DFResponse createTickAppFiles(int aid){
		
		DFResponse res;

		if(aid == WTModel.TICK_AID){
		
			res = createTicketsFile();
			if(!res.isOk()) return res;
		
		}
		else return new DFResponse(SC.APPLICATION_NOT_FOUND);
		
		return new DFResponse(SC.OPERATION_OK);
		
	}
	
	/**
	 * 
	 * @return
	 */
	private DFResponse createTicketsFile(){
		
		DFResponse res;
		
		ComSet comSet = ComSet.ENC;
		AccessRights ar = new AccessRights(WTModel.TICK_R, WTModel.TICK_W, WTModel.TICK_RW, WTModel.MASTER);

		FID fid = new FID(WTModel.TICK_FID);
		Size recSize = new Size(16);
		
		res = df.createLinearRecordFile(fid, comSet, ar, recSize, WTModel.TICK_MAX_NUM_OF_TICKETS);
		if(!res.isOk()) return res; 		
		
		return new DFResponse(SC.OPERATION_OK);
		
	}
	
	/**
	 * 
	 * @return
	 */
	protected DFResponse configWalletAppKeys(int aid, int bankID){
		
		DFResponse res;

		if(aid == WTModel.WALLET_AID){
			
			res = changeKey(0, 5, bankID);
			if(!res.isOk()) return res;
			
			res = changeKey(WTModel.WALL_ISSUER_W, 1, bankID);
			if(!res.isOk()) return res;
			
			res = df.getKeyVersion(WTModel.WALL_ISSUER_W);
			
			res = changeKey(WTModel.WALL_WALLET_R, 2, bankID);
			if(!res.isOk()) return res;
			
		}
		else return new DFResponse(SC.APPLICATION_NOT_FOUND);
		
		return new DFResponse(SC.OPERATION_OK);
	}
	
	/**
	 * 
	 * @param aid
	 * @param bankID
	 * @param initialBalance
	 * @return
	 */
	protected DFResponse createWallAppFiles(int aid, int bankID, int initialBalance){
		
		DFResponse res;

		if(aid == WTModel.WALLET_AID){
		
			res = createBankIDFile(bankID);
			if(!res.isOk()) return res;
			
			res = createBalanceFile(bankID, initialBalance);
			if(!res.isOk()) return res;
		
		}
		else return new DFResponse(SC.APPLICATION_NOT_FOUND);
		
		return new DFResponse(SC.OPERATION_OK);
		
	}
	
	/**
	 * 
	 * @param bankID
	 * @return
	 */
	private DFResponse createBankIDFile(int bankID){
		
		DFResponse res;
		
		ComSet comSet = ComSet.PLAIN;
		AccessRights ar = new AccessRights(WTModel.WALL_ISSUER_R, WTModel.WALL_ISSUER_W, WTModel.WALL_ISSUER_RW, WTModel.MASTER);

		res = createDataFile(WTModel.WALL_ISSUER_FID, comSet, ar, 2);
		if(!res.isOk()) return res; 

		res = writeBankID(bankID);
		if(!res.isOk()) return res;		
		
		return new DFResponse(SC.OPERATION_OK);
		
	}
	
	/**
	 * 	
	 * @param bankID
	 * @param initialBalance
	 * @return
	 */
	private DFResponse createBalanceFile(int bankID, int initialBalance){
		
		DFResponse res;
		
		ComSet comSet = ComSet.ENC;
		AccessRights ar = new AccessRights(WTModel.WALL_WALLET_R, WTModel.WALL_WALLET_W, WTModel.WALL_WALLET_RW, WTModel.MASTER);

		res = createValueFile(WTModel.WALL_WALLET_FID, comSet, ar, 0, 1000, initialBalance, false, true);
		if(!res.isOk()) return res;
		
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
	 * @param comSet
	 * @param ar
	 * @param min
	 * @param max
	 * @param initialBal
	 * @param limCredEn
	 * @return
	 */
	private DFResponse createValueFile(int fid, ComSet comSet, AccessRights ar, int min, int max, int initialBal, boolean limCredEn, boolean freeValue){
		
		return df.createValueFile(new FID(fid), comSet, ar, new Value(min), new Value(max), new Value(initialBal), limCredEn, freeValue);
		
		
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
		
		DFResponse res = selectApp(WTModel.MASTER);
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
	 * @return
	 */
	protected DFResponse getTicketFileSettings(){
		
		DFResponse res = df.getFileSettings(new FID(WTModel.TICK_FID));
		return res;
		
	}
	
	
	/**
	 * 
	 * @param ticket
	 * @return
	 */
	protected DFResponse writeTicket(Ticket ticket){
		
		DFResponse res;
		
		res = getTicketFileSettings();
		if(!res.isOk()) return res;
		
		RecordFileSettings fileSet;
		
		try{
			fileSet = (RecordFileSettings) res.getFileSettings();
		}catch(Exception e){
			throw new RuntimeException("Ticket file is not of proper type");
		}
		
		ComSet comSet = fileSet.getComSet();
		AccessRights ar = fileSet.getAccessRights();
		
		int keyNum = ar.getWriteAccess();
		
		if(keyNum == AccessRights.DENY) fileSet.getAccessRights().getReadWriteAccess();
		
		res = authentication(keyNum);
		if(!res.isOk()) return res;
		
		FID fid = new FID(WTModel.TICK_FID);
		Size offset = new Size(0);
		Data data = new Data(ticket.toBA());
		
		res = df.writeRecord(fid, offset, data, comSet, ar);
		if(!res.isOk()) return res;
		
		res = df.commitTransaction();
		
		return res;		
				
	}
	
	/**
	 * 
	 * @param recordID
	 * @return
	 */
	protected DFResponse clearTicketFile(){
		
		DFResponse res;
		
		res = getTicketFileSettings();
		if(!res.isOk()) return res;
		
		RecordFileSettings fileSet;
		
		try{
			fileSet = (RecordFileSettings) res.getFileSettings();
		}catch(Exception e){
			throw new RuntimeException("Ticket file is not of proper type");
		}
		
		int keyNum = fileSet.getAccessRights().getReadWriteAccess();
		
		res = authentication(keyNum);
		if(!res.isOk()) return res;
		
		FID fid = new FID(WTModel.TICK_FID);
		
		res = df.clearRecordFile(fid);
		if(!res.isOk()) return res;
		
		res = df.commitTransaction();
		
		return res;		
		
	}
	
	
	/**
	 * 
	 * @param bankID
	 * @return
	 */
	protected DFResponse writeBankID(int bankID){		
		
		byte[] bankIDBA = BAUtils.toBA(bankID, 2);
			
		DFResponse res = df.getFileSettings(new FID(WTModel.WALL_ISSUER_FID));
		if(!res.isOk()) return res;
		
		DataFileSettings fileSet = (DataFileSettings) res.getFileSettings();
		
		int keyNum = fileSet.getAccessRights().getWriteAccess();
		
		if(keyNum == AccessRights.DENY) fileSet.getAccessRights().getReadWriteAccess();
		
		res = authentication(keyNum, bankID);
		if(!res.isOk()) return res;
		
		res = df.writeData(new FID(WTModel.WALL_ISSUER_FID), new Size(0), new Data(bankIDBA), fileSet);
		
		return res;
		
		
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
		
		if(keyNum == AccessRights.DENY) fileSet.getAccessRights().getReadWriteAccess();
		
		res = authentication(keyNum);
		if(!res.isOk()) return res;
		
		res = df.writeData(new FID(fid), new Size(offset), new Data(data), fileSet);
		
		return res;
		
	}
	
	/**
	 * 
	 * @return
	 */
	protected DFResponse readBankID(){
		
		DFResponse res = df.getFileSettings(new FID(WTModel.WALL_ISSUER_FID));
		if(!res.isOk()) return res;
		
		DataFileSettings fileSet = (DataFileSettings) res.getFileSettings();
		
		int keyNum = fileSet.getAccessRights().getReadAccess();
		
		if(keyNum != AccessRights.FREE){
		
			return new DFResponse(SC.AUTHENTICATION_ERROR);
		
		}	
			
		res = df.readData(new FID(WTModel.WALL_ISSUER_FID), new Size(0), new Size(2), fileSet);
		
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
	
	protected DFResponse readTicketRecords(){
		
		DFResponse res;
		
		res = getTicketFileSettings();
		if(!res.isOk()) return res;
		
		RecordFileSettings fileSet;
		
		try{
			fileSet = (RecordFileSettings) res.getFileSettings();
		}catch(Exception e){
			throw new RuntimeException("Ticket file is not of proper type");
		}
		
		int keyNum = fileSet.getAccessRights().getReadAccess();
		
		if(keyNum == AccessRights.DENY) fileSet.getAccessRights().getReadWriteAccess();
		
		res = authentication(keyNum);
		if(!res.isOk()) return res;
	
		FID fid = new FID(WTModel.TICK_FID);
		Size offset = new Size(0);
		Size length = new Size(0);
	
		res = df.readRecords(fid, offset, length, fileSet);
		if(!res.isOk()) return res;
		
		return res;
		
	}
	
	/**
	 * 
	 * @param record
	 * @return
	 */
	protected DFResponse readTicketRecord(int record){
		
		DFResponse res;
		
		res = getTicketFileSettings();
		if(!res.isOk()) return res;
		
		RecordFileSettings fileSet;
		
		try{
			fileSet = (RecordFileSettings) res.getFileSettings();
		}catch(Exception e){
			throw new RuntimeException("Ticket file is not of proper type");
		}
		
		int keyNum = fileSet.getAccessRights().getReadAccess();
		
		if(keyNum == AccessRights.DENY) fileSet.getAccessRights().getReadWriteAccess();
		
		res = authentication(keyNum);
		if(!res.isOk()) return res;
	
		FID fid = new FID(WTModel.TICK_FID);
		Size offset = new Size(record);
		Size length = new Size(1);
	
		res = df.readRecords(fid, offset, length, fileSet);
		if(!res.isOk()) return res;
		
		return res;
		
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
			
		key = TickKeyProvider.getKey(keyVersion, alg);

		if(legacy) return df.authenticate(keyNum, key);
		else return df.authenticateISO(keyNum, key);
		
	}
	
	/**
	 * 
	 * @param keyNum
	 * @return
	 */
	protected DFResponse authentication(int keyNum, int bankID){
		
		DFResponse res;
		
		res = df.getKeyVersion(keyNum);
		
		if(!res.isOk()) return res;
		
		int keyVersion = res.getKeyVersion().toInt();
		
		DFKey key;	
		CipAlg alg;
		
		alg = getKeyAlg();
			
		key = BankKeyProvider.getKey(keyVersion, alg, bankID);

		if(legacy) return df.authenticate(keyNum, key);
		else return df.authenticateISO(keyNum, key);
		
	}
	
	/**
	 * 
	 * @param keyNum
	 * @param newKV
	 * @param bankID
	 * @return
	 */
	protected DFResponse changeKey(int keyNum, int newKV, int bankID){
		
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
		
		if(chKey == KeySettings.SAMEKEY) res = authentication(keyNum, bankID);
		else if((chKey == keyNum) || (keyNum == 0)) res = authentication(0, bankID);
		else res = authentication(chKey, bankID);
		
		if(!res.isOk()) return res;
		
		alg = getKeyAlg();
		oldKey = BankKeyProvider.getKey(oldKV, alg,bankID);
		newKey = BankKeyProvider.getKey(newKV, alg, bankID);
		
		return df.changeKey(keyNum, newKey, oldKey);
		
	}
	
	/**
	 * 
	 * @return
	 */
	protected DFResponse getKeySettings(){
		
		return df.getKeySettings();
		
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
		oldKey = TickKeyProvider.getKey(oldKV, alg);
		newKey = TickKeyProvider.getKey(newKV, alg);
		
		return df.changeKey(keyNum, newKey, oldKey);
				
	}
	
	/**
	 * 
	 * @return
	 */
	private CipAlg getKeyAlg(){
		
		int aid = df.getSession().getSelectedAID().toInt();
		
		return getKeyAlg(aid);
	}
	
	/**
	 * 
	 * @param aid
	 * @return
	 */
	private CipAlg getKeyAlg(int aid){
		
		if(aid == 0) return CipAlg.TDEA2;
		else if(legacy) {
			return CipAlg.TDEA2; } 
		else {
			if(aid == WTModel.TICK_AID) return WTModel.TICK_ALG;
			if(aid == WTModel.WALLET_AID) return WTModel.WALL_ALG;
		}
		
		return CipAlg.TDEA2;
		
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
	
	/**
	 * 
	 * @return
	 */
	public UID getUID(){ return this.uid; }
	
	/**
	 * 
	 * @return
	 */
	public boolean getLegacy(){ return this.legacy;}
	
	private DFCard df;
	private boolean legacy;
	private UID uid;
	

}
