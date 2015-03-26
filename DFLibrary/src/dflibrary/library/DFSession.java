package dflibrary.library;

import dflibrary.library.param.AID;
import dflibrary.library.security.AuthType;
import dflibrary.utils.ba.BAUtils;

/**
 * 
 * @author Francisco Rodríguez Algarra
 *
 */
public class DFSession {

	/**
	 * 
	 */
	public DFSession(){
		
		this.authType = AuthType.NO_AUTH;
		this.selectedAID = new AID(0);
		this.sessionKey = null;
		this.PICCAuth = false;
		this.cmacIV = null;
		this.cmacOK = false;
		
	}
	
	/**
	 * 
	 */
	public void resetAuth(){
		
		if(this.authType != AuthType.TDEA_NATIVE){
		
			cancelAuth();
			
		}
		
	}
	
	public void cancelAuth(){
				
		this.authType = AuthType.NO_AUTH;
		this.authKeyNum = 0;
		this.sessionKey = null;		
		this.PICCAuth = false;
		this.cmacIV = null;
		this.cmacOK = false;
		
	}
	
	
	/**
	 * 
	 * @param newAuthType
	 * @param newSessionKey
	 * @param PICCAuth
	 */
	public void setAuth(AuthType newAuthType, int authKeyNum, byte[] newSessionKey, boolean PICCAuth){
		
		if((newAuthType == null) || (newSessionKey == null)) throw new NullPointerException();
		if((authKeyNum < 0) || (authKeyNum > 14)) throw new IllegalArgumentException();
		
		this.authType = newAuthType;
		this.authKeyNum = authKeyNum;
		this.sessionKey = newSessionKey;
		this.PICCAuth = PICCAuth;
		
		if((newAuthType == AuthType.NO_AUTH) || (newAuthType == AuthType.TDEA_NATIVE)) this.cmacIV = null;
		else if(newAuthType == AuthType.AES) this.cmacIV = new byte[16];
		else this.cmacIV = new byte[8];
	}
	
	/**
	 * 
	 * @param aid
	 */
	public void setSelectedAID(AID aid){
		
		if(aid == null) throw new NullPointerException();
		
		this.selectedAID = aid;
		resetAuth();
		
	}
	
	/**
	 * 
	 * @param aid
	 */
	public void setSelectedAID(int aid){
		
		this.selectedAID = new AID(aid);
		resetAuth();
		
	}
	
	/**
	 * 
	 * @param aid
	 */
	public void setSelectedAID(byte[] aid){
		
		if(aid == null) throw new NullPointerException();
		
		this.selectedAID = new AID(aid);
		resetAuth();
		
	}
	
	/**
	 * 
	 * @return
	 */
	public AuthType getAuthType(){ return this.authType; }
	
	/**
	 * 
	 * @return
	 */
	public AID getSelectedAID(){ return this.selectedAID; }
	
	/**
	 * 
	 * @return
	 */
	public int getAuthKeyNum(){ return this.authKeyNum; }
	
	/**
	 * 
	 * @return
	 */
	public byte[] getSessionKey(){ return this.sessionKey; }
	
	/**
	 * 
	 * @return
	 */
	public boolean getPICCAuth(){ return this.PICCAuth; }
	
	/**
	 * 
	 * @param newIV
	 */
	public void updateCmacIV(byte[] newIV){
		
		if(newIV == null) throw new NullPointerException();
		
		this.cmacIV = newIV;
		
	}
	
	/**
	 * 
	 * @param newIV
	 * @param cmacOK
	 */
	public void updateCmacIV(byte[] newIV, boolean cmacOK){
		
		if(newIV == null) throw new NullPointerException();
		
		this.cmacIV = newIV;
		this.cmacOK = cmacOK;
		
	}
	
	/**
	 * 
	 * @return
	 */
	public byte[] getCmacIV(){ return this.cmacIV; }
	
	/**
	 * 
	 * @return
	 */
	public boolean getCmacOK(){ return this.cmacOK; }
	
	/**
	 * 
	 */
	public String toString(){
		
		String s = "";
		
		s = s + "Current selected application: " + this.selectedAID.toString() + "\n";
		s = s + "Current Authentication type: " + this.authType.toString() + "\n";
		
		if(this.authType != AuthType.NO_AUTH){
			
			s = s + "Authentication key number: " + this.authKeyNum + "\n";
			s = s + "Session Key: "+ BAUtils.toString(this.sessionKey) + "\n";
			if(this.PICCAuth) s = s + "PICC successfully auhtenticated\n";
			else s = s + "CAUTION: PICC not successfully authenticated\n";
		
			if(authType != AuthType.TDEA_NATIVE){
				
				s = s + "Current CMAC IV: " + BAUtils.toString(this.cmacIV) + "\n";
				s = s + "Last CMAC check: " + this.cmacOK + "\n";
			}
			
		}
		
		return s;
		
	}
	
	private AuthType authType;	
	private AID selectedAID;
	private int authKeyNum;
	private byte[] sessionKey;
	private boolean PICCAuth;
	private byte[] cmacIV;
	private boolean cmacOK;
}



