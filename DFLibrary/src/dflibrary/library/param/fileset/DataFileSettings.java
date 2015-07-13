package dflibrary.library.param.fileset;

import dflibrary.library.param.Size;
import dflibrary.utils.ba.*;

/**
 * Provides an encapsulation for the file settings of data files
 * @author Francisco Rodríguez Algarra
 */
public class DataFileSettings extends FileSettings{

	/**
	 * Creates a default instance of class <code>DataFileSettings</code>
	 */
	public DataFileSettings(){
		
		this(FileType.STANDARD_DATA, ComSet.PLAIN, 
				new AccessRights(), new Size(0));
		
	}
	
	/**
	 * Creates an instance of class <code>DataFileSettings</code>
	 * @param fileType an instance of class <code>FileType</code>
	 * representing the type of the file
	 */
	public DataFileSettings(FileType fileType){
		
		this(fileType, ComSet.PLAIN, 
				new AccessRights(), new Size(0));
		
	}
	
	/**
	 * Creates an instance of class <code>DataFileSettings</code>
	 * @param size an instance of class <code>Size</code>
	 * representing the size of the file
	 */
	public DataFileSettings(Size size){
		
		this(FileType.STANDARD_DATA, ComSet.PLAIN, 
				new AccessRights(), size);
		
	}
		
	/**
	 * Creates an instance of class <code>DataFileSettings</code>
	 * @param fileType an instance of class <code>FileType</code>
	 * representing the type of the file
	 * @param comSet an instance of class <code>ComSet</code> representing 
	 * the file's communication settings
	 * @param accessRights an instance of class <code>AccessRights</code> 
	 * representing the file's access rights
	 * @param size fileSize an instance of class <code>Size</code>
	 * representing the size of the file
	 */
	public DataFileSettings(FileType fileType, ComSet comSet, 
			AccessRights accessRights, Size size){
		
		super(fileType, comSet, accessRights);
		
		if(size == null) throw new NullPointerException();
		setFileSize(size);
		
	}
	
	/**
	 * Creates an instance of class <code>DataFileSettings</code>
	 * @param fileSet a byte array containing the file settings
	 */
	public DataFileSettings(byte[] fileSet){
		
		super(fileSet);
		if(fileSet.length < 7) throw new IllegalArgumentException();
		setFileSize(BAUtils.extractSubBA(fileSet, 4, 3));
		
	}

	/**
	 * @return an instance of class <code>Size</code> representing the current
	 * size of the file
	 */
	public Size getFileSize(){
		
		return this.size;
		
	}
	
	/**
	 * @param size a byte array representing the current
	 * size of the file
	 */
	public void setFileSize(byte[] size){
		
		if(size == null) throw new NullPointerException();
		if(size.length != 3) throw new IllegalArgumentException();
		
		setFileSize(new Size(size));
		
	}
	
	/**
	 * @param size an instance of class <code>Size</code> representing the new
	 * size of the file
	 */
	public void setFileSize(Size size){
		
		if(size == null) throw new NullPointerException();
		
		this.size = size;
		
	}
	
	@Override
	public byte[] toBA(){
		
		byte[] fileSizeBA = getFileSize().toBA();
		
		return BAUtils.concatenateBAs(super.toBA(), fileSizeBA);
		
	}
	
	@Override
	public String toString(){
		
		String s = super.toString() + "\n";
				
		s = s +	"File Size: " + this.getFileSize();
		
		return s;
		
	}
	
	private Size size;
	
}
