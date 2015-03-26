package dflibrary.library.param;

import dflibrary.library.param.Size.SizeType;
import dflibrary.utils.ba.*;

/**
 * 
 * @author Francisco Rodríguez Algarra
 *
 */
public class PICCInfo {
	
	/**
	 * 
	 * @param info
	 */
    public PICCInfo(byte[] info){
    	
    	if(info == null) throw new NullPointerException();
    	if(info.length != 7) throw new IllegalArgumentException();

        this.vendorID = BAUtils.toInt(BAUtils.extractSubBA(info, 0, 1));
        this.type = BAUtils.toInt(BAUtils.extractSubBA(info, 1, 1));
        this.subtype = BAUtils.toInt(BAUtils.extractSubBA(info, 2, 1));
        this.majorVersionNumber = BAUtils.toInt(BAUtils.extractSubBA(info, 3, 1));
        this.minorVersionNumber = BAUtils.toInt(BAUtils.extractSubBA(info, 4, 1));
        this.storageSize = new Size(BAUtils.extractSubBA(info, 5, 1), SizeType.TWOn);
        this.communicationProtocolType = BAUtils.toInt(BAUtils.extractSubBA(info, 6, 1));

    }

    /**
     * 
     * @return
     */
	public int getVendorID(){

        return this.vendorID;

    }

	/**
	 * 
	 * @return
	 */
    public int getType(){

        return this.type;
    }

    /**
     * 
     * @return
     */
    public int getSubtype(){

        return this.subtype;

    }

    /**
     * 
     * @return
     */
    public int getMajorVersionNumber(){

        return this.majorVersionNumber;

    }

    /**
     * 
     * @return
     */
    public int getMinorVersionNumber(){

        return this.minorVersionNumber;

    }

    /**
     * 
     * @return
     */
    public Size getStorageSize(){

        return this.storageSize;

    }

    /**
     * 
     * @return
     */
    public int getCommunicationProtocolType(){

        return this.communicationProtocolType;

    }
   
    /**
     * 
     */
    public String toString(){
    	
    	return "Vendor ID: " + this.getVendorID() + ",\n" +
    			"Type: " + this.getType() + ",\n" +
    			"Subtype: " + this.getSubtype() + ",\n" +
    			"Major Version Number: " + this.getMajorVersionNumber() + ",\n" +
    			"Minor Version Number: " + this.getMinorVersionNumber() + ",\n" +
    			"Storage Size: " + this.getStorageSize() + ",\n" +
    			"Communication Protocol Type: " + this.getCommunicationProtocolType();
    	
    }
	
	private int vendorID;
    private int type;
    private int subtype;
    private int majorVersionNumber;
    private int minorVersionNumber;
    private Size storageSize;
    private int communicationProtocolType;
	
}
