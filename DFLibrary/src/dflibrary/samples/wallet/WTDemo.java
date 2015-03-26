package dflibrary.samples.wallet;

import java.awt.*;

public class WTDemo {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new WTView();					
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		});
	}
	
	
}
