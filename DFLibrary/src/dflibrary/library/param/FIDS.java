package dflibrary.library.param;

import dflibrary.utils.ba.BAUtils;

/**
 * Provides an encapsulation of the structure retrieved from
 * an execution of the <code>getFileIDs</code> command
 * @author Francisco Rodriguez Algarra
 */
public class FIDS {

	/**
	 * Creates an instance of class <code>FIDS</code>
	 * @param aids a byte array containing the list of file
	 * identifiers
	 */
	public FIDS(byte[] fids){
		
		if(fids == null) throw new NullPointerException();

        this.fids = new FID[fids.length];

        for(int i = 0; i<fids.length;i++){
            this.fids[i] = new FID (BAUtils.extractSubBA(fids, i, 1));
        }
		
	}
	
	/**
	 * @return an array of instances of class <code>FID</code>
	 * containing the list of file identifiers present
	 * on an application of the card
	 */
	public FID[] getFids(){ return this.fids; }
	
	/**
	 * @param i an index
	 * @return an instance of class <code>FID</code>
	 * representing the file identifier at position
	 * <code>i</code> in the retrieved list
	 */
	public FID getFid(int i){ return this.fids[i]; }
	
	@Override
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
