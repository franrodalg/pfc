package dflibrary.library.param;

import dflibrary.utils.ba.BAUtils;

/**
 * Represents the storage space required for particular structures
 * @author Francisco Rodriguez Algarra
 */
public class Size {

	/**
	 * Creates an instance of class <code>Size</code> 
	 * @param size an int representing the size in bytes
	 */
	public Size(int size){
		
		this.size = size;
		this.type = SizeType.BYTES;
		
	}
	
	/**
	 * Creates an instance of class <code>Size</code>
	 * @param size an int representing the size
	 * @param type an instance of class <code>SizeType</code> that
	 * indicates how the <code>size</code> parameter should be interpreted
	 */
	public Size(int size, SizeType type){	
		
		if(type == SizeType.TWOn){			
			
			boolean b = ((size & 0x01)==1);
			int n = size / 2;
			if(!b){
				this.size = (int) Math.pow(2, (double) n);					
			}			
			else{
				this.size = ((int) Math.pow(2, (double) n) + 
						(int) Math.pow(2, (double) (n + 1)))/2;
			}
			this.type = SizeType.BYTES;
		}
		else{
			this.size = size;
			this.type = type;
		}
		
	}
	
	/**
	 * Creates an instance of class <code>Size</code> 
	 * @param size a byte array representing the size in bytes
	 */
	public Size(byte[] size){
		
		if(size == null) throw new NullPointerException();
		if(size.length != 3) throw new IllegalArgumentException();
		
		this.size = BAUtils.toInt(size);
		this.type = SizeType.BYTES;
		
	}
	
	/**
	 * Creates an instance of class <code>Size</code>
	 * @param size an byte array representing the size
	 * @param type an instance of class <code>SizeType</code> that
	 * indicates how the <code>size</code> parameter should be interpreted
	 */
	public Size(byte[] size, SizeType type){
		
		if((size == null) || (type == null)) 
			throw new NullPointerException();
		
		if(type == SizeType.TWOn){
			if(size.length != 1) 
				throw new IllegalArgumentException();
			
			boolean b = ((size[0] & 0x01) == 1);
			int n = BAUtils.toInt(size) / 2;
			if(!b){
				this.size = (int) Math.pow(2, (double) n);				
			}			
			else{
				this.size = ((int) Math.pow(2, (double) n) + 
						(int) Math.pow(2, (double) (n + 1)))/2;
			}
			this.type = SizeType.BYTES;
		}
		else{
			if(size.length != 3) 
				throw new IllegalArgumentException();
			this.size = BAUtils.toInt(size);
			this.type = type;
		}
	}
	
	/**
	 * @return an int representing the size
	 */
	public int getSize(){ return this.size; }
	
	/**
	 * an instance of class <code>SizeType</code> that
	 * indicates how the <code>size</code> should be interpreted
	 */
	public SizeType getType(){ return this.type; }
	
	/**
	 * 
	 * @return the byte array representation of the size number
	 */
	public byte[] toBA(){ return BAUtils.toBA(this.size, 3);}
	
	@Override
	public String toString(){
		
		return "" + this.size + " " + this.type;		
		
	}
	
	private int size;
	private SizeType type;
	
	/**
	 * Provides singleton objects to represent the different size 
	 * representations
	 * @author Francisco Rodriguez Algarra
	 *
	 */
	public enum SizeType{
		
		/**
		 * Size represented in number of bytes
		 */
		BYTES{
			public String toString(){ return "Bytes";}
		},
		/**
		 * Size represented in number of records
		 */
		RECORDS{
			public String toString(){ return "Records";}
		},
		/**
		 * Size represented as a power of 2
		 */
		TWOn{
			public String toString(){ return "Bytes"; }
		};
		
	}
	
}
