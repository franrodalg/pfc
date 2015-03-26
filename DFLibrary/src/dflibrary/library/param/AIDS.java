package dflibrary.library.param;

import dflibrary.utils.ba.BAUtils;


/**
 * 
 * @author Francisco Rodríguez Algarra
 *
 */
public class AIDS {

	/**
	 * 
	 * @param aids
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
	 * 
	 * @return
	 */
	public AID[] getAids(){ return this.aids; }
	
	/**
	 * 
	 * @param i
	 * @return
	 */
	public AID getAid(int i){ return this.aids[i]; }
	
	/**
     * 
     */
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
