package dflibrary.library.param;

import dflibrary.library.security.DFKey;
import dflibrary.utils.ba.BAUtils;

/**
 * Provides an encapsulation for the sending parameters of the 
 * <code>setConfiguration</code> command
 * @author Francisco Rodriguez Algarra
 */
public class ConfigOption {

	/**
	 * Creates an instance of class <code>ConfigOption</code> 
	 * for options of type <code>FC_RandID</code>
	 * @param formatEnabled a boolean indicating whether 
	 * card formatting is allowed or not
	 * @param randIDEnabled a boolean indicating whether 
	 * card formatting is allowed or not
	 */
	public ConfigOption(boolean formatEnabled, boolean randIDEnabled){
		
		this.opt = ConfigOptionType.FC_RandID;
		this.formatEnabled = formatEnabled;
		this.randIDEnabled = randIDEnabled;
		
	}
	
	/**
	 * Creates an instance of class <code>ConfigOption</code> 
	 * for options of type <code>KEY</code>
	 * @param key an instance of class <code>DFKey</code>
	 * representing the data of the key to be set
	 */
	public ConfigOption(DFKey key){
		
		if(key == null) throw new NullPointerException();
		
		this.opt = ConfigOptionType.KEY;
		this.key = key;
		
	}
	
	/**
	 * Creates an instance of class <code>ConfigOption</code> 
	 * for options of type <code>ATS</code>
	 * @param ATS a byte array containing the new <code>ATS</code>
	 * string to be set
	 */
	public ConfigOption(byte[] ATS){
		
		if(ATS == null) throw new NullPointerException();
		
		this.opt = ConfigOptionType.ATS;
		this.ATS = ATS;
		
	}
	
	/**
	 * @return an instance of class <code>ConfigOptionType</code>
	 * representing the current configuration option type
	 */
	public ConfigOptionType getOpt(){
		
		return this.opt;
		
	}
	
	/**
	 * @return the byte array representation of the current configuration
	 * options
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
			data = BAUtils.concatenateBAs(this.key.getKeyBytes(), 
					BAUtils.toBA(this.key.getKeyVersion(), 1));
			
		else data = this.ATS;
		
		return data;
		
	}
	
	@Override
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
