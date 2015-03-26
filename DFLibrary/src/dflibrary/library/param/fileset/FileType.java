package dflibrary.library.param.fileset;

import dflibrary.utils.ba.*;

/**
 * 
 * @author Francisco Rodríguez Algarra
 *
 */
public enum FileType {
	
	/**
	 * 
	 */
	STANDARD_DATA{
		public int toInt(){
			return 0;
		}
		public String toString(){
			return "Standard Data File";
		}
	},
	/**
	 * 
	 */
	BACKUP_DATA{
		public int toInt(){
			return 1;
		}
		public String toString(){
			return "Backup Data File";
		}	
	},
	/**
	 * 
	 */
	VALUE{
		public int toInt(){
			return 2;
		}
		public String toString(){
			return "Value File";
		}
	},
	/**
	 * 
	 */
	LINEAR_RECORD{
		public int toInt(){
			return 3;
		}
		public String toString(){
			return "Linear Record File";
		}
	},
	/**
	 * 
	 */
	CYCLIC_RECORD{
		public int toInt(){
			return 4;
		}
		public String toString(){
			return "Cyclic Record File";
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
	 * @param fileType
	 * @return
	 */
	public static FileType toFileType(byte[] fileType){
		
		if(fileType == null) throw new NullPointerException();
		if(fileType.length != 1) throw new IllegalArgumentException("");
		
		return toFileType(BAUtils.toInt(fileType));
		
	}
	
	/**
	 * 
	 * @param fileType
	 * @return
	 */
	public static FileType toFileType(int fileType){
		
		switch(fileType){
			case 0: return STANDARD_DATA;
			case 1: return BACKUP_DATA;
			case 2: return VALUE;
			case 3: return LINEAR_RECORD;
			case 4: return CYCLIC_RECORD;
			default: throw new IllegalArgumentException(""); 
		}		
	}

}
