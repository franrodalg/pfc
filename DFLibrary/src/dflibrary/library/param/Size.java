package dflibrary.library.param;

import dflibrary.utils.ba.BAUtils;


/**
 * 
 * @author frankie
 *
 */
public class Size {

	/**
	 * 
	 * @param size
	 */
	public Size(int size){
		
		this.size = size;
		this.type = SizeType.BYTES;
		
	}
	
	/**
	 * 
	 * @param size
	 * @param type
	 */
	public Size(int size, SizeType type){	
		
		if(type == SizeType.TWOn){			
			
			boolean b = ((size & 0x01)==1);
			int n = size / 2;
			if(!b){
				this.size = (int) Math.pow(2, (double) n);					
			}			
			else{
				this.size = ((int) Math.pow(2, (double) n) + (int) Math.pow(2, (double) (n + 1)))/2;
			}
			this.type = SizeType.BYTES;
		}
		else{
			this.size = size;
			this.type = type;
		}
		
	}
	
	/**
	 * 
	 * @param size
	 */
	public Size(byte[] size){
		
		if(size == null) throw new NullPointerException();
		if(size.length != 3) throw new IllegalArgumentException();
		
		this.size = BAUtils.toInt(size);
		this.type = SizeType.BYTES;
		
	}
	
	/**
	 * 
	 * @param size
	 * @param type
	 */
	public Size(byte[] size, SizeType type){
		
		if((size == null) || (type == null)) throw new NullPointerException();
		
		if(type == SizeType.TWOn){
			if(size.length != 1) throw new IllegalArgumentException();
			
			boolean b = ((size[0] & 0x01) == 1);
			int n = BAUtils.toInt(size) / 2;
			if(!b){
				this.size = (int) Math.pow(2, (double) n);				
			}			
			else{
				this.size = ((int) Math.pow(2, (double) n) + (int) Math.pow(2, (double) (n + 1)))/2;
			}
			this.type = SizeType.BYTES;
		}
		else{
			if(size.length != 3) throw new IllegalArgumentException();
			this.size = BAUtils.toInt(size);
			this.type = type;
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public int getSize(){ return this.size; }
	
	/**
	 * 
	 * @return
	 */
	public SizeType getType(){ return this.type; }
	
	/**
	 * 
	 * @return
	 */
	public byte[] toBA(){ return BAUtils.toBA(this.size, 3);}
	
	/**
	 * 
	 */
	public String toString(){
		
		return "" + this.size + " " + this.type;		
		
	}
	
	private int size;
	private SizeType type;
	
	public enum SizeType{
		
		
		BYTES{
			public String toString(){ return "Bytes";}
		},
		RECORDS{
			public String toString(){ return "Records";}
		},
		TWOn{
			public String toString(){ return "Bytes"; }
		};
		
		
	}
	
}
