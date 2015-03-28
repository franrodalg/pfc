package dflibrary.library;

public class DFLException extends RuntimeException {

	
	public DFLException(ExType type){
		
		if(type == null) throw new NullPointerException();
		
		this.type = type;
		
	}
	
	public ExType getType(){
		return this.type;
	}
	
	private ExType type;
	
	public enum ExType{
		
		CANCELLED{},
		CANT_DISPOSE{},
		CARD_NOT_CONNECTED{},
		CARD_UNSUPPORTED{},		
		COMMAND_NOT_ALLOWED{},
		CONTEXT_NOT_INITIALIZED{},
		DUPLICATE_READER{},
		INSUFFICIENT_BUFFER{},
		INVALID_ATR{},
		INVALID_HANDLE{},
		INVALID_PARAMETER{},
		INVALID_TARGET{},
		INVALID_VALUE{},
		NO_MEMORY{},
		NO_READER_SELECTED{},
		NO_READERS_FOUND{},
		NO_SERVICE{},
		NO_SMARTCARD{},
		NOT_READY{},
		NOT_TRANSACTED{},
		PCI_TOO_SMALL{},
		PROTO_MISMATCH{},
		READER_NOT_FOUND{},
		READER_UNAVAILABLE{},
		READER_UNSUPPORTED{},		
		SECURITY_EXCEPTION{},
		SERVICE_STOPPED{},
		SHARING_VIOLATION{},
		SYSTEM_CANCELLED{},
		TIMEOUT{},
		UNKNOWN_CARD{},
		UNKNOWN_CARD_TYPE{},
		UNKNOWN_READER{},		
		UNKNOWN_STATUS_CODE{},		
		WRONG_FIELD_CLASS{},
		
		COMM_ERROR{},
		INTERNAL_ERROR{},
		UNKNOWN_ERROR{},
		WAITED_TOO_LONG{};
		
	}
	
	public String toString(){
		return "DESFire Library Exception: " + type.toString();
	}
	
}
