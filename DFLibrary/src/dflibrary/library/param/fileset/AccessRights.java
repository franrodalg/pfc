package dflibrary.library.param.fileset;

import dflibrary.utils.ba.BAUtils;

/**
 * 
 * @author Francisco Rodríguez Algarra
 *
 */
public class AccessRights {
	
	/**
	 * 
	 */
	public AccessRights(){
		
		this.readAccess = 0;
		this.writeAccess = 0;
		this.readWriteAccess = 0;
		this.changeAccessRights = 0;
	}
	
	/**
	 * 
	 * @param readAccess
	 * @param writeAccess
	 * @param readWriteAccess
	 * @param changeAccessRights
	 */
	public AccessRights(int readAccess, int writeAccess, int readWriteAccess, int changeAccessRights){
	
		if((readAccess < 0) || (readAccess > DENY)) throw new IllegalArgumentException();
		if((writeAccess < 0) || (writeAccess > DENY)) throw new IllegalArgumentException();
		if((readWriteAccess < 0) || (readWriteAccess > DENY)) throw new IllegalArgumentException();
		if((changeAccessRights < 0) || (changeAccessRights > DENY)) throw new IllegalArgumentException();
		
		this.readAccess = readAccess;
		this.writeAccess = writeAccess;
		this.readWriteAccess = readWriteAccess;
		this.changeAccessRights = changeAccessRights;
	
	}
	
	/**
	 * 
	 * @param accessRights
	 */
	public AccessRights(byte[] accessRights){

		if(accessRights == null) throw new NullPointerException();
		if(accessRights.length != 2) throw new IllegalArgumentException();
		
        this.setReadAccess(accessRights);
        this.setWriteAccess(accessRights);
        this.setReadWriteAccess(accessRights);
        this.setChangeAccessRights(accessRights);

    }
	
	/**
	 * 
	 * @return
	 */
	public int getReadAccess(){
		
		return this.readAccess;
		
	}
	
	/**
	 * 
	 * @return
	 */
	public int getWriteAccess(){
		
		return this.writeAccess;
		
	}
	
	/**
	 * 
	 * @return
	 */
	public int getReadWriteAccess(){
		
		return this.readWriteAccess;				
		
	}
	
	/**
	 * 
	 * @return
	 */
	public int getChangeAccessRights(){
		
		return this.changeAccessRights;
		
	}
	
	/**
	 * 
	 * @param accessRights
	 */
    private void setReadAccess(byte[] accessRights){
    	
    	if(accessRights == null) throw new NullPointerException();
		if(accessRights.length != 2) throw new IllegalArgumentException();
    	
    	int aux = BAUtils.toInt(BAUtils.extractSubBA(accessRights, 1, 1));
    	setReadAccess((aux & (byte)0xF0) >> 4);      

    }
	
	/**
	 * 
	 * @param readAccess
	 */
	public void setReadAccess(int readAccess){
		
		if((readAccess < 0) || (readAccess > DENY)) throw new IllegalArgumentException("");
		
		this.readAccess = readAccess;
		
	}
	
	/**
	 * 
	 * @param accessRights
	 */
	private void setWriteAccess(byte[] accessRights){
	
		if(accessRights == null) throw new NullPointerException();
		if(accessRights.length != 2) throw new IllegalArgumentException();
		
		int aux = BAUtils.toInt(BAUtils.extractSubBA(accessRights, 1, 1));
		setWriteAccess((aux & (byte)0x0F));

    }
	
	/**
	 * 
	 * @param writeAccess
	 */
	public void setWriteAccess(int writeAccess){
		
		if((writeAccess < 0) || (writeAccess > DENY)) throw new IllegalArgumentException("");
		
		this.writeAccess = writeAccess;
		
	}
	
	/**
	 * 
	 * @param accessRights
	 */
	private void setReadWriteAccess(byte[] accessRights){

		if(accessRights == null) throw new NullPointerException();
		if(accessRights.length != 2) throw new IllegalArgumentException();
		
		int aux = BAUtils.toInt(BAUtils.extractSubBA(accessRights, 0, 1));
		setReadWriteAccess((aux & (byte)0xF0) >> 4);

    }
	
	/**
	 * 
	 * @param readWriteAccess
	 */
	public void setReadWriteAccess(int readWriteAccess){
		
		if((readWriteAccess < 0) || (readWriteAccess > DENY)) throw new IllegalArgumentException("");
		
		this.readWriteAccess = readWriteAccess;
		
	}
	
	/**
	 * 
	 * @param accessRights
	 */
	private void setChangeAccessRights(byte[] accessRights){

		if(accessRights == null) throw new NullPointerException();
		if(accessRights.length != 2) throw new IllegalArgumentException();
		
		int aux = BAUtils.toInt(BAUtils.extractSubBA(accessRights, 0, 1));
        setChangeAccessRights((aux & (byte)0x0F));
	}
        
	/**
	 * 
	 * @param changeAccessRights
	 */
	public void setChangeAccessRights(int changeAccessRights){
		
		if((changeAccessRights < 0) || (changeAccessRights > DENY)) throw new IllegalArgumentException("");
		
		this.changeAccessRights = changeAccessRights;
		
	}   
	
	/**
	 * 
	 * @return
	 */
	public byte[] toBA(){
		
		return toBA(this);
		
	}
	
	/**
	 * 
	 * @param accessRights
	 * @return
	 */
	public static byte[] toBA(AccessRights accessRights){
		
		if(accessRights == null) throw new NullPointerException();
		
		byte[] aux = new byte[2];

        aux[0] = (byte) ((byte)accessRights.getReadWriteAccess()  << 4 | ((byte) accessRights.getChangeAccessRights()));
        aux[1] = (byte) ((byte)accessRights.getReadAccess()  << 4 | ((byte)accessRights.getWriteAccess()));

        return aux;
		
	}
	
	/**
	 * 
	 */
	public String toString(){
		
		return 	"Read Access: " + toString(this.getReadAccess()) + "\n" +
				"Write Access: " + toString(this.getWriteAccess()) + "\n" +
				"Read & Write Access: " + toString(this.getReadWriteAccess()) + "\n" +
				"Change Access Rights: " + toString(this.getChangeAccessRights());
		
	}
	
	/**
	 * 
	 * @param aR
	 * @return
	 */
	private static String toString(int aR){
        
        switch(aR){
        
            case AccessRights.FREE: return "Free access";
            case AccessRights.DENY: return "Access denied";
            default: return "Access allowed with key number " + aR;
        }
    }

	
	/**
	 * 
	 */
	public static final int FREE = 0xE;
	/**
	 * 
	 */
	public static final int DENY = 0xF;
	
	private int readAccess;
	private int writeAccess;
	private int readWriteAccess;
	private int changeAccessRights;
	
}
