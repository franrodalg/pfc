package dflibrary.library.security;

/**
 * 
 * @author Francisco Rodríguez Algarra
 *
 */
public enum AuthType {

		/**
		 * 
		 */
		NO_AUTH{
			
			public String toString(){
				return "Not authenticated";
			}
			
		},
		/**
		 * 
		 */
		TDEA_NATIVE{
			
			public String toString(){
				return "Triple DES Crypto DESFire " +
						"Native Mode Authentication";
			}
			
		},
		/**
		 * 
		 */
		TDEA_STANDARD{
			
			public String toString(){
				return "Triple DES Crypto Standard " +
						"Mode Authentication";
			}
			
		},
		/**
		 * 
		 */
		TDEA3{
			
			public String toString(){
				return "3 Key Triple DES Crypto Authentication";
			}
			
		},
		/**
		 * 
		 */
		AES{
			
			public String toString(){
				return "AES Crypto Authentication";
			}
			
		};

}
