package dflibrary.library.param.fileset;

import dflibrary.utils.ba.*;

/**
 * Provides singleton objects representing the different communication
 * settings available in Mifare DESFire cards
 * @author Francisco Rodriguez Algarra
 */
public enum ComSet {
	
	/**
	 * Plain communication
	 */
	PLAIN{
		public int toInt(){
			return 0;
		}
		public String toString(){
			return "Plain Communication";
		}
	},
	/**
	 * Plain communication with authentication code
	 */
	MAC{
		public int toInt(){
			return 1;
		}
		public String toString(){
			return  "Maced Communication";
		}
	},
	/**
	 * Enciphered communication
	 */
	ENC{
		public int toInt(){
			return 3;
		}
		public String toString(){
			return "Enciphered Communication";
		}
	};
	
	/**
	 * @return the byte array representation of the current communication setting
	 */
	public byte[] toBA(){
		return BAUtils.toBA(this.toInt(), 1);
	}
	
	/**
	 * @return the int representation of the current communication setting
	 */
	public int toInt(){
		return this.toInt();
	}
	
	/**
	 * Obtains the <code>ComSet</code> object corresponding to a given 
	 * byte array representation
	 * @param comSet a byte array representing a communication setting
	 * @return the <code>ComSet</code> object corresponding to 
	 * <code>comSet</code>
	 */
	public static ComSet toComSet(byte[] comSet){
		
		if(comSet == null) throw new NullPointerException();
		if(comSet.length != 1) throw new IllegalArgumentException("");
		
		return toComSet(BAUtils.toInt(comSet));
	}
	
	/**
	 * Obtains the <code>ComSet</code> object corresponding to a given 
	 * integer representation
	 * @param comSet an int representing a communication setting
	 * @return the <code>ComSet</code> object corresponding to 
	 * <code>comSet</code>
	 */
	public static ComSet toComSet(int comSet){
		
		switch(comSet){
			case 0:
			case 2: return PLAIN;
			case 1: return MAC;
			case 3: return ENC;
			default: throw new IllegalArgumentException(); 
		}
		
	}
	
	@Override
	public abstract String toString();
	
}
