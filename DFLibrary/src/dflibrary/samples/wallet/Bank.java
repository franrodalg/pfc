package dflibrary.samples.wallet;

/**
 * 
 * @author Francisco Rodríguez Algarra
 *
 */
public class Bank {

	/**
	 * 
	 * @param bank_id
	 */
	public Bank(int bank_id){
		
		this.bank_id = bank_id;
		this.bank_name = "";
		
	}
	
	/**
	 * 
	 * @param bank_id
	 * @param bank_name
	 */
	public Bank(int bank_id, String bank_name){
		this.bank_id = bank_id;
		this.bank_name = bank_name;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getBankID(){ return this.bank_id; }
	
	/**
	 * 
	 * @return
	 */
	public String getBankName(){ return this.bank_name; }
	
	/**
	 * 
	 */
	public String toString(){ return "" + this.bank_id + ".- " + this.bank_name; }
	
	private int bank_id;
	private String bank_name;
	
}
