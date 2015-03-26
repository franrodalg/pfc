package dflibrary.samples.wallet;

import java.io.*;
import java.net.*;
import java.util.*;

import dflibrary.middleware.RemoteComManager;
import dflibrary.utils.ba.BAUtils;
import dflibrary.utils.security.CipAlg;
import dflibrary.library.*;
import dflibrary.library.param.AID;
import dflibrary.library.param.FID;
import dflibrary.library.param.Value;
import dflibrary.library.param.fileset.ValueFileSettings;
import dflibrary.library.security.DFKey;


public class BankServer {

	public static void main(String[] args){
		
		try{
			
			int i = 1;
			
			ServerSocket s = new ServerSocket(BANKID);
			
			while(true){
				
				Socket inSock = s.accept();
				System.out.println("Generating handler number " + i);
				Runnable r = new BankHandler(inSock, i);
				Thread t = new Thread(r);
				t.start();
				i++;
				
			}
			
		}catch(IOException e){e.printStackTrace();}
		
	}
	
	public static int getBankID(){ return BANKID;}
	
	public static final int BANKID = 8189;
	
}

class BankHandler implements Runnable{
	
	public BankHandler(Socket s, int c){
		
		this.s = s;
		this.count = c;
	}
	
	public void run(){
		
		try{
			try{
				
				InputStream inS = s.getInputStream();
				OutputStream outS = s.getOutputStream();
				
				Scanner in = new Scanner(inS);
				PrintWriter out = new PrintWriter(outS, true);
				
				RemoteComManager cm = new RemoteComManager(in, out);
				
				DFCard df = new DFCard(cm);
				DFResponse res;
				
				
				boolean ended = false;
				
				while((!ended) && (in.hasNextLine())){
					
					String line = in.nextLine();
					
					//Determines whether the connected card needs Legacy mode
					
					if(line.trim().endsWith("true")){
						this.legacy = true;
						line = line.trim().substring(0, line.trim().length() - "true".length());
					}
					else{
						this.legacy = false;
						line = line.trim().substring(0, line.trim().length() - "false".length());
					}
					
					//Determines the demanded application
					
					if(line.trim().startsWith("BALANCE")){
						
						out.println(balanceCheck(df));
						ended = true;
						break;
					}
					else if(line.trim().startsWith("CREDIT")){
						
						int credit = Integer.parseInt(line.trim().substring("CREDIT ".length()));
						out.println(refilling(df, credit));
					}
					else  if(line.trim().startsWith("DEBIT")){
						
						int debit = Integer.parseInt(line.trim().substring("DEBIT ".length()));
						out.println(pay(df, debit));
					}
					else{
						ended = true;			
					}
					
				}
				
				
			}
			finally{
				
				s.close();
				System.out.println("Exiting handler " + count);
				
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		
	}

	/**
	 * 
	 * @param df
	 * @return
	 */
	private String balanceCheck(DFCard df){
		
		
		int ba = 0;
		DFResponse res;
		
		//Selecting the Wallet application in the card
		
		res = df.selectApplication(new AID(WTModel.WALLET_AID));
		if(!res.isOk())	return "ERROR: AID";
		
		//Obtaining Wallet File Settings
		
		res = df.getFileSettings(new FID(WTModel.WALL_WALLET_FID));
		if(!res.isOk())	return "ERROR: FID";
		
	    //Obtaining Read & Write Key from the File Settings
		
		ValueFileSettings fileSet = (ValueFileSettings) res.getFileSettings();
		
		int keyNum = fileSet.getAccessRights().getReadWriteAccess();
		
		// Obtaining the key version of the Read & Write Key
		
		res = df.getKeyVersion(keyNum);
		if(!res.isOk()) return "ERROR: KEY";
		
	    int keyVersion = res.getKeyVersion().toInt();
		
	    //Obtaining the key bytes for the specified key number and version
	    
		DFKey key;	
		CipAlg alg;
		
        alg = getKeyAlg();
			
		key = BankKeyProvider.getKey(keyVersion, alg, BankServer.getBankID());

		//Authenticating with the specified key
		
		if(legacy){ 
			res = df.authenticate(keyNum, key);
		}		
		else{
			res = df.authenticateISO(keyNum, key);
		}
		
		if(!res.isOk()) return "ERROR: AUTHENTICATION";
		
		//Getting the balance from the Wallet File
		
	    res = df.getValue(new FID(WTModel.WALL_WALLET_FID), fileSet);
	    if(!res.isOk()) return "ERROR: VALUE";
		
	    ba = res.getValueRes().getValue().getValue();
	    
		return "SUCCESS: " + ba; 
	}
	
	/**
	 * 
	 * @param df
	 * @param credit
	 * @return
	 */
	private String refilling(DFCard df, int credit){
		
		//Getting the current balance of the Wallet
		
		String br = balanceCheck(df);
		
		if(!br.trim().startsWith("SUCCESS")) return br;
		
		int ib = Integer.parseInt(br.trim().substring("SUCCESS: ".length()));
		
		//Checking if the refilling amount requested is over the maximum allowed
		
		if((ib + credit) > 1000) return "ERROR: CREDIT " + (1000 - ib);
		
		DFResponse res; 
		
		//Obtaining Wallet File Settings
		
		res = df.getFileSettings(new FID(WTModel.WALL_WALLET_FID));
		if(!res.isOk())	return "ERROR: FID";
		
		ValueFileSettings fileSet = (ValueFileSettings) res.getFileSettings();
		
		//Performing credit operation to increase the balance of the Wallet
		
		res = df.credit(new FID(WTModel.WALL_WALLET_FID), new Value(credit), fileSet);
		if(!res.isOk())	return "ERROR: CREDIT";

		res = df.commitTransaction();
		if(!res.isOk())	return "ERROR: CREDIT";
		
		return "SUCCESS";
		
	}
	
	/**
	 * 
	 * @param df
	 * @param debit
	 * @return
	 */
	private String pay(DFCard df, int debit){
		
		//Getting the current balance of the Wallet
		
		String br = balanceCheck(df);
		
		if(!br.trim().startsWith("SUCCESS")) return br;
		
		int ib = Integer.parseInt(br.trim().substring("SUCCESS: ".length()));
		
		//Checking if current balance is below the quantity requested
		
		if(ib < debit) return "ERROR: DEBIT " + ib;
//		
		DFResponse res; 
		
		//Obtaining Wallet File Settings
		
		res = df.getFileSettings(new FID(WTModel.WALL_WALLET_FID));
		if(!res.isOk())	return "ERROR: FID";
		
		ValueFileSettings fileSet = (ValueFileSettings) res.getFileSettings();
		
//		//Performing debit operation to decrease the balance of the Wallet
	
		res = df.debit(new FID(WTModel.WALL_WALLET_FID), new Value(debit), fileSet);
		if(!res.isOk())	return "ERROR: DEBIT";
		
		res = df.commitTransaction();
		if(!res.isOk())	return "ERROR: DEBIT";
		
		return "SUCCESS";
		
	}
	
	
	/**
	 * 
	 * @param aid
	 * @return
	 */
	private CipAlg getKeyAlg(){
		
		if(legacy) {
			return CipAlg.TDEA2; } 
		else {
			return WTModel.WALL_ALG;
		}
		
	}
	
	boolean legacy;
	
	private Socket s;
	private int count;
}