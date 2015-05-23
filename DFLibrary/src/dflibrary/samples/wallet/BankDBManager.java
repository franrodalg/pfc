package dflibrary.samples.wallet;

import java.sql.*;

/**
 * 
 * @author Francisco Rodríguez Algarra
 *
 */
public class BankDBManager {
	
	/**
	 * 
	 */
	public BankDBManager(){
		this(null);
	}
	
	/**
	 * 
	 */
	public BankDBManager(WTView view){

		setIP(LOCALHOST);
		this.view = view;
				
	}
	
	/**
	 * 
	 */
	protected void connect(){ setConnection();}
	
	/**
	 * 
	 */
	private void setConnection(){
		
		log("Trying to connect to database");
		
		try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e) {
        	e.printStackTrace();
            throw new RuntimeException("MySql Driver creation failed.");
        }
        
        try {
            con = DriverManager.getConnection("jdbc:mysql://" + 
            			getIP() + ":" + PORT + "/" + 
            			DB_NAME, DB_USER_NAME, DB_USER_PWD);
        } catch (Exception e){
        	throw new RuntimeException("Database connection failed.");
        	}
	        
	    this.conStatus = true;
        log("Database connection successful");
    }
	
	protected void disconnect(){
		
		if(con != null){
				
			try {
				con.close();
	        } catch (Exception e){
	        	throw new RuntimeException("Database disconnection failed.");
	        }		
				
		}
			
		this.conStatus = false;
        log("Database disconnection successful");
		
	}
			
	/**
	 * 
	 * @return
	 */
	public Connection getCon(){
			
		return this.con;
			
	}
		
	/**
	 * 
	 * @return
	 */
	public Bank[] getBanks(){
			
		Bank[] res = null;
		
        try {
 
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery ("SELECT * FROM banks");
            
            if(!rs.next()) return null;
	            
            rs.last();
	            
            res = new Bank[rs.getRow()];
            rs.beforeFirst();
            int i = 0;
	            
            while (rs.next()) {
	                    	
                res[i] = new Bank(rs.getInt(BANKS_BANK_ID), rs.getString(BANKS_BANK_NAME));
                i++;
            }
        } catch (Exception e) {
        	System.out.println(e.getMessage());
        	throw new RuntimeException(e.toString());
	        	
        }
			
		return res;
						
	}

		
	/**
	 * 
	 * @return
	 */
	public String getIP(){
		
		return this.IP;
		
	}
	
	/**
	 * 
	 * @param IP
	 */
	public void setIP(String IP){
		
		this.IP = IP;
			
	}
    
	/**
	 * 
	 * @return
	 */
	public boolean getConStatus(){ return this.conStatus; }
	
	/**
	 * 
	 */
	private void log(String msg){
			
		if(this.view != null){
			view.log(msg);
		}
		else{
			System.out.println(msg);
		}
			
	}
		
	private String IP;
	private Connection con;
	private boolean conStatus;
		
	private WTView view;
		
	public static final int BANKS_BANK_ID = 1;
	public static final int BANKS_BANK_NAME = 2;
		
	public static final int CLIENTS_CLIENT_ID = 1;
	public static final int CLIENTS_CLIENT_USERNAME = 2;
	public static final int CLIENTS_CLIENT_PWD = 3;
	
	public static final int ACCOUNTS_ACCOUNT_ID = 1;
	public static final int ACCOUNTS_BANK_ID = 2;
	public static final int ACCOUNTS_CLIENT_ID = 3;
		public static final int ACCOUNTS_BALANCE = 4;
		
		public static final String LOCALHOST = "127.0.0.1";
		
		public static final String PORT = "3306";
		
		public static final String DB_NAME = "bank_db";
		
		public static final String DB_USER_NAME = "bankusr";
		
		public static final String DB_USER_PWD = "bankpwd";
		
	
	
}
