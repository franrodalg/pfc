package dflibrary.library.param.fileset;

import dflibrary.utils.ba.BAUtils;

/**
 * Provides an encapsulation for the generic file settings
 * @author Francisco Rodriguez Algarra
 */
public class FileSettings {

	/**
	 * Creates a default instance of class <code>FileSettings</code>
	 */
	public FileSettings(){
		
		this.fileType = FileType.STANDARD_DATA;
		this.comSet = ComSet.PLAIN;
		this.accessRights = new AccessRights();
		
	}	
	
	/**
	 * Creates an instance of class <code>FileSettings</code>
	 * @param fileType an instance of class <code>FileType</code> representing 
	 * the type of the current file
	 * @param comSet an instance of class <code>ComSet</code> representing 
	 * the file's communication settings
	 * @param accessRights an instance of class <code>AccessRights</code> 
	 * representing the file's access rights
	 */
	public FileSettings(FileType fileType, ComSet comSet, 
			AccessRights accessRights){
		
		if((fileType == null) || (comSet == null) || (accessRights == null)) 
			throw new NullPointerException();
		
		this.fileType = fileType;
		this.comSet = comSet;
		this.accessRights = accessRights;
		
	}
	
	/**
	 * Creates an instance of class <code>FileSettings</code>
	 * @param fileSet a byte array containing the file settings
	 */
	public FileSettings(byte[] fileSet){
		
		if(fileSet == null) throw new NullPointerException();
		if(fileSet.length < 4) throw new IllegalArgumentException(); 
		
		setFileType(BAUtils.extractSubBA(fileSet, 0, 1));
		setComSet(BAUtils.extractSubBA(fileSet, 1, 1));
		setAccessRights(BAUtils.extractSubBA(fileSet, 2, 2));
		
	}
	
	/**
	 * Creates an instance of class <code>FileSettings</code>
	 * @param fileType a byte array representing 
	 * the type of the current file
	 * @param comSet a byte array representing 
	 * the file's communication settings
	 * @param accessRights a byte array 
	 * representing the file's access rights
	 */
	public FileSettings(byte[] fileType, byte[] comSet, byte[] accessRights){
		
		setFileType(fileType);
		setComSet(comSet);
		setAccessRights(accessRights);
		
	}
	
	/**
	 * @return an instance of class <code>FileType</code> representing 
	 * the type of the current file
	 */
	public FileType getFileType(){
		
		return this.fileType;
		
	}
	
	/**
	 * @return an instance of class <code>ComSet</code> representing 
	 * the file's communication settings
	 */
	public ComSet getComSet(){
		
		return this.comSet;
		
	}
	
	/**
	 * @return an instance of class <code>AccessRights</code> 
	 * representing the file's access rights
	 */
	public AccessRights getAccessRights(){
		
		return this.accessRights;
		
	}
	
	/**
	 * @param fileType an instance of class <code>FileType</code> representing 
	 * the new type of the current file
	 */
	public void setFileType(FileType fileType){
		
		if(fileType == null) throw new NullPointerException();
		
		this.fileType = fileType;
		
	}
	
	/**
	 * @param fileType a int representing 
	 * the new type of the current file
	 */
	public void setFileType(int fileType){
		
		this.fileType = FileType.toFileType(fileType);
		
	}

	/**
	 * @param fileType a byte array representing 
	 * the new type of the current file
	 */
	public void setFileType(byte[] fileType){
		
		if(fileType == null) throw new NullPointerException();
	
		this.fileType = FileType.toFileType(fileType);
	
	}
	
	/**
	 * @param comSet an instance of class <code>ComSet</code> representing 
	 * the file's new communication settings
	 */
	public void setComSet(ComSet comSet){
		
		if(comSet == null) throw new NullPointerException();
		
		this.comSet = comSet;
		
	}
	
	/**
	 * @param comSet an int representing 
	 * the file's new communication settings
	 */
	public void setComSet(int comSet){
		
		this.comSet = ComSet.toComSet(comSet);
		
	}
	
	/**
	 * @param comSet a byte array representing 
	 * the file's new communication settings
	 */
	public void setComSet(byte[] comSet){
		
		if(comSet == null) throw new NullPointerException();
		
		this.comSet = ComSet.toComSet(comSet);
		
	}
	
	/**
	 * @param accessRights an instance of class <code>AccessRights</code> 
	 * representing the file's new access rights
	 */
	public void setAccessRights(AccessRights accessRights){
		
		if(accessRights == null) throw new NullPointerException();
		
		this.accessRights = accessRights;
		
	}
	
	/**
	 * @param accessRights a byte array
	 * representing the file's new access rights
	 */
	public void setAccessRights(byte[] accessRights){
		
		if(accessRights == null) throw new NullPointerException();
		
		this.accessRights = new AccessRights(accessRights);
		
	}
	
	/**
	 * @return the byte array representation of the current file settings
	 */
	public byte[] toBA(){
		
		byte[] fileTypeBA = this.getFileType().toBA();
		byte[] comSetBA = this.getComSet().toBA();
		byte[] accessRightsBA = this.getAccessRights().toBA();
		
		return BAUtils.concatenateBAs(fileTypeBA, comSetBA, accessRightsBA);
		
	}
	
	@Override
	public String toString(){
		
		String s = "";
		
		s =	s + "File Type: " +	this.fileType.toString() + "\n";
		s = s + "Communication settings: " + this.comSet.toString() + "\n";
		s = s +	"Access Rights: \n" + this.getAccessRights().toString();
		
		return s;
	}
	
	private FileType fileType;
	private ComSet comSet;
	private AccessRights accessRights;
	
}
