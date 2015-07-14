package dflibrary.utils.security;

/**
 * 
 * @author Francisco Rodriguez Algarra
 *
 */
public enum CipAlg {
	
	/**
	 * 
	 */
	DES{
		public int getBlockLength(){
			return 8;
		}
		public int getKeyLength(){
			return 8;
		}
	},
	/**
	 * 
	 */
	TDEA2{
		public int getBlockLength(){
			return 8;
		}
		public int getKeyLength(){
			return 16;
		}
	},
	/**
	 * 
	 */
	TDEA3{
		public int getBlockLength(){
			return 8;
		}
		public int getKeyLength(){
			return 24;
		}
	},
	/**
	 * 
	 */
	AES{
		public int getBlockLength(){
			return 16;
		}
		public int getKeyLength(){
			return 16;
		}
	};	

	public abstract int getBlockLength();
	public abstract int getKeyLength();
	
}
