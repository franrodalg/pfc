package dflibrary.utils.ba;

/**
* A collection of methods designed for providing help in 
* digit format treatment.
* @author Francisco Rodriguez Algarra
*/

public class DigitUtils {
	
    //TODO Strings with implicit format (Ej: 0xA5)
    //TODO Implementation of methods for pctal and decimal formats

    // Auxiliary classes

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
         * */
    	DEC{
            public String toString(){
                return "Decimal";
            }		
    	},
        /**
    	 * Octal
         * */
    	OCT{
            public String toString(){
                return "Octal";
    	    }		
	};	
    }
	
    /**
     * Indicates whether a number should be interpreted as 
     * Signed or Unsigned
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
         * */
	UNSIGNED{
            public String toString(){
                return "Unsigned";
            }		
	};
    }
	
    /**
     * Byte significance order of the number representation
     */
    public static enum ByteOrder {
        /**
    	 * Less Significant Byte to Most Significant Byte
         * */
    	LSB_MSB{
            public String toString(){
                return "Less Significant Byte to Most Significant Byte";
            }
    	},
        /**
    	 * Most Significant Byte to Less Significant Byte
         * */
    	MSB_LSB{
            public String toString(){
                return "Most Significant Byte to Less Significant Byte";
            }		
    	};
    }    
   
    // Type Conversion Methods

    /**
     * Converts an hexadecimal string into its byte equivalent
     * @param s the string containing a byte in hexadecimal format
     * @return a byte equivalent to <code>s</code>
     */
    public static byte toByte(String s){
    	
    	return toByte(s, DigitFormat.HEX);	
    	
    }
    
    /**
     * Converts a string in <code>df</code> digit format
     * into its byte equivalent
     * @param s the string containing a byte
     * @param df an instance of class <code>DigitFormat</code>
     * indicating the digit format in which <code>s</code>
     * should be interpreted
     * @return a byte equivalent to <code>s</code>
     */
    public static byte toByte(String s, DigitFormat df){   	
    	
    	if(!checkDigits(s,df))
            throw new IllegalArgumentException(
                    "Invalid " + df + " Digit Format: " + s);  
    	   	
    	if(numOfBytes(s,df)!=1)
            throw new IllegalArgumentException(
                    "Invalid String Length: " + s);
    	
    	return (byte)toInt(s,df);  			 		  	
    }
    
    /**
     * Converts an hexadecimal char into its byte equivalent
     * @param c the char containing a digit in hexadecimal format
     * @return a byte equivalent to <code>c</code>
     */
    public static byte toByte(char c){

        return toByte(c, DigitFormat.HEX);

    }
    
    /**
     * Converts a char in <code>df</code> digit format
     * into its byte equivalent
     * @param c the char containing a digit in <code>df</code> format
     * @param df an instance of class <code>DigitFormat</code>
     * indicating the digit format in which <code>c</code>
     * should be interpreted
     * @return a byte equivalent to <code>c</code>
     */
    public static byte toByte(char c, DigitFormat df){
    	
    	if(!checkDigit(c, df))
            throw new IllegalArgumentException(
                    "Invalid " + df + " Digit Format: " + c);
    	  	
    	return (byte)toInt(c, df);
    	
    }
        
    /**
     * Converts an unsigned hexadecimal string with bytes stored
     * from less significant to most significant into its int
     * equivalent
     * @param s the string containing an unsigned number
     * @return an unsigned int number equivalent to <code>s</code>
     */
    public static int toInt(String s){
    	
    	return toInt(s, DigitFormat.HEX, SignMode.UNSIGNED,
                ByteOrder.LSB_MSB);

    }
    
    /**
     * Converts an unsigned string in <code>df</code> digit format
     * with bytes stored from less significant to most significant 
     * into its int equivalent
     * @param s the string containing an unsigned number
     * @param df an instance of class <code>DigitFormat</code>
     * indicating the digit format in which <code>s</code>
     * should be interpreted
     * @return a unsigned int number equivalent to <code>s</code>
     */
    public static int toInt(String s, DigitFormat df){
    	
    	return toInt(s, df, SignMode.UNSIGNED, ByteOrder.LSB_MSB);
  	
    }
    
    /**
     * Converts an hexadecimal string with <code>sm</code> sign mode and
     * with bytes stored from less significant to most significant 
     * into its int equivalent
     * @param s the string containing a number
     * @param sm an instance of class <code>SignMode</code>
     * indicating whether <code>s</code> should be interpreted
     * as representing a signed or unsigned value
     * @return a int number equivalent to <code>s</code>
       */
    public static int toInt(String s, SignMode sm){
    	
    	return toInt(s, DigitFormat.HEX, sm, ByteOrder.LSB_MSB);
    	
    }
    
    /**
     * Converts an hexadecimal unsigned string
     * with bytes stored as indicated by <code>bo</code> 
     * into its int equivalent
     * @param s the string containing an unsigned number
     * @param bo an instance of class <code>ByteOrder</code>
     * indicating whether <code>s</code> should be interpreted as
     * being represented with the less significant bytes first or
     * the most significant bytes first
     * @return a unsigned int number equivalent to <code>s</code>
     */
    public static int toInt(String s, ByteOrder bo){
    	
    	return toInt(s, DigitFormat.HEX, SignMode.UNSIGNED, bo);
    	
    }
    
    /**
     * Converts a string in digit format <code>df</code>
     * with <code>sm</code> sign mode and
     * with bytes stored from less significant to most significant 
     * into its int equivalent
     * @param s the string containing a number
     * @param df an instance of class <code>DigitFormat</code>
     * indicating the digit format in which <code>s</code>
     * should be interpreted
     * @param sm an instance of class <code>SignMode</code>
     * indicating whether <code>s</code> should be interpreted
     * as representing a signed or unsigned value
     * @return a int number equivalent to <code>s</code>
     */
    public static int toInt(String s, DigitFormat df, SignMode sm){

    	return toInt(s, df, sm, ByteOrder.LSB_MSB);
    	
    }
    
    /**
     * Converts an unsigned string in digit format <code>df</code>
     * with bytes stored as indicated by <code>bo</code> 
     * into its int equivalent
     * @param s the string containing an unsigned number
     * @param df an instance of class <code>DigitFormat</code>
     * indicating the digit format in which <code>s</code>
     * should be interpreted
     * @param bo an instance of class <code>ByteOrder</code>
     * indicating whether <code>s</code> should be interpreted as
     * being represented with the less significant bytes first or
     * the most significant bytes first
     * @return a unsigned int number equivalent to <code>s</code>
     */
    public static int toInt(String s, DigitFormat df, ByteOrder bo){

    	return toInt(s, df, SignMode.UNSIGNED, bo);
    	
    }
  
    /**
     * Converts an hexadecimal string with <code>sm</code> sign mode and
     * with bytes stored as indicated by <code>bo</code> 
     * into its int equivalent
     * @param s the string containing an unsigned number
     * @param sm an instance of class <code>SignMode</code>
     * indicating whether <code>s</code> should be interpreted
     * as representing a signed or unsigned value
     * @param bo an instance of class <code>ByteOrder</code>
     * indicating whether <code>s</code> should be interpreted as
     * being represented with the less significant bytes first or
     * the most significant bytes first
     * @return an int number equivalent to <code>s</code>
     */
    public static int toInt(String s, SignMode sm, ByteOrder bo){

    	return toInt(s, DigitFormat.HEX, sm, bo);
    	
    }
    
    /**
     * Converts a string in digit format <code>df</code>
     * with <code>sm</code> sign mode and
     * with bytes stored as indicated by <code>bo</code> 
     * into its int equivalent
     * @param s the string containing an unsigned number
     * @param df an instance of class <code>DigitFormat</code>
     * indicating the digit format in which <code>s</code>
     * should be interpreted
     * @param sm an instance of class <code>SignMode</code>
     * indicating whether <code>s</code> should be interpreted
     * as representing a signed or unsigned value
     * @param bo an instance of class <code>ByteOrder</code>
     * indicating whether <code>s</code> should be interpreted as
     * being represented with the less significant bytes first or
     * the most significant bytes first
     * @return an int number equivalent to <code>s</code>
     */
    public static int toInt(String s, DigitFormat df, SignMode sm, 
            ByteOrder bo){

    	boolean neg = false;
    	
    	if((sm == SignMode.SIGNED) && isNeg(s,df,bo)){
    		neg = true;
    		s = twosComp(s,df,bo);
    	}
    	
    	int len = numOfBytes(s, df);
    	if(!checkIntSpace(s, df, sm, bo))
            throw new IllegalArgumentException(
                    "Invalid String Length: " + s);   	
       	
    	int blen = s.length()/len;
    	
    	int num = 0;
    	
	if(bo == ByteOrder.LSB_MSB){
	    for(int i = 0; i<len;i++){
                num = num +
                    byteStringValue(s.substring(i*blen, i*blen + blen), df) *
                    (int) Math.pow(256, i);
            }
	}
    	else{
            for(int i = len-1; i>=0;i--){			
                num = num +
                    byteStringValue(s.substring(i*blen, i*blen + blen), df) * 
                    (int) Math.pow(256, len-i-1);
		}			
	    }
		
	if(neg) num = -num;
		
    	return num;
    	    	
    }
    
    /**
     * Converts an hexadecimal string representing a single byte
     * into its int equivalent
     * @param s the string to be converted
     * @return an int number equivalent to <code>s</code>
     */
    public static int byteStringValue(String s){
    	
    	return byteStringValue(s, DigitFormat.HEX);    	
    	
    }
    
    /**
     * Converts a string in digit format <code>df</code>
     * representing a single byte into its int equivalent
     * @param s the string to be converted
     * @param df an instance of class <code>DigitFormat</code>
     * indicating the digit format in which <code>s</code>
     * should be interpreted
     * @return an int number equivalent to <code>s</code>
     */
    public static int byteStringValue(String s, DigitFormat df){
    	
    	int base;
    	
    	if(numOfBytes(s, df)!=1) 
            throw new IllegalArgumentException(
                    "Invalid String Length: " + s);
    	
    	if(df == DigitFormat.HEX) base = 16;
    	else if(df == DigitFormat.BIN) base = 2;
    	else 
            throw new IllegalArgumentException(
                    df +
                    " Digit Format Not Supported in " +
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
     * Converts an haxadecimal char into its int equivalent
     * @param c the char to be converted
     * @return an int equivalent to <code>c</code> 
     */
    public static int toInt(char c){
    	
    	return toInt(c, DigitFormat.HEX);	
    	
    }
   
    /**
     * Converts a char in digit format <code>df</code> into
     * its int equivalent
     * @param c the char to be converted
     * @param df an instance of class <code>DigitFormat</code>
     * indicating the digit format in which <code>c</code>
     * should be interpreted
     * @return an int equivalent to <code>c</code> 
     */
    public static int toInt(char c, DigitFormat df){
    	
    	if(!checkDigit(c,df))
            throw new IllegalArgumentException(
                    "Invalid " + df + " Digit Format: " + c);
    	
    	if((c >= '0')&&(c<='9')) return c - '0';
    	else if ((c >= 'a')&&(c <= 'f')) return c - 'a' + 10;
    	else return c - 'A' + 10;
    	
    }
 
    /**
     * Changes the digit format representation of a string 
     * @param s the string to be modified
     * @param idf an instance of class <code>DigitFormat</code>
     * indicating the digit format in which the input string
     * <code>s</code> should be interpreted
     * @param odf an instance of class <code>DigitFormat</code>
     * indicating the digit format in which the output string
     * should be represented
     * @return a string equivalent to <code>s</code> but in
     * <code>odf</code> digit format
     */
    public static String toString(String s, DigitFormat idf, 
            DigitFormat odf){
    	
        return toString(toInt(s,idf), odf);    	
    
    }
   
    /**
     * Generates an hexadecimal unsigned string representation of a byte
     * @param b the byte to be converted
     * @return a string representation of <code>b</code>
     */
    public static String toString(byte b){
    	
    	return toString(b, DigitFormat.HEX, SignMode.UNSIGNED);

    } 
   
    /**
     * Generates an unsigned string representation in <code>df</code>
     * digit format of a byte
     * @param b the byte to be converted
     * @param df an instance of class <code>DigitFormat</code>
     * indicating the digit format in which the output string
     * should be represented
     * @return a string representation of <code>b</code>
     */
    public static String toString(byte b, DigitFormat df){
    	
    	return toString(b, df, SignMode.UNSIGNED);
    	
    }
    
    /**
     * Generates an hexadecimal string representation
     * with <code>sm</code> sign mode of a byte
     * @param b the byte to be converted
     * @param sm an instance of class <code>SignMode</code>
     * indicating whether the output string should be represented
     * as a signed or unsigned value
     * @return a string representation of <code>b</code>
     */
    public static String toString(byte b, SignMode sm){
    	
	return toString(b, DigitFormat.HEX, sm);
	
    }
   
    /**
     * Generates a string representation in <code>df</code>
     * digit format with <code>sm</code> sign mode of a byte
     * @param b the byte to be converted
     * @param df an instance of class <code>DigitFormat</code>
     * indicating the digit format in which the output string
     * should be represented
     * @param sm an instance of class <code>SignMode</code>
     * indicating whether the output string should be represented
     * as a signed or unsigned value
     * @return a string representation of <code>b</code>
     */
    public static String toString(byte b, DigitFormat df, SignMode sm){
    	
    	return toByteString((int)b, df);
    	
    }
   
    /**
     * Generates an hexadecimal unsigned byte string representation
     * with the bytes ordered from less significant to most significant
     * of an int
     * @param num the int to be converted
     * @return a string representation of <code>num</code>
     */
    public static String toString(int num){
    	
    	return toString(num, DigitFormat.HEX, 
                SignMode.UNSIGNED, ByteOrder.LSB_MSB);

    } 
   
    /**
     * Generates an unsigned byte string representation
     * in <code>df</code> digit format 
     * with the bytes ordered from less significant to most significant
     * of an int
     * @param num the int to be converted
     * @param df an instance of class <code>DigitFormat</code>
     * indicating the digit format in which the output string
     * should be represented
     * @return a string representation of <code>num</code>
     */
    public static String toString(int num, DigitFormat df){
    	
    	return toString(num, df, SignMode.UNSIGNED, ByteOrder.LSB_MSB);
    		
    }
   
    /**
     * Generates an hexadecimal byte string representation
     * with <code>sm</code> sign mode and 
     * with the bytes ordered from less significant to most significant
     * of an int
     * @param num the int to be converted
     * @param sm an instance of class <code>SignMode</code>
     * indicating whether the output string should be represented
     * as a signed or unsigned value
     * @return a string representation of <code>num</code>
     */
    public static String toString(int num, SignMode sm){
    	
	return toString(num, DigitFormat.HEX, sm, ByteOrder.LSB_MSB);
		
    }
   
    /**
     * Generates an hexadecimal unsigned byte string representation
     * with the bytes ordered as indicated by <code>bo</code>
     * of an int
     * @param num the int to be converted
     * @param bo an instance of class <code>ByteOrder</code>
     * indicating whether <code>s</code> should be represented
     * with the less significant bytes first or
     * the most significant bytes first
     * @return a string representation of <code>num</code>
     */
    public static String toString(int num, ByteOrder bo){
    	
	return toString(num, DigitFormat.HEX, SignMode.UNSIGNED, bo);
		
    }
   
    /**
     * Generates a byte string representation in <code>df</code> digit format
     * with <code>sm</code> sign mode and 
     * with the bytes ordered from less significant to most significant
     * of an int
     * @param num the int to be converted
     * @param df an instance of class <code>DigitFormat</code>
     * indicating the digit format in which the output string
     * should be represented
     * @param sm an instance of class <code>SignMode</code>
     * indicating whether the output string should be represented
     * as a signed or unsigned value
     * @return a string representation of <code>num</code>
     */
    public static String toString(int num, DigitFormat df, SignMode sm){
    	
    	return toString(num, df, sm, ByteOrder.LSB_MSB);
    	
    }
   
    /**
     * Generates an unsigned byte string representation 
     * in <code>df</code> digit format
     * with the bytes ordered as indicated by <code>bo</code>
     * of an int
     * @param num the int to be converted
     * @param df an instance of class <code>DigitFormat</code>
     * indicating the digit format in which the output string
     * should be represented
     * @param bo an instance of class <code>ByteOrder</code>
     * indicating whether <code>s</code> should be represented
     * with the less significant bytes first or
     * the most significant bytes first
     * @return a string representation of <code>num</code>
     */
    public static String toString(int num, DigitFormat df, ByteOrder bo){
    	
    	return toString(num, df, SignMode.UNSIGNED, bo);
    	
    }
  
    /**
     * Generates an hexadecimal byte string representation 
     * with <code>sm</code> sign mode and 
     * with the bytes ordered as indicated by <code>bo</code>
     * of an int
     * @param num the int to be converted
     * @param sm an instance of class <code>SignMode</code>
     * indicating whether the output string should be represented
     * as a signed or unsigned value     
     * @param bo an instance of class <code>ByteOrder</code>
     * indicating whether <code>s</code> should be represented
     * with the less significant bytes first or
     * the most significant bytes first
     * @return a string representation of <code>num</code>
     */
    public static String toString(int num, SignMode sm, ByteOrder bo){
    	
    	return toString(num, DigitFormat.HEX, sm, bo);
    	
    }
     
    /**
     * Generates a byte string representation 
     * in <code>df</code> digit format
     * with <code>sm</code> sign mode and 
     * with the bytes ordered as indicated by <code>bo</code>
     * of an int
     * @param num the int to be converted
     * @param df an instance of class <code>DigitFormat</code>
     * indicating the digit format in which the output string
     * should be represented
     * @param sm an instance of class <code>SignMode</code>
     * indicating whether the output string should be represented
     * as a signed or unsigned value     
     * @param bo an instance of class <code>ByteOrder</code>
     * indicating whether <code>s</code> should be represented
     * with the less significant bytes first or
     * the most significant bytes first
     * @return a string representation of <code>num</code>
     */
    public static String toString(int num, DigitFormat df, 
            SignMode sm, ByteOrder bo){
    	
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
    
    /**
     * Converts an int representing a single byte
     * into its hexadecimal byte string equivalent
     * @param num the int to be converted
     * @return a string equivalent to <code>num</code>
     */
    public static String toByteString(int num){
    	
    	return toByteString(num, DigitFormat.HEX);
    	
    }
  
    /**
     * Converts an int representing a single byte
     * into its byte string equivalent
     * @param num the int to be converted
     * @param df an instance of class <code>DigitFormat</code>
     * indicating the digit format in which the output string
     * should be represented
     * @return a string equivalent to <code>num</code>
     */
    public static String toByteString(int num, DigitFormat df){
    	
    	int blen, aux;
    	byte mask;
    	char c;
    	StringBuilder sb = new StringBuilder();
    	
    	if(numOfBytes(num)!=1)
            throw new IllegalArgumentException(
                    "Invalid Int Length");    		
    	
    	if(df == DigitFormat.HEX){
    		blen = 2;
    		mask = 0x0F;
    		
    	}
    	else if(df == DigitFormat.BIN){
    		blen = 8;
    		mask = 0x01;
    	}
    	else 
            throw new IllegalArgumentException(df +
                    " Digit Format Not Supported in " +
                    "toByteString(int, DigitFormat):String");
    	
    	for(int i = 0; i < blen; i++){
    		aux = num & mask;
    		c = toDigit(aux, df);
    		sb.insert(0, c);
    		num = num>>(8/blen);
    	}
    	
    	return sb.toString();
    	
    }
   
    /**
     * Converts an int representing a single nibble
     * into its hexadecimal digit char equivalent
     * @param num the int to be converted
     * @return a char equivalent to <code>num</code>
     */
    public static char toDigit(int num){
    	
    	return toDigit(num, DigitFormat.HEX);
    	
    }
  
    /**
     * Converts an int representing a single nibble
     * into its digit char equivalent in <code>df</code> digit format
     * @param num the int to be converted
     * @param df an instance of class <code>DigitFormat</code>
     * indicating the digit format in which the output char
     * should be represented
     * @return a char equivalent to <code>num</code>
     */
    public static char toDigit(int num, DigitFormat df){
    	
    	char c;
    	
    	if((num>=0)&&(num<=9)) 
            c = (char)('0' + num);
    	else if((num>=10)&&(num<=15)) 
            c = (char) ('A' + num - 10);
    	else 
            throw new IllegalArgumentException(
                    "Invalid int (" + num + ") for a " + df + " Digit");
    	
    	if(!checkDigit(c,df)) 
            throw new IllegalArgumentException(
                    "Invalid int (" + num + ") " + 
                    "for a " + df + " Digit");
    	
    	return c;
    }   
  
    // Binary Operation Methods


    /**
     * Computes the two's complement of the value stored in the 
     * hexadecimal byte string <code>s</code>, represented
     * from less significant byte to most significant byte
     * @param s a string representing a value
     * @return an hexadecimal byte string corresponding to the 
     * two's complement of <code>s</code>, represented
     * from less significant byte to most significant byte
     */
    public static String twosComp(String s){
		
	return twosComp(s, DigitFormat.HEX, ByteOrder.LSB_MSB);
   	  	
    }
   
    /**
     * Computes the two's complement of the value stored in the 
     * byte string <code>s</code>, represented in
     * <code>df</code> digit format
     * from less significant byte to most significant byte
     * @param s a string representing a value
     * @param df an instance of class <code>DigitFormat</code>
     * indicating the digit format in which <code>s</code>
     * should be interpreted
     * @return a byte string corresponding to the 
     * two's complement of <code>s</code>, represented
     * in <code>df</code> digit format
     * from less significant byte to most significant byte
     */
    public static String twosComp(String s, DigitFormat df){
		
	return twosComp(s, df, ByteOrder.LSB_MSB);
   	  	
    }
   
    /**
     * Computes the two's complement of the value stored in the 
     * hexadecimal byte string <code>s</code>, represented
     * in <code>bo</code> byte order
     * @param s a string representing a value
     * @param bo an instance of class <code>ByteOrder</code>
     * indicating whether <code>s</code> should be represented
     * with the less significant bytes first or
     * the most significant bytes first
     * @return an hexadecimal byte string corresponding to the 
     * two's complement of <code>s</code>, represented
     * in <code>bo</code> byte order
     */
    public static String twosComp(String s, ByteOrder bo){
		
	return twosComp(s, DigitFormat.HEX, bo);
   	  	
    }
   
    /**
     * Computes the two's complement of the value stored in the 
     * byte string <code>s</code>, represented
     * in <code>df</code> digit format
     * with <code>bo</code> byte order
     * @param s a string representing a value
     * @param df an instance of class <code>DigitFormat</code>
     * indicating the digit format in which <code>s</code>
     * should be interpreted
     * @param bo an instance of class <code>ByteOrder</code>
     * indicating whether <code>s</code> should be represented
     * with the less significant bytes first or
     * the most significant bytes first
     * @return a byte string corresponding to the 
     * two's complement of <code>s</code>, represented
     * in <code>df</code> digit format with
     * <code>bo</code> byte order
     */
    public static String twosComp(String s, DigitFormat df, ByteOrder bo){

	s = not(s, df);
	return toString(
                toInt(s, df, DigitUtils.SignMode.UNSIGNED, bo) + 1,
                df, bo);
   	  	
    }
   
    /**
     * Computes the logical negation (NOT) of the hexadecimal
     * byte string <code>s</code>
     * @param s the byte string to be negated
     * @return the negated byte string
     * */
    public static String not(String s){
    	
    	return not(s, DigitFormat.HEX);
    	
    }
   
    /**
     * Computes the logical negation (NOT) of the
     * byte string <code>s</code>, represented in
     * <code>df</code> digit format
     * @param df an instance of class <code>DigitFormat</code>
     * indicating the digit format in which <code>s</code>
     * should be interpreted
     * @param s the byte string to be negated
     * @return the negated byte string
     */
    public static String not(String s, DigitFormat df){
    	
    	int base;
   	
    	if(df == DigitFormat.HEX){
    	    base = 16; 		
    	}
    	else if(df == DigitFormat.BIN){
    	    base = 2;
    	}
    	else 
            throw new IllegalArgumentException(df + 
                    " Digit Format Not Supported in " +
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
    
    // Checker methods

    /**
     * Checks whether an hexadecimal unsigned byte string <code>s</code>,
     * represented from less significant to most significant byte
     * can be fitted within the size of a Java int
     * @param s a byte string
     * @return <code>true</code> if <code>s</code> fits in an int;
     * <code>false</code> otherwise
     */
    public static boolean checkIntSpace(String s){
    	
    	return checkIntSpace(
                s, DigitFormat.HEX, SignMode.UNSIGNED, ByteOrder.LSB_MSB);
    	
    }
    
    /**
     * Checks whether an unsigned byte string <code>s</code>,
     * represented in <code>df</code> digit format
     * from less significant to most significant byte
     * can be fitted within the size of a Java int
     * @param df an instance of class <code>DigitFormat</code>
     * indicating the digit format in which <code>s</code>
     * should be interpreted
     * @param s a byte string
     * @return <code>true</code> if <code>s</code> fits in an int;
     * <code>false</code> otherwise
     */
    public static boolean checkIntSpace(String s, DigitFormat df){
    	
    	return checkIntSpace(s, df, SignMode.UNSIGNED, ByteOrder.LSB_MSB);
    	
    }
    
    /**
     * Checks whether an hexadecimal byte string <code>s</code>,
     * represented in <code>df</code> digit format
     * from less significant to most significant byte
     * can be fitted within the size of a Java int
     * @param sm an instance of class <code>SignMode</code>
     * indicating whether <code>s</code> should be interpreted
     * as representing a signed or unsigned value
     * @param s a byte string
     * @return <code>true</code> if <code>s</code> fits in an int;
     * <code>false</code> otherwise
     */
    public static boolean checkIntSpace(String s, SignMode sm){
    	
    	return checkIntSpace(s, DigitFormat.HEX, sm, ByteOrder.LSB_MSB);
    	
    }
   
    /**
     * Checks whether an unsigned hexadecimal byte string <code>s</code>,
     * represented in <code>bo</code> byte order
     * can be fitted within the size of a Java int
     * @param bo an instance of class <code>ByteOrder</code>
     * indicating whether <code>s</code> should be interpreted as
     * being represented with the less significant bytes first or
     * the most significant bytes first
     * @param s a byte string
     * @return <code>true</code> if <code>s</code> fits in an int;
     * <code>false</code> otherwise
     */
    public static boolean checkIntSpace(String s, ByteOrder bo){
    	
    	return checkIntSpace(s, DigitFormat.HEX, SignMode.UNSIGNED, bo);
    	
    }
  
    /**
     * Checks whether a byte string <code>s</code>,
     * represented in <code>df</code> digit format
     * and <code>sm</code> sign mode
     * from less significant to most significant byte
     * can be fitted within the size of a Java int
     * @param df an instance of class <code>DigitFormat</code>
     * indicating the digit format in which <code>s</code>
     * should be interpreted
     * @param sm an instance of class <code>SignMode</code>
     * indicating whether <code>s</code> should be interpreted
     * as representing a signed or unsigned value
     * @param s a byte string
     * @return <code>true</code> if <code>s</code> fits in an int;
     * <code>false</code> otherwise
     */
    public static boolean checkIntSpace(String s, DigitFormat df, 
            SignMode sm){
    	
    	return checkIntSpace(s, df, sm, ByteOrder.LSB_MSB);
    	
    }
   
    /**
     * Checks whether an unsigned byte string <code>s</code>,
     * represented in <code>df</code> digit format
     * with <code>bo</code> byte order
     * can be fitted within the size of a Java int
     * @param df an instance of class <code>DigitFormat</code>
     * indicating the digit format in which <code>s</code>
     * should be interpreted
     * @param bo an instance of class <code>ByteOrder</code>
     * indicating whether <code>s</code> should be interpreted as
     * being represented with the less significant bytes first or
     * the most significant bytes first
     * @param s a byte string
     * @return <code>true</code> if <code>s</code> fits in an int;
     * <code>false</code> otherwise
     */
    public static boolean checkIntSpace(String s, DigitFormat df, 
            ByteOrder bo){
    	
    	return checkIntSpace(s, df, SignMode.UNSIGNED, bo);
    	
    }
    
    /**
     * Checks whether an hexadecimal byte string <code>s</code>,
     * represented in <code>sm</code> sign mode
     * with <code>bo</code> byte order
     * can be fitted within the size of a Java int
     * @param sm an instance of class <code>SignMode</code>
     * indicating whether <code>s</code> should be interpreted
     * as representing a signed or unsigned value    
     * @param bo an instance of class <code>ByteOrder</code>
     * indicating whether <code>s</code> should be interpreted as
     * being represented with the less significant bytes first or
     * the most significant bytes first
     * @param s a byte string
     * @return <code>true</code> if <code>s</code> fits in an int;
     * <code>false</code> otherwise
     */
    public static boolean checkIntSpace(String s, SignMode sm, 
            ByteOrder bo){
    	
    	return checkIntSpace(s, DigitFormat.HEX, sm, bo);
    	
    }
    
    /**
     * Checks whether a byte string <code>s</code>,
     * represented in <code>df</code> digit format
     * and <code>sm</code> sign mode
     * with <code>bo</code> byte order
     * can be fitted within the size of a Java int
     * @param df an instance of class <code>DigitFormat</code>
     * indicating the digit format in which <code>s</code>
     * should be interpreted
     * @param sm an instance of class <code>SignMode</code>
     * indicating whether <code>s</code> should be interpreted
     * as representing a signed or unsigned value    
     * @param bo an instance of class <code>ByteOrder</code>
     * indicating whether <code>s</code> should be interpreted as
     * being represented with the less significant bytes first or
     * the most significant bytes first
     * @param s a byte string
     * @return <code>true</code> if <code>s</code> fits in an int;
     * <code>false</code> otherwise
     */
    public static boolean checkIntSpace(String s, DigitFormat df, 
            SignMode sm, ByteOrder bo){
    	
    	int len = numOfBytes(s, df);

    	if(len > 4) return false;
    	else if(len == 4){
    	    if((isNeg(s,df,bo)) && (sm == SignMode.UNSIGNED)) 
                return false;
    	    else return true;
    	}
    	else return true;

    }
    
    /**
     * Determines the number of bytes that the hexadecimal
     * byte string <code>s</code> represents
     * @param s a byte string
     * @return an int indicating the number of bytes contained
     * in <code>s</code>
     */
    public static int numOfBytes(String s){
    	
    	return numOfBytes(s, DigitFormat.HEX);
    	
    }
  
    /**
     * Determines the number of bytes that the
     * byte string <code>s</code> represents
     * @param s a byte string
     * @param df an instance of class <code>DigitFormat</code>
     * indicating the digit format in which <code>s</code>
     * should be interpreted
     * @return an int indicating the number of bytes contained
     * in <code>s</code>
     */
    public static int numOfBytes(String s, DigitFormat df){
    	
    	if(!checkDigits(s, df))
            throw new IllegalArgumentException(
                    "Invalid " + df + " Digit Format: " + s);
    	
    	int len = s.length();
    	int blen;
    	
    	if(df == DigitFormat.HEX) blen = 2;
    	else if(df == DigitFormat.BIN) blen = 8;
    	else throw new IllegalArgumentException(
                df + " Digit Format not supported in " +
                "numOfBytes(String, DigitFormat):int" );
    	
    	if(len%blen != 0)
            throw new IllegalArgumentException(
                    "Invalid String Length: " + s);
    	else return len/blen;
    	
    }
    
    /**
     * Determines the number of bytes that the
     * int <code>num</code> represents
     * @param num an int
     * @return an int indicating the number of bytes contained
     * in <code>num</code>
     */
    public static int numOfBytes(int num){
    	
    	return numOfBytes(num, SignMode.UNSIGNED);
    }
  
   /**
     * Determines the number of bytes that the
     * int <code>num</code> represents
     * @param num an int 
     * @param sm an instance of class <code>SignMode</code>
     * indicating whether <code>num</code> should be interpreted
     * as representing a signed or unsigned value   
     * @return an int indicating the number of bytes contained
     * in <code>num</code>
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
   
    /**
     * Checks whether the hexadecimal string <code>s</code>,
     * represented from less significant byte to most significant byte
     * corresponds to a negative value or not
     * @param s a byte string
     * @return <code>true</code> if <code>s</code> represents a negative
     * value; <code>false</code> otherwise
     */
    public static boolean isNeg(String s){
    	
    	return isNeg(s, DigitFormat.HEX, ByteOrder.LSB_MSB);
    		
    }
  
    /**
     * Checks whether the string <code>s</code>,
     * represented in <code>df</code> digit format
     * from less significant byte to most significant byte
     * corresponds to a negative value or not
     * @param s a byte string
     * @param df an instance of class <code>DigitFormat</code>
     * indicating the digit format in which <code>s</code>
     * should be interpreted
     * @return <code>true</code> if <code>s</code> represents a negative
     * value; <code>false</code> otherwise
     */
    public static boolean isNeg(String s, DigitFormat df){

    	return isNeg(s, df, ByteOrder.LSB_MSB);
    	
    }
  
    /**
     * Checks whether the hexadecimal string <code>s</code>,
     * represented with <code>bo</code> byte order
     * corresponds to a negative value or not
     * @param s a byte string
     * @param bo an instance of class <code>ByteOrder</code>
     * indicating whether <code>s</code> should be interpreted as
     * being represented with the less significant bytes first or
     * the most significant bytes first
     * @return <code>true</code> if <code>s</code> represents a negative
     * value; <code>false</code> otherwise
     */
    public static boolean isNeg(String s, ByteOrder bo){
    	
    	return isNeg(s, DigitFormat.HEX, bo); 
    		
    }
  
    /**
     * Checks whether the string <code>s</code>,
     * represented in <code>df</code> digit format
     * from less significant byte to most significant byte
     * corresponds to a negative value or not
     * @param s a byte string
     * @param df an instance of class <code>DigitFormat</code>
     * indicating the digit format in which <code>s</code>
     * should be interpreted
     * @param bo an instance of class <code>ByteOrder</code>
     * indicating whether <code>s</code> should be interpreted as
     * being represented with the less significant bytes first or
     * the most significant bytes first
     * @return <code>true</code> if <code>s</code> represents a negative
     * value; <code>false</code> otherwise
     */
    public static boolean isNeg(String s, DigitFormat df, ByteOrder bo){
    	
    	if(!checkDigits(s, df))
            throw new IllegalArgumentException(
                    "Invalid " + df + " Digit Format: "+ s);
    	
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
     * Checks whether the string <code>s</code> is a valid
     * representation in <code>df</code> digit format
     * @param s a byte string
     * @param df the digit format to be checked
     * @return <code>true</code> if all characters of 
     * <code>s</code> are valid <code>df</code> digits; 
     * <code>false</code> otherwise
     */    
    public static boolean checkDigits(String s, DigitFormat df){
    	
    	if(df == DigitFormat.HEX) return checkHexDigits(s);
    	else if(df == DigitFormat.BIN) return checkBinDigits(s);
    	else if(df == DigitFormat.DEC) return checkDecDigits(s);
    	else if(df == DigitFormat.OCT) return checkOctDigits(s);
    	else 
            throw new IllegalArgumentException(df +
                    " Digit Format Not Supported in " +
                    "checkDigits(String, DigitFormat):boolean");

    }
    
    /**
     * Checks whether the char <code>c</code> is a valid
     * representation in <code>df</code> digit format
     * @param c a char
     * @param df the digit format to be checked
     * @return <code>true</code> if <code>c</code> is a valid
     * <code>df</code> digit; 
     * <code>false</code> otherwise
     */
    public static boolean checkDigit(char c, DigitFormat df){
    	
    	if(df == DigitFormat.HEX) return checkHexDigit(c);
    	else if(df == DigitFormat.BIN) return checkBinDigit(c);
    	else if(df == DigitFormat.DEC) return checkDecDigit(c);
    	else if(df == DigitFormat.OCT) return checkOctDigit(c);
    	else 
            throw new IllegalArgumentException(df +
                    " Digit Format Not Supported in " +
                    "checkDigit(char, DigitFormat):boolean");
    	
    }
    
    /**
     * Checks whether the string <code>s</code> is a valid
     * representation in hexadecimal digit format
     * @param s a byte string
     * @return <code>true</code> if all characters of 
     * <code>s</code> are valid hexadecimal digits; 
     * <code>false</code> otherwise
    */
    public static boolean checkHexDigits(String s){
    	
    	for(int i = 0; i<s.length(); i++){
    		if(!checkHexDigit(s.charAt(i))) return false;
    	}
    	
    	return true;
    }
    
    
    /**
     * Checks whether the char <code>c</code> is a valid
     * representation in hexadecimal digit format
     * @param c a char
     * @return <code>true</code> if <code>c</code> is a valid
     * hexadecimal digit; 
     * <code>false</code> otherwise
    */
    public static boolean checkHexDigit(char c){
    	
    	if((c>='0') && (c<='9')) return true;
    	else if ((c>='a') && (c<='f')) return true;
    	else if ((c>='A') && (c<='F')) return true;
    	return false;
    }
    
    /**
     * Checks whether the string <code>s</code> is a valid
     * representation in binary digit format
     * @param s a byte string
     * @return <code>true</code> if all characters of 
     * <code>s</code> are valid binary digits; 
     * <code>false</code> otherwise
    */
    public static boolean checkBinDigits(String s){
    	
    	for(int i = 0; i<s.length(); i++){
    		if(!checkBinDigit(s.charAt(i))) return false;
    	}
    	
    	return true;
    }
    
    
    /**
     * Checks whether the char <code>c</code> is a valid
     * representation in binary digit format
     * @param c a char
     * @return <code>true</code> if <code>c</code> is a valid
     * binary digit; 
     * <code>false</code> otherwise
    */
    public static boolean checkBinDigit(char c){
    	
    	if((c=='0') || (c=='1')) return true;
    	return false;
    }

    /**
     * Checks whether the string <code>s</code> is a valid
     * representation in decimal digit format
     * @param s a byte string
     * @return <code>true</code> if all characters of 
     * <code>s</code> are valid decimal digits; 
     * <code>false</code> otherwise
    */
    public static boolean checkDecDigits(String s){
    	
    	for(int i = 0; i<s.length(); i++){
    		if(!checkDecDigit(s.charAt(i))) return false;
    	}
    	
    	return true;
    }
    
    
    /**
     * Checks whether the char <code>c</code> is a valid
     * representation in decimal digit format
     * @param c a char
     * @return <code>true</code> if <code>c</code> is a valid
     * decimal digit; 
     * <code>false</code> otherwise
    */
    public static boolean checkDecDigit(char c){
    	
    	if((c>='0') && (c<='9')) return true;
    	return false;
    }
    
    /**
     * Checks whether the string <code>s</code> is a valid
     * representation in octal digit format
     * @param s a byte string
     * @return <code>true</code> if all characters of 
     * <code>s</code> are valid octal digits; 
     * <code>false</code> otherwise
    */
    public static boolean checkOctDigits(String s){
    	
    	for(int i = 0; i<s.length(); i++){
    		if(!checkDecDigit(s.charAt(i))) return false;
    	}
    	
    	return true;
    }
    
    /**
     * Checks whether the char <code>c</code> is a valid
     * representation in octal digit format
     * @param c a char
     * @return <code>true</code> if <code>c</code> is a valid
     * octal digit; 
     * <code>false</code> otherwise
    */
    public static boolean checkOctDigit(char c){
    	
    	if((c>='0') && (c<='7')) return true;
    	return false;
    }
    
}
