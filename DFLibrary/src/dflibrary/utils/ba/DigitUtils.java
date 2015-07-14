package dflibrary.utils.ba;

/**
*
* A collection of methods designed for providing help in Digit Format treatment.
* 
* @author Francisco Rodríguez Algarra
* @version 28.8.2012
*
*/

public class DigitUtils {
	
	//TODO 2 Revisar comprobaciones redundantes
	//TODO 2 Revisar excepciones lanzadas y documentarlas
	//TODO 5 Ampliación: Strings con formato implícito (Ej: 0xA5)
    //TODO 5 Ampliación: Octal y Decimal

	
	/**
	 * Format of the digits representing a value
	 */
	public enum DigitFormat{
		/**
		 * Hexadecimal
		 */
		HEX{
			public String toString(){
				return "Hexadecimal";
			}
			
		},
		/**
		 * Binary
		 */
		BIN{
			public String toString(){
				return "Binary";
			}
			
		},
		/**
		 * Decimal
		 */
		DEC{
			public String toString(){
				return "Decimal";
			}
			
		},
		/**
		 * Octal
		 */
		OCT{
			public String toString(){
				return "Octal";
			}
			
		};	
	}
	
	/**
	 * Indicates whether a number should be interpreted as Signed or Unsigned
	 */
	public static enum SignMode {
		/**
		 * A signed number.
		 */
		SIGNED{
			public String toString(){
				return "Signed";
			}
			
		},
		/**
		 * An unsigned number.
		 */
		UNSIGNED{
			public String toString(){
				return "Unsigned";
			}
			
		};
	}
	
	/**
	 * Byte significance order of the number representation.
	 */
	public static enum ByteOrder {
		/**
		 * Less Significant Byte to Most Significant Byte
		 */
		LSB_MSB{
			public String toString(){
				return "Less Significant Byte to Most Significant Byte";
			}
			
		},
		/**
		 * Most Significant Byte to Less Significant Byte
		 */
		MSB_LSB{
			public String toString(){
				return "Most Significant Byte to Less Significant Byte";
			}
			
		};
	}
	
    /**
     * Sole constructor. (For invocation by subclass constructors, typically implicit.)
     */
    
    protected DigitUtils(){}
    
    
    /**
     * Returns a byte representing the value stored in the hexadecimal String <code>s</code>.
     * 
     * @param s the String containing a byte in hexadecimal format
     * @return a byte equivalent to <code>s</code>
     * @throws IllegalArgumentException
     */
    public static byte toByte(String s){
    	
    	return toByte(s, DigitFormat.HEX);	
    	
    }
    
    /**
     * Returns a byte representing the value stored in the String <code>s</code>.
     * 
     * @param s the String containing a byte
     * @param df the Digit Format of the String <code>s</code>
     * @return a byte equivalent to <code>s</code>
     * @throws IllegalArgumentException
     */
    
    public static byte toByte(String s, DigitFormat df){   	
    	
    	if(!checkDigits(s,df)) throw new IllegalArgumentException("Invalid " + df + " Digit Format: " + s);  
    	   	
    	if(numOfBytes(s,df)!=1) throw new IllegalArgumentException("Invalid String Length: " + s);
    	
    	return (byte)toInt(s,df);  			 		  	
    }
    
    /**
     * Returns a byte representing the value stored in the hexadecimal char <code>c</code>.
     * 
     * @param c the char containing a digit in hexadecimal format
     * @return a byte equivalent to <code>c</code>
     * @throws IllegalArgumentException
     */
    public static byte toByte(char c){
    	
    		return toByte(c, DigitFormat.HEX);

    }
    
    /**
     * Returns a byte representing the value stored in the char <code>s</code>.
     * 
     * @param c the char containing a digit
     * @param df the Digit Format of the char <code>c</code>
     * @return a byte equivalent to <code>c</code>
     * @throws IllegalArgumentException
     */
    public static byte toByte(char c, DigitFormat df){
    	
    	if(!checkDigit(c, df)) throw new IllegalArgumentException("Invalid " + df + " Digit Format: " + c);
    	  	
    	return (byte)toInt(c, df);
    	
    }
        
    /**
     * Returns a unsigned int number representing the value stored in the String <code>s</code> 
     * in hexadecimal digit format, with bytes ordered form less significant to most significant.
     * 
     * @param s the String containing a unsigned number
     * @return a unsigned int number equivalent to <code>s</code>
     * @throws IllegalArgumentException
     */
    public static int toInt(String s){
    	
    	return toInt(s, DigitFormat.HEX, SignMode.UNSIGNED, ByteOrder.LSB_MSB);

    }
    
    /**
     *  Returns a unsigned int number representing the value stored in the String <code>s</code> 
     *  in digit format <code>df</code>, with bytes ordered from less significant to most significant.
     * 
     * @param s the String containing a unsigned number
     * @param df the Digit Format of the String <code>s</code>
     * @return a unsigned int number equivalent to <code>s</code>
     * @throws IllegalArgumentException
     */
    public static int toInt(String s, DigitFormat df){
    	
    	return toInt(s, df, SignMode.UNSIGNED, ByteOrder.LSB_MSB);
  	
    }
    
    //TODO 3 Documentar
    
    /**
     * 
     * 
     * @param s
     * @param sm
     * @return
     */
    public static int toInt(String s, SignMode sm){
    	
    	return toInt(s, DigitFormat.HEX, sm, ByteOrder.LSB_MSB);
    	
    }
    
    //TODO 3 Documentar
    
    /**
     * 
     * 
     * @param s
     * @param bo
     * @return
     */
    public static int toInt(String s, ByteOrder bo){
    	
    	return toInt(s, DigitFormat.HEX, SignMode.UNSIGNED, bo);
    	
    }
    
    /**
     *  Returns a int number of sign mode <code>sm</code> representing the value stored in the 
     *  String <code>s</code> in <code>df</code> digit format, with bytes ordered from less significant 
     *  to most significant.
     * 
     * @param s the String containing a number
     * @param df the Digit Format of the String <code>s</code>
     * @param sm the Sign Mode of the value stored in <code>s</code>
     * @return a int number equivalent to <code>s</code>
     * @throws IllegalArgumentException
     */
    public static int toInt(String s, DigitFormat df, SignMode sm){

    	return toInt(s, df, sm, ByteOrder.LSB_MSB);
    	
    }
    
    /**
     * 
     * 
     * @param s
     * @param df
     * @param bo
     * @return
     */
    public static int toInt(String s, DigitFormat df, ByteOrder bo){

    	return toInt(s, df, SignMode.UNSIGNED, bo);
    	
    }
  
    /**
     * 
     * @param s
     * @param sm
     * @param bo
     * @return
     */
    public static int toInt(String s, SignMode sm, ByteOrder bo){

    	return toInt(s, DigitFormat.HEX, sm, bo);
    	
    }
    
    /**
     *  Returns a int number of sign mode <code>sm</code> representing the value stored in the 
     *  String <code>s</code> in <code>df</code> digit format, with bytes ordered as specified in 
     *  <code>bo</code>
     * 
     * @param s the String containing a number
     * @param df the Digit Format of the String <code>s</code>
     * @param sm the Sign Mode of the value stored in <code>s</code>
     * @param bo the Byte Order of the value stored in <code>s</code>
     * @return a int number equivalent to <code>s</code>
     * @throws IllegalArgumentException
     */
    public static int toInt(String s, DigitFormat df, SignMode sm, ByteOrder bo){
    	   	

    	boolean neg = false;
    	
    	if((sm == SignMode.SIGNED) && isNeg(s,df,bo)){
    		neg = true;
    		s = twosComp(s,df,bo);
    	}
    	
    	int len = numOfBytes(s, df);
    	if(!checkIntSpace(s, df, sm, bo)) throw new IllegalArgumentException("Invalid String Length: " + s);   	
       	
    	int blen = s.length()/len;
    	
    	int num = 0;
    	
		if(bo == ByteOrder.LSB_MSB){
			for(int i = 0; i<len;i++){
				num = num + byteStringValue(s.substring(i*blen, i*blen + blen), df)*(int)Math.pow(256, i);
			}
		}
		else{
			for(int i = len-1; i>=0;i--){
				num = num + byteStringValue(s.substring(i*blen, i*blen + blen), df)*(int)Math.pow(256, len-i-1);
			}			
		}
		
		if(neg) num = -num;
		
    	return num;
    	    	
    }
    
    //TODO 3 Documentar byteValue(String s, DigitFormat df)
    
    public static int byteStringValue(String s){
    	
    	return byteStringValue(s, DigitFormat.HEX);    	
    	
    }
    
    /**
     * UNSIGNED
     * 
     * @param s the String containing a number
     * @param df the Digit Format of the String <code>s</code>
     * @return
     * @throws IllegalArgumentException
     */
    public static int byteStringValue(String s, DigitFormat df){
    	
    	
    	int base;
    	
    	if(numOfBytes(s, df)!=1) throw new IllegalArgumentException("Invalid String Length: " + s);
    	
    	if(df == DigitFormat.HEX) base = 16;
    	else if(df == DigitFormat.BIN) base = 2;
    	else throw new IllegalArgumentException(df + " Digit Format Not Supported in " +
    			"byteStringValue(String, DigitFormat):int");
    	
    	int num = 0;
		for (int i = s.length()-1; i >= 0; i--){
			char c = s.charAt(i);
			int n = toInt(c);
			num = num + n*(int)Math.pow(base, s.length()-i-1); 		
		}
    	
		return num;
    	
    }
    
    /**
     * Returns a int number representing the value stored in the hexadecimal char <code>s</code>.
     * 
     * @param c the char containing a hexadecimal digit
     * @return a byte equivalent to <code>c</code> 
     * @throws IllegalArgumentException
     * 
     */
    public static int toInt(char c){
    	
    		return toInt(c, DigitFormat.HEX);	
    	
    }
    
    //TODO 3 Documentar toInt(char c, DigitFormat df)
    
    /**
     * 
     * @param c the char containing a digit
     * @param df the Digit Format of the char <code>c</code>
     * @return
     * @throws IllegalArgumentException
     */
    public static int toInt(char c, DigitFormat df){
    	
    	if(!checkDigit(c,df)) throw new IllegalArgumentException("Invalid " + df + " Digit Format: " + c);
    	
    	
    	if((c >= '0')&&(c<='9')) return c - '0';
    	else if ((c >= 'a')&&(c <= 'f')) return c - 'a' + 10;
    	else return c - 'A' + 10;
    	
    }
    
    //TODO 3 Documentar toString(String, DigitFormat, DigitFormat)
    
    /**
     * 
     * 
     * @param s the String containing a number
     * @param idf the Digit Format of the String <code>s</code>
     * @param odf the Digit Format of the output String
     * @return
     * @throws IllegalArgumentException
     */
    public static String toString(String s, DigitFormat idf, DigitFormat odf){
    	
    		return toString(toInt(s,idf), odf);    	
    }
    
    //TODO 3 Documentar toString(byte b)
    
    /**
     * 
     * 
     * @param b
     * @return
     * @throws IllegalArgumentException
     */
    public static String toString(byte b){
    	
    		return toString(b, DigitFormat.HEX, SignMode.UNSIGNED);

    } 
    
    //TODO 3 Documentar toString(byte b, DigitFormat df)
    
    /**
     * 
     * 
     * @param b
     * @param df the Digit Format of the output String
     * @return
     * @throws IllegalArgumentException
     */
    public static String toString(byte b, DigitFormat df){
    	
    		return toString(b, df, SignMode.UNSIGNED);
    	
    }
    
    /**
     * 
     * @param b
     * @param sm
     * @return
     */
    public static String toString(byte b, SignMode sm){
    	
		return toString(b, DigitFormat.HEX, sm);
	
    }
    
    //TODO 3 Documentar toString(byte b, DigitFormat df, SignMode sm)
    
    /**
     * 
     * @param b
     * @param df the Digit Format of the output String
     * @param sm the Sign Mode of the output String
     * @return
     * @throws IllegalArgumentException
     */
    public static String toString(byte b, DigitFormat df, SignMode sm){
    	
    		return toByteString((int)b, df);
    	
    }
    
    //TODO 3 Documentar toString(int num)
    
    /**
     * 
     * 
     * @param num
     * @return
     * @throws IllegalArgumentException
     */
    public static String toString(int num){
    	
    		return toString(num, DigitFormat.HEX, SignMode.UNSIGNED, ByteOrder.LSB_MSB);

    } 
    
    //TODO 3 Documentar toString(int num, DigitFormat df)
    
    /**
     * 
     * 
     * @param num
     * @param df the Digit Format of the output String
     * @return
     * @throws IllegalArgumentException
     */
    public static String toString(int num, DigitFormat df){
    	
    		return toString(num, df, SignMode.UNSIGNED, ByteOrder.LSB_MSB);
    		
    }
    
    //TODO 3 Documentar
    
    /**
     * 
     * @param num
     * @param sm
     * @return
     */
    public static String toString(int num, SignMode sm){
    	
		return toString(num, DigitFormat.HEX, sm, ByteOrder.LSB_MSB);
		
    }
    
    //TODO 3 Documentar
    
    /**
     * 
     * @param num
     * @param bo
     * @return
     */
    public static String toString(int num, ByteOrder bo){
    	
		return toString(num, DigitFormat.HEX, SignMode.UNSIGNED, bo);
		
    }
    
    //TODO 3 Documentar
    
    /**
     * 
     * 
     * @param num
     * @param df
     * @param sm
     * @return
     */
    public static String toString(int num, DigitFormat df, SignMode sm){
    	
    	return toString(num, df, sm, ByteOrder.LSB_MSB);
    	
    }
    
    //TODO 3 Documentar toString(int num, DigitFormat df, ByteOrder bo)
    
    /**
     * 
     * @param num
     * @param df the Digit Format of the output String
     * @param bo the Byte Order of the output String
     * @return
     * @throws IllegalArgumentException
     */
    public static String toString(int num, DigitFormat df, ByteOrder bo){
    	
    	return toString(num, df, SignMode.UNSIGNED, bo);
    	
    }
    
    //TODO 3 Documentar
    
    /**
     * 
     * 
     * @param num
     * @param sm
     * @param bo
     * @return
     */
    public static String toString(int num, SignMode sm, ByteOrder bo){
    	
    	return toString(num, DigitFormat.HEX, sm, bo);
    	
    }
    
    //TODO 3 Documentar toString(int num, DigitFormat df, SignMode sm, ByteOrder bo)
    
    /**
     * 
     * @param num
     * @param df the Digit Format of the output String
     * @param bo the Byte Order of the output String
     * @return
     * @throws IllegalArgumentException
     */
    public static String toString(int num, DigitFormat df, SignMode sm, ByteOrder bo){
    	
    	StringBuilder sb = new StringBuilder();
    	String s;
    	int len, aux;
    	int mask = 0xFF;
    	
    	len = numOfBytes(num, sm);   	
    	
    	for(int i = 0; i<len; i++){
    		
    		aux = num & mask;
    		
	    	s = toByteString(aux, df);
	    	
	    	if(bo == ByteOrder.LSB_MSB) sb.append(s);
	    	else sb.insert(0, s);
	    	
	    	num = num>>8;
    	}
    	
    	return sb.toString();
    	
    }

    //TODO 3 Documentar toByteString(int n)
    
    
    /**
     * 
     * @param num
     * @return
     */
    public static String toByteString(int num){
    	
    	return toByteString(num, DigitFormat.HEX);
    	
    }
    
    //TODO 3 Documentar toByteString(int n, DigitFormat df)
    
    /**
     * 
     * @param num
     * @param df the Digit Format of the output String
     * @return
     * @throws IllegalArgumentException
     */
    public static String toByteString(int num, DigitFormat df){
    	
    	int blen, aux;
    	byte mask;
    	char c;
    	StringBuilder sb = new StringBuilder();
    	
    	if(numOfBytes(num)!=1) throw new IllegalArgumentException("Invalid Int Length");    		
    	
    	if(df == DigitFormat.HEX){
    		blen = 2;
    		mask = 0x0F;
    		
    	}
    	else if(df == DigitFormat.BIN){
    		blen = 8;
    		mask = 0x01;
    	}
    	else throw new IllegalArgumentException(df + " Digit Format Not Supported in " +
    			"toByteString(int, DigitFormat):String");
    	
    	for(int i = 0; i < blen; i++){
    		aux = num & mask;
    		c = toDigit(aux, df);
    		sb.insert(0, c);
    		num = num>>(8/blen);
    	}
    	
    	return sb.toString();
    	
    }
    
    //TODO 3 Documentar toDigit(int num)
    
    /**
     * 
     * @param num
     * @return
     */
    public static char toDigit(int num){
    	
    	return toDigit(num, DigitFormat.HEX);
    	
    }
    
    //TODO 3 Documentar toDigit(int num, DigitFormat df)
    
    /**
     * 
     * @param num
     * @param df the Digit Format of the output char
     * @return
     * @throws IllegalArgumentException
     */
    public static char toDigit(int num, DigitFormat df){
    	
    	char c;
    	
    	if((num>=0)&&(num<=9)) c = (char)('0' + num);
    	else if((num>=10)&&(num<=15)) c = (char) ('A' + num - 10);
    	else throw new IllegalArgumentException("Invalid int (" + num + ") for a " + df + " Digit");
    	
    	if(!checkDigit(c,df)) throw new IllegalArgumentException("Invalid int (" + num + ") " +
    			"for a " + df + " Digit");
    	
    	return c;
    }   
    
    //TODO 3 Documentar twoscomp(String s)
    
    /**
     * 
     * @param s
     * @return
     */
    public static String twosComp(String s){
		
		return twosComp(s, DigitFormat.HEX, ByteOrder.LSB_MSB);
   	  	
    }
    
	//TODO 3 Documentar twoscomp(String, DigitFormat)
    
    /**
     * 
     * @param s
     * @param df
     * @return
     */
    public static String twosComp(String s, DigitFormat df){
		
		return twosComp(s, df, ByteOrder.LSB_MSB);
   	  	
    }

    //TODO 3 Documentar twosComp(String, ByteOrder)
    
    /**
     * 
     * 
     * @param s
     * @param bo
     * @return
     */
    public static String twosComp(String s, ByteOrder bo){
		
		return twosComp(s, DigitFormat.HEX, bo);
   	  	
    }
    
    //TODO 3 Documentar twoscomp(String, DigitFormat, ByteOrder)
    
    /**
     * 
     * @param s
     * @param df the Digit Format of the String <code>s</code>
     * @param bo
     * @return
     * @throws IllegalArgumentException
     */
    public static String twosComp(String s, DigitFormat df, ByteOrder bo){

		s = not(s, df);
		return toString(toInt(s, df, DigitUtils.SignMode.UNSIGNED, bo) + 1, df, bo);
   	  	
    }
    
    //TODO 3 Documentar not(String)
    
    /**
     * 
     * 
     * @param s
     * @return
     */
    public static String not(String s){
    	
    	return not(s, DigitFormat.HEX);
    	
    }
    
    //TODO 3 Documentar not(String s, DigitFormat df)
    
    /**
     * 
     * 
     * @param s
     * @param df the Digit Format of the String <code>s</code>
     * @return
     * @throws IllegalArgumentException
     */
    public static String not(String s, DigitFormat df){
    	
    	int base;
   	
    	if(df == DigitFormat.HEX){
    		base = 16; 		
    	}
    	else if(df == DigitFormat.BIN){
    		base = 2;
    	}
    	else throw new IllegalArgumentException(df + " Digit Format Not Supported in " +
    			"not(String, DigitFormat):String");
    	
    	StringBuilder sb = new StringBuilder(s);
    	
    	for(int i = 0; i<sb.length(); i++){    		
     			char oc = sb.charAt(i);
    			int n = toInt(oc, df);
    			char nc = toDigit((base-1-n)%base, df);
    			sb.setCharAt(i, nc);
    	}
    	
    	return sb.toString();
    }
    
	//TODO 3 Documentar checkIntSpace(String s)

    
    /**
     * 
     * @param s
     * @return
     */
    public static boolean checkIntSpace(String s){
    	
    	return checkIntSpace(s, DigitFormat.HEX, SignMode.UNSIGNED, ByteOrder.LSB_MSB);
    	
    }
    
    //TODO 3 Documentar checkIntSpace(String s, ByteOrder)

    
    /**
     * 
     * @param s
     * @param df
     * @return
     */
    public static boolean checkIntSpace(String s, DigitFormat df){
    	
    	return checkIntSpace(s, df, SignMode.UNSIGNED, ByteOrder.LSB_MSB);
    	
    }
    
    //TODO 3 Documentar checkIntSpace(String, SignMode)

    
    /**
     * 
     * @param s
     * @param sm
     * @return
     */
    public static boolean checkIntSpace(String s, SignMode sm){
    	
    	return checkIntSpace(s, DigitFormat.HEX, sm, ByteOrder.LSB_MSB);
    	
    }
    
    //TODO 3 Documentar checkIntSpace(String s, ByteOrder)

    
    /**
     * 
     * @param s
     * @param bo
     * @return
     */
    public static boolean checkIntSpace(String s, ByteOrder bo){
    	
    	return checkIntSpace(s, DigitFormat.HEX, SignMode.UNSIGNED, bo);
    	
    }
    
    //TODO 3 Documentar checkIntSpace(String s, DigitFormat, SignMode)
    
    /**
     * 
     * @param s
     * @param df
     * @param sm
     * @return
     */
    public static boolean checkIntSpace(String s, DigitFormat df, SignMode sm){
    	
    	return checkIntSpace(s, df, sm, ByteOrder.LSB_MSB);
    	
    }
    
    //TODO 3 Documentar checkIntSpace(String s, DigitFormat, ByteOrder)
    
    /**
     * 
     * @param s
     * @param df
     * @param bo
     * @return
     */
    public static boolean checkIntSpace(String s, DigitFormat df, ByteOrder bo){
    	
    	return checkIntSpace(s, df, SignMode.UNSIGNED, bo);
    	
    }
    
    //TODO 3 Documentar checkIntSpace(String s, SignMode, ByteOrder)
    
    /**
     * 
     * @param s
     * @param sm
     * @param bo
     * @return
     */
    public static boolean checkIntSpace(String s, SignMode sm, ByteOrder bo){
    	
    	return checkIntSpace(s, DigitFormat.HEX, sm, bo);
    	
    }
    
    //TODO 3 Documentar checkIntSpace(String s, DigitFormat df, SignMode sm, ByteOrder bo)
    
    /**
     * 
     * @param s
     * @param df the Digit Format of the String <code>s</code>
     * @param sm  the Sign Mode of value stored in <code>s</code>
     * @param bo
     * @return
     * @throws IllegalArgumentException
     */
    public static boolean checkIntSpace(String s, DigitFormat df, SignMode sm, ByteOrder bo){
    	
    	int len = numOfBytes(s, df);

    	if(len > 4) return false;
    	else if(len == 4){
    		if((isNeg(s,df,bo)) && (sm == SignMode.UNSIGNED)) return false;
    		else return true;
    	}
    	else return true;

    }
    
   //TODO 3 Documentar numOfBytes(String s)
    
    /**
     * 
     * @param s
     * @return
     * @throws IllegalArgumentException
     */
    public static int numOfBytes(String s){
    	
    		return numOfBytes(s, DigitFormat.HEX);
    	
    }
    
    //TODO 3 Documentar numOfBytes(String s, DigitFormat df)
    
    /**
     * 
     * @param s
     * @param df the Digit Format of the String <code>s</code>
     * @return
     * @throws IllegalArgumentException
     */
    public static int numOfBytes(String s, DigitFormat df){
    	
    	if(!checkDigits(s, df)) throw new IllegalArgumentException("Invalid " + df + " Digit Format: " + s);
    	
    	int len = s.length();
    	int blen;
    	
    	if(df == DigitFormat.HEX) blen = 2;
    	else if(df == DigitFormat.BIN) blen = 8;
    	else throw new IllegalArgumentException(df + " Digit Format not supported in "
    			+ "numOfBytes(String, DigitFormat):int" );
    	
    	if(len%blen != 0) throw new IllegalArgumentException("Invalid String Length: " + s);
    	else return len/blen;
    	
    }
    
    //TODO 3 Documentar numOfBytes(int n)
    
    /**
     * 
     * @param n
     * @return
     * @throws IllegalArgumentException
     */
    public static int numOfBytes(int num){
    	
    	return numOfBytes(num, SignMode.UNSIGNED);
    }
    
    //TODO 3 Documentar numOfBytes(int num, SignMode sm)
    
   /**
    * 
    * @param num
    * @param sm 
    * @return
    * @throws IllegalArgumentException
    */
    public static int numOfBytes(int num, SignMode sm){
    	
    	num = Math.abs(num);
    	
    	if(sm == SignMode.UNSIGNED){

            for(int i = 1; i < 4; i++){
            	if (num <= (int)(Math.pow(2, 8*i) - 1)) return i;
            }
        }
        else{
            
            for(int i = 1; i < 4; i++){
             if(num <= (int)(Math.pow(2, 8*i - 1) - 1)) return i;
            }
        }
    	return 4;
    	
    }
    
    //TODO 3 Documentar isNeg(String s)
    
    /**
     * 
     * @param s
     * @return
     * @throws IllegalArgumentException
     */
    public static boolean isNeg(String s){
    	
    		return isNeg(s, DigitFormat.HEX, ByteOrder.LSB_MSB);
    		
    }
    
    //TODO 3 Documentar isNeg(String s, DigitFormat df)
    
    /**
     * 
     * @param s
     * @param df the Digit Format of the output String
     * @return
     * @throws IllegalArgumentException
     */
    public static boolean isNeg(String s, DigitFormat df){

    		return isNeg(s, df, ByteOrder.LSB_MSB);
    	
    }
    
    //TODO 3 Documentar isNeg(String s, ByteOrder bo)
    
    /**
     * 
     * @param s
     * @param bo
     * @return
     * @throws IllegalArgumentException
     */
    public static boolean isNeg(String s, ByteOrder bo){
    	
    		return isNeg(s, DigitFormat.HEX, bo); 
    		
    }
    
    //TODO 3 Documentar isNeg(String s, DigitFormat df, ByteOrder bo)
    
    /**
     * 
     * @param s
     * @param df the Digit Format of the output String
     * @param bo
     * @return
     * @throws IllegalArgumentException
     */
    public static boolean isNeg(String s, DigitFormat df, ByteOrder bo){
    	
    		if(!checkDigits(s, df)) throw new IllegalArgumentException("Invalid " + df + " Digit Format: "+ s);
    	
    		if(bo == ByteOrder.LSB_MSB){
    			if(df == DigitFormat.HEX){
    				if(toInt(s.charAt(s.length()-2))>=8) return true;
    				else return false;
    			}
    			else if(df == DigitFormat.BIN){
    				if(toInt(s.charAt(s.length()-8))==1) return true;
    				else return false;    				
    			}    			
    		}
    		else{
    			if(df == DigitFormat.HEX){
    				if(toInt(s.charAt(0))>=8) return true;
    				else return false;
    			}
    			else if(df == DigitFormat.BIN){
    				if(toInt(s.charAt(0))==1) return true;
    				else return false;    				
    			}
    		}
    		return false;
    	
    }
    
    
    /**
     * Returns <code>true</code> if all characters of <code>s</code> are valid <code>df</code> digits
     * 
     * @param s a String representing a value
     * @param df the digit format of the String <code>s</code>
     * @return <code>true</code> if if all characters of <code>s</code> are valid <code>df</code> digits; 
     * <code>false</code> otherwise
     * @throws IllegalArgumentException
     * @see DigitFormat
     */    
    public static boolean checkDigits(String s, DigitFormat df){
    	
    	if(df == DigitFormat.HEX) return checkHexDigits(s);
    	else if(df == DigitFormat.BIN) return checkBinDigits(s);
    	else if(df == DigitFormat.DEC) return checkDecDigits(s);
    	else if(df == DigitFormat.OCT) return checkOctDigits(s);
    	else throw new IllegalArgumentException(df + " Digit Format Not Supported in " +
    			"checkDigits(String, DigitFormat):boolean");

    }
    
    /**
     * Returns <code>true</code> if <code>c</code> is a valid <code>df</code> digit
     * 
     * @param c a char representing a digit
     * @param df the digit format of the char <code>c</code>
     * @return <code>true</code> if <code>c</code> is a valid <code>df</code> digit; 
     * <code>false</code> otherwise
     * @throws IllegalArgumentException
     * @see DigitFormat
     */
    public static boolean checkDigit(char c, DigitFormat df){
    	
    	if(df == DigitFormat.HEX) return checkHexDigit(c);
    	else if(df == DigitFormat.BIN) return checkBinDigit(c);
    	else if(df == DigitFormat.DEC) return checkDecDigit(c);
    	else if(df == DigitFormat.OCT) return checkOctDigit(c);
    	else throw new IllegalArgumentException(df + " Digit Format Not Supported in " +
    			"checkDigit(char, DigitFormat):boolean");
    	
    }
    
    /**
     * Returns <code>true</code> if all characters of <code>s</code> are valid Hexadecimal digits
     * 
     * @param s a String representing a Hexadecimal value
     * @return <code>true</code> if all characters of <code>s</code> are valid Hexadecimal digits; 
     * <code>false</code> otherwise
    */
    public static boolean checkHexDigits(String s){
    	
    	for(int i = 0; i<s.length(); i++){
    		if(!checkHexDigit(s.charAt(i))) return false;
    	}
    	
    	return true;
    }
    
    
    /**
     * Returns <code>true</code> if <code>c</code> is a valid Hexadecimal digit 
     * 
     * @param c a char representing a Hexadecimal digit
     * @return <code>true</code> if <code>c</code> is a valid Hexadecimal digit; 
     * <code>false</code> otherwise
    */
    public static boolean checkHexDigit(char c){
    	
    	if((c>='0') && (c<='9')) return true;
    	else if ((c>='a') && (c<='f')) return true;
    	else if ((c>='A') && (c<='F')) return true;
    	return false;
    }
    
    /**
     * Returns <code>true</code> if all characters of <code>s</code> are valid Binary digits 
     * 
     * @param s a String representing a Binary value 
     * @return <code>true</code> if all characters of <code>s</code> are valid Binary digits; 
     * <code>false</code> otherwise
    */
    public static boolean checkBinDigits(String s){
    	
    	for(int i = 0; i<s.length(); i++){
    		if(!checkBinDigit(s.charAt(i))) return false;
    	}
    	
    	return true;
    }
    
    
    /**
     * Returns <code>true</code> if <code>c</code> is a valid Binary digit
     * 
     * @param c a char representing a Binary digit 
     * @return <code>true</code> if <code>c</code> is a valid Binary digit; 
     * <code>false</code> otherwise
    */
    public static boolean checkBinDigit(char c){
    	
    	if((c=='0') || (c=='1')) return true;
    	return false;
    }

    /**
     * Returns <code>true</code> if all characters of <code>s</code> are valid Decimal digits
     * 
     * @param s a String representing a Decimal value 
     * @return <code>true</code> if all characters of <code>s</code> are valid Decimal digits; 
     * <code>false</code> otherwise
    */
    public static boolean checkDecDigits(String s){
    	
    	for(int i = 0; i<s.length(); i++){
    		if(!checkDecDigit(s.charAt(i))) return false;
    	}
    	
    	return true;
    }
    
    
    /**
     * Returns <code>true</code> if <code>c</code> is a valid Decimal digit
     * 
     * @param c a char representing a Decimal digit 
     * @return <code>true</code> if <code>c</code> is a valid Decimal digit; 
     * <code>false</code> otherwise
    */
    public static boolean checkDecDigit(char c){
    	
    	if((c>='0') && (c<='9')) return true;
    	return false;
    }
    
    /**
     * Returns <code>true</code> if all characters of <code>s</code> are valid Octal digits
     * 
     * @param s a String representing a Octal value 
     * @return <code>true</code> if all characters of <code>s</code> are valid Octal digits; 
     * <code>false</code> otherwise
    */
    public static boolean checkOctDigits(String s){
    	
    	for(int i = 0; i<s.length(); i++){
    		if(!checkDecDigit(s.charAt(i))) return false;
    	}
    	
    	return true;
    }
    
    /**
     * Returns <code>true</code> if <code>c</code> is a valid Decimal digit
     * 
     * @param c a char representing a Decimal digit 
     * @return <code>true</code> if <code>c</code> is a valid Decimal digit; 
     * <code>false</code> otherwise
    */
    public static boolean checkOctDigit(char c){
    	
    	if((c>='0') && (c<='7')) return true;
    	return false;
    }
    
}
