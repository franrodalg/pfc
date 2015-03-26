package dflibrary.library.param;

/**
 * 
 * @author Francisco Rodríguez Algarra
 *
 */
public class KeySettings {

	/**
	 * 
	 */
    public KeySettings(){

        setChangeKeyAccessRights(0);
        setConfigurationChangeable(true);
        setFreeCreateDelete(true);
        setFreeDirectoryListAccess(true);
        setAllowChangingMasterKey(true);

    }

    /**
     * 
     * @param changeKeyAccessRights
     * @param configurationChangeable
     * @param freeCreateDelete
     * @param freeDirectoryListAccess
     * @param allowChangingMasterKey
     */
    public KeySettings(int changeKeyAccessRights, boolean configurationChangeable, boolean freeCreateDelete, boolean freeDirectoryListAccess,
            boolean allowChangingMasterKey){

        setChangeKeyAccessRights(changeKeyAccessRights);
        setConfigurationChangeable(configurationChangeable);
        setFreeCreateDelete(freeCreateDelete);
        setFreeDirectoryListAccess(freeDirectoryListAccess);
        setAllowChangingMasterKey(allowChangingMasterKey);

    }

    /**
     * 
     * @param configurationChangeable
     * @param freeCreateDelete
     * @param freeDirectoryListAccess
     * @param allowChangingMasterKey
     */
    public KeySettings(boolean configurationChangeable, boolean freeCreateDelete, boolean freeDirectoryListAccess, boolean allowChangingMasterKey){

    	setChangeKeyAccessRights(0);
    	setConfigurationChangeable(true);
        setFreeCreateDelete(true);
        setFreeDirectoryListAccess(true);
        setAllowChangingMasterKey(true);

    }

    /**
     * 
     * @param keySettings
     */
    public KeySettings(byte[] keySettings){
     
    	if(keySettings == null) throw new NullPointerException();
    	if(keySettings.length != 1) throw new IllegalArgumentException();
    	
        setChangeKeyAccessRights(keySettings);
        setConfigurationChangeable(keySettings);
        setFreeCreateDelete(keySettings);
        setFreeDirectoryListAccess(keySettings);
        setAllowChangingMasterKey(keySettings);
    
    }

    /**
     * 
     * @return
     */
    public int getChangeKeyAccessRights(){

        return this.changeKeyAccessRights;

    }

    /**
     * 
     * @return
     */
    public boolean getConfigurationChangeable(){

        return this.configurationChangeable;

    }

    /**
     * 
     * @return
     */
    public boolean getFreeCreateDelete(){

        return this.freeCreateDelete;

    }

    /**
     * 
     * @return
     */
    public boolean getFreeDirectoryListAccess(){

        return this.freeDirectoryListAccess;

    }

    /**
     * 
     * @return
     */
    public boolean getAllowChangingMasterKey(){

        return this.allowChangingMasterKey;

    }

    /**
     * 
     * @param keySettings
     */
    private void setChangeKeyAccessRights(byte[] keySettings){
    	
    	if(keySettings == null) throw new NullPointerException();
    	if(keySettings.length != 1) throw new IllegalArgumentException();
    	
        byte aux = (byte)((keySettings[0] & (byte)0xF0) >> 4);
        this.changeKeyAccessRights = (int)(aux & 0x0F);
        
    }

    /**
     * 
     * @param changeKeyAccessRights
     */
    public void setChangeKeyAccessRights(int changeKeyAccessRights){
    	
    	if((changeKeyAccessRights < 0) || (changeKeyAccessRights > 15)) throw new IllegalArgumentException();

        this.changeKeyAccessRights = changeKeyAccessRights;
    }
    
    /**
     * 
     * @param keySettings
     */
    private void setConfigurationChangeable(byte[] keySettings){
    	
    	if(keySettings == null) throw new NullPointerException();
    	if(keySettings.length != 1) throw new IllegalArgumentException();

        byte aux = (byte)((keySettings[0] & (byte)0x08) >> 3);
        this.configurationChangeable = (aux == 1 ? true : false);

    }
    
    /**
     * 
     * @param configurationChangeable
     */
    public void setConfigurationChangeable(boolean configurationChangeable){

        this.configurationChangeable = configurationChangeable;

    }

    /**
     * 
     * @param keySettings
     */
    private void setFreeCreateDelete(byte[] keySettings){
    	
    	if(keySettings == null) throw new NullPointerException();
    	if(keySettings.length != 1) throw new IllegalArgumentException();

        byte aux = (byte)((keySettings[0] & (byte)0x04) >> 2);
        this.freeCreateDelete = (aux == 1 ? true : false);

    }
    
    /**
     * 
     * @param freeCreateDelete
     */
    public void setFreeCreateDelete(boolean freeCreateDelete){

        this.freeCreateDelete = freeCreateDelete;

    }

    /**
     * 
     * @param keySettings
     */
    private void setFreeDirectoryListAccess(byte[] keySettings){
    	
    	if(keySettings == null) throw new NullPointerException();
    	if(keySettings.length != 1) throw new IllegalArgumentException();

        byte aux = (byte)((keySettings[0] & (byte)0x02) >> 1);
        this.freeDirectoryListAccess = (aux == 1 ? true : false);

    }

    /**
     * 
     * @param freeDirectoryListAccess
     */
    public void setFreeDirectoryListAccess(boolean freeDirectoryListAccess){

        this.freeDirectoryListAccess = freeDirectoryListAccess;

    }

    private void setAllowChangingMasterKey(byte[] keySettings){
    	
    	if(keySettings == null) throw new NullPointerException();
    	if(keySettings.length != 1) throw new IllegalArgumentException();

        byte aux = (byte)((keySettings[0] & (byte)0x01));
        this.allowChangingMasterKey = (aux == 1 ? true : false);

    }

    /**
     * 
     * @param allowChangingMasterKey
     */
    public void setAllowChangingMasterKey(boolean allowChangingMasterKey){

        this.allowChangingMasterKey = allowChangingMasterKey;

    }

    /**
     * 
     * @return
     */
    public byte[] toBA(){

        byte[] aux = new byte[1];

        byte confChang = (byte) (this.getConfigurationChangeable() ? 0x01 : 0x00);
        byte freeCD  = (byte) (this.getFreeCreateDelete() ? 0x01 : 0x00);
        byte freeDLA  = (byte) (this.getFreeDirectoryListAccess() ? 0x01 : 0x00);
        byte allowChangMK  = (byte) (this.getAllowChangingMasterKey() ? 0x01 : 0x00);

        aux[0] = (byte) ((aux[0] | (byte) this.getChangeKeyAccessRights() << 4));
        aux[0] = (byte) ((aux[0] | (byte) confChang << 3));
        aux[0] = (byte) ((aux[0] | (byte) freeCD << 2));
        aux[0] = (byte) ((aux[0] | (byte) freeDLA << 1));
        aux[0] = (byte) ((aux[0] | (byte) allowChangMK));

        return aux;
    }

    /**
     * 
     */
    public String toString(){

        return "Change Key Access Rights: " + this.getChangeKeyAccessRights() + ",\n" +
        		"Configuration changeable: " + this.getConfigurationChangeable() + ",\n" + 
        		"Free Create/Delete: " + this.getFreeCreateDelete() + ",\n" + 
        		"Free Directory List Access: " + this.getFreeDirectoryListAccess() + ",\n" + 
        		"Allow changing the Master Key: " + this.getAllowChangingMasterKey();

    }
    /**
     * 
     */
    public static final int DEFAULT = 0x0;
    /**
     * 
     */
    public static final int SAMEKEY = 0xE;
    /**
     * 
     */
    public static final int ALLKEYSFROZEN = 0xF;

    private int changeKeyAccessRights;
    private boolean configurationChangeable;
    private boolean freeCreateDelete;
    private boolean freeDirectoryListAccess;
    private boolean allowChangingMasterKey;

}