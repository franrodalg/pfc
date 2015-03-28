package dflibrary.library;

import dflibrary.library.DFLException.ExType;

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
	
	/**
	 * 
	 * @param atr
	 * @return
	 */
	public static CardType getCardType(String atr){
		
		if(atr.equals(DESFIRE_ATR)) 
			return MIFARE_DESFIRE;
		else if(atr.equals(MIFARE_CLASSIC_1K_ATR)) 
			return MIFARE_CLASSIC_1K;
		else if(atr.equals(MIFARE_CLASSIC_4K_ATR)) 
			return MIFARE_CLASSIC_4K;
		else if(atr.equals(MIFARE_ULTRALIGHT_ATR)) 
			return MIFARE_ULTRALIGHT;
		else if(atr.equals(MIFARE_PLUS_ATR)) 
			return MIFARE_PLUS;
		else 
			throw new DFLException(ExType.UNKNOWN_CARD_TYPE);
		
	}
	
	public static final String DESFIRE_ATR = "3B8180018080";
	public static final String MIFARE_CLASSIC_1K_ATR = "3B8F8001804F0CA000000306030001000000006A";
	public static final String MIFARE_CLASSIC_4K_ATR = "3B8F8001804F0CA0000003060300020000000069";
	public static final String MIFARE_ULTRALIGHT_ATR = "3B8F8001804F0CA0000003060300030000000068";
	public static final String MIFARE_PLUS_ATR = "3B878001C1052F2F01BCD6A9";

}
