package dflibrary.library.param;

import dflibrary.utils.ba.*;

/**
 * 
 * @author Francisco Rodríguez Algarra
 *
 */
public class PICCVersion {
    
	/**
	 * 
	 * @param version
	 */
    public PICCVersion(byte[] version){

    	if(version == null) throw new NullPointerException();
    	if(version.length != 28) throw new IllegalArgumentException();
    	
        this.hardware = new PICCInfo(BAUtils.extractSubBA(version, 0, 7));
        this.software = new PICCInfo(BAUtils.extractSubBA(version, 7, 7));
        this.uid = new UID(BAUtils.extractSubBA(version, 14, 7));
        this.productionBatchNumber = new ProdBatchNum(BAUtils.extractSubBA(version, 21, 5));
        this.productionWeek = BAUtils.toInt(BAUtils.extractSubBA(version, 26, 1));
        this.productionYear = BAUtils.toInt(BAUtils.extractSubBA(version, 27, 1));

    }
	
    /**
     * 
     * @return
     */
    public PICCInfo getHardwareInfo(){

        return this.hardware;

    }

    /**
     * 
     * @return
     */
    public PICCInfo getSoftwareInfo(){

        return this.software;

    }

    /**
     * 
     * @return
     */
    public UID getUID(){
    
        return this.uid;
    
    }

    /**
     * 
     * @return
     */
    public ProdBatchNum getProductionBatchNumber(){
    
        return this.productionBatchNumber;
    
    }
    
    /**
     * 
     * @return
     */
    public int getProductionWeek(){
    
        return this.productionWeek;
    
    }

    /**
     * 
     * @return
     */
    public int getProductionYear(){

        return this.productionYear;

    }
    
   
    /**
     * 
     */
    public String toString(){
    	
    	return "Hardware Info: \n[" + this.getHardwareInfo().toString() + "],\n" +
    			"Software Info: \n[" + this.getSoftwareInfo().toString() + "],\n" +
    			"UID: 0x" + this.getUID().toString() + ",\n" + 
    			"Production Batch Number: " + this.getProductionBatchNumber().toString() + ",\n" +
    			"Production Week: " + this.getProductionWeek() + ",\n" +
    			"Production Year: " + this.getProductionYear();
    	
    }
	
	private PICCInfo hardware;
    private PICCInfo software;
    private UID uid;
    private ProdBatchNum productionBatchNumber;
    private int productionWeek;
    private int productionYear;
    
}
