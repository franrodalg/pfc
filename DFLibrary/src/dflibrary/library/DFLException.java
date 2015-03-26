package dflibrary.library;

import com.linuxnet.jpcsc.PCSC;
import com.linuxnet.jpcsc.PCSCException;

public class DFLException extends RuntimeException {

	public DFLException(PCSCException e){
		
		int r = e.getReason();
		
		if (r == PCSC.E_CANCELLED){
			this.type = ExType.PCSC_CANCELLED;
		}
		else if(r == PCSC.E_CANT_DISPOSE){
			this.type = ExType.PCSC_CANT_DISPOSE;
		}
		else if(r == PCSC.E_CARD_UNSUPPORTED){
			this.type = ExType.PCSC_CARD_UNSUPPORTED;
		}
		else if(r == PCSC.E_DUPLICATE_READER){
			this.type = ExType.PCSC_DUPLICATE_READER;
		}
		else if(r == PCSC.E_INSUFFICIENT_BUFFER){
			this.type = ExType.PCSC_INSUFFICIENT_BUFFER;
		}
		else if(r == PCSC.E_INVALID_ATR){
			this.type = ExType.PCSC_INSUFFICIENT_BUFFER;
		}
		else if(r == PCSC.E_INVALID_HANDLE){
			this.type = ExType.PCSC_INVALID_HANDLE;
		}
		else if(r == PCSC.E_INVALID_PARAMETER){
			this.type = ExType.PCSC_INVALID_PARAMETER;
		}
		else if(r == PCSC.E_INVALID_TARGET){
			this.type = ExType.PCSC_INVALID_TARGET;
		}
		else if(r == PCSC.E_INVALID_VALUE){
			this.type = ExType.PCSC_INVALID_VALUE;
		}
		else if(r == PCSC.E_NO_MEMORY){
			this.type = ExType.PCSC_NO_MEMORY;
		}
		else if(r == PCSC.E_NO_SERVICE){
			this.type = ExType.PCSC_NO_SERVICE;
		}
		else if(r == PCSC.E_NO_SMARTCARD){
			this.type = ExType.PCSC_NO_SMARTCARD;
		}
		else if(r == PCSC.E_NOT_READY){
			this.type = ExType.PCSC_NOT_READY;
		}	
		else if(r == PCSC.E_NOT_TRANSACTED){
			this.type = ExType.PCSC_NOT_TRANSACTED;
		}
		else if(r == PCSC.E_PCI_TOO_SMALL){
			this.type = ExType.PCSC_PCI_TOO_SMALL;
		}
		else if(r == PCSC.E_PROTO_MISMATCH){
			this.type = ExType.PCSC_PROTO_MISMATCH;
		}
		else if(r == PCSC.E_READER_UNAVAILABLE){
			this.type = ExType.PCSC_READER_UNAVAILABLE;
		}
		else if(r == PCSC.E_READER_UNSUPPORTED){
			this.type = ExType.PCSC_READER_UNSUPPORTED;
		}
		else if(r == PCSC.E_SERVICE_STOPPED){
			this.type = ExType.PCSC_SERVICE_STOPPED;
		}
		else if(r == PCSC.E_SHARING_VIOLATION){
			this.type = ExType.PCSC_SHARING_VIOLATION;
		}
		else if(r == PCSC.E_SYSTEM_CANCELLED){
			this.type = ExType.PCSC_SYSTEM_CANCELLED;
		}
		else if(r == PCSC.E_TIMEOUT){
			this.type = ExType.PCSC_TIMEOUT;
		}
		else if(r == PCSC.E_UNKNOWN_CARD){
			this.type = ExType.PCSC_UNKNOWN_CARD;
		}
		else if(r == PCSC.E_UNKNOWN_READER){
			this.type = ExType.PCSC_UNKNOWN_READER;
		}
		else if(r == PCSC.F_COMM_ERROR){
			this.type = ExType.PCSC_COMM_ERROR;
		}
		else if(r == PCSC.F_INTERNAL_ERROR){
			this.type = ExType.PCSC_INTERNAL_ERROR;
		}
		else if(r == PCSC.F_UNKNOWN_ERROR){
			this.type = ExType.PCSC_UNKNOWN_ERROR;
		}
		else if(r == PCSC.F_WAITED_TOO_LONG){
			this.type = ExType.PCSC_WAITED_TOO_LONG;
		}
		else this.type = ExType.PCSC_UNKNOWN_ERROR;
	}
	
	
	public DFLException(ExType type){
		
		if(type == null) throw new NullPointerException();
		
		this.type = type;
		
	}
	
	public ExType getType(){
		return this.type;
	}
	
	private ExType type;
	
	public enum ExType{
		
		PCSC_CANCELLED{},
		PCSC_CANT_DISPOSE{},
		PCSC_CARD_UNSUPPORTED{},
		PCSC_DUPLICATE_READER{},
		PCSC_INSUFFICIENT_BUFFER{},
		PCSC_INVALID_ATR{},
		PCSC_INVALID_HANDLE{},
		PCSC_INVALID_PARAMETER{},
		PCSC_INVALID_TARGET{},
		PCSC_INVALID_VALUE{},
		PCSC_NO_MEMORY{},
		PCSC_NO_SERVICE{},
		PCSC_NO_SMARTCARD{},
		PCSC_NOT_READY{},
		PCSC_NOT_TRANSACTED{},
		PCSC_PCI_TOO_SMALL{},
		PCSC_PROTO_MISMATCH{},
		PCSC_READER_UNAVAILABLE{},
		PCSC_READER_UNSUPPORTED{},
		PCSC_SERVICE_STOPPED{},
		PCSC_SHARING_VIOLATION{},
		PCSC_SYSTEM_CANCELLED{},
		PCSC_TIMEOUT{},
		PCSC_UNKNOWN_CARD{},
		PCSC_UNKNOWN_READER{},
		
		PCSC_COMM_ERROR{},
		PCSC_INTERNAL_ERROR{},
		PCSC_UNKNOWN_ERROR{},
		PCSC_WAITED_TOO_LONG{},
	
		NO_READERS_FOUND{},
		CONTEXT_NOT_INITIALIZED{},
		READER_NOT_FOUND{},
		NO_READER_SELECTED{},
		CARD_NOT_CONNECTED{},
		UNKNOWN_CARD_TYPE{},
		
		UNKNOWN_STATUS_CODE{},
		
		WRONG_FIELD_CLASS{},
		
		COMMAND_NOT_ALLOWED{},
		
		SECURITY_EXCEPTION{};
		
	}
	
}
