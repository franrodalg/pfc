package dflibrary.samples.ticketing;

import dflibrary.library.*;
import dflibrary.library.DFLException.ExType;
import dflibrary.library.param.*;
import dflibrary.library.param.fileset.*;
import dflibrary.middleware.*;
import dflibrary.utils.ba.BAUtils;

/**
 * 
 * @author Francisco Rodríguez Algarra
 *
 */
public class TicketingModel {

	/**
	 * 
	 */
	public TicketingModel(TicketingView view){
		
		this.view = view;
		this.currApp = TicketingApp.SELECT_READER;
		this.cm = new JPCSCComManager();
		this.opt = ConfigOption.FORMAT_AND_CONFIG;
		this.db = new TicketingDBManager(view);
		setAppState(AppState.S0);
		
	}
	
	/**
	 * 
	 */
	protected void start(){

		scan();
		listReaders();
		
	}
	
	
	
	/**
	 * 
	 */
	private void scan(){
		
		cm.scan();
		getView().scanComplete();
		
	}
	
	/**
	 * 
	 */
	private void listReaders(){
		
		String[] readers;
		
		try{
			readers = cm.listReaders();
		}catch(DFLException e){
			readers = new String[0];
		}
		
		getView().listReaders(readers);
		
	}
	
	/**
	 * 
	 * @param reader
	 */
	protected void selectReader(String reader){
		
		cm.select(reader);
		view.readerConnected(reader);
		
	}
	
	
	
	/**
	 * 
	 * @param app
	 */
	protected void setApp(TicketingApp app){ 
		
		this.currApp = app;
		setAppState(AppState.S0);
		if(db.getConStatus()) db.disconnect();
		
		
		if(app == TicketingApp.CONFIG_PICC){
			setConfigOption(ConfigOption.FORMAT_AND_CONFIG);
		}
		else if(app == TicketingApp.BUY_TICKET){
			
			db.connect();
			
		}
		else if(app == TicketingApp.EVENT_ENTRANCE){
			
			db.connect();
		}
		else return;
		
		getView().execAppWorker(app);
		
	}
	
	/**
	 * 
	 * @return
	 */
	protected Event[] getEvents(){ return db.getEvents(); }
	
	/**
	 * 
	 * @param description
	 * @param date
	 * @return
	 */
	protected int getEventID(String description, String date){
		
		return db.getEventID(description, date);
		
	}
	
	/**
	 * 
	 * @param event
	 * @return
	 */
	protected String[] getDates(String event){ return db.getDates(event); }
	
	/**
	 * 
	 * @param event
	 * @param date
	 * @return
	 */
	protected Ticket getAvTicket(String event, String date){
		
		int[] tickets = db.getAvTickets(event, date);
		
		return db.getTicket(tickets[0]);
		
	}
	
	/**
	 * 
	 * @param event_id
	 * @return
	 */
	protected int getTicketId(int event_id){ return getAvTickets(event_id)[0]; }
	
	/**
	 * 
	 * @param event_id
	 * @return
	 */
	protected int[] getAvTickets(int event_id){
		
		int[] tickets = db.getAvTickets(event_id);
		if((tickets == null)||(tickets.length == 0)){
			throw new RuntimeException("No available tickets for the selected event");
		}
		return tickets;
	}
	
	/**
	 * 
	 * @param ticket
	 */
	protected void registerTicket(Ticket ticket){
		
		if(ticket == null) throw new NullPointerException();
		
		db.registerTicket(ticket.getId(), ticket.getUser());
		
	}
	
	/**
	 * 
	 * @return
	 */
	private int getUserID(){
		
		String sUID = df.getUID().toString();
		int user_id = db.getUserId(sUID);
		return user_id;
		
	}
	
	/**
	 * 
	 */
	protected void work(){
		
		if(getAppState() == AppState.S0){
			boolean b = true;
			while(getLock()){
				if(b){ 
					getView().log("Waiting for user interface to be updated...");
					b = false;
				}
			}
		}
		else if(getAppState() == AppState.S1){
			getView().log("User interface successfully updated\n");
			setCard();
		}
		else if(getAppState() == AppState.S2){
			while((isCard())&&(!getLock()));
		}
		else if(getAppState() == AppState.S3){
			while((!isCard())&&(!getLock()));
		}
		else if(getAppState() == AppState.S4){
			doTask();
			while(isCard());
		}
		
	}
	
	/**
	 * 
	 */
	private void doTask(){
		
		getView().log("Connecting card...");
		connect();
		getView().log("Card successfully connected\n");
		
		getView().log("Checking card type...");
		if(checkDF()) {
			getView().log("Card successfully recognized as a Mifare DESFire\n");
			this.df = new TicketingDFCard(cm);
			doAppTask();
		}
		else getView().log("Wrong card type");
		
		getView().log("Disconnecting card...");
		disconnect();
		getView().log("Card successfully disconnected\n");
		
		
	}
	
	/**
	 * 
	 */
	private void connect(){
		
		cm.connect();
		
	}
	
	/**
	 * 
	 * @return
	 */
	private boolean checkDF(){
		
		CardType ct = cm.getCardType();
		return (ct == CardType.MIFARE_DESFIRE);
		
	}
	
	/**
	 * 
	 */
	private void disconnect(){
		
		cm.disconnect();
		
	}
	
	/**
	 * 
	 */
	private void doAppTask(){
		
		if(getCurrApp() == TicketingApp.CONFIG_PICC){
			
			getView().log("Starting configuration process...\n");
			config();
			
		}
		else if(getCurrApp() == TicketingApp.BUY_TICKET){
			
			getView().log("BUYING TICKET\n");
			buy();
			
			
		}
		else if(getCurrApp() == TicketingApp.EVENT_ENTRANCE){
			
			getView().log("CHECKING TICKET\n");
			entrance();
		}
		
		
	}
	
	/**
	 * 
	 */
	private void config(){
		
		try{
			
			selectMasterApp();
			
			if(getConfigOption() == ConfigOption.KEEP_CONFIG){
				checkApp(false);
			}
			else{
				
				cardFormat();
				if(getConfigOption() == ConfigOption.FORMAT){
					
					getView().log("Card successfully formated");
					getView().show("Card successfully formated");
					return;
					
				}
				
			}
			
			createApp();
			
		}catch(DFLException e){
			
			if(e.getType() == ExType.PCSC_INVALID_HANDLE){
				getView().log(e.getMessage());
				connect();
				config();
			}
			else{
				getView().error(e.getMessage());
				return;
			}
			
		}catch(Exception e){
			
			getView().error(e.getMessage());
			return;
			
		}
		getView().log("Card successfully configured");
		getView().show("Card successfully configured");
		
	}
	
	/**
	 * 
	 */
	private void buy(){
		
		if(ticket == null){
			
			getView().log("No available tickets for the selected event");
			return;
			
		}
		try{
			
			checkConfiguredCard(true, false);			
			getView().log("Card properly configured");
			
			int user_id = getUserID();
			getView().log("User successfully found in database. User ID: " + user_id);
			
			getTicket().setUser(user_id);
			
			createTicketFile();
			writeTicketFile(getTicket());
			
			registerTicket(getTicket());
			
			getView().log("Ticket successfully bought");
			getView().show("Ticket successfully bought");
			
		}catch(DFLException e){
			
			if(e.getType() == ExType.PCSC_INVALID_HANDLE){
				getView().log(e.getMessage());
				connect();
				buy();
			}
			else{
				getView().error(e.getMessage());
				return;
			}
			
		}catch(Exception e){
			
			getView().error(e.getMessage());
			return;
			
		}
		
		getView().fillComp(getCurrApp(), false, false);
		
		
	}
	
	/**
	 * 
	 */
	private void entrance(){
		
		try{
			
			checkConfiguredCard(true, true);		
			getView().log("Card properly configured");
			
			int user_id = getUserID();
			getView().log("User successfully found in database. User ID: " + user_id);
			
			checkTicket(user_id);
			
			getView().log("Entrance allowed");
			getView().show("Entrance allowed");
			
		}catch(DFLException e){
			
			if(e.getType() == ExType.PCSC_INVALID_HANDLE){
				getView().log(e.getMessage());
				connect();
				entrance();
			}
			else{
				getView().error(e.getMessage());
				return;
			}
			
		}catch(Exception e){
			
			getView().error(e.getMessage());
			return;
			
		}
		
		getView().fillComp(getCurrApp(), false, false);
		
	}
	
	//Card communication methods
	
	/**
	 * 
	 * @param keyNum
	 */
	private void authentication(int keyNum){
		
		getView().log("Authenticating with key Number " + keyNum);
		getView().log(df.authentication(keyNum));
		getView().log("");
		
	}
	
	/**
	 * 
	 */
	private void cardFormat(){
		
		getView().log("Formatting card:");
		getView().log(df.piccFormat());
		getView().log("");
		
	}
	
	
	/**
	 * 
	 */
	private void cardFullFormat(){
		
		getView().log("Full formatting card:");
		getView().log(df.piccFullFormat());
		getView().log("");
		
	}
	
	/**
	 * 
	 */
	private void selectMasterApp(){
		
		selectApp(MASTER);
		
	}
	
	/**
	 * 
	 */
	private void selectApp(){
		
		selectApp(AID);
		
	}
	
	/**
	 * 
	 * @param aid
	 */
	private void selectApp(int aid){
		
		getView().log("Selecting app with aid " + aid);
		getView().log(df.selectApp(aid));  		
		getView().log("");
			
	}
	
	/**
	 * 
	 * @param app
	 * @param file
	 */
	private void checkConfiguredCard(boolean app, boolean file){
		
		selectMasterApp();
		authentication(MASTER);
		
		checkApp(app);
		selectApp();
		
		authentication(MASTER);
		checkFile(file);
		
	}	
	
	/**
	 * 
	 * @param wanted
	 */
	private void checkApp(boolean wanted){
		
		checkApp(AID, wanted);		
		
	}
	
	/**
	 * 
	 * @param aid
	 * @param wanted
	 */
	private void checkApp(int aid, boolean wanted){
		
		getView().log("Checking if app with aid " + aid + " already exists in the card...");
		
		boolean b = df.checkApp(aid);
		String msg;
		
		if(b){
			getView().log("It does");
			msg = "already exists";
		}
		else{
			getView().log("It doesn't");
			msg = "doesn't exist";
		}
		
		if(wanted == b) getView().log("Proceed\n");
		
		else throw new RuntimeException("Application with aid " + aid + " " + msg + " in the card");
	}
	
	/**
	 * 
	 * @param wanted
	 */
	protected void checkFile(boolean wanted){
		
		checkFile(FID, wanted);
		
	}
	
	/**
	 * 
	 * @param fid
	 * @param wanted
	 */
	private void checkFile(int fid, boolean wanted){
		
		getView().log("Checking if file with fid " + fid + " already exists...");
		
		boolean b = df.checkFile(fid);
		String msg;
		
		if(b){
			getView().log("It does");
			msg = "already exists";
		}
		else{
			getView().log("It doesn't");
			msg = "doesn't exist";
		}
		
		if(wanted == b) getView().log("Proceed\n");
		else throw new RuntimeException("File with fid " + fid + " " + msg);
		
	}
	
	/**
	 * 
	 */
	protected void createApp(){
		
		createApp(AID, NUM_OF_KEYS);
		
	}
	
	
	/**
	 * 
	 * @param aid
	 * @param numOfKeys
	 */
	private void createApp(int aid, int numOfKeys){
		
		getView().log("Creating app with aid " + aid);
		getView().log(df.createApp(aid, numOfKeys)); 
		getView().log("");
	}
	
	/**
	 * 
	 */
	protected void createTicketFile(){
		
		getView().log("Creating new data file..");
		AccessRights ar = new AccessRights(MASTER, READ, WRITE, READWRITE);
		ComSet comSet = ComSet.ENC;
		getView().log(df.createDataFile(FID, comSet, ar, 16));	
		getView().log("");
				
	}
	
	/**
	 * 
	 * @param ticket
	 */
	protected void writeTicketFile(Ticket ticket){
		
		getView().log("Writing data in file " + FID + "...");
		
		byte[] data = ticket.toBA();
		getView().log(df.writeData(FID, 0, data));		
		getView().log("");
		
	}
	
	
	/**
	 * 
	 * @param user_id
	 */
	private void checkTicket(int user_id){
		
		Ticket ticket = readTicket();
		
		if(ticket.isUsed()) throw new RuntimeException("Ticket already used");
		if(ticket.getEvent() != getEventID()) throw new RuntimeException("Ticket not valid for the selected event");
		if(ticket.getUser() != user_id) throw new RuntimeException("Ticket not valid for this user");
		
		markUsed(ticket);
		
	}
	
	/**
	 * 
	 * @return
	 */
	private Ticket readTicket(){
		
		getView().log("Reading data from file " + FID + "...");
		DFResponse res = df.authentication(READ);
		
		res = df.readData(FID, 0, 16);
		if(res.isOk())  getView().log(SC.OPERATION_OK.toString());
		
		byte[] data = res.getDataRes().getData().toBA();
		
		return new Ticket(data);
		
		
	}
	
	/**
	 * 
	 * @param ticket
	 */
	private void markUsed(Ticket ticket){
		
		changeFile(ticket);
		db.markUsed(ticket.getId());
		
	}
	
	/**
	 * 
	 * @param ticket
	 */
	private void changeFile(Ticket ticket){
		
		DFResponse res;
		
		ticket.used();
		
		byte[] data = ticket.toBA();
		
		df.writeData(FID, 0, data);
		
		
	}
	
	
	//Gets and sets
	
	/**
	 * 
	 * @return
	 */
	protected TicketingView getView(){ return this.view; }
	
	/**
	 * 
	 * @return
	 */
	protected TicketingApp getCurrApp(){ return this.currApp; }
	
	/**
	 * 
	 * @return
	 */
	protected ConfigOption getConfigOption(){ return this.opt; }
	
	/**
	 * 
	 * @param opt
	 */
	protected void setConfigOption(ConfigOption opt){ this.opt = opt; }
	
	/**
	 * 
	 * @return
	 */
	protected AppState getAppState(){ return this.appState; }
	
	/**
	 * 
	 * @param appState
	 */
	protected void setAppState(AppState appState){ this.appState = appState; }
	
	/**
	 * 
	 * @return
	 */
	protected boolean getLock(){ return getView().getLock(); }
	
	/**
	 * 
	 */
	protected void setCard(){ this.isCard = cm.isCardPresent(); }
		
	/**
	 * 
	 * @return
	 */
	protected boolean getCard(){ return this.isCard;}
	
	/**
	 * 
	 * @return
	 */
	protected synchronized boolean isCard(){
		
		setCard();
		return getCard();
		
	}
	
	/**
	 * 
	 * @return
	 */
	protected Ticket getTicket(){ return this.ticket; }
	
	/**
	 * 
	 * @param ticket
	 */
	protected void setTicket(Ticket ticket){ this.ticket = ticket; }
	
	/**
	 * 
	 * @return
	 */
	protected int getEventID(){ return this.event_id; }
	
	/**
	 * 
	 * @param event_id
	 */
	protected void setEventID(int event_id){ this.event_id = event_id; }
	
	public static final int AID = 1;
	public static final int FID = 1;
	public static final int MASTER = 0;
	public static final int NUM_OF_KEYS = 4;
	public static final int READ = 1;
	public static final int WRITE = 2;
	public static final int READWRITE = 3;
	
	
	private TicketingView view;
	private TicketingApp currApp;
	private JPCSCComManager cm;
	private TicketingDBManager db;
	
	private TicketingDFCard df;
	
	private AppState appState;
	private boolean isCard;
	
	private ConfigOption opt;
	
	private Ticket ticket;
	
	private int event_id;
	
}

/**
 * 
 * @author Francisco Rodríguez Algarra
 *
 */
enum TicketingApp{
	
	SELECT_READER{
		public String toString(){ return "Select Reader"; }
	},
	APP_LAUNCHER{
		public String toString(){ return "Select Application"; }
	},
	CONFIG_PICC{
		public String toString(){ return "Configure Card"; }
	},
	BUY_TICKET{
		public String toString(){ return "Buy Ticket"; }
	},
	EVENT_ENTRANCE{
		public String toString(){ return "Event Entrance"; }
	};
	
}

/**
 * 
 * @author Francisco Rodríguez Algarra
 *
 */
enum ConfigOption{
	
	FORMAT_AND_CONFIG{
		public String toString(){
			return "Format Card and Configure Ticketing App";
		}
	},
	FORMAT{
		public String toString(){
			return "Only Format Card";
		}
	},
	KEEP_CONFIG{
		public String toString(){
			return "Keep Ticketing App if already configured";
		}
	};
		
}

/**
 * 
 * @author Francisco Rodríguez Algarra
 *
 */
enum AppState{
	
	S0{
		void nextState(TicketingModel model){
			model.setAppState(S1);
		}
	},
	S1{
		void nextState(TicketingModel model){
			
			if(model.getCard()){
				model.setAppState(S2);
				
			}
			else{
				model.setAppState(S3);
			}
	
		}
	},
	S2{
		public void nextState(TicketingModel model){
			if(model.getLock()){
				model.setAppState(S0);
			}
			else if(!model.getCard()){
				model.setAppState(S3);
			}
			else{
				model.setAppState(S2);
			}
		}
	},
	S3{
		public void nextState(TicketingModel model){
			if(model.getLock()){
				model.setAppState(S0);
			}
			else if(model.getCard()){
				model.setAppState(S4);
			}
			else{
				model.setAppState(S3);
			}
		}
	},
	S4{
		public void nextState(TicketingModel model){
			if(!model.getCard()){
				model.setAppState(S0);
			}
			else{
				model.setAppState(S4);
			}
		}
	};
	
	abstract void nextState(TicketingModel model);
	
}
	
	
	
	
	
