package dflibrary.library.param;

import dflibrary.utils.ba.BAUtils;

/**
 * 
 * @author Francisco Rodríguez Algarra
 *
 */
public class RecordsRes {
	
	/**
	 * 
	 * @param records
	 * @param recSize
	 * @param checked
	 */
	public RecordsRes(byte[] records, Size recSize, boolean checked){
	
		
		if((records == null) || (recSize == null))
			throw new NullPointerException();
		
		if((records.length % recSize.getSize()) != 0)
			throw new IllegalArgumentException();
		
		int numOfRecords = records.length / recSize.getSize();
		
		this.records = new Data[numOfRecords];
		
		for(int i = 0; i < numOfRecords; i ++){
			
			this.records[i] = new Data(BAUtils.extractSubBA(records, i*recSize.getSize(), recSize.getSize()));
			
		}
		
		this.isChecked = checked;
		
	}
	
	/**
	 * 
	 * @return
	 */
	public Data[] getRecords(){ return this.records; }
	
	/**
	 * 
	 * @return
	 */
	public boolean isChecked(){ return this.isChecked; }
	
	/**
	 * 
	 */
	public String toString(){
		
		String s =  "Records: \n"; 
		for(int i = 0; i < this.records.length; i++){
			s = s + i + ".- " + this.records[i] + "\n";
		}
		if(!isChecked) s = s + "\nCaution: those Records haven't passed an integrity test";
		return s;
		
	}
	
	
	private boolean isChecked;
	private Data[] records;
	
}
