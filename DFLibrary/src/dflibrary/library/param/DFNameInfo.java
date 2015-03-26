package dflibrary.library.param;

import dflibrary.utils.ba.BAUtils;

/**
 * 
 * @author Francisco Rodríguez Algarra 
 *
 */
public class DFNameInfo {

	/**
	 * 
	 * @param aid
	 * @param fid
	 * @param dfname
	 */
	public DFNameInfo(AID aid, ISOFileID fid, DFName dfname){
		
		if((aid == null) || (fid == null) || (dfname == null)) throw new NullPointerException();
		
		this.aid = aid;
		this.fid = fid;
		this.dfname = dfname;
		
		
	}
	
	/**
	 * 
	 * @param dfNameInfo
	 */
	public DFNameInfo(byte[] dfNameInfo){
		
		if(dfNameInfo == null) throw new NullPointerException();
		if(dfNameInfo.length < 5) throw new IllegalArgumentException();
		
		this.aid = new AID(BAUtils.extractSubBA(dfNameInfo, 0, 3));
		this.fid = new ISOFileID(BAUtils.extractSubBA(dfNameInfo, 3, 2));
		this.dfname = new DFName(BAUtils.extractSubBA(dfNameInfo, 5, dfNameInfo.length - 5));	
		
	}
	
	/**
	 * 
	 * @param aid
	 * @param fid
	 * @param dfname
	 */
	public DFNameInfo(byte[] aid, byte[] fid, byte[] dfname){
		
		if((aid == null) || (fid == null) || (dfname == null)) throw new NullPointerException();
		
		this.aid = new AID(aid);
		this.fid = new ISOFileID(fid);
		this.dfname = new DFName(dfname);
		
	}
	
	/**
	 * 
	 * @return
	 */
	public AID getAID(){ return this.aid; }
	
	/**
	 * 
	 * @return
	 */
	public ISOFileID getFID(){ return this.fid; }
	
	/**
	 * 
	 * @return
	 */
	public DFName getDFName(){ return this.dfname; }
	
	/**
	 * 
	 */
	public String toString(){
		
		String s = "";
		
		s = s + "AID: " + this.aid.toString() + "\n";
		s = s + "FID: " + this.fid.toString() + "\n";
		s = s + "DFName: " + this.dfname.toString();
		
		return s;
		
	}
	
	private DFName dfname;
	private AID aid;
	private ISOFileID fid;
	
}
