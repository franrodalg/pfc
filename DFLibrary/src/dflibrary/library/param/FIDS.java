package dflibrary.library.param;

import dflibrary.utils.ba.BAUtils;


/**
 * 
 * @author Francisco Rodríguez Algarra
 *
 */
public class FIDS {

	/**
	 * 
	 * @param fids
	 */
	public FIDS(byte[] fids){
		
		if(fids == null) throw new NullPointerException();

        this.fids = new FID[fids.length];

        for(int i = 0; i<fids.length;i++){
            this.fids[i] = new FID (BAUtils.extractSubBA(fids, i, 1));
        }
		
	}
	
	/**
	 * 
	 * @return
	 */
	public FID[] getFids(){ return this.fids; }
	
	/**
	 * 
	 * @param i
	 * @return
	 */
	public FID getFid(int i){ return this.fids[i]; }
	
	/**
     * 
     */
    public String toString(){
    
        String s = "File Identifiers present in the Application:\n";
        
        if((fids != null) && (fids.length != 0)){           
            for(int i = 0; i<fids.length;i++){
                s = s + " Fid " + i + ": " + fids[i] + "\n";
            }
        }
        else s = s + " No files present in the Application\n";
        
        return s;
        
    }
	
	private FID[] fids;
	
}
