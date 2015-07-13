package dflibrary.library.param;

/**
 * Provides an encapsulation to a key settings structure
 * @author Francisco Rodríguez Algarra
 */
public class KeySettings {

	/**
	 * Creates a default key settings instance
	 */
    public KeySettings(){

        setChangeKeyAccessRights(0);
        setConfigurationChangeable(true);
        setFreeCreateDelete(true);
        setFreeDirectoryListAccess(true);
        setAllowChangingMasterKey(true);

    }

    /**
     * Creates an instance of class <code>KeySettings</code>
     * @param changeKeyAccessRights an int indicating the key to authenticate 
     * with to obtain permission to subsequent change keys operations
     * @param configurationChangeable a boolean indicating whether the current
     * application configuration is changeable or not
     * @param freeCreateDelete a boolean indicating whether applications or 
     * files can be created and/or deleted without previous authentication
     * @param freeDirectoryListAccess a boolean indicating whether application 
     * or file identifiers can be listed without previous authentication
     * @param allowChangingMasterKey a boolean indicating whether the 
     * card or application master key can be modified
     */
    public KeySettings(int changeKeyAccessRights, 
    		boolean configurationChangeable, 
    		boolean freeCreateDelete, 
    		boolean freeDirectoryListAccess,
            boolean allowChangingMasterKey){

        setChangeKeyAccessRights(changeKeyAccessRights);
        setConfigurationChangeable(configurationChangeable);
        setFreeCreateDelete(freeCreateDelete);
        setFreeDirectoryListAccess(freeDirectoryListAccess);
        setAllowChangingMasterKey(allowChangingMasterKey);

    }

    /**
     * Creates an instance of class <code>KeySettings</code>
     * @param configurationChangeable a boolean indicating whether the current
     * application configuration is changeable or not
     * @param freeCreateDelete a boolean indicating whether applications or 
     * files can be created and/or deleted without previous authentication
     * @param freeDirectoryListAccess a boolean indicating whether application 
     * or file identifiers can be listed without previous authentication
     * @param allowChangingMasterKey a boolean indicating whether the 
     * card or application master key can be modified
     */
    public KeySettings(boolean configurationChangeable, 
    		boolean freeCreateDelete, 
    		boolean freeDirectoryListAccess, 
    		boolean allowChangingMasterKey){

    	setChangeKeyAccessRights(0);
    	setConfigurationChangeable(true);
        setFreeCreateDelete(true);
        setFreeDirectoryListAccess(true);
        setAllowChangingMasterKey(true);

    }

    /**
     * Creates an instance of class <code>KeySettings</code>
     * @param keySettings a byte array containing the key settings
     */
    public KeySettings(byte[] keySettings){
     
    	if(keySettings == null) 
    		throw new NullPointerException();
    	if(keySettings.length != 1) 
    		throw new IllegalArgumentException();
    	
        setChangeKeyAccessRights(keySettings);
        setConfigurationChangeable(keySettings);
        setFreeCreateDelete(keySettings);
        setFreeDirectoryListAccess(keySettings);
        setAllowChangingMasterKey(keySettings);
    
    }

    /**
	 * @return an int indicating the key to authenticate 
     * with to obtain permission to subsequent change keys operations
     */
    public int getChangeKeyAccessRights(){

        return this.changeKeyAccessRights;

    }

    /**
     * @return <code>true</code> if the current application configuration 
     * is changeable; <code>false</code> otherwise
     */
    public boolean getConfigurationChangeable(){

        return this.configurationChangeable;

    }

    /**
     * @return <code>true</code> if applications or files can be created 
     * and/or deleted without previous authentication;
     * <code>false</code> otherwise
     */
    public boolean getFreeCreateDelete(){

        return this.freeCreateDelete;

    }

    /**
     * @return <code>true</code> if application or file identifiers can be 
     * listed without previous authentication;
     * <code>false</code> otherwise
     */
    public boolean getFreeDirectoryListAccess(){

        return this.freeDirectoryListAccess;

    }

    /**
     * @return <code>true</code> if the card or application master key 
     * can be modified; <code>false</code> otherwise
     */
    public boolean getAllowChangingMasterKey(){

        return this.allowChangingMasterKey;

    }

    /**
     * @param keySettings a byte array containing the new key settings
     */
    private void setChangeKeyAccessRights(byte[] keySettings){
    	
    	if(keySettings == null) 
    		throw new NullPointerException();
    	if(keySettings.length != 1) 
    		throw new IllegalArgumentException();
    	
        byte aux = (byte)((keySettings[0] & (byte)0xF0) >> 4);
        this.changeKeyAccessRights = (int)(aux & 0x0F);
        
    }

    /**
     * @param changeKeyAccessRights an int indicating the key to authenticate
     * with to obtain permission to subsequent change keys operations
     */
    public void setChangeKeyAccessRights(int changeKeyAccessRights){
    	
    	if((changeKeyAccessRights < 0) || (changeKeyAccessRights > 15)) 
    		throw new IllegalArgumentException();

        this.changeKeyAccessRights = changeKeyAccessRights;
    }
    
    /**
     * @param keySettings a byte array containing the new key settings
     */
    private void setConfigurationChangeable(byte[] keySettings){
    	
    	if(keySettings == null) 
    		throw new NullPointerException();
    	if(keySettings.length != 1) 
    		throw new IllegalArgumentException();

        byte aux = (byte)((keySettings[0] & (byte)0x08) >> 3);
        this.configurationChangeable = (aux == 1 ? true : false);

    }
    
    /**
     * @param configurationChangeable a boolean indicating whether the current
     * application configuration is changeable or not
     */
    public void setConfigurationChangeable(boolean configurationChangeable){

        this.configurationChangeable = configurationChangeable;

    }

    /**
     * @param keySettings a byte array containing the new key settings
     */
    private void setFreeCreateDelete(byte[] keySettings){
    	
    	if(keySettings == null) 
    		throw new NullPointerException();
    	if(keySettings.length != 1) 
    		throw new IllegalArgumentException();

        byte aux = (byte)((keySettings[0] & (byte)0x04) >> 2);
        this.freeCreateDelete = (aux == 1 ? true : false);

    }
    
    /**
     * @param freeCreateDelete a boolean indicating whether applications or 
     * files can be created and/or deleted without previous authentication
     */
    public void setFreeCreateDelete(boolean freeCreateDelete){

        this.freeCreateDelete = freeCreateDelete;

    }

    /**
     * @param keySettings a byte array containing the new key settings
     */
    private void setFreeDirectoryListAccess(byte[] keySettings){
    	
    	if(keySettings == null) 
    		throw new NullPointerException();
    	if(keySettings.length != 1) 
    		throw new IllegalArgumentException();

        byte aux = (byte)((keySettings[0] & (byte)0x02) >> 1);
        this.freeDirectoryListAccess = (aux == 1 ? true : false);

    }

    /**
     * @param freeDirectoryListAccess a boolean indicating whether application 
     * or file identifiers can be listed without previous authentication
     */
    public void setFreeDirectoryListAccess(boolean freeDirectoryListAccess){

        this.freeDirectoryListAccess = freeDirectoryListAccess;

    }

    /**
     * @param keySettings a byte array containing the new key settings
     */
    private void setAllowChangingMasterKey(byte[] keySettings){
    	
    	if(keySettings == null) 
    		throw new NullPointerException();
    	if(keySettings.length != 1) 
    		throw new IllegalArgumentException();

        byte aux = (byte)((keySettings[0] & (byte)0x01));
        this.allowChangingMasterKey = (aux == 1 ? true : false);

    }

    /**
     * @param allowChangingMasterKey a boolean indicating whether the 
     * card or application master key can be modified
     */
    public void setAllowChangingMasterKey(boolean allowChangingMasterKey){

        this.allowChangingMasterKey = allowChangingMasterKey;

    }

    /**
     * @return the byte array representation of the current key settings
     */
    public byte[] toBA(){

        byte[] aux = new byte[1];

        byte confChang = (byte) (
        		this.getConfigurationChangeable() ? 0x01 : 0x00);
        byte freeCD  = (byte) (
        		this.getFreeCreateDelete() ? 0x01 : 0x00);
        byte freeDLA  = (byte) (
        		this.getFreeDirectoryListAccess() ? 0x01 : 0x00);
        byte allowChangMK  = (
        		byte) (this.getAllowChangingMasterKey() ? 0x01 : 0x00);

        aux[0] = (byte) (
        		(aux[0] | (byte) this.getChangeKeyAccessRights() << 4));
        aux[0] = (byte) (
        		(aux[0] | (byte) confChang << 3));
        aux[0] = (byte) (
        		(aux[0] | (byte) freeCD << 2));
        aux[0] = (byte) (
        		(aux[0] | (byte) freeDLA << 1));
        aux[0] = (byte) (
        		(aux[0] | (byte) allowChangMK));

        return aux;
    }

    @Override
    public String toString(){

        return "Change Key Access Rights: " + 
                this.getChangeKeyAccessRights() + ",\n" +
        		"Configuration changeable: " + 
                this.getConfigurationChangeable() + ",\n" + 
        		"Free Create/Delete: " + 
                this.getFreeCreateDelete() + ",\n" + 
        		"Free Directory List Access: " + 
                this.getFreeDirectoryListAccess() + ",\n" + 
        		"Allow changing the Master Key: " + 
                this.getAllowChangingMasterKey();

    }

    public static final int DEFAULT = 0x0;
    public static final int SAMEKEY = 0xE;
    public static final int ALLKEYSFROZEN = 0xF;

    private int changeKeyAccessRights;
    private boolean configurationChangeable;
    private boolean freeCreateDelete;
    private boolean freeDirectoryListAccess;
    private boolean allowChangingMasterKey;

}