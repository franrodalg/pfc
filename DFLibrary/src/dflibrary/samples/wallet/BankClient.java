package dflibrary.samples.wallet;

import java.io.*;
import java.net.*;
import java.util.*;

import dflibrary.library.*;
import dflibrary.middleware.*;
import dflibrary.utils.ba.BAUtils;

public class BankClient {
	
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
						
						System.out.println(df.getApplicationIDs());
						
						run(cm);
							
		
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
	
	private static void run(ComManager cm){
		
		try{	
			
			System.out.println("Connecting to the server...");
			
			Socket s = new Socket("Localhost", PORT);
			
				
			InputStream inS = s.getInputStream();
			OutputStream outS = s.getOutputStream();
				
			try{
				
				Scanner in = new Scanner(inS);
				PrintWriter out = new PrintWriter(outS, true);
				
				boolean ended = false;
				
				if(in.hasNextLine()){
					String line = in.nextLine();
					System.out.println(line);
					byte[] res = cm.send(BAUtils.toBA(line));
					out.println(BAUtils.toString(res));
				}
				
				out.println("CONSULT");
				
				while((!ended) && (in.hasNextLine())){
					String line = in.nextLine();
					System.out.println(line);
					if(line.trim().equals("EXIT")) ended = true;
					else{
						byte[] res = cm.send(BAUtils.toBA(line));
						out.println(BAUtils.toString(res));
						
					}
				}
			
				}
				finally{ s.close(); System.out.println("Connection closed by remote server");}
			}
		catch(IOException e){ e.printStackTrace(); }
		//catch(InterruptedException e){}
		
	}

	
	public static final int PORT = 8189;
}