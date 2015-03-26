package dflibrary.library.param;

import dflibrary.library.security.DFKey;
import dflibrary.utils.ba.BAUtils;

/**
 * 
 * @author frankie
 *
 */
public class ConfigOption {

	/**
	 * 
	 * @param formatEnabled
	 * @param randIDEnabled
	 */
	public ConfigOption(boolean formatEnabled, boolean randIDEnabled){
		
		this.opt = ConfigOptionType.FC_RandID;
		this.formatEnabled = formatEnabled;
		this.randIDEnabled = randIDEnabled;
		
	}
	
	/**
	 * 
	 * @param key
	 */
	public ConfigOption(DFKey key){
		
		if(key == null) throw new NullPointerException();
		
		this.opt = ConfigOptionType.KEY;
		this.key = key;
		
	}
	
	/**
	 * 
	 * @param ATS
	 */
	public ConfigOption(byte[] ATS){
		
		if(ATS == null) throw new NullPointerException();
		
		this.opt = ConfigOptionType.ATS;
		this.ATS = ATS;
		
	}
	
	/**
	 * 
	 * @return
	 */
	public ConfigOptionType getOpt(){
		
		return this.opt;
		
	}
	
	/**
	 * 
	 * @return
	 */
	public byte[] getDataBA(){
		
		byte[] data;
		
		if(opt == ConfigOptionType.FC_RandID){
		
			data = new byte[1];
			
			byte[] f = BAUtils.toBA("01");
			byte[] r = BAUtils.toBA("02");
			
			if(!this.formatEnabled) data = BAUtils.xor(data, f);
			if(this.randIDEnabled) data = BAUtils.xor(data, r);
			
			
		}
		else if(opt == ConfigOptionType.KEY)			
			data = BAUtils.concatenateBAs(this.key.getKeyBytes(), BAUtils.toBA(this.key.getKeyVersion(), 1));
			
		else data = this.ATS;
		
		return data;
		
	}
	
	/**
	 * 
	 */
	public String toString(){
		
		String s = opt.toString();
		
		if(opt == ConfigOptionType.FC_RandID){
			s = s + "\n";
			s = s + "Format card " + ((this.formatEnabled) ? "enabled" : "disabled") + "\n";
			s = s + "Rando ID " + ((this.randIDEnabled) ? "enabled" : "disabled");
		}
		else if(opt == ConfigOptionType.KEY)
			s = s + "\n" + this.key.toString();

		else s = s + ": 0x" + BAUtils.toString(ATS);
			
		return s;
		
	}
	
	private ConfigOptionType opt;
	private boolean formatEnabled;
	private boolean randIDEnabled;
	private DFKey key;
	private byte[] ATS;
	
	
}
