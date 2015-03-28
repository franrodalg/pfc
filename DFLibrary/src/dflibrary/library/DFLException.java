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
		
		COMM_ERROR{},
		INTERNAL_ERROR{},
		UNKNOWN_ERROR{},
		WAITED_TOO_LONG{},
	
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
	
	public String toString(){
		return "DESFire Library Exception: " + type.toString();
	}
	
}
