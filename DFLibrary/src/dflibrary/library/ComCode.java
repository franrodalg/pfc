package dflibrary.library;

import dflibrary.utils.ba.BAUtils;

/**
 * 
 * @author Francisco Rodríguez Algarra
 *
 */
public enum ComCode {
	
	//Security related commands
	
	AUTHENTICATE{
		
		public byte[] toBA(){
			return BAUtils.toBA("0A");
		}
		
		public String toString(){
			return "Authenticate";
		}
		
	},	
	AUTHENTICATE_ISO{
		
		public byte[] toBA(){
			return BAUtils.toBA("1A");
		}
		
		public String toString(){
			return "Authenticate ISO";
		}
		
	},
	AUTHENTICATE_AES{
		
		public byte[] toBA(){
			return BAUtils.toBA("AA");
		}
		
		public String toString(){
			return "Authenticate AES";
		}
		
	},
	CHANGE_KEY_SETTINGS{
		
		public byte[] toBA(){
			return BAUtils.toBA("54");
		}
		
		public String toString(){
			return "Change Key Settings";
		}
		
	},
	SET_CONFIGURATION{
		
		public byte[] toBA(){
			return BAUtils.toBA("5C");
		}
		
		public String toString(){
			return "Set Configuration";
		}
		
	},
	CHANGE_KEY{
		
		public byte[] toBA(){
			return BAUtils.toBA("C4");
		}
		
		public String toString(){
			return "Change Key";
		}
		
	},
	GET_KEY_VERSION{
		
		public byte[] toBA(){
			return BAUtils.toBA("64");
		}
		
		public String toString(){
			return "Get Key Version";
		}
		
	},
	
	//PICC level commands
	
	CREATE_APPLICATION{
		
		public byte[] toBA(){
			return BAUtils.toBA("CA");
		}
		
		public String toString(){
			return "Create Application";
		}
		
	},	
	DELETE_APPLICATION{
		
		public byte[] toBA(){
			return BAUtils.toBA("DA");
		}
		
		public String toString(){
			return "Delete Application";
		}
		
	},	
	GET_APPLICATION_IDS{
		
		public byte[] toBA(){
			return BAUtils.toBA("6A");
		}
		
		public String toString(){
			return "Get Application IDs";
		}
		
	},
	FREE_MEMORY{
		
		public byte[] toBA(){
			return BAUtils.toBA("6E");
		}
		
		public String toString(){
			return "Free Memory";
		}
		
	},	
	GET_DF_NAMES{
		
		public byte[] toBA(){
			return BAUtils.toBA("6D");
		}
		
		public String toString(){
			return "Get DF Names";
		}
		
	},
	GET_KEY_SETTINGS{
		
		public byte[] toBA(){
			return BAUtils.toBA("45");
		}
		
		public String toString(){
			return "Get Key Settings";
		}
		
	},	
	SELECT_APPLICATION{
		
		public byte[] toBA(){
			return BAUtils.toBA("5A");
		}
		
		public String toString(){
			return "Select Application";
		}
		
	},	
	FORMAT_PICC{
		
		public byte[] toBA(){
			return BAUtils.toBA("FC");
		}
		
		public String toString(){
			return "Format PICC";
		}
		
	},	
	GET_VERSION{
		
		public byte[] toBA(){
			return BAUtils.toBA("60");
		}
		
		public String toString(){
			return "Get Version";
		}
		
	},	
	GET_CARD_UID{
		
		public byte[] toBA(){
			return BAUtils.toBA("51");
		}
		
		public String toString(){
			return "Get Card UID";
		}
		
	},
	
	//Application level commands
	
	GET_FILE_IDS{
		
		public byte[] toBA(){
			return BAUtils.toBA("6F");
		}
		
		public String toString(){
			return "Get File IDs";
		}
		
	},	
	GET_FILE_SETTINGS{
		
		public byte[] toBA(){
			return BAUtils.toBA("F5");
		}
		
		public String toString(){
			return "Get File Settings";
		}
		
	},	
	CHANGE_FILE_SETTINGS{
		
		public byte[] toBA(){
			return BAUtils.toBA("5F");
		}
		
		public String toString(){
			return "Change File Settings";
		}
		
	},	
	CREATE_STD_DATA_FILE{
		
		public byte[] toBA(){
			return BAUtils.toBA("CD");
		}
		
		public String toString(){
			return "Create Standard Data File";
		}
		
	},	
	CREATE_BACKUP_DATA_FILE{
		
		public byte[] toBA(){
			return BAUtils.toBA("CB");
		}
		
		public String toString(){
			return "Create Backup Data File";
		}
		
	},	
	CREATE_VALUE_FILE{
		
		public byte[] toBA(){
			return BAUtils.toBA("CC");
		}
		
		public String toString(){
			return "Create Value File";
		}
		
	},	
	CREATE_LINEAR_RECORD_FILE{
		
		public byte[] toBA(){
			return BAUtils.toBA("C1");
		}
		
		public String toString(){
			return "Create Linear Record File";
		}
		
	},	
	CREATE_CYCLIC_RECORD_FILE{
		
		public byte[] toBA(){
			return BAUtils.toBA("C0");
		}
		
		public String toString(){
			return "Create Cyclic Record File";
		}
		
	},	
	DELETE_FILE{
		
		public byte[] toBA(){
			return BAUtils.toBA("DF");
		}
		
		public String toString(){
			return "Delete File";
		}
		
	},	
	GET_ISO_FILE_IDS{
		
		public byte[] toBA(){
			return BAUtils.toBA("61");
		}
		
		public String toString(){
			return "Get ISO File IDs";
		}
		
	},
	
	//Data manipulation commands
	
	READ_DATA{
		
		public byte[] toBA(){
			return BAUtils.toBA("BD");
		}
		
		public String toString(){
			return "Read Data";
		}
		
	},	
	WRITE_DATA{
		
		public byte[] toBA(){
			return BAUtils.toBA("3D");
		}
		
		public String toString(){
			return "Write Data";
		}
		
	},
	GET_VALUE{
		
		public byte[] toBA(){
			return BAUtils.toBA("6C");
		}
		
		public String toString(){
			return "Get Value";
		}
		
	},	
	CREDIT{
		
		public byte[] toBA(){
			return BAUtils.toBA("0C");
		}
		
		public String toString(){
			return "Credit";
		}
		
	},	
	DEBIT{
		
		public byte[] toBA(){
			return BAUtils.toBA("DC");
		}
		
		public String toString(){
			return "Debit";
		}
		
	},	
	LIMITED_CREDIT{
		
		public byte[] toBA(){
			return BAUtils.toBA("1C");
		}
		
		public String toString(){
			return "Limited Credit";
		}
		
	},	
	WRITE_RECORD{
		
		public byte[] toBA(){
			return BAUtils.toBA("3B");
		}
		
		public String toString(){
			return "Write Record";
		}
		
	},	
	READ_RECORDS{
		
		public byte[] toBA(){
			return BAUtils.toBA("BB");
		}
		
		public String toString(){
			return "Read Records";
		}
		
	},	
	CLEAR_RECORD_FILE{
		
		public byte[] toBA(){
			return BAUtils.toBA("EB");
		}
		
		public String toString(){
			return "Clear Record File";
		}
		
	},	
	COMMIT_TRANSACTION{
		
		public byte[] toBA(){
			return BAUtils.toBA("C7");
		}
		
		public String toString(){
			return "Commit Transaction";
		}
		
	},	
	ABORT_TRANSACTION{
		
		public byte[] toBA(){
			return BAUtils.toBA("A7");
		}
		
		public String toString(){
			return "Abort Transaction";
		}
		
	};
	
	public abstract byte[] toBA();
	
}
