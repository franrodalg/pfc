package dflibrary.library.param;

import dflibrary.utils.ba.*;

/**
 * Provides an encapsulation of the structure retrieved from the execution of
 * the <code>getVersion</code> command
 * @author Francisco Rodriguez Algarra
 */
public class PICCVersion {
    
	/**
	 * Creates an instance of class <code>PICCVersion</code>
	 * @param version a byte array containing the PICC version information
	 * retrieved from the card
	 */
    public PICCVersion(byte[] version){

    	if(version == null) 
    		throw new NullPointerException();
    	if(version.length != 28) 
    		throw new IllegalArgumentException();
    	
        this.hardware = new PICCInfo(
        		BAUtils.extractSubBA(version, 0, 7));
        this.software = new PICCInfo(
        		BAUtils.extractSubBA(version, 7, 7));
        this.uid = new UID(
        		BAUtils.extractSubBA(version, 14, 7));
        this.productionBatchNumber = new ProdBatchNum(
        		BAUtils.extractSubBA(version, 21, 5));
        this.productionWeek = BAUtils.toInt(
        		BAUtils.extractSubBA(version, 26, 1));
        this.productionYear = BAUtils.toInt(
        		BAUtils.extractSubBA(version, 27, 1));

    }
	
    /**
     * @return an instance of class <code>PICCInfo</code> representing
     * the hardware version information
     */
    public PICCInfo getHardwareInfo(){

        return this.hardware;

    }

    /**
     * @return an instance of class <code>PICCInfo</code> representing
     * the software version information
     */
    public PICCInfo getSoftwareInfo(){

        return this.software;

    }

    /**
     * @return an instance of class <code>UID</code> representing
     * the card's unique identifier
     */
    public UID getUID(){
    
        return this.uid;
    
    }

    /**
     * @return an instance of class <code>ProdBatchNum</code> representing
     * the card's production batch number
     */
    public ProdBatchNum getProductionBatchNumber(){
    
        return this.productionBatchNumber;
    
    }
    
    /**
     * @return an int representing the card's production week
     */
    public int getProductionWeek(){
    
        return this.productionWeek;
    
    }

    /**
     * @return an int representing the card's production year
     */
    public int getProductionYear(){

        return this.productionYear;

    }
   
    @Override
    public String toString(){
    	
    	return "Hardware Info: \n[" + 
    	        this.getHardwareInfo().toString() + "],\n" +
    			"Software Info: \n[" + 
    	        this.getSoftwareInfo().toString() + "],\n" +
    			"UID: 0x" + 
    	        this.getUID().toString() + ",\n" + 
    			"Production Batch Number: " + 
    	        this.getProductionBatchNumber().toString() + ",\n" +
    			"Production Week: " + 
    	        this.getProductionWeek() + ",\n" +
    			"Production Year: " + 
    	        this.getProductionYear();
    	
    }
	
	private PICCInfo hardware;
    private PICCInfo software;
    private UID uid;
    private ProdBatchNum productionBatchNumber;
    private int productionWeek;
    private int productionYear;
    
}
