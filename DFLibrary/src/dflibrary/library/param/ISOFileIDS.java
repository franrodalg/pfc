package dflibrary.library.param;

import dflibrary.utils.ba.BAUtils;

/**
 * Provides an encapsulation of the structure retrieved from
 * an execution of the <code>getFileIDs</code> command
 * @author Francisco Rodriguez Algarra
 */
public class ISOFileIDS {

	/**
	 * Creates an instance of class <code>ISOFileIDS</code>
	 * @param fids a byte array containing the list of file
	 * identifiers
	 */
	public ISOFileIDS(byte[] fids){
		
		if(fids == null) throw new NullPointerException();
		if(fids.length % 2 != 0) 
			throw new IllegalArgumentException("");
		
        int numOfFiles = (fids.length)/2;

        this.fids = new ISOFileID[numOfFiles];

        for(int i = 0; i<numOfFiles;i++){
            this.fids[i] = new ISOFileID (
            		BAUtils.extractSubBA(fids, i*2, 2));
        }
		
	}
	
	/**
	 * @return an array of instances of class <code>ISOFileIDs</code>
	 * containing the list of ISO file identifiers present
	 * on the card
	 */
	public ISOFileID[] getISOFileIDs(){ return this.fids; }
	
	/**
	 * @param i an index
	 * @return an instance of class <code>ISOFileIDs</code>
	 * representing the ISO file identifier at position
	 * <code>i</code> in the retrieved list
	 */
	public ISOFileID getISOFileID(int i){ return this.fids[i]; }
	
	@Override
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
