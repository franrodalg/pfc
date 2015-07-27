package dflibrary.utils.security;

/**
 * Provides singleton objects for the representation of 
 * symmetric key criptographic algorithms
 * @author Francisco Rodriguez Algarra
 */
public enum CipAlg {
	
	/**
	 * Data Encryption Standard 
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
	 * Two-key Triple DES Encryption Algorithm
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
	 * Three-key Triple DES Encryption Algorithm
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
	 * Advanced Encryption Standard 
	 */
	AES{
	    public int getBlockLength(){
		return 16;
	    }
	    public int getKeyLength(){
		return 16;
	    }
	};	
        
        /**
         * @return an int representing the length of a cryptographic block
         * in the specific algorithm
         */
	public abstract int getBlockLength();
	
        /**
         * @return an int representing the length of the key in the 
         * specific algorithm
         */
        public abstract int getKeyLength();
	
}
