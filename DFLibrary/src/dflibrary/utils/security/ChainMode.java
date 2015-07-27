package dflibrary.utils.security;

/**
 * Provides singleton objects to represent the different cryptographic
 * chaining options
 * @author Francisco Rodriguez Algarra
 */
public enum ChainMode {

	/**
	 * ISO Standard Cipher Block Chain Mode for receive operations 
	 */
	CBCSendISO{},
	/**
	 * ISO Standard Cipher Block Chain Mode for receive operations 
	 */
	CBCReceiveISO{},
	/**
	 * Native DESFire Cipher Block Chain Mode for send operations 
	 */
	CBCSendDF{},
	/**
	 * Native DESFire Cipher Block Chain Mode for receive operations 
	 */
	CBCReceiveDF{};
	
}
