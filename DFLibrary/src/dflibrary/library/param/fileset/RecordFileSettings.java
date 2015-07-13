package dflibrary.library.param.fileset;

import dflibrary.library.param.Size;
import dflibrary.utils.ba.*;

/**
 * Provides an encapsulation for the file settings of record files
 * @author Francisco Rodríguez Algarra
 */
public class RecordFileSettings extends FileSettings{
	
	/**
	 * Creates a default instance of class <code>RecordFileSettings</code>
	 */
	public RecordFileSettings(){
		
		this(FileType.LINEAR_RECORD, ComSet.PLAIN, new AccessRights(), 
				new Size(0), 0, 0);
		
	}
	
	/** 
	 * Creates an instance of class <code>RecordFileSettings</code>
	 * @param fileType an instance of class <code>FileType</code>
	 * representing the type of the file
	 */
	public RecordFileSettings(FileType fileType){
		
		this(fileType, ComSet.PLAIN, new AccessRights(), new Size(0), 0, 0);
		
	}
	
	/**
	 * Creates an instance of class <code>RecordFileSettings</code>
	 * @param size an instance of class <code>Size</code>
	 * representing the size of the file
	 * @param max an int representing the maximum number of records
	 * @param current an int representing the current number of records
	 */
	public RecordFileSettings(Size size, int max, int current){
		
		this(FileType.LINEAR_RECORD, ComSet.PLAIN, new AccessRights(), 
				size, 0, 0);
		
	}
	
	/**
	 * Creates an instance of class <code>RecordFileSettings</code>
	 * @param fileType an instance of class <code>FileType</code>
	 * representing the type of the file
	 * @param comSet an instance of class <code>ComSet</code> representing 
	 * the file's communication settings
	 * @param accessRights an instance of class <code>AccessRights</code> 
	 * representing the file's access rights
	 * @param size fileSize an instance of class <code>Size</code>
	 * representing the size of the file
	 * @param max an int representing the maximum number of records
	 * @param current an int representing the current number of records
	 */
	public RecordFileSettings(FileType fileType, ComSet comSet, 
			AccessRights accessRights, Size size, int max, int current){
		
		super(fileType, comSet, accessRights);
		
		if(size == null) throw new NullPointerException();
		
		setRecordSize(size);
		setMaxNumberOfRecords(max);
		setCurrentNumberOfRecords(current);
		
	}
	
	/**
	 * Creates an instance of class <code>RecordFileSettings</code>
	 * @param fileSet a byte array containing the file settings
	 */
	public RecordFileSettings(byte[] fileSet){
		
		super(fileSet);
		
		if(fileSet.length < 13) throw new IllegalArgumentException();
		
		byte[] sizeBA = BAUtils.extractSubBA(fileSet, 4, 3);
		byte[] maxBA = BAUtils.extractSubBA(fileSet, 7, 3);
		byte[] currentBA = BAUtils.extractSubBA(fileSet, 10, 3);

		setRecordSize(sizeBA);
		setMaxNumberOfRecords(maxBA);
		setCurrentNumberOfRecords(currentBA);
				
	}
	
	/**
	 * @return an instance of class <code>Size</code> representing the
	 * size of the records
	 */
	public Size getRecordSize(){
		
		return this.recordSize; 
		
    }
	
	/**
	 * @return an int representing the maximum number of records
	 */
	public int getMaxNumberOfRecords(){ 
		
		return this.maxNumberOfRecords; 
		
	}
	
	/**
	 * @return an int representing the current number of records
	 */
	public int getCurrentNumberOfRecords(){
		
		return this.currentNumberOfRecords;
		
	}
	
	/**
	 * @param size a byte array representing the new size of the records
	 */
	public void setRecordSize(byte[] size){
		
		if(size == null) throw new NullPointerException();
		if(size.length != 3) throw new IllegalArgumentException();
		
		setRecordSize(new Size(size));
		
	}
	
	/**
	 * @param size an instance of class <code>Size</code> representing 
	 * the new size of the records
	 */
	public void setRecordSize(Size size){
		
		if((size.getSize() < 0) || 
				(size.getSize() > DigitUtils.toInt("FFFFFF"))) 
			throw new IllegalArgumentException();
		
		this.recordSize = size;
		
	}
	
	/**
	 * @param max a byte array representing the maximum number of records
	 */
	public void setMaxNumberOfRecords(byte[] max){
		
		if(max == null) throw new NullPointerException();
		if(max.length != 3) throw new IllegalArgumentException();
		
		setMaxNumberOfRecords(BAUtils.toInt(max));
		
	}
	
	/**
	 * @param max an int representing the maximum number of records
	 */
	public void setMaxNumberOfRecords(int max){
		
		if((max < 0) || (max > DigitUtils.toInt("FFFFFF"))) 
			throw new IllegalArgumentException();
		if(max < this.getCurrentNumberOfRecords()) 
			throw new IllegalArgumentException();
		
		this.maxNumberOfRecords = max;
	
	}
	
	/**
	 * @param current an byte array representing the current number of records
	 */
	public void setCurrentNumberOfRecords(byte[] current){
		
		if(current == null) 
			throw new NullPointerException();
		if(current.length != 3) 
			throw new IllegalArgumentException();
		
		setCurrentNumberOfRecords(BAUtils.toInt(current));
		
	}
	
	/**
	 * @param current an int representing the current number of records
	 */
	public void setCurrentNumberOfRecords(int current){
		
		if((current < 0) || (current > DigitUtils.toInt("FFFFFF"))) 
			throw new IllegalArgumentException();
		if(current > this.getMaxNumberOfRecords()) 
			throw new IllegalArgumentException();
		
		this.currentNumberOfRecords = current;
		
	}
	
	@Override
	public byte[] toBA(){
		
		byte[] sizeBA = getRecordSize().toBA();
		byte[] maxBA = BAUtils.toBA(this.getMaxNumberOfRecords(), 3);
        byte[] currentBA = BAUtils.toBA(this.getCurrentNumberOfRecords(), 3);
 
        return BAUtils.concatenateBAs(super.toBA(), sizeBA, maxBA, currentBA);
		
	}
	
	@Override
	public String toString(){
		
		String s = super.toString() + "\n";
		
		s = s + "Record Size: " + this.getRecordSize() + "\n";
		s = s + "Max Number of Records: " + this.getMaxNumberOfRecords() + "\n";
		s = s +	"Current Number of Records: " + this.getCurrentNumberOfRecords();
				
		return s;
	}
	
	private Size recordSize;
    private int maxNumberOfRecords;
    private int currentNumberOfRecords;

}
