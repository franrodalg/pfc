package dflibrary.library;

import dflibrary.library.DFLException.ExType;
import dflibrary.library.param.*;
import dflibrary.library.param.fileset.*;

/**
 * Encapsulates the response returned by the execution of a native command
 * in a Mifare DESFire. It contains an object of class <code>SC</code>
 * representing the Status Code returned, and sometimes also a generic
 * <code>Object</code> that can be casted to the particular class of the
 * response field
 * @author Francisco Rodriguez Algarra
 *
 */
public class DFResponse {

	/**
	 * 
	 * @param sc a byte array representing the Status Code obtained from
	 * the card response
	 * 
	 */
    public DFResponse(byte[] sc){
    	
    	if(sc == null) 
    		throw new NullPointerException();
    	if(sc.length != 1) 
    		throw new IllegalArgumentException();
    	
        this.sc = SC.toSC(sc);

    }

    /**
     * 
	 * @param sc an object of class <code>SC</code> representing 
	 * the Status Code obtained from the card response
     */
    public DFResponse(SC sc){

    	if(sc == null) throw new NullPointerException();
    	
        this.sc = sc;

    }

    /**
     * 
	 * @param sc an object of class <code>SC</code> representing 
	 * the Status Code obtained from the card response
     * @param field a generic <code>Object</code> instance encapsulating
     * the response field obtained from the card response
     */
    public DFResponse(SC sc, Object field){
    
    	if(sc == null) throw new NullPointerException();
    	
        this.sc = sc;
        this.field = field;
    
    }
    
    /**
     * Determines if the command has been successfully executed
     * @return <code>true</code> if the response's Status Code
     * corresponds to <code>OPERATION_OK</code>; 
     * <code>false</code> otherwise
     */
    public boolean isOk(){

        return this.getSC().isOk();

    }
    
    /** 
     * @return an object of class <code>SC</code> representing
     * the Status Code obtained from the card response
     */
    public SC getSC(){
    	
    	return this.sc;
    }
	
    /**
     * @return a generic <code>Object</code> instance encapsulating
     * the response field obtained from the card response
     */
    public Object getField(){
    	
    	return this.field;
    	
    }
    
    /**
     * Retrieves the obtained key version
     * from a call to <code>KeyVersion</code>
     * if and only if the stored <code>field</code> can be casted to
     * an object of class <code>KeyVersion</code>
     * @return an object of class <code>KeyVersion</code> representing
     * the response field of an execution of the <code>getKeyVersion<code>
     * command
     */
    public KeyVersion getKeyVersion(){
    	
    	if(this.field instanceof KeyVersion)
            return (KeyVersion)this.field;

		throw new DFLException(ExType.WRONG_FIELD_CLASS);
    	 	
    }
    
    /**
     * Retrieves the obtained key settings 
     * from a call to <code>getKeySettings</code>
     * if and only if the stored <code>field</code> can be casted to
     * an object of class <code>KeySettings</code> 
     * or <code>KeySettingsRes</code>
     * @return an object of class <code>KeySettings</code> representing
     * the response field of an execution of the <code>getKeySettings<code>
     * command
     */
	public KeySettings getKeySettings(){

        if(this.field instanceof KeySettings) 
        	return (KeySettings)this.field;
        else if(this.field instanceof KeySettingsRes)
            return ((KeySettings)((KeySettingsRes)this.field).getKeySettings());
        throw new DFLException(ExType.WRONG_FIELD_CLASS);

	}
		   
    /**
     * Retrieves the obtained number of keys
     * from a call to <code>getKeySettings</code>
     * if and only if the stored <code>field</code> can be casted to
     * an object of class <code>KeySettingsRes</code> 
     * @return an int representing the response field of an execution
     * of the <code>getKeySettings<code> command
     */
	public int getNumOfKeys(){

		
		if(this.field instanceof KeySettingsRes)
            return ((KeySettingsRes)this.field).getMaxNumOfKeys();

		throw new DFLException(ExType.WRONG_FIELD_CLASS);
		
	}
    
	/**
     * Retrieves the obtained application identifiers
     * from a call to <code>getApplicationIDs</code>
     * if and only if the stored <code>field</code> can be casted to
     * an object of class <code>AIDS</code> 
     * @return an object of class <code>AIDS</code> representing
     * the response field of an execution of the <code>getApplicationIDs<code>
     * command
	 */
	public AIDS getAIDs(){
		
		if(this.field instanceof AIDS) 
			return (AIDS)this.field;

		throw new DFLException(ExType.WRONG_FIELD_CLASS);
			
	}
	
	/**
     * Retrieves the obtained free memory size
     * from a call to <code>getFreeMemory</code>
     * if and only if the stored <code>field</code> can be casted to
     * an object of class <code>Size</code> 
     * @return an object of class <code>Size</code> representing
     * the response field of an execution of the <code>getFreeMemory<code>
     * command
	 */
	public Size getFreeMemory(){
		
		if(this.field instanceof Size) 
			return (Size)this.field;
		
		throw new DFLException(ExType.WRONG_FIELD_CLASS);
		
	}
	
	/**
     * Retrieves the obtained version of the card
     * from a call to <code>getVersion</code>
     * if and only if the stored <code>field</code> can be casted to
     * an object of class <code>PICCVersion</code> 
     * @return an object of class <code>PICCVersion</code> representing
     * the response field of an execution of the <code>getVersion<code>
     * command
	 * @return
	 */
	public PICCVersion getPICCVersion(){
		
		if(this.field instanceof PICCVersion) 
			return (PICCVersion)this.field;
		
		throw new DFLException(ExType.WRONG_FIELD_CLASS);
	}

	/**
     * Retrieves the obtained free memory size
     * from a call to <code>getFreeMemory</code>
     * if and only if the stored <code>field</code> can be casted to
     * an object of class <code>Size</code> 
     * @return an object of class <code>Size</code> representing
     * the response field of an execution of the <code>getFreeMemory<code>
     * command
	 */
	public UIDRes getUIDRes(){
		
		if(this.field instanceof UIDRes) 
			return (UIDRes)this.field;
		
		throw new DFLException(ExType.WRONG_FIELD_CLASS);	
		
	}
	
	/**
     * Retrieves the obtained card unique identifier
     * from a call to <code>getPICCVersion</code> or <code>getUID</code>
     * if and only if the stored <code>field</code> can be casted to
     * an object of class <code>PICCVersion</code> or <code>UIDRes</code> 
     * @return an object of class <code>UID</code> representing
     * the unique identifier of the connected card
     */
	public UID getUID(){
		
		if(this.field instanceof UIDRes) 
			return ((UIDRes)this.field).getUID();
		else if(this.field instanceof PICCVersion) 
			return ((PICCVersion)this.field).getUID();
		
		throw new DFLException(ExType.WRONG_FIELD_CLASS);
		
	}
	
	/**
     * Retrieves the obtained DF-Names
     * from a call to <code>getDFNames</code>
     * if and only if the stored <code>field</code> can be casted to
     * an object of class <code>DFNamesRes</code> 
     * @return an object of class <code>DFNamesRes</code> representing
     * the response field of an execution of the <code>getDFNames<code>
     * command
	 */
	public DFNamesRes getDFNames(){
		
		if(this.field instanceof DFNamesRes) 
			return (DFNamesRes)this.field;
		
		throw new DFLException(ExType.WRONG_FIELD_CLASS);
		
	}
	
	/**
     * Retrieves the obtained file identifiers
     * from a call to <code>getFileIDs</code>
     * if and only if the stored <code>field</code> can be casted to
     * an object of class <code>FIDS</code> 
     * @return an object of class <code>FIDS</code> representing
     * the response field of an execution of the <code>getFileIDs<code>
     * command
	 */
	public FIDS getFIDs(){
		
	if(this.field instanceof FIDS) 
		return (FIDS)this.field;
		
		throw new DFLException(ExType.WRONG_FIELD_CLASS);
		
	}
	
	/**
     * Retrieves the obtained ISO file identifiers
     * from a call to <code>getISOFileIDs</code>
     * if and only if the stored <code>field</code> can be casted to
     * an object of class <code>ISOFileIDS</code> 
     * @return an object of class <code>ISOFileIDS</code> representing
     * the response field of an execution of the <code>getISOFileIDs<code>
     * command
	 */
	public ISOFileIDS getISOFileIDs(){
		
		if(this.field instanceof ISOFileIDS) 
			return (ISOFileIDS)this.field;
		
		throw new DFLException(ExType.WRONG_FIELD_CLASS);
		
	}
	
	/**
     * Retrieves the obtained file settings
     * from a call to <code>getFileSettings</code>
     * if and only if the stored <code>field</code> can be casted to
     * an object of class <code>FileSettings</code>.
     * The particular settings for each type of file can be 
     * obtained via casting of the returned object
     * @return an object of class <code>FileSettings</code> representing
     * the response field of an execution of the <code>getFileSettings<code>
     * command
	 */
	public FileSettings getFileSettings(){
		
		if(this.field instanceof FileSettings) 
			return (FileSettings)this.field;
		
		throw new DFLException(ExType.WRONG_FIELD_CLASS);
	
	}
	
	/**
     * Retrieves the obtained data
     * from a call to <code>readData</code>
     * if and only if the stored <code>field</code> can be casted to
     * an object of class <code>Data</code> 
     * @return an object of class <code>Data</code> representing
     * the response field of an execution of the <code>readData<code>
     * command
	 */
	public DataRes getDataRes(){
		
		if(this.field instanceof DataRes) 
			return (DataRes)this.field;
		
		throw new DFLException(ExType.WRONG_FIELD_CLASS);
		
	}
	
	/**
     * Retrieves the obtained response field
     * from a call to <code>getValue</code>
     * if and only if the stored <code>field</code> can be casted to
     * an object of class <code>ValueRes</code> 
     * @return an object of class <code>ValueRes</code> representing
     * the response field of an execution of the <code>getValue<code>
     * command
	 */
	public ValueRes getValueRes(){
		
		if(this.field instanceof ValueRes) 
			return (ValueRes)this.field;
		
		throw new DFLException(ExType.WRONG_FIELD_CLASS);
		
	}
	
	/**
     * Retrieves the obtained response field
     * from a call to <code>readRecords</code>
     * if and only if the stored <code>field</code> can be casted to
     * an object of class <code>RecordRes</code> 
     * @return an object of class <code>RecordRes</code> representing
     * the response field of an execution of the <code>readRecords<code>
     * command
	 */
	public RecordsRes getRecordsRes(){
		
		if(this.field instanceof RecordsRes) 
			return (RecordsRes)this.field;
		
		throw new DFLException(ExType.WRONG_FIELD_CLASS);
		
	}
	
    public String toString(){
    	
        if((this.field == null) || !this.isOk())
            return this.getSC().toString() + "\n";
        else 
        	return this.getSC().toString() + "\n" + field.toString() + "\n";

    }

	private SC sc;
    private Object field;
	
}
