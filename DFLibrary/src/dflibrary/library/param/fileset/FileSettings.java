package dflibrary.library.param.fileset;

import dflibrary.utils.ba.BAUtils;


/**
 * 
 * @author Francisco Rodríguez Algarra
 *
 */
public class FileSettings {

	/**
	 * 
	 */
	public FileSettings(){
		
		this.fileType = FileType.STANDARD_DATA;
		this.comSet = ComSet.PLAIN;
		this.accessRights = new AccessRights();
		
	}	
	
	/**
	 * 
	 * @param fileType
	 * @param comSet
	 * @param accessRights
	 */
	public FileSettings(FileType fileType, ComSet comSet, AccessRights accessRights){
		
		if((fileType == null) || (comSet == null) || (accessRights == null)) throw new NullPointerException();
		
		this.fileType = fileType;
		this.comSet = comSet;
		this.accessRights = accessRights;
		
	}
	
	/**
	 * 
	 * @param fileSet
	 */
	public FileSettings(byte[] fileSet){
		
		if(fileSet == null) throw new NullPointerException();
		if(fileSet.length < 4) throw new IllegalArgumentException(); 
		
		setFileType(BAUtils.extractSubBA(fileSet, 0, 1));
		setComSet(BAUtils.extractSubBA(fileSet, 1, 1));
		setAccessRights(BAUtils.extractSubBA(fileSet, 2, 2));
		
	}
	
	/**
	 * 
	 * @param fileType
	 * @param comSet
	 * @param accessRights
	 */
	public FileSettings(byte[] fileType, byte[] comSet, byte[] accessRights){
		
		setFileType(fileType);
		setComSet(comSet);
		setAccessRights(accessRights);
		
	}
	
	/**
	 * 
	 * @return
	 */
	public FileType getFileType(){
		
		return this.fileType;
		
	}
	
	/**
	 * 
	 * @return
	 */
	public ComSet getComSet(){
		
		return this.comSet;
		
	}
	
	/**
	 * 
	 * @return
	 */
	public AccessRights getAccessRights(){
		
		return this.accessRights;
		
	}
	
	/**
	 * 
	 * @param fileType
	 */
	public void setFileType(FileType fileType){
		
		if(fileType == null) throw new NullPointerException();
		
		this.fileType = fileType;
		
	}
	
	/**
	 * 
	 * @param fileType
	 */
	public void setFileType(int fileType){
		
		this.fileType = FileType.toFileType(fileType);
		
	}

	/**
	 * 
	 * @param fileType
	 */
	public void setFileType(byte[] fileType){
		
		if(fileType == null) throw new NullPointerException();
	
		this.fileType = FileType.toFileType(fileType);
	
	}
	
	/**
	 * 
	 * @param comSet
	 */
	public void setComSet(ComSet comSet){
		
		if(comSet == null) throw new NullPointerException();
		
		this.comSet = comSet;
		
	}
	
	/**
	 * 
	 * @param comSet
	 */
	public void setComSet(int comSet){
		
		this.comSet = ComSet.toComSet(comSet);
		
	}
	
	/**
	 * 
	 * @param comSet
	 */
	public void setComSet(byte[] comSet){
		
		if(comSet == null) throw new NullPointerException();
		
		this.comSet = ComSet.toComSet(comSet);
		
	}
	
	/**
	 * 
	 * @param accessRights
	 */
	public void setAccessRights(AccessRights accessRights){
		
		if(accessRights == null) throw new NullPointerException();
		
		this.accessRights = accessRights;
		
	}
	
	/**
	 * 
	 * @param readAccess
	 * @param writeAccess
	 * @param readWriteAccess
	 * @param changeAccessRights
	 */
	public void setAccessRights(int readAccess, int writeAccess, int readWriteAccess, int changeAccessRights){
		
		this.accessRights = new AccessRights(readAccess, writeAccess, readWriteAccess, changeAccessRights);
		
	}
	
	/**
	 * 
	 * @param accessRights
	 */
	public void setAccessRights(byte[] accessRights){
		
		if(accessRights == null) throw new NullPointerException();
		
		this.accessRights = new AccessRights(accessRights);
		
	}
	
	/**
	 * 
	 * @return
	 */
	public byte[] toBA(){
		
		byte[] fileTypeBA = this.getFileType().toBA();
		byte[] comSetBA = this.getComSet().toBA();
		byte[] accessRightsBA = this.getAccessRights().toBA();
		
		return BAUtils.concatenateBAs(fileTypeBA, comSetBA, accessRightsBA);
		
	}
	
	/**
	 * 
	 */
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
