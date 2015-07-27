package dflibrary.utils.security;

/**
 * Provides singleton objects for the representation of key
 * diversification options
 * @author Francisco Rodriguez Algarra
 */
public enum KeyDivMode {

	/**
	 * No Key Diversification 
	 */
	NOdiv{},
	/**
	 * Key Diversification based on the Unique Identifier
	 */
	UIDdiv{},
	/**
	 * Key Diversification based on CMAC computation
	 */
	CMACdiv{};
	
}
