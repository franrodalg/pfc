package dflibrary.library.param.fileset;

import dflibrary.utils.ba.*;

public enum ComSet {
	
	/**
	 * 
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
	 * 
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
	 * 
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
	 * 
	 * @return
	 */
	public byte[] toBA(){
		return BAUtils.toBA(this.toInt(), 1);
	}
	
	/**
	 * 
	 * @return
	 */
	public int toInt(){
		return this.toInt();
	}
	
	/**
	 * 
	 * @param comSet
	 * @return
	 */
	public static ComSet toComSet(byte[] comSet){
		
		if(comSet == null) throw new NullPointerException();
		if(comSet.length != 1) throw new IllegalArgumentException("");
		
		return toComSet(BAUtils.toInt(comSet));
	}
	
	/**
	 * 
	 * @param comSet
	 * @return
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
	
}
