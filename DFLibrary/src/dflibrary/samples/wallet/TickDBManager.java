package dflibrary.samples.wallet;

import java.sql.*;

/**
 * 
 * @author Francisco Rodríguez Algarra
 *
 */
public class TickDBManager {
	
		/**
		 * 
		 */
		public TickDBManager(WTView view){

			setIP(LOCALHOST);
			this.view = view;
					
		}
		
		
		protected void connect(){ setConnection();}
		
		/**
		 * 
		 */
		private void setConnection(){
			
			view.log("Trying to connect to database");
			
			try {
	            Class.forName("com.mysql.jdbc.Driver");
	        } catch (Exception e) {
	        	e.printStackTrace();
	            throw new RuntimeException("MySql Driver creation failed.");
	        }
	        
	        try {
	            con = DriverManager.getConnection("jdbc:mysql://" + 
	            		getIP() + ":" + PORT + "/" + "chivas_db", 
	            		"chivasweb", "mysqlchivasweb");
	        } catch (Exception e){
	        	throw new RuntimeException("Database connection failed.");
	        }
	        
	        this.conStatus = true;
	        view.log("Database connection successful");
	        
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
	        view.log("Database disconnection successful");
			
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
		public Event[] getEvents(){
			
			Event[] res = null;
			
	        try {
	 
	            Statement s = con.createStatement();
	            ResultSet rs = s.executeQuery ("SELECT * FROM events");
	            
	            if(!rs.next()) return null;
	            
	            rs.last();
	            
	            res = new Event[rs.getRow()];
	            rs.beforeFirst();
	            int i = 0;
	            
	            while (rs.next()) {
	             	
	            	
	                res[i] = new Event(rs.getInt(EVENTS_EVENT_ID), rs.getString(EVENTS_DESCRIPTION), rs.getTimestamp(EVENTS_DATE));
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
		 * @param event
		 * @return
		 */
		public String[] getDates(String event){
			
			String[] res = null;
			
			  try {
				  
		            Statement s = con.createStatement();
		            ResultSet rs = s.executeQuery ("SELECT * FROM events WHERE description = '" + event + "'");
		            
		            if(!rs.next()) return null;
		            
		            rs.last();
		            
		            res = new String[rs.getRow()];
		            rs.beforeFirst();
		            int i = 0;
		            
		            while (rs.next()) {
		             		            	
		                res[i] = rs.getString(EVENTS_DATE);
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
		 * @param description
		 * @param date
		 * @return
		 */
		public int getEventID(String description, String date){
			
			int event_id;
		       
	        try {

	            Statement s = con.createStatement();
	            ResultSet rs = s.executeQuery ("SELECT event_id FROM events WHERE description = '" + description +
	            		"' AND date = '" + date + "'");            
	            
	            if(!rs.next()) throw new RuntimeException("Unknown event");
	            rs.beforeFirst();
	            rs.next();
	            
	            event_id = rs.getInt(EVENTS_EVENT_ID);
	            
	        } catch (Exception e) {
	        	throw new RuntimeException(e.toString());
	        }
			
			return event_id;
						
		}
		
		/**
		 * 
		 * @param event_id
		 * @return
		 */
		public int[] getAvTickets(int event_id){
			
			int[] res = null;
		       
	        try {
	 
	            Statement s = con.createStatement();
	            ResultSet rs = s.executeQuery ("SELECT ticket_id " +
	            								"FROM tickets " +
	            								"WHERE event_id = " + event_id + 
	            								" AND user_id = 0");
	            
	            
	            if(!rs.next()) return null;
	            
	            rs.last();
	            
	            res = new int[rs.getRow()];
	            rs.beforeFirst();
	            int i = 0;
	            
	            while (rs.next()) {
	                res[i] = rs.getInt(TICKETS_TICKET_ID);
	                i++;
	            }
	        } catch (Exception e) {
	        	throw new RuntimeException(e.toString());
	        }
			
			return res;
			
		}
		
		/**
		 * 
		 * @param description
		 * @param date
		 * @return
		 */
		public int[] getAvTickets(String description, String date){
			
			int[] res = null;
		       
	        try {

	            Statement s = con.createStatement();
	            ResultSet rs = s.executeQuery ("SELECT event_id FROM events " +
	            		"WHERE description = '" + description + "' " +
	            		"AND date = '" + date + "'");            
	            
	            if(!rs.next()) throw new RuntimeException("Unknown event");
	            rs.beforeFirst();
	            rs.next();
	            
	            int event_id = rs.getInt(EVENTS_EVENT_ID);
	            
	            s = con.createStatement();
	            rs = s.executeQuery("SELECT ticket_id FROM tickets " +
	            		"WHERE event_id = " + event_id + " AND user_id = 0");
	            
	            if(!rs.next()) throw new RuntimeException("No available tickets");
	            
	            rs.last();
	            
	            res = new int[rs.getRow()];
	            rs.beforeFirst();
	            int i = 0;
	            
	            while (rs.next()) {
	                res[i] = rs.getInt(TICKETS_TICKET_ID);
	                i++;
	            }
	        } catch (Exception e) {
	        	throw new RuntimeException(e.toString());
	        }
			
			return res;
						
		}
				
		/**
		 * 
		 * @param ticket_id
		 * @return
		 */
		public Ticket getTicket(int ticket_id){
		       
	        try {
	 
	            Statement s = con.createStatement();
	            ResultSet rs = s.executeQuery ("SELECT * FROM tickets " +
	            		"WHERE ticket_id = " + ticket_id);
	            
	            
	            if(!rs.next()) return null;
	            
	            rs.beforeFirst();
	            rs.next();
	            
	            int id = rs.getInt(TICKETS_TICKET_ID);
	            int event = rs.getInt(TICKETS_EVENT_ID);
	            int user = rs.getInt(TICKETS_USER_ID);
	            boolean state = (rs.getInt(TICKETS_STATE) == 0) ? false : true;
	            
	            int area = rs.getInt(TICKETS_AREA);
	            int zone = rs.getInt(TICKETS_ZONE);
	            int row = rs.getInt(TICKETS_ROW);
	            int seat = rs.getInt(TICKETS_SEAT);
	            int price = rs.getInt(TICKETS_PRICE);
	            
	            return new Ticket(id, event, user, state, 
	            		area, zone, row, seat, price);
	            
	           
	        } catch (Exception e) {
	        	throw new RuntimeException(e.toString());
	        }
			
		}

		/**
		 * 
		 * @param uid
		 * @return
		 */
		public int getUserId(String uid){
			
			 try {
				 
		           Statement s = con.createStatement();
		           ResultSet rs = s.executeQuery ("SELECT user_id FROM users " +
		           		"WHERE card_id = '" + uid + "'");	            
		            
		           if(!rs.next()) throw new RuntimeException("Unregistered card");
		           
		           int user_id = rs.getInt(USERS_USER_ID);
		           
		           return user_id;
		           
		       } catch (Exception e) {
		    	   throw new RuntimeException(e.toString());
		       }
			
		}
		
		/**
		 * 
		 * @param ticket_id
		 * @param user_id
		 */
		public void registerTicket(int ticket_id, int user_id){
			
			   
	        try {
	 
	            Statement s = con.createStatement();
	            s.executeUpdate ("UPDATE tickets SET user_id = " + user_id + 
	            					" WHERE ticket_id = " + ticket_id);

	        } catch (Exception e) {
	        	throw new RuntimeException(e.toString());
	        }			
			
		}
		
		/**
		 * 
		 * @param ticket
		 */
		public void checkTicket(Ticket ticket){
			
			 try {
				 
		           Statement s = con.createStatement();
		           ResultSet rs = s.executeQuery ("SELECT * FROM tickets WHERE ticket_id = " + ticket.getId());	            
		            
		           if(!rs.next()) throw new RuntimeException("Invalid ticket");
		           
		           if(rs.getInt(TICKETS_STATE) != 0) throw new RuntimeException("Ticket already used");
		           
		           int event_id = rs.getInt(TICKETS_EVENT_ID);
		           if(event_id != ticket.getEvent()) throw new RuntimeException("Invalid ticket");
		           int user_id = rs.getInt(TICKETS_USER_ID);
		           if(user_id != ticket.getEvent()) throw new RuntimeException("Invalid ticket");
		           
		           
		       } catch (Exception e) {
		    	   throw new RuntimeException(e.toString());
		       }
			
		}
		
		/**
		 * 
		 * @param ticket_id
		 */
		public void markUsed(int ticket_id){
			
	        try {
	        	 
	            Statement s = con.createStatement();
	            s.executeUpdate ("UPDATE tickets SET state = 1 WHERE ticket_id = " + ticket_id);

	        } catch (Exception e) {
	        	throw new RuntimeException(e.toString());
	        }
			
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
		
		private String IP;
		private Connection con;
		private boolean conStatus;
		
		private WTView view;
		
		public static final int EVENTS_EVENT_ID = 1;
		public static final int EVENTS_DESCRIPTION = 2;
		public static final int EVENTS_DATE = 3;
		public static final int EVENTS_OCCUPANCY = 4;
		
		public static final int TICKETS_TICKET_ID = 1;
		public static final int TICKETS_EVENT_ID = 2;
		public static final int TICKETS_USER_ID = 3;
		public static final int TICKETS_NOT_BEFORE = 4;
		public static final int TICKETS_NOT_AFTER = 5;
		public static final int TICKETS_AREA = 6;
		public static final int TICKETS_ZONE = 7;
		public static final int TICKETS_ROW = 8;
		public static final int TICKETS_SEAT = 9;
		public static final int TICKETS_PRICE = 10;
		public static final int TICKETS_STATE = 11;
		
		public static final int USERS_USER_ID = 1;
		public static final int USERS_FIRST_NAME = 2;
		public static final int USERS_LAST_NAME = 3;
		public static final int USERS_CARD_ID = 4;
		
		public static final String LOCALHOST = "127.0.0.1";
		public static final String REMOTE = "147.83.39.241";
		
		public static final String PORT = "3306";
		
	
	
}
