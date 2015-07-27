package dflibrary.samples.comtester;

import dflibrary.library.*;
import dflibrary.middleware.*;
import dflibrary.utils.ba.BAUtils;

import java.util.Arrays;

public class SimplestTestJPCSC {
	
	public static void main(String[] args){		
		
		ComManager cm = new JPCSCComManager();		
		try{
			cm.scan();				
			String[] readers = cm.listReaders();
			System.out.println(Arrays.toString(readers));
			cm.select(readers[0]);
			
			if(cm.isCardPresent()){					
				cm.connect();					
				CardType ct = cm.getCardType();			
				if(ct == CardType.MIFARE_DESFIRE){
					System.out.println("Card identified as Mifare DESFire");		
					System.out.println("****");
					DFCard df = new DFCard(cm);						
					System.out.println(BAUtils.toString(df.GetVersion()));
					System.out.println("****");
					System.out.println(df.getVersion());											
				}										
				cm.disconnect();									
			}
			else{
				System.out.println("No card present");
			}
			cm.deselect();				
		}catch(DFLException e){	e.printStackTrace();}
		try{
			cm.release();
		}
		catch(DFLException e){e.printStackTrace();}
	}	
}
