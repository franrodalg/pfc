package dflibrary.library;

import dflibrary.library.DFLException.ExType;
import dflibrary.utils.ba.*;

/**
 * 
 * @author Francisco Rodríguez Algarra
 *
 */

public enum SC {
		
		/**
		 * 
		 */
		OPERATION_OK{
			public byte[] toBA(){
				return BAUtils.toBA("00");
			}
			public String toString(){
				return "Successful operation.";
			}
		},
		/**
		 * 
		 */
		NO_CHANGES{
			public byte[] toBA(){
				return BAUtils.toBA("0C");
			}
			public String toString(){
				return  "No changes done to backup files. CommitTransaction/AbortTransaction not necessary.";
			}
		},
		/**
		 * 
		 */
		OUT_OF_EEPROM_ERROR{
			public byte[] toBA(){
				return BAUtils.toBA("0E");
			}
			public String toString(){
				return "Insufficient NV-Memory to complete command.";
			}
		},
		/**
		 * 
		 */
		ILLEGAL_COMMAND_CODE{
			public byte[] toBA(){
				return BAUtils.toBA("1C");
			}
			public String toString(){
				return "Command code not supported.";
			}
		},
		/**
		 * 
		 */
		INTEGRITY_ERROR{
			public byte[] toBA(){
				return BAUtils.toBA("1E");
			}
			public String toString(){
				return "CRC or MAC does not match data or Padding bytes not valid.";
			}
		},
		/**
		 * 
		 */
		NO_SUCH_KEY{
			public byte[] toBA(){
				return BAUtils.toBA("40");
			}
			public String toString(){
				return "Invalid key number specified.";
			}
		},
		/**
		 * 
		 */
		LENGTH_ERROR{
			public byte[] toBA(){
				return BAUtils.toBA("7E");
			}
			public String toString(){
				return "Length of command string invalid.";
			}
		},
		/**
		 * 
		 */
		PERMISSION_DENIED{
			public byte[] toBA(){
				return BAUtils.toBA("9D");
			}
			public String toString(){
				return "Current configuration or status does not allow the requested command.";
			}
		},
		/**
		 * 
		 */
		PARAMETER_ERROR{
			public byte[] toBA(){
				return BAUtils.toBA("9E");
			}
			public String toString(){
				return "Value of the parameter(s) invalid.";
			}
		},
		/**
		 * 
		 */
		APPLICATION_NOT_FOUND{
			public byte[] toBA(){
				return BAUtils.toBA("A0");
			}
			public String toString(){
				return "Requested AID not present on PICC.";
			}
		},
		/**
		 * 
		 */
		APPL_INTEGRITY_ERROR{
			public byte[] toBA(){
				return BAUtils.toBA("A1");
			}
			public String toString(){
				return "Unrecoverable error within application. Application will be disabled.";
			}
		},
		/**
		 * 
		 */
		AUTHENTICATION_ERROR{
			public byte[] toBA(){
				return BAUtils.toBA("AE");
			}
			public String toString(){
				return "Current authentication status does not allow the requested command.";
			}
		},
		/**
		 * 
		 */
		ADDITIONAL_FRAME{
			public byte[] toBA(){
				return BAUtils.toBA("AF");
			}
			public String toString(){
				return "Additional data frame is expected to be sent.";
			}
		},
		/**
		 * 
		 */
		BOUNDARY_ERROR{
			public byte[] toBA(){
				return BAUtils.toBA("BE");
			}
			public String toString(){
				return "Attempt to read/write data from/to beyond the file's/record's limits " +
						"or Attempt to exceed the limits of a value file.";
			}
		},
		/**
		 * 
		 */
		PICC_INTEGRITY_ERROR{
			public byte[] toBA(){
				return BAUtils.toBA("C1");
			}
			public String toString(){
				return "Unrecoverable error within PICC. PICC will be disabled.";
			}
		},
		/**
		 * 
		 */
		COMMAND_ABORTED{
			public byte[] toBA(){
				return BAUtils.toBA("CA");
			}
			public String toString(){
				return "Previous command was not fully completed or Not all frames were requested or provided by the PCD.";
			}
		},
		/**
		 * 
		 */
		PICC_DISABLED_ERROR{
			public byte[] toBA(){
				return BAUtils.toBA("CD");
			}
			public String toString(){
				return "PICC was disabled by an unrecoverable error.";
			}
		},
		/**
		 * 
		 */
		COUNT_ERROR{
			public byte[] toBA(){
				return BAUtils.toBA("CE");
			}
			public String toString(){
				return "Number of Applications limited to 28. No additional CreateApplication possible.";
			}
		},
		/**
		 * 
		 */
		DUPLICATE_ERROR{
			public byte[] toBA(){
				return BAUtils.toBA("DE");
			}
			public String toString(){
				return "Creation of file/application failed because file/application with same number already exists.";
			}
		},
		/**
		 * 
		 */
		EEPROM_ERROR{
			public byte[] toBA(){
				return BAUtils.toBA("EE");
			}
			public String toString(){
				return "Could not complete NV-write operation due to loss of power. Internal backup/rollback mechanism activated.";
			}
		},
		/**
		 * 
		 */
		FILE_NOT_FOUND{
			public byte[] toBA(){
				return BAUtils.toBA("F0");
			}
			public String toString(){
				return "Specified file number does not exist.";
			}
		},
		/**
		 * 
		 */
		FILE_INTEGRITY_ERROR{
			public byte[] toBA(){
				return BAUtils.toBA("F1");
			}
			public String toString(){
				return "Unrecoverable error within file. File will be disabled.";
			}
		};

		/**
		 * 
		 * @return
		 */
		public abstract byte[] toBA();
		
		public boolean isOk(){
			
			return (this == SC.OPERATION_OK);
			
		}
		
		/**
		 * 
		 * @param res
		 * @return
		 */
		public static boolean isOk(byte[] res){
			
			if(res == null) throw new NullPointerException();
			if(res.length < 1) throw new IllegalArgumentException();
			
			SC sc = toSC(BAUtils.extractSubBA(res, 0, 1));
			
			return (sc == OPERATION_OK);
		}
		
		/**
		 * 
		 * @param res
		 * @return
		 */
		public static boolean isAF(byte[] res){
			
			if(res == null) throw new NullPointerException();
			if(res.length < 1) throw new IllegalArgumentException();
			
			SC sc = toSC(BAUtils.extractSubBA(res, 0, 1));
			
			return (sc == ADDITIONAL_FRAME);
		}
		
		
		public static boolean isISO(byte[] res){
			
			if(res == null) throw new NullPointerException();
			if(res.length < 1) throw new IllegalArgumentException();
			
			byte[] sc = BAUtils.extractSubBA(res, 0, 1);
			
			if(BAUtils.compareBAs(sc, BAUtils.toBA("90"))) return true;
			
			else{
				sc = BAUtils.and(sc, BAUtils.toBA("F0"));
				if(BAUtils.compareBAs(sc, BAUtils.toBA("60"))) return true;
			}
			
			return false;
			
			
		}
		
		
		/**
		 * 
		 * @param sc
		 * @return
		 */
		public static SC toSC(byte[] sc){
			
			if(sc == null) throw new NullPointerException();
			if(sc.length != 1) throw new IllegalArgumentException();
			
			return toSC(BAUtils.toInt(sc));
		}
		
		/**
		 * 
		 * @param sc
		 * @return
		 */
		private static SC toSC(int sc){
			
			switch(sc){
				case 0x00: return OPERATION_OK;
				case 0x0C: return NO_CHANGES;
				case 0x0E: return OUT_OF_EEPROM_ERROR;
				case 0x1C: return ILLEGAL_COMMAND_CODE;
				case 0x1E: return INTEGRITY_ERROR;
				case 0x40: return NO_SUCH_KEY;
				case 0x7E: return LENGTH_ERROR;
				case 0x9D: return PERMISSION_DENIED;
				case 0x9E: return PARAMETER_ERROR;
				case 0xA0: return APPLICATION_NOT_FOUND;
				case 0xA1: return APPL_INTEGRITY_ERROR;
				case 0xAE: return AUTHENTICATION_ERROR;
				case 0xAF: return ADDITIONAL_FRAME;
				case 0xBE: return BOUNDARY_ERROR;
				case 0xC1: return PICC_INTEGRITY_ERROR;
				case 0xCA: return COMMAND_ABORTED;
				case 0xCD: return PICC_DISABLED_ERROR;
				case 0xCE: return COUNT_ERROR;
				case 0xDE: return DUPLICATE_ERROR;
				case 0xEE: return EEPROM_ERROR;
				case 0xF0: return FILE_NOT_FOUND;
				case 0xF1: return FILE_INTEGRITY_ERROR;
				default: throw new DFLException(ExType.UNKNOWN_STATUS_CODE); 
			}
			
		}
		
	}

	
