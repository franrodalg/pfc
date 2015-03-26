package dflibrary.samples.comtester;

import dflibrary.library.*;
import dflibrary.library.DFLException.ExType;
import dflibrary.library.param.*;
import dflibrary.library.param.fileset.*;
import dflibrary.library.security.AuthType;
import dflibrary.library.security.DFKey;
import dflibrary.middleware.*;
import dflibrary.utils.ba.*;
import dflibrary.utils.security.*;

public class ComTester {

	
	public static void main(String[] args){		
		
		JPCSCComManager cm = new JPCSCComManager();
		
		try{
				cm.scan();
				
				System.out.println("Context scan complete. Use method listReaders() to view connected readers");
				System.out.println("");
				
				String[] readers = cm.listReaders();
				
				System.out.println("Connected readers");
				for(int i = 0; i < readers.length; i++){
					System.out.println(i + ".- " + readers[i]);
				}
				System.out.println("");
				
				System.out.println("Selecting reader 0...");
				cm.select(readers[0]);
				
				System.out.println("Reader successfully selected");
				System.out.println("");
				
				if(cm.isCardPresent()){
					System.out.println("Card present in the reader. Trying to connect");
					System.out.println("");
					
					System.out.println("Connecting card...");
					cm.connect();
					System.out.println("Card successfully connected");
					System.out.println("");
					
					System.out.println("Card Type: ");
					CardType ct = cm.getCardType();
					System.out.println(ct);
					System.out.println("");
				
					if(ct == CardType.MIFARE_DESFIRE){
					
					
						DFCard df = new DFCard(cm);
						DFResponse dfres;
					
						System.out.println("Trying to send commands:\n\n");
						
						//AUTHENTICATE
						
						byte[] keyData;
						
						System.out.println(df.getSession());						
						
						
						try{
							

						System.out.println("Get Version:");
						
						dfres = df.getVersion();
						System.out.println(dfres.getUID());
						System.out.println("");
						
						/*	
							
						System.out.println("Authenticate:");
						keyData = new byte[16];
						System.out.println(df.authenticate(0, keyData));
						System.out.println(df.getSession());
						System.out.println("");						
						*/
												
						//CREATE APPLICATION
			
						KeySettings ks = new KeySettings();
						CipAlg alg = CipAlg.TDEA3;
						
						
						System.out.println("Create Application:");
						System.out.println(df.createApplication(new AID(5), ks, 1, false, alg));				
						System.out.println(df.getSession());
						System.out.println("");
						
						
						
						
						//SELECT APPLICATION
						
						
						/*
						System.out.println("Select Application 1:");
						System.out.println(df.selectApplication(new AID(1)));
						System.out.println(df.getSession());
						System.out.println("");
						*/
						
						System.out.println("Get Key Settings: ");
						System.out.println(df.getKeySettings());
						System.out.println(df.getSession());
						System.out.println("");
						
						System.out.println("Delete Application: ");
						System.out.println(df.deleteApplication(new AID(5)));
						System.out.println(df.getSession());
						System.out.println("");
						
						
						/*
						DFKey key;
						//DFKey nKey;
						
						if(alg == CipAlg.TDEA3){
							key = new DFKey(new byte[24], alg);
							//nKey = new DFKey(BAUtils.toBA("01112131415161718191A1B1C1D1E1F1F1E1D1C1B1A19181"), alg, 1);
							//nKey = new DFKey(new byte[24], alg, 25);
							//nKey = new DFKey(BAUtils.concatenateBAs(new byte[8], new byte[7], BAUtils.toBA("10"), new byte[7], BAUtils.toBA("20")), alg, 25);
						}
						else{
							key = new DFKey(new byte[16], alg, 0);
							//nKey = new DFKey(new byte[26], alg, 1);
						}
						System.out.println("Authentication: ");
						if(alg == CipAlg.TDEA3){
							System.out.println(df.authenticateISO(0, key));
						}
						else{
							System.out.println(df.authenticateAES(0, key));
						}
						System.out.println(df.getSession());
						System.out.println("");
						
						*/
						
						/*
						System.out.println("Changing Key: ");					
						System.out.println(df.changeKey(0, nKey, key));
						System.out.println(df.getSession());
						System.out.println("");
						*/
						
						/*
						System.out.println("Get Key Version: ");					
						System.out.println(df.getKeyVersion(0));
						System.out.println(df.getSession());
						System.out.println("");
						*/
						/*
						System.out.println("Authentication: ");
						if(alg == CipAlg.TDEA3){
							System.out.println(df.authenticateISO(0, nKey));
						}
						else{
							System.out.println(df.authenticateAES(0, nKey));
						}
						System.out.println(df.getSession());
						System.out.println("");
						*/
						/*
						
						System.out.println("Authenticate:");
						keyData = new byte[16];
						System.out.println(df.authenticateISO(0, keyData));
						System.out.println(df.getSession());
						System.out.println("");

						
						System.out.println("Get ISO File IDs:");
						System.out.println(df.getISOFileIDs());
						System.out.println(df.getSession());
						System.out.println("");
						
						*/
						
						/*
						
						FID fid = new FID(1);
						ISOFileID isoFileID = new ISOFileID(3);
						Size size = new Size(60);
						
						Value lowLimit = new Value(-1);
						Value upLimit = new Value(2);
						Value value = new Value(1);
						
						
						ComSet comSet = ComSet.MAC;
						AccessRights ar = new AccessRights();
						boolean getFreeValue = false;
						
						AuthType auth = AuthType.TDEA_NATIVE;
						int authKey = 1;
						
						ar.setReadAccess(2);
						//ar.setReadAccess(AccessRights.FREE);
						//ar.setReadAccess(AccessRights.DENY);
						
						//ar.setReadWriteAccess(1);
						//ar.setReadWriteAccess(AccessRights.FREE);
						//ar.setReadWriteAccess(AccessRights.DENY);
						
						ar.setWriteAccess(1);
						//ar.setWriteAccess(AccessRights.FREE);
						//ar.setWriteAccess(AccessRights.DENY);						
						
						*/
						/*
						
						//DATA FILES
						
						System.out.println("Create Backup Data File: ");
						System.out.println(df.createBackupDataFile(fid, comSet, ar, size));
						System.out.println(df.getSession());
						System.out.println("");
						
						System.out.println("Get File Settings:");
						dfres = df.getFileSettings(fid);
						System.out.println(dfres);
						System.out.println(df.getSession());
						System.out.println("");
						
						DataFileSettings fileSet = (DataFileSettings)dfres.getFileSettings();
						
						if(auth == AuthType.TDEA_NATIVE){
							
							System.out.println("Authenticate:");
							keyData = new byte[16];
							System.out.println(df.authenticate(authKey, keyData));
							System.out.println(df.getSession());
							System.out.println("");
						
						}
						else if(auth == AuthType.TDEA_STANDARD){
							
							System.out.println("Authenticate:");
							keyData = new byte[16];
							System.out.println(df.authenticateISO(authKey, keyData));
							System.out.println(df.getSession());
							System.out.println("");
							
							
						}
						
						Data data = new Data(new byte[56]);
						Size offset = new Size(0);
						
						System.out.println("Write Data: ");
						System.out.println(df.writeData(fid, offset, data, fileSet));
						System.out.println(df.getSession());
						System.out.println("");
						
						System.out.println("Commit Transaction: ");
						System.out.println(df.commitTransaction());
						System.out.println(df.getSession());
						System.out.println("");
						
						authKey = 2;
						
						if(auth == AuthType.TDEA_NATIVE){
							
							System.out.println("Authenticate:");
							keyData = new byte[16];
							System.out.println(df.authenticate(authKey, keyData));
							System.out.println(df.getSession());
							System.out.println("");
						
						}
						else if(auth == AuthType.TDEA_STANDARD){
							
							System.out.println("Authenticate:");
							keyData = new byte[16];
							System.out.println(df.authenticateISO(authKey, keyData));
							System.out.println(df.getSession());
							System.out.println("");
							
							
						}
						
						Size length = new Size(0);
						
						System.out.println("Read Data: ");
						System.out.println(df.readData(fid, offset, length, fileSet));
						System.out.println(df.getSession());
						System.out.println("");
						
						*/
						
						
						/*
						
						//RECORD FILES
						
						System.out.println("Create Linear Record File: ");
						System.out.println(df.createLinearRecordFile(fid, comSet, ar, size, 2));
						System.out.println(df.getSession());
						System.out.println("");
						
						System.out.println("Get File Settings:");
						dfres = df.getFileSettings(fid);
						System.out.println(dfres);
						System.out.println(df.getSession());
						System.out.println("");
						
						RecordFileSettings fileSet = (RecordFileSettings)dfres.getFileSettings();
						
						if(auth == AuthType.TDEA_NATIVE){
							
							System.out.println("Authenticate:");
							keyData = new byte[16];
							System.out.println(df.authenticate(authKey, keyData));
							System.out.println(df.getSession());
							System.out.println("");
						
						}
						else if(auth == AuthType.TDEA_STANDARD){
							
							System.out.println("Authenticate:");
							keyData = new byte[16];
							System.out.println(df.authenticateISO(authKey, keyData));
							System.out.println(df.getSession());
							System.out.println("");
							
							
						}
						
						Data data = new Data(BAUtils.toBA("010203"));
						Size offset = new Size(0);
						
						System.out.println("Write Record: ");
						System.out.println(df.writeRecord(fid, offset, data, fileSet));
						System.out.println(df.getSession());
						System.out.println("");
						
						System.out.println("Commit Transaction: ");
						System.out.println(df.commitTransaction());
						System.out.println(df.getSession());
						System.out.println("");
						
						System.out.println("Get File Settings:");
						dfres = df.getFileSettings(fid);
						System.out.println(dfres);
						System.out.println(df.getSession());
						System.out.println("");
						
						data = new Data(BAUtils.toBA("AABB"));
						offset = new Size(1);
						
						System.out.println("Write Record: ");
						System.out.println(df.writeRecord(fid, offset, data, fileSet));
						System.out.println(df.getSession());
						System.out.println("");
						
						System.out.println("Commit Transaction: ");
						System.out.println(df.commitTransaction());
						System.out.println(df.getSession());
						System.out.println("");
						
						System.out.println("Get File Settings:");
						dfres = df.getFileSettings(fid);
						System.out.println(dfres);
						System.out.println(df.getSession());
						System.out.println("");
						
						authKey = 2;
						
						if(auth == AuthType.TDEA_NATIVE){
							
							System.out.println("Authenticate:");
							keyData = new byte[16];
							System.out.println(df.authenticate(authKey, keyData));
							System.out.println(df.getSession());
							System.out.println("");
						
						}
						else if(auth == AuthType.TDEA_STANDARD){
							
							System.out.println("Authenticate:");
							keyData = new byte[16];
							System.out.println(df.authenticateISO(authKey, keyData));
							System.out.println(df.getSession());
							System.out.println("");
							
							
						}
						
						offset = new Size(1);
						Size length = new Size(0);
						
						System.out.println("Read Records: ");
						System.out.println(df.readRecords(fid, offset, length, fileSet));
						System.out.println(df.getSession());
						System.out.println("");
						
						*/
						
						/*
						
						//VALUE FILES
						
						System.out.println("Create Value File: ");
						System.out.println(df.createValueFile(fid, comSet, ar, lowLimit, upLimit, value, true, getFreeValue));
						System.out.println(df.getSession());
						System.out.println("");
						
						System.out.println("Get File Settings:");
						dfres = df.getFileSettings(fid);
						System.out.println(dfres);
						System.out.println(df.getSession());
						System.out.println("");
						
						ValueFileSettings fileSet = (ValueFileSettings)dfres.getFileSettings();
						
						if(auth == AuthType.TDEA_NATIVE){
						
							System.out.println("Authenticate:");
							keyData = new byte[16];
							System.out.println(df.authenticate(authKey, keyData));
							System.out.println(df.getSession());
							System.out.println("");
						
						}
						else if(auth == AuthType.TDEA_STANDARD){
							
							System.out.println("Authenticate:");
							keyData = new byte[16];
							System.out.println(df.authenticateISO(authKey, keyData));
							System.out.println(df.getSession());
							System.out.println("");
							
							
						}
						
						
						
						System.out.println("Get Value: ");
						System.out.println(df.getValue(fid, fileSet));
						System.out.println(df.getSession());
						System.out.println("");
						
						//CREDIT
						
						value = new Value(1);
						
						System.out.println("Credit: ");
						System.out.println(df.credit(fid, value, comSet, ar));
						System.out.println(df.getSession());
						System.out.println("");
						
						System.out.println("Commit Transaction: ");
						System.out.println(df.commitTransaction());
						System.out.println(df.getSession());
						System.out.println("");
						
						System.out.println("Get Value: ");
						System.out.println(df.getValue(fid, fileSet));
						System.out.println(df.getSession());
						System.out.println("");
						
						//DEBIT
						
						value = new Value(2);
						
						authKey = 2;

						if(auth == AuthType.TDEA_NATIVE){
						
							System.out.println("Authenticate:");
							keyData = new byte[16];
							System.out.println(df.authenticate(authKey, keyData));
							System.out.println(df.getSession());
							System.out.println("");
						
						}
						else if(auth == AuthType.TDEA_STANDARD){
							
							System.out.println("Authenticate:");
							keyData = new byte[16];
							System.out.println(df.authenticateISO(authKey, keyData));
							System.out.println(df.getSession());
							System.out.println("");
							
							
						}
						
						System.out.println("Debit: ");
						System.out.println(df.debit(fid, value, comSet, ar));
						System.out.println(df.getSession());
						System.out.println("");
						
						System.out.println("Commit Transaction: ");
						System.out.println(df.commitTransaction());
						System.out.println(df.getSession());
						System.out.println("");
						
						System.out.println("Get Value: ");
						System.out.println(df.getValue(fid, fileSet));
						System.out.println(df.getSession());
						System.out.println("");
						
						//LIMITED CREDIT
						
						value = new Value(1);
						
						authKey = 0;

						if(auth == AuthType.TDEA_NATIVE){
						
							System.out.println("Authenticate:");
							keyData = new byte[16];
							System.out.println(df.authenticate(authKey, keyData));
							System.out.println(df.getSession());
							System.out.println("");
						
						}
						else if(auth == AuthType.TDEA_STANDARD){
							
							System.out.println("Authenticate:");
							keyData = new byte[16];
							System.out.println(df.authenticateISO(authKey, keyData));
							System.out.println(df.getSession());
							System.out.println("");
							
							
						}
						
						System.out.println("Limited Credit: ");
						System.out.println(df.limitedCredit(fid, value, comSet, ar));
						System.out.println(df.getSession());
						System.out.println("");
						
						System.out.println("Commit Transaction: ");
						System.out.println(df.commitTransaction());
						System.out.println(df.getSession());
						System.out.println("");
						
						System.out.println("Get Value: ");
						System.out.println(df.getValue(fid, fileSet));
						System.out.println(df.getSession());
						System.out.println("");
						
						*/
						
											
						
						/*
						
						System.out.println("Create Cyclic Record File: ");
						System.out.println(df.createCyclicRecordFile(fid, isoFileID, comSet, ar, size, 2));
						System.out.println(df.getSession());
						System.out.println("");
						
						*/
						
						/*
						
						System.out.println("Get File IDs:");
						System.out.println(df.getFileIDs());
						System.out.println(df.getSession());
						System.out.println("");
						
						System.out.println("Get ISO File IDs:");
						System.out.println(df.getISOFileIDs());
						System.out.println(df.getSession());
						System.out.println("");
						
						System.out.println("Get File Settings:");
						System.out.println(df.getFileSettings(fid));
						System.out.println(df.getSession());
						System.out.println("");
						
						comSet = ComSet.PLAIN;
						
						System.out.println("Change File Settings:");
						System.out.println(df.changeFileSettings(fid, comSet, ar, ar));
						System.out.println(df.getSession());
						System.out.println("");
						
						System.out.println("Get File Settings:");
						System.out.println(df.getFileSettings(fid));
						System.out.println(df.getSession());
						System.out.println("");
						
						*/
						/*
						
						//CLEAR RECORD FILE
						
						System.out.println("Clear Record File:");
						System.out.println(df.clearRecordFile(fid));
						System.out.println(df.getSession());
						System.out.println("");
						
						System.out.println("Abort Transaction:");
						System.out.println(df.abortTransaction());
						System.out.println(df.getSession());
						System.out.println("");
						
						System.out.println("Clear Record File:");
						System.out.println(df.clearRecordFile(fid));
						System.out.println(df.getSession());
						System.out.println("");
						
						System.out.println("Commit Transaction:");
						System.out.println(df.commitTransaction());
						System.out.println(df.getSession());
						System.out.println("");
						
						System.out.println("Get File Settings:");
						System.out.println(df.getFileSettings(fid));
						System.out.println(df.getSession());
						System.out.println("");
						
						*/
						
						/*
						
						//DELETE FILE
												
						System.out.println("Delete file:");
						System.out.println(df.deleteFile(fid));
						System.out.println(df.getSession());
						System.out.println("");
						
						System.out.println("Get File IDs:");
						System.out.println(df.getFileIDs());
						System.out.println(df.getSession());
						System.out.println("");
						
						
						*/
						
						}catch(Exception e){e.printStackTrace();}
						
						/*
						
						System.out.println("Select Application 0:");
						System.out.println(df.selectApplication(new AID(0)));
						//System.out.println(df.getSession());
						System.out.println("");
						
						
						
						
						System.out.println("Authenticate:");
						keyData = new byte[16];
						System.out.println(df.authenticate(0, keyData));
						//System.out.println(df.getSession());
						System.out.println("");
						
						//FORMAT PICC
						
						
						System.out.println("Format PICC:");
						System.out.println(df.formatPICC());
						//System.out.println(df.getSession());
						System.out.println("");
						
						//GET APPLICATION IDS
						
						System.out.println("Get Application IDs:");
						System.out.println(df.getApplicationIDs());
						//System.out.println(df.getSession());
						System.out.println("");
						
						*/
					
					}
					
					System.out.println("Disconnecting card...");
					cm.disconnect();
					System.out.println("Card successfully disconnected");
					System.out.println("");				
					
				}
				else{
					System.out.println("Empty reader");
					System.out.println("");
				}				
				
				System.out.println("Deselecting reader...");
				cm.deselect();
				System.out.println("Reader successfully deselected");
				System.out.println("");
				
			}catch(DFLException e){
				
				e.printStackTrace();
				
			}
			try{
				cm.release();
				
				System.out.println("Context successfully released");
				System.out.println("");
			}
			catch(DFLException e){
				e.printStackTrace();
			}
	}
	
}
