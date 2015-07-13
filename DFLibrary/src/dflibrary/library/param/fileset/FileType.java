package dflibrary.library.param.fileset;

import dflibrary.utils.ba.*;

/**
 * Provides singleton objects representing the different file types available
 * in Mifare DESFire cards
 * @author Francisco Rodriguez Algarra
 *
 */
public enum FileType {
	
	/**
	 * Standard Data File
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
	 * Backup Data File
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
	 * Value File
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
	 * Linear Record File
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
	 * Cyclic Record File
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
	 * @return the byte array representation of the current file type
	 */
	public byte[] toBA(){
		
		return BAUtils.toBA(this.toInt(), 1);
	
	}
	
	/**
	 * @return the int representation of the current file type
	 */
	public int toInt(){
		
		return this.toInt();		
		
	}
	
	/**
	 * Obtains the <code>FileType</code> object corresponding to the given
	 * byte array representation of a file type
	 * @param fileType a byte array representing a file type
	 * @return the <code>FileType</code> object corresponding to
	 * <code>fileType</code>
	 */
	public static FileType toFileType(byte[] fileType){
		
		if(fileType == null) throw new NullPointerException();
		if(fileType.length != 1) throw new IllegalArgumentException("");
		
		return toFileType(BAUtils.toInt(fileType));
		
	}
	
	/**
	 * Obtains the <code>FileType</code> object corresponding to the given
	 * integer representation of a file type
	 * @param fileType an int representing a file type
	 * @return the <code>FileType</code> object corresponding to
	 * <code>fileType</code>
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

	@Override
	public abstract String toString();
	
}
