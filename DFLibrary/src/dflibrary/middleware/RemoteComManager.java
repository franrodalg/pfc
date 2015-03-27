package dflibrary.middleware;

import java.io.*;
import java.util.*;

import dflibrary.library.CardType;
import dflibrary.utils.ba.BAUtils;


public class RemoteComManager implements ComManager {

	public RemoteComManager(Scanner in, PrintWriter out){
		
		this.in = in;
		this.out = out;
	}
	
	@Override
	public void scan() {}

	@Override
	public String[] listReaders() {
		return null;
	}

	@Override
	public void select(String readerName) {}

	@Override
	public void deselect() {}

	@Override
	public boolean isCardPresent() { return true;}

	@Override
	public boolean isCardPresent(String readerName) {	return true; }

	@Override
	public void waitForCard() {}

	@Override
	public void waitForCard(String readerName) {}

	@Override
	public void waitCardExtraction() {}

	@Override
	public void waitCardExtraction(String readerName) {}

	@Override
	public void connect() {}

	@Override
	public void connect(String readerName) {}

	@Override
	public byte[] send(byte[] command) {
		
		out.println(BAUtils.toString(command));
		
		
		boolean ended = false;
		
		while((!ended) && (in.hasNextLine())){
			
			String line = in.nextLine();
			System.out.println(line);
			return BAUtils.toBA(line);
			
		}
		
		return null;
		
	}

	@Override
	public void reconnect() {
		// TODO Auto-generated method stub

	}

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub

	}

	@Override
	public void release() {
		// TODO Auto-generated method stub

	}

	@Override
	public CardType getCardType() {
		// TODO Auto-generated method stub
		return null;
	}

	private Scanner in;
	private PrintWriter out;
	
}
