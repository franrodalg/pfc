package dflibrary.samples.wallet;

import dflibrary.library.*;
import dflibrary.library.param.*;
import dflibrary.library.param.fileset.*;
import dflibrary.library.security.DFKey;
import dflibrary.middleware.*;
import dflibrary.utils.ba.BAUtils;
import dflibrary.utils.security.CipAlg;

public class BankDFCard {

	/**
	 * 
	 * @param cm
	 */
	public BankDFCard(int bankID, ComManager cm){
		
		this.df = new DFCard(cm);
		this.bankID = bankID;
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
			
		key = BankKeyProvider.getKey(keyVersion, alg, bankID);

		if(legacy) return df.authenticate(keyNum, key);
		else return df.authenticateISO(keyNum, key);
		
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
	
	private int bankID;
	private DFCard df;
	private boolean legacy;
	private UID uid;
	
}
