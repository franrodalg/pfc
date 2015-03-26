package dflibrary.library;

/**
 * 
 * @author Francisco Rodríguez Algarra
 *
 */
public enum CardType {
	
	/**
	 * 
	 */
	MIFARE_DESFIRE{
		public String toString(){
			return "Mifare DESFire";
		}
	},
	/**
	 * 
	 */
	MIFARE_DESFIRE_EV1{
		public String toString(){
			return "Mifare DESFire EV1";
		}
	},
	/**
	 * 
	 */
	MIFARE_CLASSIC_1K{
		public String toString(){
			return "Mifare Classic 1K";
		}
	},
	/**
	 * 
	 */
	MIFARE_CLASSIC_4K{
		public String toString(){
			return "Mifare Classic 4K";
		}
	},
	/**
	 * 
	 */
	MIFARE_ULTRALIGHT{
		public String toString(){
			return "Mifare Ultralight";
		}
	},
	/**
	 * 
	 */
	MIFARE_PLUS{
		public String toString(){
			return "Mifare Plus";
		}
	};

}
