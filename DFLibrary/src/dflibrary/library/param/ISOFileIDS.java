package dflibrary.library.param;

import dflibrary.utils.ba.BAUtils;


/**
 * 
 * @author Francisco Rodríguez Algarra
 *
 */
public class ISOFileIDS {

	/**
	 * 
	 * @param fids
	 */
	public ISOFileIDS(byte[] fids){
		
		if(fids == null) throw new NullPointerException();
		if(fids.length % 2 != 0) throw new IllegalArgumentException("");
		

        int numOfFiles = (fids.length)/2;

        this.fids = new ISOFileID[numOfFiles];

        for(int i = 0; i<numOfFiles;i++){
            this.fids[i] = new ISOFileID (BAUtils.extractSubBA(fids, i*2, 2));
        }
		
	}
	
	/**
	 * 
	 * @return
	 */
	public ISOFileID[] getISOFileIDs(){ return this.fids; }
	
	/**
	 * 
	 * @param i
	 * @return
	 */
	public ISOFileID getISOFileID(int i){ return this.fids[i]; }
	
	/**
     * 
     */
    public String toString(){
    
        String s = "ISO File Identifiers present in the Application:\n";
        
        if((fids != null) && (fids.length != 0)){           
            for(int i = 0; i<fids.length;i++){
                s = s + " ISOFid " + i + ": " + fids[i] + "\n";
            }
        }
        else s = s + " No ISO files present in the Application\n";
        
        return s;
        
    }
	
	private ISOFileID[] fids;
	
}
