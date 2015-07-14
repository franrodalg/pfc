package dflibrary.samples.wallet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import dflibrary.library.*;
import dflibrary.library.DFLException.ExType;
import dflibrary.library.param.*;
import dflibrary.library.param.fileset.*;
import dflibrary.middleware.*;
import dflibrary.utils.ba.BAUtils;
import dflibrary.utils.security.CipAlg;

/**
 * 
 * @author Francisco Rodríguez Algarra
 *
 */
public class WTModel {

    /**
     * 
     */
    public WTModel(WTView view){
            
        this.view = view;
        this.currApp = WTApp.SELECT_READER;
        this.cm = new JSCIOComManager();
        //this.cm = new JPCSCComManager();
        this.opt = ConfigOption.FORMAT_AND_CONFIG;
        this.bdb = new BankDBManager(view);
        this.tdb = new TickDBManager(view);
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
    protected void setApp(WTApp app){ 
        
        this.currApp = app;
        setAppState(AppState.S0);
        if(bdb.getConStatus()) bdb.disconnect();
        if(tdb.getConStatus()) tdb.disconnect();
        
        if(app == WTApp.FACTORY_RESET){		
            bdb.connect();
        }
        else if(app == WTApp.CONFIG_WALLET){		
            bdb.connect();
        }
        else if(app == WTApp.BALANCE_CHECK){}
        else if(app == WTApp.REFILLING){}
        else if(app == WTApp.CONFIG_TICK){
            setConfigOption(ConfigOption.FORMAT_AND_CONFIG);
        }
        else if(app == WTApp.PURCHASE_TICKET){			
            tdb.connect();			
        }
        else if(app == WTApp.EVENT_ENTRANCE){			
            tdb.connect();
        }
        else return;
        
        getView().execAppWorker(app);
        
    }

    /**
     * 
     * @return
     */
    protected Bank[] getBanks(){
        
        return bdb.getBanks();
    
    }

    /**
     * 
     * @return
     */
    protected Event[] getEvents(){

        return tdb.getEvents();
    
    }

    /**
     * 
     * @param description
     * @param date
     * @return
     */
    protected int getEventID(String description, String date){
            
        return tdb.getEventID(description, date);
            
    }

    /**
     * 
     * @param event
     * @return
     */
    protected String[] getDates(String event){

        return tdb.getDates(event);
    
    }

    /**
     * 
     * @param event
     * @param date
     * @return
     */
    protected Ticket getAvTicket(String event, String date){
            
        int[] tickets = tdb.getAvTickets(event, date);
        return tdb.getTicket(tickets[0]);
            
    }

    /**
     * 
     * @param event_id
     * @return
     */
    protected int getTicketId(int event_id){

        return getAvTickets(event_id)[0]; 

    }

    /**
     * 
     * @param event_id
     * @return
     */
    protected int[] getAvTickets(int event_id){
            
        int[] tickets = tdb.getAvTickets(event_id);
        if((tickets == null)||(tickets.length == 0)){
            throw new RuntimeException("No available tickets for " + 
                                            "the selected event");
        }
        return tickets;

    }

    /**
     * 
     * @param ticket
     */
    protected void registerTicket(Ticket ticket){
            
        if(ticket == null) throw new NullPointerException();
        tdb.registerTicket(ticket.getId(), ticket.getUser());
            
    }

    /**
     * 
     * @return
     */
    private int getUserID(){
            
        String sUID = df.getUID().toString();
        int user_id = tdb.getUserId(sUID);
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
                try{
                    Thread.sleep(50);
                }catch(InterruptedException e){}
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
            this.df = new WTDFCard(cm);
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
            
        if(getCurrApp() == WTApp.FACTORY_RESET){
            
            if(getView().getFRAllowed()){
                getView().log("Starting the factory reset process...\n");
                factoryReset();
            }
            else{
                getView().log("Factory Reset not allowed\n");
            }

        }		
        else if(getCurrApp() == WTApp.CONFIG_WALLET){
            
            if(getView().getIBAllowed()){	
                getView().log("Starting the wallet configuration process...\n");
                walletConfig();			
            }
            else{
                getView().log("Wallet configuration not allowed until " + 
                                "a valid initial balance is provided\n");
            }
            
        }		
        else if(getCurrApp() == WTApp.BALANCE_CHECK){
                
                getView().log("Getting the current wallet balance...\n");
                balanceCheck();
                
        }		
        else if(getCurrApp() == WTApp.REFILLING){

            if(getView().getRFAllowed()){
                getView().log("Starting the wallet refilling process...\n");
                refilling();
            }
            else{
                getView().log("Wallet refilling not allowed until " + 
                                "a valid amount is provided\n");
            }

        }		
        else if(getCurrApp() == WTApp.CONFIG_TICK){	
                
            boolean tdAllowed = getView().getTDAllowed();
            boolean tcAllowed = getView().getTCAllowed();
            
            if((tdAllowed)||(tcAllowed)){
                getView().log("Starting the ticketing app " + 
                                "configuration process...\n");
                tickConfig(tdAllowed, tcAllowed);
            }
            else{
                getView().log("Ticketing app configuration process " + 
                                "not allowed until at least one option " + 
                                "is selected\n");
            }
                                        
        }
        else if(getCurrApp() == WTApp.PURCHASE_TICKET){			

            getView().log("Proceeding with purchase process...\n");
            purchase();

        }
        else if(getCurrApp() == WTApp.EVENT_ENTRANCE){			

            getView().log("Checking valid ticket fot current event...\n");
            entrance();

        }		
            
    }

    /**
     * 
     */
    private void factoryReset(){
            
        try{
                
            getView().log("Starting the factory reset process " + 
                            "in the inserted card...\n");
            
            format();
            setDefaultCardMasterKey();
                
        }catch(DFLException e){
                
            if(e.getType() == ExType.INVALID_HANDLE){
                getView().log(e.getMessage());
                connect();
                factoryReset();
            }
            else{
                getView().error(e.getMessage());
                return;
            }
                
        }catch(Exception e){
                
            getView().error(e.getMessage());
            return;
                
        }
        getView().log("Factory Reset successfully finished\n");
        getView().show("Factory Reset successfully finished");
        
    }

    /**
     * 
     */
    private void format(){
            
        getView().log("Formating card to delete all existing applications " + 
                        "and files in the card...");
        
        DFResponse res = df.format(bank.getBankID());
        getView().log(res);
        getView().log("");
        
        if(!res.isOk())
            throw new RuntimeException("Card format not successfully " +
                                        "finished");
            
    }

    /**
     *
     */
    private void setDefaultCardMasterKey(){
            
        getView().log("Setting the card Master Key to it's default value...");
        
        DFResponse res = df.setDefaultCardMasterKey(bank.getBankID());
        getView().log(res);
        getView().log("");
        
        if(!res.isOk()) 
            throw new RuntimeException("Card Master Key wasn't " + 
                                        "successfully set to its " + 
                                        "default value");
    }

    /**
     * 
     */
    private void walletConfig(){
            
        try{
                
            setMasterKey();
            createWalletApp();
            configWalletApp();
                
        }catch(DFLException e){
                
            if(e.getType() == ExType.INVALID_HANDLE){
                getView().log(e.getMessage());
                connect();
                walletConfig();
            }
            else{
                getView().error(e.getMessage());
                return;
            }
                
        }catch(Exception e){
                
            getView().error(e.getMessage());
            return;
                
        }
        getView().log("Wallet configuration successfully finished\n");
        getView().show("Wallet configuration successfully finished");
        
    }	

    /**
     * 
     */
    private void setMasterKey(){
            
        selectMasterApp();
        
        getView().log("Changing Card Master Key...");
        DFResponse res = df.changeKey(0, 5, bank.getBankID());
        getView().log(res);
        getView().log("");
        
        if(!res.isOk())
            throw new RuntimeException("Card Master Key wasn't "+ 
                                        "successfully set");
            
    }

    /**
     * 
     */
    protected void createWalletApp(){

        getView().log("Creating the Wallet application in the card...");
        
        DFResponse res;
        res = df.createApp(WALLET_AID, WALL_NUM_OF_KEYS); 
        getView().log(res);
        getView().log("");
        
        if(!res.isOk())
            throw new RuntimeException("Wallet Application wasn't " + 
                                        "successfully created");
            
    }

    /**
     * 
     */
    protected void configWalletApp(){
            
        selectWalletApp();
        
        getView().log("Configuring the recently created " + 
                        "Wallet application...");
        
        DFResponse res;
        getView().log("Configuring the Wallet application keys...\n");		
        res = df.configWalletAppKeys(WALLET_AID, bank.getBankID());
        if(!res.isOk())
            throw new RuntimeException("Wallet Application wasn't " + 
                                        "properly configured:  " + 
                                        "Key configuration error");
        getView().log("Wallet application keys successfully configured\n");
        
        getView().log("Creating the Wallet application files...");
        res = df.createWallAppFiles(WALLET_AID, bank.getBankID(), credit);
        if(!res.isOk())
            throw new RuntimeException("Wallet Application wasn't " + 
                                        "properly configured: " + 
                                        "File creation error");
        getView().log("Wallet application files successfully created\n");
        
        getView().log("Wallet application successfully created\n");
                            
    }

    /**
     * 
     */
    private void balanceCheck(){
            
        int balance = 0;
        
        try{
                
            checkWalletApp();
            int bankID = getBankID();
            balance = getBalance(bankID);
                
        }catch(DFLException e){
                
            if(e.getType() == ExType.INVALID_HANDLE){
                getView().log(e.getMessage());
                connect();
                balanceCheck();
            }
            else{
                getView().error(e.getMessage());
                return;
            }
                
        }catch(Exception e){
                
            getView().error(e.getMessage());
            return;
                
        }
        getView().setCurrentBalance(balance);
        getView().log("Your current balance is: " + balance + "\n");
        getView().show("Your current balance is: " + balance);		
            
    }

    /**
     * 
     */
    private void checkWalletApp(){
            
        selectWalletApp();
        checkWalletFiles();		
            
    }

    /**
     * 
     */
    private void checkWalletFiles(){}

    /**
     * 
     * @return
     */
    private boolean checkTickApp(){
            
        return df.checkApp(TICK_AID);
            
    }

    /**
     * 
     * @return
     */
    private int getBankID(){
            
        DFResponse res;
        
        res = df.readBankID();
        
        byte[] bankIDBA = res.getDataRes().getData().toBA();
        
        return BAUtils.toInt(bankIDBA);
            
    }

    /**
     * 
     */
    private int getBalance(int bankID){
            
        getView().log("Connecting with bank number " + bankID);
        String bankIDRes = bankConnection(bankID, "BALANCE", df.getLegacy());
        
        return Integer.parseInt(bankIDRes);
            
    }

    /**
     * 
     * @param credit
     */
    private void refilling(){
            
        try{
                
            checkWalletApp();
            int bankID = getBankID();
            refilling(bankID, credit);
                
        }catch(DFLException e){
                
            if(e.getType() == ExType.INVALID_HANDLE){
                getView().log(e.getMessage());
                connect();
                refilling();
            }
            else{
                getView().error(e.getMessage());
                return;
            }
                
        }catch(Exception e){
                
            getView().error(e.getMessage());
            return;
                
        }
        getView().log("Refilling successfully done\n");
        getView().show("Refilling successfully done");	
        
            
    }

    /**
     * 
     * @param bankID
     * @param credit
     */
    private void refilling(int bankID, int credit){
            
        getView().log("Connecting with bank number " + bankID);
        bankConnection(bankID, "CREDIT " + credit, df.getLegacy());		
            
    }

    /**
     * 
     */
    private void tickConfig(boolean tdAllowed, boolean tcAllowed){
            
        try{
                
            selectMasterApp();
            checkWalletApp();
            
            selectMasterApp();
            boolean isApp = checkTickApp();
            
            if(tdAllowed){
                    
                if((!isApp) && (!tcAllowed)){
                        
                    getView().log("Ticketing application doesn't exists " + 
                                    "in the card yet");
                    throw new RuntimeException("Ticketing app deletion " + 
                                                "didn't succeed. \n" + 
                                                "The application doesn't " +
                                                "exist in the card yet");
                }
                
                deleteTickApp();
                if(!tcAllowed){
                    getView().log("Ticketing app successfully deleted");
                    getView().show("Ticketing app successfully deleted");
                    return;
                }
                    
            }
                
            if(tcAllowed){
                    
                if((isApp) && (!tdAllowed)){
                    getView().log("Ticketing application already " + 
                            "exists in the card");
                    throw new RuntimeException("Ticketing app creation " + 
                                                "didn't succeed. \n" + 
                                                "The application already " + 
                                                "exists in the card");
                }
                
                createTickApp();
                configTickApp();
                getView().log("Ticketing app successfully created");
                getView().show("Ticketing app successfully created");
                    
            }
                
        }catch(DFLException e){
                
            if(e.getType() == ExType.INVALID_HANDLE){
                    getView().log(e.getMessage());
                    connect();
                    tickConfig(tdAllowed, tcAllowed);
            }
            else{
                    getView().error(e.getMessage());
                    return;
            }
                
        }catch(Exception e){
                
            getView().error(e.getMessage());
            return;
                
        }

    }

    /**
     * 
     */
    protected void deleteTickApp(){
            
        getView().log("Deleting the Ticketing application of the card");
        getView().log(df.deleteApp(TICK_AID)); 
        getView().log("");
            
    }

    /**
     * 
     */
    protected void createTickApp(){

        getView().log("Creating the Ticketing application in the card");
        getView().log(df.createApp(TICK_AID, TICK_NUM_OF_KEYS)); 
        getView().log("");
            
    }

    /**
     * 
     */
    protected void configTickApp(){
            
        selectTickApp();
        
        getView().log("Configuring the recently created Ticketing application");
        
        DFResponse res;
        
        getView().log("Configuring the Ticketing application keys...\n");		
        res = df.configTickAppKeys(TICK_AID);
        if(!res.isOk())
            throw new RuntimeException("Ticketing application wasn't " + 
                                        "properly configured:  " + 
                                        "Key configuration error");
        getView().log("Ticketing application keys successfully configured\n");
        
        getView().log("Creating the Ticketing application files...");
        res = df.createTickAppFiles(TICK_AID);
        if(!res.isOk()) 
            throw new RuntimeException("Ticketing application wasn't " + 
                                        "properly configured: " + 
                                        "File creation error");
        getView().log("Ticketing application files successfully created\n");
        
        getView().log("Ticketing application successfully configured\n");
        
        getView().log(df.configTickAppKeys(TICK_AID)); 
        getView().log("");
        
    }

    /**
     * Performs the ticket purchase operation 
     */
    private void purchase(){        

        if(ticket == null){
                
            getView().log("No available tickets for the selected event");
            return;
                
        }
        try{
                
            //Checking if the Wallet Application is present in the card 
            //in order to pay the purchase
            
            selectMasterApp();
            checkWalletApp();
            
            //Checking if the Ticketing Application and the Tickets file 
            //are present in the card
            
            selectMasterApp();
            boolean isApp = checkTickApp();
            if(!isApp) 
                throw new RuntimeException("Ticketing app not present " + 
                                            "in the card");			
            
            selectTickApp();
            boolean isFile = checkTickFile();
            if(!isFile) 
                throw new RuntimeException("Tickets file not present " + 
                                            "in the card");
            
            //Checking if there is enough space in the Tickets file 
            //for another ticket
            
            boolean freeRecords = checkTicketFileSpace();
            if(!freeRecords) 
                throw new RuntimeException("There is not enough space " + 
                                            "for another ticket");
            
            //Recovering the selected ticket
            
            Ticket ticket = getTicket();
            
            //Looking for the userID associated with the UID of the card 
            //at the Ticketing database
            
            int user_id = getUserID();
            getView().log("User successfully found in database. User ID: " + 
                            user_id);
            
            //Proceed with the payment
            
            int price = ticket.getPrice();
            
            selectWalletApp();
            int bankID = getBankID();
            
            payment(bankID, price);
    
            //Personalize ticket
            
            getTicket().setUser(user_id);
            
            //Write ticket in card
            
            selectTickApp();
            
            writeTicketRecord(ticket);
            
            //Register ticket in database
            
            registerTicket(ticket);
            
            getView().log("Ticket successfully bought");
            getView().show("Ticket successfully bought");
                
        }catch(DFLException e){
                
            if(e.getType() == ExType.INVALID_HANDLE){
                getView().log(e.getMessage());
                connect();
                purchase();
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
     * @param bankID
     * @param credit
     */
    private void payment(int bankID, int price){
            
        getView().log("Connecting with bank number " + bankID);
        bankConnection(bankID, "DEBIT " + price, df.getLegacy());
            
    }

    /**
     * 
     * @return
     */
    private boolean checkTickFile(){
            
        return df.checkFile(TICK_FID);
            
    }

    /**
     * 
     * @return
     */
    private boolean checkTicketFileSpace(){
            
        DFResponse res = df.getTicketFileSettings();
        
        RecordFileSettings fileSet;
        
        try{
            fileSet = (RecordFileSettings) res.getFileSettings();
        }catch(Exception e){
            throw new RuntimeException("Ticket file is not of proper type");
        }
        
        int currNumOfRecords = fileSet.getCurrentNumberOfRecords();
        
        if(currNumOfRecords < WTModel.TICK_MAX_NUM_OF_TICKETS) return true;
        
        return false;
            
            
    }

    /**
     * 
     * @param ticket
     */
    private void writeTicketRecord(Ticket ticket){
            
        getView().log("Writing ticket data in the file...");

        DFResponse res = df.writeTicket(ticket);
        getView().log(res);		
        if(!res.isOk()) throw new RuntimeException("Writing ticket data failed");		
        getView().log("");

    }

    /**
     *
     */
    private void entrance(){
            
        try{
                
            //Checking if the Ticketing Application and the Tickets file 
            //are present in the card
            
            selectMasterApp();
            boolean isApp = checkTickApp();
            if(!isApp)
                throw new RuntimeException("Ticketing app not present " + 
                                            "in the card");			
            
            selectTickApp();
            boolean isFile = checkTickFile();
            if(!isFile)
                throw new RuntimeException("Tickets file not present " + 
                                            "in the card");
            
            //Looking for the userID associated with the UID of the card 
            //at the Ticketing database
            
            int user_id = getUserID();
            getView().log("User successfully found in database. " + 
                    "User ID: " + user_id);
            
            //
            
            searchTicket(user_id);
            
            //
            
            getView().show("Entrance allowed");
            getView().log("Entrance allowed");
                
        }catch(DFLException e){
                
            if(e.getType() == ExType.INVALID_HANDLE){
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
     */
    private void selectMasterApp(){
            
        getView().log("Selecting Card Master Application...");
        
        DFResponse res = df.selectApp(0);
        getView().log(res);  		
        getView().log("");
                
        if(!res.isOk())
            throw new RuntimeException("Selection of Card Master Application " +
                                        "didn't succeed");
    }

    /**
     * 
     */
    private void selectWalletApp(){
            
        getView().log("Selecting Wallet Application...");
        
        DFResponse res = df.selectApp(WALLET_AID);
        getView().log(res);  		
        getView().log("");
                
        if(!res.isOk())
            throw new RuntimeException("Selection of Wallet Application " +
                                        "didn't succeed");
            
    }

    /**
     * 
     */
    private void selectTickApp(){
            
        getView().log("Selecting Ticketing Application...");
        
        DFResponse res = df.selectApp(TICK_AID);
        getView().log(res);  		
        getView().log("");
                
        if(!res.isOk())
            throw new RuntimeException("Selection of Ticketing Application " +
                                        "didn't succeed");
            
    }

    /**
     * 
     * @param user_id
     */
    private void searchTicket(int user_id){
            
        DFResponse res;
        
        res = df.getTicketFileSettings();
        if(!res.isOk())
            throw new RuntimeException("Could not retreive Ticket " + 
                                        "file settings");
        
        RecordFileSettings fileSet;
        
        try{
            fileSet = (RecordFileSettings) res.getFileSettings();
        }catch(Exception e){
            throw new RuntimeException("Ticket file is not of proper type");
        }
        
        int currNumOfTickets = fileSet.getCurrentNumberOfRecords();
        
        if(currNumOfTickets == 0)
            throw new RuntimeException("No available tickets");
        
        int event_id = getEventID();
        boolean event_found = false;
        boolean ticket_used = false;
        
        Ticket ticket;
        
        for(int i = 0; i < currNumOfTickets; i++){
                
            ticket = readTicketRecord(i);
                
            if(event_id == ticket.getEvent()){
                        
                event_found = true;
                ticket_used = ticket.isUsed();
                if(!ticket_used){
                    markUsed(currNumOfTickets - i - 1, ticket);
                    break;
                }
                    
            }
                
        }
        
        if(!event_found)
            throw new RuntimeException("No ticket available for this event");
        if(ticket_used)
            throw new RuntimeException("All tickets available for this event " + 
                                        "are already used");
            
    }

    /**
     * 
     * @return
     */
    private Ticket[] readTicketRecords(){
            
        DFResponse res;
        
        res = df.readTicketRecords();
        if(!res.isOk()) throw new RuntimeException("Ticket reading error");
        
        Data[] data = res.getRecordsRes().getRecords();
        
        Ticket[] tickets = new Ticket[data.length];
        
        for(int i = 0; i < tickets.length; i ++){
            tickets[i] = new Ticket(data[i].toBA());
        }
        
        return tickets;

    }

    /**
     * 
     * @param record
     * @return
     */
    private Ticket readTicketRecord(int record){
            
        DFResponse res;
        
        res = df.readTicketRecord(record);
        if(!res.isOk()) throw new RuntimeException("Ticket reading error");
        
        byte[] ticketBA = res.getRecordsRes().getRecords()[0].toBA();
        getView().log(res);
        
        Ticket ticket = new Ticket(ticketBA);
        
        return ticket;
            
    }

    /**
     * 
     * @param recordID
     * @param ticket
     */
    private void markUsed(int recordID, Ticket ticket){
            
        Ticket[] tickets = readTicketRecords();
        ticket.setState(true);
        tickets[recordID] = ticket;
        clearTicketFile();
        tdb.markUsed(ticket.getId());
        
        for(int i = 0; i < tickets.length; i ++){
                writeTicketRecord(tickets[i]);
        }
                    
    }

    /**
     * 
     * @param recordID
     */
    private void clearTicketFile(){
            
        getView().log("Clearing ticket record...");
        
        DFResponse res = df.clearTicketFile();
        getView().log(res);		
        if(!res.isOk()) throw new RuntimeException("Clearing ticket record failed");		
        getView().log("");
            
    }

    //Bank Connection

    /**
     * 
     * @param bankID
     * @param order
     * @return
     */
    private String bankConnection(int bankID, String order, boolean legacy){
            
        try{	
                
            getView().log("Connecting to the bank server...");
            
            Socket s = new Socket("Localhost", bankID);
                    
            InputStream inS = s.getInputStream();
            OutputStream outS = s.getOutputStream();
                    
            try{
                
                Scanner in = new Scanner(inS);
                PrintWriter out = new PrintWriter(outS, true);
                
                boolean ended = false;
                
                if(in.hasNextLine()){
                    String line = in.nextLine();
                    byte[] res = cm.send(BAUtils.toBA(line));
                    out.println(BAUtils.toString(res));
                }
                
                out.println(order + legacy);
                
                while((!ended) && (in.hasNextLine())){
                    String line = in.nextLine();
                    if(line.trim().startsWith("SUCCESS:")){
                        ended = true;
                        return line.trim().substring("SUCCESS: ".length());
                    }
                    else if(line.trim().startsWith("SUCCESS")){
                        ended = true;
                    }
                    else if(line.trim().startsWith("ERROR:")){
                        ended = true;
                        String error = line.trim().substring("ERROR: ".length());
                        if(error.trim().equals("ID")) 
                            throw new RuntimeException("Error accessing " + 
                                                        "application remotely");
                        if(error.trim().equals("FID")) 
                            throw new RuntimeException("Error reading " + 
                                                        "file remotely");
                        if(error.trim().startsWith("CREDIT ")){
                            int i = Integer.parseInt(error.trim().
                                        substring("CREDIT ".length()));
                            throw new RuntimeException("The amount you " + 
                                                        "requested excesses " +
                                                        "the maximum allowed." + 
                                                        "\n" +
                                                        "Please write " + i + 
                                                        " or less and " +
                                                        "try it again");
                        }
                        if(error.trim().startsWith("DEBIT ")){
                            int i = Integer.parseInt(error.trim().
                                        substring("DEBIT ".length()));
                            throw new RuntimeException("Price exceeds the " + 
                                    "amount available in the wallet. \n" +
                                    "Purchase aborted");
                        }
                        throw new RuntimeException(line.trim());
                    }
                    else{
                        byte[] res = cm.send(BAUtils.toBA(line));
                        out.println(BAUtils.toString(res));
                    }

                }
        
            }
            finally{
                s.close(); 
                getView().log("Connection closed by remote server");
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return "";
        
    }

    //Gets and sets

    /**
     * 
     * @return
     */
    protected WTView getView(){

        return this.view;

    }

    /**
     * 
     * @return
     */
    protected WTApp getCurrApp(){
        
        return this.currApp;
    
    }

    /**
     * 
     * @return
     */
    protected ConfigOption getConfigOption(){
        
        return this.opt;
    
    }

    /**
     * 
     * @param opt
     */
    protected void setConfigOption(ConfigOption opt){
        
        this.opt = opt;
    
    }

    /**
     * 
     * @return
     */
    protected AppState getAppState(){
        
        return this.appState;
    
    }

    /**
     * 
     * @param appState
     */
    protected void setAppState(AppState appState){
        
        this.appState = appState;
    
    }

    /**
     * 
     * @return
     */
    protected boolean getLock(){
        
        return getView().getLock();
    
    }

    /**
     * 
     */
    protected void setCard(){
        
        this.isCard = cm.isCardPresent();
    
    }
            
    /**
     * 
     * @return
     */
    protected boolean getCard(){
        
        return this.isCard;
    
    }

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
    protected Bank getBank(){
        
        return this.bank;
    
    }
            
    /**
     * 
     * @param bank
     */
    protected void setBank(Bank bank){
        
        this.bank = bank;
    
    }

    /**
     * 
     * @return
     */
    protected int getCredit(){
        
        return this.credit;
    
    }

    /**
     * 
     * @param i
     */
    protected void setCredit(int i){
        
        this.credit = i;
    
    }

    /**
     * 
     * @return
     */
    protected Ticket getTicket(){
        
        return this.ticket;
    
    }

    /**
     * 
     * @param ticket
     */
    protected void setTicket(Ticket ticket){
        
        this.ticket = ticket;
    
    }

    /**
     * 
     * @return
     */
    protected int getEventID(){
        
        return this.event_id;
    
    }

    /**
     * 
     * @param event_id
     */
    protected void setEventID(int event_id){
        
        this.event_id = event_id;
    
    }

    public static final int WALLET_AID = 1;
    public static final int TICK_AID = 2;

    public static final CipAlg WALL_ALG = CipAlg.TDEA3;
    public static final CipAlg TICK_ALG = CipAlg.TDEA3;

    public static final int MASTER = 0;

    public static final int WALL_ISSUER_FID = 1;
    public static final int WALL_WALLET_FID = 2;

    public static final int TICK_FID = 1;
    public static final int TICK_MAX_NUM_OF_TICKETS = 20;

    public static final int WALL_NUM_OF_KEYS = 3;
    public static final int TICK_NUM_OF_KEYS = 4;

    public static final int WALL_ISSUER_R = AccessRights.FREE;
    public static final int WALL_ISSUER_W = 1;
    public static final int WALL_ISSUER_RW = AccessRights.DENY;

    public static final int WALL_WALLET_R = 2;
    public static final int WALL_WALLET_W = AccessRights.DENY;
    public static final int WALL_WALLET_RW = 1;

    public static final int TICK_R = 1;
    public static final int TICK_W = 2;
    public static final int TICK_RW = 3;

    private WTView view;
    private WTApp currApp;
    private ComManager cm;

    private BankDBManager bdb;
    private TickDBManager tdb;

    private WTDFCard df;

    private AppState appState;
    private boolean isCard;

    private ConfigOption opt;

    private Bank bank;

    private int credit;

    private Ticket ticket;

    private int event_id;

}

/**
 *
 */
enum WTApp{

    SELECT_READER{
        public String toString(){ return "Select Reader"; }
    },
    SELECT_APP{
        public String toString(){ return "Select Application"; }
    },
    FACTORY_RESET{
        public String toString(){ return "Factory Reset"; }
    },
    SELECT_WALLET_APP{
        public String toString(){ return "Select Wallet Application"; }
    },
    CONFIG_WALLET{
        public String toString(){ return "Configure Wallet Application"; }
    },
    BALANCE_CHECK{
        public String toString(){ return "Check Wallet Balance"; }
    },
    REFILLING{
        public String toString(){ return "Refill Wallet"; }
    },
    SELECT_TICK_APP{
        public String toString(){ return "Select Ticketing Application"; }
    },
    CONFIG_TICK{
        public String toString(){ return "Configure Ticketing Application"; }
    },
    PURCHASE_TICKET{
        public String toString(){ return "Purchase Tickets"; }
    },
    EVENT_ENTRANCE{
        public String toString(){ return "Event Entrance"; }
    };

}

/**
 *
 */
enum ConfigOption{

    FORMAT_AND_CONFIG{
        public String toString(){
            return "Format Card and Configure Ticketing App";
        }
    },
    DELETE{
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
 */
enum AppState{

    S0{
        void nextState(WTModel model){
            model.setAppState(S1);
        }
    },
    S1{
        void nextState(WTModel model){
                
            if(model.getCard()){
                model.setAppState(S2);
                    
            }
            else{
                model.setAppState(S3);
            }

        }
    },
    S2{
        public void nextState(WTModel model){
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
        public void nextState(WTModel model){
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
        public void nextState(WTModel model){
            if(!model.getCard()){
                model.setAppState(S0);
            }
            else{
                model.setAppState(S4);
            }
        }
    };

    abstract void nextState(WTModel model);

}
