package dflibrary.library.param;

import dflibrary.utils.ba.BAUtils;


/**
 * 
 * @author Francisco Rodríguez Algarra
 *
 */
public class DFName {

	/**
	 * 
	 * @param dfname
	 */
	public DFName(byte[] dfname){
		
		if(dfname == null) throw new NullPointerException();
		if(dfname.length > 16) throw new IllegalArgumentException();
		
		this.dfname = dfname;
		
	}
	
	/**
	 * 
	 * @return
	 */
	public byte[] getDFName(){ return this.dfname; }
	
	/**
	 * 
	 */
	public String toString(){ return "0x" + BAUtils.toString(this.dfname); }
	
	private byte[] dfname;
	
}
