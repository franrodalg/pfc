package dflibrary.samples.comtester;

import dflibrary.library.*;
import dflibrary.middleware.*;


public class SimplestTest {
	
	public static void main(String[] args){		
		
		JPCSCComManager cm = new JPCSCComManager();		
		try{
				cm.scan();				
				String[] readers = cm.listReaders();				
				cm.select(readers[0]);
				
				if(cm.isCardPresent()){					
					cm.connect();					
					CardType ct = cm.getCardType();			
					if(ct == CardType.MIFARE_DESFIRE){										
						DFCard df = new DFCard(cm);						
						System.out.println(df.getKeyVersion(0));												
					}										
					cm.disconnect();									
				}					
				cm.deselect();				
			}catch(DFLException e){	e.printStackTrace();}
			try{
				cm.release();
			}
			catch(DFLException e){e.printStackTrace();}
	}	
}
