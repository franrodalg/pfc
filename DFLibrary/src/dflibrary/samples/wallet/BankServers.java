package dflibrary.samples.wallet;

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
	 * 
	 * @return
	 */
	private static Bank[] getBanks(){
		
		BankDBManager bdb = new BankDBManager();
	 
		bdb.connect();
		Bank[] banks = bdb.getBanks();
		bdb.disconnect();
		
		return banks;
		
	}
}
