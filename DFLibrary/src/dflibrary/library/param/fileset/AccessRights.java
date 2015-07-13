package dflibrary.library.param.fileset;

import dflibrary.utils.ba.BAUtils;

/**
 * Provides an encapsulation for the files' access rights
 * @author Francisco Rodriguez Algarra
 */
public class AccessRights {
	
	/**
	 * Creates a default instance of class <code>AccessRights</code>
	 */
	public AccessRights(){
		
		this.readAccess = 0;
		this.writeAccess = 0;
		this.readWriteAccess = 0;
		this.changeAccessRights = 0;
	}
	
	/**
	 * Creates an instance of class <code>AccessRights</code>
	 * @param readAccess an int indicating the read access right
	 * @param writeAccess an int indicating the write access right
	 * @param readWriteAccess an int indicating the read & write access right
	 * @param changeAccessRights an int indicating the access right
	 * necessary to modify the other access rights
	 */
	public AccessRights(int readAccess, int writeAccess, 
			int readWriteAccess, int changeAccessRights){
	
		if((readAccess < 0) || (readAccess > DENY)) 
			throw new IllegalArgumentException();
		if((writeAccess < 0) || (writeAccess > DENY)) 
			throw new IllegalArgumentException();
		if((readWriteAccess < 0) || (readWriteAccess > DENY)) 
			throw new IllegalArgumentException();
		if((changeAccessRights < 0) || (changeAccessRights > DENY)) 
			throw new IllegalArgumentException();
		
		this.readAccess = readAccess;
		this.writeAccess = writeAccess;
		this.readWriteAccess = readWriteAccess;
		this.changeAccessRights = changeAccessRights;
	
	}
	
	/**
	 * Creates an instance of class <code>AccessRights</code>
	 * @param accessRights a byte array representing all the access rights
	 */
	public AccessRights(byte[] accessRights){

		if(accessRights == null) 
			throw new NullPointerException();
		if(accessRights.length != 2) 
			throw new IllegalArgumentException();
		
        this.setReadAccess(accessRights);
        this.setWriteAccess(accessRights);
        this.setReadWriteAccess(accessRights);
        this.setChangeAccessRights(accessRights);

    }
	
	/**
	 * @return an int indicating the current read access right
	 */
	public int getReadAccess(){
		
		return this.readAccess;
		
	}
	
	/**
	 * @return an int indicating the current write access right
	 */
	public int getWriteAccess(){
		
		return this.writeAccess;
		
	}
	
	/**
	 * @return an int indicating the current read & write access right
	 */
	public int getReadWriteAccess(){
		
		return this.readWriteAccess;				
		
	}
	
	/**
	 * @return an int indicating the current access right necessary to modify
	 * the other access rights
	 */
	public int getChangeAccessRights(){
		
		return this.changeAccessRights;
		
	}
	
	/**
	 * @return an byte array containing the new access rights
	 */
    private void setReadAccess(byte[] accessRights){
    	
    	if(accessRights == null) 
    		throw new NullPointerException();
		if(accessRights.length != 2) 
			throw new IllegalArgumentException();
    	
    	int aux = BAUtils.toInt(BAUtils.extractSubBA(accessRights, 1, 1));
    	setReadAccess((aux & (byte)0xF0) >> 4);      

    }
	
	/**
	 * @param readAccess an int indicating the new read access right
	 */
	public void setReadAccess(int readAccess){
		
		if((readAccess < 0) || (readAccess > DENY)) 
			throw new IllegalArgumentException("");
		
		this.readAccess = readAccess;
		
	}
	
	/**
	 * @return an byte array containing the new access rights
	 */
	private void setWriteAccess(byte[] accessRights){
	
		if(accessRights == null) 
			throw new NullPointerException();
		if(accessRights.length != 2) 
			throw new IllegalArgumentException();
		
		int aux = BAUtils.toInt(BAUtils.extractSubBA(accessRights, 1, 1));
		setWriteAccess((aux & (byte)0x0F));

    }
	
	/**
	 * @param writeAccess an int indicating the new write access right
	 */
	public void setWriteAccess(int writeAccess){
		
		if((writeAccess < 0) || (writeAccess > DENY)) 
			throw new IllegalArgumentException("");
		
		this.writeAccess = writeAccess;
		
	}
	
	/**
	 * @return an byte array containing the new access rights
	 */
	private void setReadWriteAccess(byte[] accessRights){

		if(accessRights == null) 
			throw new NullPointerException();
		if(accessRights.length != 2) 
			throw new IllegalArgumentException();
		
		int aux = BAUtils.toInt(BAUtils.extractSubBA(accessRights, 0, 1));
		setReadWriteAccess((aux & (byte)0xF0) >> 4);

    }
	
	/**
	 * @param readWriteAccess an int indicating the new read & write access right
	 */
	public void setReadWriteAccess(int readWriteAccess){
		
		if((readWriteAccess < 0) || (readWriteAccess > DENY)) 
			throw new IllegalArgumentException("");
		
		this.readWriteAccess = readWriteAccess;
		
	}
	
	/**
	 * @return an byte array containing the new access rights
	 */
	private void setChangeAccessRights(byte[] accessRights){

		if(accessRights == null) 
			throw new NullPointerException();
		if(accessRights.length != 2) 
			throw new IllegalArgumentException();
		
		int aux = BAUtils.toInt(BAUtils.extractSubBA(accessRights, 0, 1));
        setChangeAccessRights((aux & (byte)0x0F));
	}
        
	/**
	 * @param changeAccessRights an int indicating the new access right 
	 * necessary to modify the other access rights
	 */
	public void setChangeAccessRights(int changeAccessRights){
		
		if((changeAccessRights < 0) || (changeAccessRights > DENY)) 
			throw new IllegalArgumentException("");
		
		this.changeAccessRights = changeAccessRights;
		
	}   
	
	/**
	 * @return the byte array representation of the current access rights
	 */
	public byte[] toBA(){
		
		return toBA(this);
		
	}
	
	/**
	 * Obtains the byte array representation of the given access rights
	 * @param accessRights an instance of class <code>AccessRights</code>
	 * @return the byte array representation of <code>accessRights</code>
	 */
	public static byte[] toBA(AccessRights accessRights){
		
		if(accessRights == null) throw new NullPointerException();
		
		byte[] aux = new byte[2];

        aux[0] = (byte) ((byte)accessRights.getReadWriteAccess()  << 4 | 
        		((byte) accessRights.getChangeAccessRights()));
        aux[1] = (byte) ((byte)accessRights.getReadAccess()  << 4 | 
        		((byte)accessRights.getWriteAccess()));

        return aux;
		
	}
	
	@Override
	public String toString(){
		
		return "Read Access: " + 
		        toString(this.getReadAccess()) + "\n" +
				"Write Access: " + 
		        toString(this.getWriteAccess()) + "\n" +
				"Read & Write Access: " + 
		        toString(this.getReadWriteAccess()) + "\n" +
				"Change Access Rights: " + 
		        toString(this.getChangeAccessRights());
		
	}
	
	private static String toString(int aR){
        
        switch(aR){
        
            case AccessRights.FREE: return "Free access";
            case AccessRights.DENY: return "Access denied";
            default: return "Access allowed with key number " + aR;
        }
    }

	
	/**
	 * Grants the particular permission freely
	 */
	public static final int FREE = 0xE;
	/**
	 * Forbids the granting of a particular permission
	 */
	public static final int DENY = 0xF;
	
	private int readAccess;
	private int writeAccess;
	private int readWriteAccess;
	private int changeAccessRights;
	
}
