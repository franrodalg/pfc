package dflibrary.library.param;

import dflibrary.library.param.Size.SizeType;
import dflibrary.utils.ba.*;

/**
 * Provides an encapsulation of the hardware and software information
 * structures stored in <code>PICCVersion</code> objects
 * @author Francisco Rodriguez Algarra
 */
public class PICCInfo {
	
	/**
	 * Creates an instance of class <code>PICCInfo</code>
	 * @param info a byte array containing the hardware or software
	 * version information of a card
	 */
    public PICCInfo(byte[] info){
    	
    	if(info == null) 
    		throw new NullPointerException();
    	if(info.length != 7) 
    		throw new IllegalArgumentException();

        this.vendorID = BAUtils.toInt(
        		BAUtils.extractSubBA(info, 0, 1));
        this.type = BAUtils.toInt(
        		BAUtils.extractSubBA(info, 1, 1));
        this.subtype = BAUtils.toInt(
        		BAUtils.extractSubBA(info, 2, 1));
        this.majorVersionNumber = BAUtils.toInt(
        		BAUtils.extractSubBA(info, 3, 1));
        this.minorVersionNumber = BAUtils.toInt(
        		BAUtils.extractSubBA(info, 4, 1));
        this.storageSize = new Size(
        		BAUtils.extractSubBA(info, 5, 1), SizeType.TWOn);
        this.communicationProtocolType = BAUtils.toInt(
        		BAUtils.extractSubBA(info, 6, 1));

    }

    /**
     * @return an int indicating the vendor identifier
     */
	public int getVendorID(){

        return this.vendorID;

    }

	/**
	 * @return an int indicating the hardware or software type
	 */
    public int getType(){

        return this.type;
    }

    /**
	 * @return an int indicating the hardware or software subtype
     */
    public int getSubtype(){

        return this.subtype;

    }

    /**
     * @return an int indicating the hardware or software 
	 * major version number
     */
    public int getMajorVersionNumber(){

        return this.majorVersionNumber;

    }

    /**
     * @return an int indicating the hardware or software 
	 * minor version number
     */
    public int getMinorVersionNumber(){

        return this.minorVersionNumber;

    }

    /**
     * @return an instance of class <code>Size</code> representing 
     * card's storage size
     */
    public Size getStorageSize(){

        return this.storageSize;

    }

    /**
     * @return an int indicating the available communication
     * protocol type
     */
    public int getCommunicationProtocolType(){

        return this.communicationProtocolType;

    }
   
    @Override
    public String toString(){
    	
    	return "Vendor ID: " + 
    	        this.getVendorID() + ",\n" +
    			"Type: " + 
    			this.getType() + ",\n" +
    			"Subtype: " + 
    			this.getSubtype() + ",\n" +
    			"Major Version Number: " + 
    			this.getMajorVersionNumber() + ",\n" +
    			"Minor Version Number: " + 
    			this.getMinorVersionNumber() + ",\n" +
    			"Storage Size: " + 
    			this.getStorageSize() + ",\n" +
    			"Communication Protocol Type: " + 
    			this.getCommunicationProtocolType();
    	
    }
	
	private int vendorID;
    private int type;
    private int subtype;
    private int majorVersionNumber;
    private int minorVersionNumber;
    private Size storageSize;
    private int communicationProtocolType;
	
}
