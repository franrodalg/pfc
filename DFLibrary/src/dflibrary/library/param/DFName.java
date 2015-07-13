package dflibrary.library.param;

import dflibrary.utils.ba.BAUtils;

/**
 * Provides an encapsulation for a DF-Name string
 * @author Francisco Rodriguez Algarra
 *
 */
public class DFName {

	/**
	 * Creates an instance of class <code>DFName</code>
	 * @param dfname a byte array representing a DF-Name string
	 */
	public DFName(byte[] dfname){
		
		if(dfname == null) throw new NullPointerException();
		if(dfname.length > 16) throw new IllegalArgumentException();
		
		this.dfname = dfname;
		
	}
	
	/**
	 * @return the byte array representation of a DF-Name string
	 */
	public byte[] getDFName(){ return this.dfname; }
	
	@Override
	public String toString(){ return "0x" + BAUtils.toString(this.dfname); }
	
	private byte[] dfname;
	
}
