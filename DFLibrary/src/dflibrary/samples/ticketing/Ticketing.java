package dflibrary.samples.ticketing;

import java.awt.*;

public class Ticketing {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new TicketingView();					
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		});
	}
	
	
}
