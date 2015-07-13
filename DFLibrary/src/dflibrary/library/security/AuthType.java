package dflibrary.library.security;

/**
 * Provides singleton objects to represent the current authentication status
 * @author Francisco Rodriguez Algarra
 *
 */
public enum AuthType {

		/**
		 * Card not currently authenticated
		 */
		NO_AUTH{
			
			public String toString(){
				return "Not authenticated";
			}
			
		},
		/**
		 * Card authenticated with Triple DES Crypto DESFire Native Mode
		 */
		TDEA_NATIVE{
			
			public String toString(){
				return "Triple DES Crypto DESFire " +
						"Native Mode Authentication";
			}
			
		},
		/**
		 * Card authenticated with Triple DES Crypto DESFire Standard Mode
		 */
		TDEA_STANDARD{
			
			public String toString(){
				return "Triple DES Crypto Standard " +
						"Mode Authentication";
			}
			
		},
		/**
		 * Card authenticated with 3 Key Triple DES Crypto
		 */
		TDEA3{
			
			public String toString(){
				return "3 Key Triple DES Crypto Authentication";
			}
			
		},
		/**
		 * Card authenticated with AES Crypto
		 */
		AES{
			
			public String toString(){
				return "AES Crypto Authentication";
			}
			
		};

}
