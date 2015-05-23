package dflibrary.samples.wallet;

/**
 * When executed, generates as many instances of <code>BankServer</code> 
 * as entries exist in the Banking Services Database. It sets each one in
 * a port corresponding to its own bankID.
 * @author Francisco Rodriguez Algarra
 *
 */
public class BankServers {

	public static void main(String[] args){
			
		Bank[] banks = getBanks();
		
		for (int i = 0; i < banks.length; i++){
			int bankID = banks[i].getBankID();
			System.out.println("Generating Bank Server with ID number " + bankID);
			Runnable r = new BankServer(bankID);
			Thread t = new Thread(r);
			t.start();
		}
		
	}
	
	/**
	 * Obtains information about banks from the database
	 * @return an array containing the banks stored in the database
	 */
	private static Bank[] getBanks(){
		
		BankDBManager bdb = new BankDBManager();
	 
		bdb.connect();
		Bank[] banks = bdb.getBanks();
		bdb.disconnect();
		
		return banks;
		
	}
}
