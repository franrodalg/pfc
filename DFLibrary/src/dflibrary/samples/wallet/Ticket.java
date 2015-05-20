package dflibrary.samples.wallet;

import dflibrary.utils.ba.*;

/**
 * 
 * @author Francisco Rodríguez Algarra
 *
 */
public class Ticket {

	/**
	 * 
	 * @param id
	 * @param event
	 * @param user
	 * @param state
	 */
	public Ticket(int id, int event, int user, boolean state){
		
		this.id = id;
		this.event = event;
		this.user = user;	
		this.state = state;
		this.ubication = new Ubication();
		this.price = 0;
		
	}
	
	/**
	 * 
	 * @param ticket
	 */
	public Ticket(byte[] ticket){
		
		this.id = BAUtils.toInt(BAUtils.extractSubBA(ticket, 0, 4));		
		this.event = BAUtils.toInt(BAUtils.extractSubBA(ticket, 4, 4));
		this.user = BAUtils.toInt(BAUtils.extractSubBA(ticket, 8, 4));
		this.state = BAUtils.toBoolean(BAUtils.extractSubBA(ticket, 12, 4));
		this.ubication = new Ubication();
		this.price = 0;
		
	}
	
	/**
	 * 
	 * @param id
	 * @param event
	 * @param user
	 * @param state
	 * @param area
	 * @param zone
	 * @param row
	 * @param seat
	 */
	public Ticket(int id, int event, int user, boolean state, 
			int area, int zone, int row, int seat){
		
		this(id, event, user, state);
		this.ubication = new Ubication(area, zone, row, seat);
		
	}
	
	/**
	 * 
	 * @param id
	 * @param event
	 * @param user
	 * @param state
	 * @param area
	 * @param zone
	 * @param row
	 * @param seat
	 * @param price
	 */
	public Ticket(int id, int event, int user, boolean state, 
			int area, int zone, int row, int seat, int price){
		
		this(id, event, user, state);
		this.ubication = new Ubication(area, zone, row, seat);
		this.price = price;
		
		
	}
	
	/**
	 * 
	 * @return
	 */
	public int getId(){
		return this.id;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getUser(){
		return this.user;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getEvent(){
		return this.event;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isUsed(){
		return this.state;
	}
	
	/**
	 * 
	 * @return
	 */
	public Ubication getUbication(){
		return this.ubication;
	}
		
	/**
	 * 
	 * @return
	 */
	public int getPrice(){
		
		return this.price;
		
	}
	
	/**
	 * 
	 */
	public void used(){
		this.state = true;
	}
	
	/**
	 * 
	 * @param id
	 */
	public void setId(int id){this.id = id;}
	
	/**
	 * 
	 * @param user
	 */
	public void setUser(int user){this.user = user;}
	
	/**
	 * 
	 * @param event
	 */
	public void setEvent(int event){this.event = event;}
	
	/**
	 * 
	 * @param state
	 */
	public void setState(boolean state){this.state = state;}
	
	/**
	 * 
	 * @return
	 */
	public byte[] toBA(){
		
		byte[] idBA = BAUtils.toBA(this.id, 4);
		byte[] eventBA = BAUtils.toBA(this.event, 4);
		byte[] userBA = BAUtils.toBA(this.user, 4);		
		byte[] stateBA = BAUtils.toBA(this.state, 4);
		byte[] res = BAUtils.concatenateBAs(idBA, eventBA, userBA, stateBA);
		
		return res;
		
	}
	
	@Override
	public String toString(){
		
		String s = "";
		
		s = s + "Ticket id: " + id + "\n";
		s = s + "User id: " + user + "\n";
		s = s + "Event id: " + event + "\n";
		s = s + "Ubication: " + ubication + "\n";
		s = s + "Price: " + price + "\n";
		s = s + "Used: "  + state;
		
		return s;
		
	}
	
	private int id;
	private int user;
	private int event;
	private boolean state;
	private Ubication ubication;
	private int price;

	/**
	 * 
	 * @author Francisco Rodríguez Algarra
	 *
	 */
	class Ubication{

		public Ubication(){}
		
		/**
		 * 
		 * @param area
		 * @param zone
		 * @param row
		 * @param seat
		 */
		public Ubication(int area, int zone, int row, int seat){
						
			this.setArea(area);
			this.setZone(zone);
			this.setRow(row);
			this.setSeat(seat);
			
		}		

		/**
		 * 
		 * @return
		 */
		public int getArea(){return this.area;}
		
		/**
		 * 
		 * @return
		 */
		public int getZone(){return this.zone;}
		
		/**
		 * 
		 * @return
		 */
		public int getRow(){return this.row;}
		
		/**
		 * 
		 * @return
		 */
		public int getSeat(){return this.seat;}
		
		/**
		 * 
		 * @param area
		 */
		public void setArea(int area){this.area = area;}
		
		/**
		 * 
		 * @param zone
		 */
		public void setZone(int zone){this.zone = zone;}
		
		/**
		 * 
		 * @param row
		 */
		public void setRow(int row){this.row = row;}
		
		/**
		 * 
		 * @param seat
		 */
		public void setSeat(int seat){this.seat = seat;}
	
		@Override
		public String toString(){
			
			String s = "";
			
			s = s + "Area: " + getArea();
			s = s + ", Zone: " + getZone();
			s = s + ", Row: " + getRow();
			s = s + ", Seat: " + getSeat();
			
			return s;
			
			
		}
		
		private int area;
		private int zone;
		private int row;
		private int seat;
		
	}
	
}
