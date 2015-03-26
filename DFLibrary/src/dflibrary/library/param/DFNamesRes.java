package dflibrary.library.param;

/**
 * 
 * @author Francisco Rodríguez Algarra
 *
 */
public class DFNamesRes {

	/**
	 * 
	 * @param dfNames
	 */
	public DFNamesRes(byte[][] dfNames){
		
		if(dfNames == null) throw new NullPointerException();
		
		this.dfnames = new DFNameInfo[0];
		
		for(int i = 0; i < dfNames.length; i++){
			addDFName(dfNames[i]);
		}
		
	}
	
	/**
	 * 
	 * @param dfName
	 */
	public void addDFName(byte[] dfName){
		
		if(dfName == null) throw new NullPointerException();
		
		DFNameInfo[] aux = new DFNameInfo[this.dfnames.length + 1];
		
		System.arraycopy(this.dfnames, 0, aux, 0, dfnames.length);
		
		aux[dfnames.length] = new DFNameInfo(dfName);
		
		this.dfnames = aux;
		
	}
	
	
	/**
	 * 
	 * @return
	 */
	public DFNameInfo[] getDFNames(){ return this.dfnames; }
	
	/**
	 * 
	 */
	public String toString(){
			
		String s = "DF-Names of all active applications on the PICC: \n";
		
		if(this.dfnames.length == 0) s = s + "No applications with DF-Name present on the PICC";
		else{
			for(int i = 0; i < this.dfnames.length; i++){
				s = s + this.dfnames[i].toString() + "\n";
			}
		}
		
		return s;
			
			
	}
	
	private DFNameInfo[] dfnames;
	
}
