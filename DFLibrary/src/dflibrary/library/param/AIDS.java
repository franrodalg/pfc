package dflibrary.library.param;

import dflibrary.utils.ba.BAUtils;

/**
 * Provides an encapsulation of the structure retrieved from
 * an execution of the <code>getApplicationIDs</code> command
 * @author Francisco Rodriguez Algarra
 */
public class AIDS {

	/**
	 * Creates an instance of class <code>AIDS</code>
	 * @param aids a byte array containing the list of application
	 * identifiers
	 */
	public AIDS(byte[] aids){
		
		if(aids == null) throw new NullPointerException();
		if(aids.length % 3 != 0) throw new IllegalArgumentException();		

        int numOfApps = (aids.length)/3;

        this.aids = new AID[numOfApps];

        for(int i = 0; i<numOfApps;i++){
            this.aids[i] = new AID (BAUtils.extractSubBA(aids, i*3, 3));
        }
		
	}
	
	/**
	 * @return an array of instances of class <code>AID</code>
	 * containing the list of application identifiers present
	 * on the card
	 */
	public AID[] getAids(){ return this.aids; }
	
	/**
	 * @param i an index
	 * @return an instance of class <code>AID</code>
	 * representing the application identifier at position
	 * <code>i</code> in the retrieved list
	 */
	public AID getAid(int i){ return this.aids[i]; }
	
	@Override
    public String toString(){
    
        String s = "Application Identifiers present in the Card:\n";
        
        if((aids != null) && (aids.length != 0)){           
            for(int i = 0; i<aids.length;i++){
                s = s + " Aid " + i + ": " + aids[i] + "\n";
            }
        }
        else s = s + " Only PICC Master Application present\n";
        
        return s;
        
    }
	
	private AID[] aids;
	
}
