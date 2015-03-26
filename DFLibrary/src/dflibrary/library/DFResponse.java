package dflibrary.library;

import dflibrary.library.DFLException.ExType;
import dflibrary.library.param.*;
import dflibrary.library.param.fileset.*;

public class DFResponse {

	/**
	 * 
	 * @param sc
	 */
    public DFResponse(byte[] sc){
    	
    	if(sc == null) throw new NullPointerException();
    	if(sc.length != 1) throw new IllegalArgumentException();
    	
        this.sc = SC.toSC(sc);

    }

    /**
     * 
     * @param sc
     */
    public DFResponse(SC sc){

    	if(sc == null) throw new NullPointerException();
    	
        this.sc = sc;

    }

    /**
     * 
     * @param sc
     * @param field
     */
    public DFResponse(SC sc, Object field){
    
    	if(sc == null) throw new NullPointerException();
    	
        this.sc = sc;
        this.field = field;
    
    }

    
    /**
     * 
     * @return
     */
    public boolean isOk(){

        return this.getSC().isOk();

    }
    
    /**
     * 
     * @return
     */
    public SC getSC(){
    	
    	return this.sc;
    }
	
    /**
     * 
     * @return
     */
    public Object getField(){
    	
    	return this.field;
    	
    }
    
    /**
     * 
     * @return
     */
    public KeyVersion getKeyVersion(){
    	
    	if(this.field instanceof KeyVersion)
            return (KeyVersion)this.field;

		throw new DFLException(ExType.WRONG_FIELD_CLASS);
    	
    	
    }
    
    /**
     * 
     * @return
     */
	public KeySettings getKeySettings(){

        if(this.field instanceof KeySettings) return (KeySettings)this.field;
        else if(this.field instanceof KeySettingsRes)
            return ((KeySettings)((KeySettingsRes)this.field).getKeySettings());
        throw new DFLException(ExType.WRONG_FIELD_CLASS);

	}
	
	   
    /**
     * 
     * @return
     */
	public int getNumOfKeys(){

		
		if(this.field instanceof KeySettingsRes)
            return ((KeySettingsRes)this.field).getMaxNumOfKeys();

		throw new DFLException(ExType.WRONG_FIELD_CLASS);
		
	}
    
	/**
	 * 
	 * @return
	 */
	public AIDS getAIDs(){
		
		if(this.field instanceof AIDS) return (AIDS)this.field;

		throw new DFLException(ExType.WRONG_FIELD_CLASS);
		
		
	}
	
	/**
	 * 
	 * @return
	 */
	public Size getFreeMemory(){
		
		if(this.field instanceof Size) return (Size)this.field;
		
		throw new DFLException(ExType.WRONG_FIELD_CLASS);
		
	}
	
	/**
	 * 
	 * @return
	 */
	public PICCVersion getPICCVersion(){
		
		if(this.field instanceof PICCVersion) return (PICCVersion)this.field;
		
		throw new DFLException(ExType.WRONG_FIELD_CLASS);
	}

	/**
	 * 
	 * @return
	 */
	public UIDRes getUIDRes(){
		
		if(this.field instanceof UIDRes) return (UIDRes)this.field;
		
		throw new DFLException(ExType.WRONG_FIELD_CLASS);	
		
	}
	
	/**
	 * 
	 * @return
	 */
	public UID getUID(){
		
		if(this.field instanceof UIDRes) return ((UIDRes)this.field).getUID();
		else if(this.field instanceof PICCVersion) return ((PICCVersion)this.field).getUID();
		
		throw new DFLException(ExType.WRONG_FIELD_CLASS);
		
	}
	
	/**
	 * 
	 * @return
	 */
	public DFNamesRes getDFNames(){
		
		if(this.field instanceof DFNamesRes) return (DFNamesRes)this.field;
		
		throw new DFLException(ExType.WRONG_FIELD_CLASS);
		
	}
	
	/**
	 * 
	 * @return
	 */
	public FIDS getFIDs(){
		
	if(this.field instanceof FIDS) return (FIDS)this.field;
		
		throw new DFLException(ExType.WRONG_FIELD_CLASS);
		
	}
	
	/**
	 * 
	 * @return
	 */
	public ISOFileIDS getISOFileIDs(){
		
		if(this.field instanceof ISOFileIDS) return (ISOFileIDS)this.field;
		
		throw new DFLException(ExType.WRONG_FIELD_CLASS);
		
	}
	
	/**
	 * 
	 * @return
	 */
	public FileSettings getFileSettings(){
		
		if(this.field instanceof FileSettings) return (FileSettings)this.field;
		
		throw new DFLException(ExType.WRONG_FIELD_CLASS);
	
	}
	
	/**
	 * 
	 * @return
	 */
	public DataRes getDataRes(){
		
		if(this.field instanceof DataRes) return (DataRes)this.field;
		
		throw new DFLException(ExType.WRONG_FIELD_CLASS);
		
	}
	
	/**
	 * 
	 * @return
	 */
	public ValueRes getValueRes(){
		
		if(this.field instanceof ValueRes) return (ValueRes)this.field;
		
		throw new DFLException(ExType.WRONG_FIELD_CLASS);
		
	}
	
	/**
	 * 
	 * @return
	 */
	public RecordsRes getRecordsRes(){
		
		if(this.field instanceof RecordsRes) return (RecordsRes)this.field;
		
		throw new DFLException(ExType.WRONG_FIELD_CLASS);
		
	}
	
    /**
     * 
     */
    public String toString(){
    	
        if((this.field == null) || !this.isOk())
            return this.getSC().toString() + "\n";
        else return this.getSC().toString() + "\n" + field.toString() + "\n";

    }

    
	private SC sc;
    private Object field;
	
}
