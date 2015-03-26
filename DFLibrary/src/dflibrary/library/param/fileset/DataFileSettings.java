package dflibrary.library.param.fileset;

import dflibrary.library.param.Size;
import dflibrary.utils.ba.*;

/**
 * 
 * @author Francisco Rodríguez Algarra
 *
 */
public class DataFileSettings extends FileSettings{

	
	/**
	 * 
	 * @param fileSize
	 */
	public DataFileSettings(){
		
		this(FileType.STANDARD_DATA, ComSet.PLAIN, new AccessRights(), new Size(0));
		
	}
	
	/**
	 * 
	 * @param fileSize
	 */
	public DataFileSettings(FileType fileType){
		
		this(fileType, ComSet.PLAIN, new AccessRights(), new Size(0));
		
	}
	
	
	/**
	 * 
	 * @param fileSize
	 */
	public DataFileSettings(Size fileSize){
		
		this(FileType.STANDARD_DATA, ComSet.PLAIN, new AccessRights(), fileSize);
		
	}
		
	
	/**
	 * 
	 * @param fileType
	 * @param comSet
	 * @param accessRights
	 * @param fileSize
	 */
	public DataFileSettings(FileType fileType, ComSet comSet, AccessRights accessRights, Size fileSize){
		
		super(fileType, comSet, accessRights);
		
		if(fileSize == null) throw new NullPointerException();
		setFileSize(fileSize);
		
	}
	
	
	/**
	 * 
	 * @param fileSet
	 */
	public DataFileSettings(byte[] fileSet){
		
		super(fileSet);
		if(fileSet.length < 7) throw new IllegalArgumentException();
		setFileSize(BAUtils.extractSubBA(fileSet, 4, 3));
		
	}

	
	/**
	 * 
	 * @return
	 */
	public Size getFileSize(){
		
		return this.fileSize;
		
	}
	
	/**
	 * 
	 * @param fileSize
	 */
	public void setFileSize(byte[] fileSize){
		
		if(fileSize == null) throw new NullPointerException();
		if(fileSize.length != 3) throw new IllegalArgumentException();
		
		setFileSize(new Size(fileSize));
		
	}
	
	/**
	 * 
	 * @param fileSize
	 */
	public void setFileSize(Size fileSize){
		
		if(fileSize == null) throw new NullPointerException();
		
		this.fileSize = fileSize;
		
	}
	
	/**
	 * 
	 */
	public byte[] toBA(){
		
		byte[] fileSizeBA = getFileSize().toBA();
		
		return BAUtils.concatenateBAs(super.toBA(), fileSizeBA);
		
	}
	
	/**
	 * 
	 */
	public String toString(){
		
		String s = super.toString() + "\n";
				
		s = s +	"File Size: " + this.getFileSize();
		
		return s;
		
	}
	
	private Size fileSize;
	
}
