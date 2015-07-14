package dflibrary.samples.wallet;

import java.sql.Timestamp;

/**
 * 
 * @author Francisco Rodríguez Algarra
 *
 */
public class Event {

	/**
	 * 
	 * @param event_id
	 * @param description
	 * @param date
	 */
	public Event(int event_id, String description, Timestamp date){
	
		this.setEventID(event_id);
		this.setDescription(description);
		this.setDate(date);
		
	}
	
	/**
	 * 
	 * @return
	 */
	public int getEventID(){
		return this.event_id;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getDescription(){
		return this.description;
	}
	
	public Timestamp getDate(){
		return this.date;
	}
	
	/**
	 * 
	 * @param event_id
	 */
	public void setEventID(int event_id){
		this.event_id = event_id;
	}
	
	public void setDescription(String description){
		this.description = description;
	}
	
	/**
	 * 
	 * @param date
	 */
	public void setDate(Timestamp date){
		this.date = date;
	}
	
	@Override
	public String toString(){
		
		return description;
		
	}
	
	private int event_id;
	private String description;
	private Timestamp date;
		
}
