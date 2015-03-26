package dflibrary.utils.ba;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import dflibrary.utils.ba.DigitUtils.*;


/**
 *
 * A collection of methods designed for providing help in Byte Array treatment.
 * 
 * @author Francisco Rodríguez Algarra
 * @version 29.8.2012
 *
 */

public class BAUtils {
	
	//TODO 2 Revisar excepciones lanzadas y documentarlas
  
    /**
     * Sole constructor. (For invocation by subclass constructors, typically implicit)
     */
    
    protected BAUtils(){}
    
    //TODO 3 Documentar

    /**
     * 
     * @param s
     * @param olen
     * @return
     */
    public static byte[] toBA(String s, int olen){
    	
    	return toBA(s, olen, DigitFormat.HEX, SignMode.UNSIGNED, ByteOrder.LSB_MSB);
    	
    }
    
    //TODO 3 Documentar
    
    /**
     * 
     * @param s
     * @param len
     * @param df
     * @return
     */
    public static byte[] toBA(String s, int olen, DigitFormat df){
    	
    	return toBA(s, olen, df, SignMode.UNSIGNED, ByteOrder.LSB_MSB);
    	
    }
    
   //TODO 3 Documentar
    
   /**
    * 
    * @param s
    * @param olen
    * @param sm
    * @return
    */
    public static byte[] toBA(String s, int olen, SignMode sm){
    	
    	return toBA(s, olen, DigitFormat.HEX, sm, ByteOrder.LSB_MSB);
    	
    }
    
    //TODO 3 Documentar
    
    /**
     * 
     * @param s
     * @param olen
     * @param bo
     * @return
     */
    public static byte[] toBA(String s, int olen, ByteOrder bo){
    	
    	return toBA(s, olen, DigitFormat.HEX, SignMode.UNSIGNED, bo);
    	
    }
    
    //TODO 3 Documentar
    
    /**
     * 
     * @param s
     * @param olen
     * @param df
     * @param sm
     * @return
     */
    public static byte[] toBA(String s, int olen, DigitFormat df, SignMode sm){
    	
    	return toBA(s, olen, df, sm, ByteOrder.LSB_MSB);
    	
    }
    
    //TODO 3 Documentar
    
    /**
     * 
     * @param s
     * @param olen
     * @param sm
     * @param bo
     * @return
     */
    public static byte[] toBA(String s, int olen, SignMode sm, ByteOrder bo){
    	
    	return toBA(s, olen, DigitFormat.HEX, sm, bo);
    	
    }

    //TODO 3 Documentar
    
    /**
     * 
     * @param s
     * @param olen
     * @param df
     * @param bo
     * @return
     */
    public static byte[] toBA(String s, int olen, DigitFormat df, ByteOrder bo){
    	
    	return toBA(s, olen, df, SignMode.UNSIGNED, bo);
    	
    }
    
    //TODO 3 Documentar    
    
    /**
     * 
     * @param s
     * @param len
     * @param df
     * @param bo
     * @return
     */
    public static byte[] toBA(String s, int olen, DigitFormat df, SignMode sm, ByteOrder bo){
    	
    	int len = DigitUtils.numOfBytes(s, df);

        if(olen < len) throw new IllegalArgumentException("Invalid Byte Array Length: " + olen);
                
        byte[] auxba = toBA(s, df);
        byte[] ba = new byte[olen];
        boolean neg = DigitUtils.isNeg(s, bo) && (sm == SignMode.SIGNED);
              
        if(bo == ByteOrder.LSB_MSB){
        	for(int i = 0; i<len; i++ ){
        		ba[i] = auxba[i];       		
        	}
        	if(neg){
        		for(int i = len; i<olen; i++){
        			ba[i] = (byte)0xFF;
        		}
        	}
        }
        else{
        	for(int i = olen - len; i<olen; i++){
        		ba[i] = auxba[i - (olen - len)];       		
        	}
        	if(neg){
        		for(int i = 0; i<olen - len; i++){
        			ba[i] = (byte)0xFF;
        		}
        	}
        }
 
        return ba;
    	
    }
    
    /**
     * Returns a new ByteArray representing the value stored in the String <code>s</code>.
     * 
     * @param s the String containing a value in hexadecimal format 
     * @return a Byte Array representing <code>s</code>
     * @throws IllegalArgumentException
     */
    public static byte[] toBA(String s){
    	
    	return toBA(s, DigitFormat.HEX);
   	
    }
    
    
    /**
     * Returns a new ByteArray representing the value stored in the String <code>s</code>
     * with digit format <code>df</code>.
     * 
     * @param s the String containing a value in <code>df</code> format 
     * @return a Byte Array representing <code>s</code>
     * @throws IllegalArgumentException
     */
    public static byte[] toBA(String s, DigitFormat df){
    	
    	byte[] ba = new byte[DigitUtils.numOfBytes(s, df)];
    	
    	if(df == DigitFormat.HEX){
    		
    		for(int i = 0; i < ba.length; i++){
    			String ss = new String(s.substring(2*i, 2*i+2));
    			ba[i] = DigitUtils.toByte(ss, df);
    		}
    		
    	}
    	else if(df == DigitFormat.BIN){
    		
    		for(int i = 0; i < ba.length; i++){
    			String ss = new String(s.substring(2*i, 2*i+8));
    			ba[i] = DigitUtils.toByte(ss, df);
    		}    		
    	}
    	
    	else throw new IllegalArgumentException(df + " Digit Format Not Supported in " +
    			"toBA(String s, DigitFormat df):byte[]");
    	
    	return ba;
    	
    }
    
    
    /**
     * Returns a Byte Array in Less Significant Byte to Most Significant Byte DigitUtils.ByteOrder
     * representing the specified Unsigned int value
     * 
     * @param num the integer to be converted 
     * @param len the length of the resulting Byte Array
     * @return a Byte Array representing <code>num</code>
     * @throws IllegalArgumentException
     */
       
    public static byte[] toBA(int num,  int olen){

        return toBA(num, olen, SignMode.UNSIGNED, ByteOrder.LSB_MSB);
    }

    //TODO 3 Documentar
    
    /**
     * Returns a Byte Array representing the specified int value
     * 
     * @param num
     * @param len
     * @param bo
     * @return a Byte Array representing <code>num</code>
     */
    
    
    public static byte[] toBA(int num, int olen, ByteOrder bo){

        return toBA(num, olen, SignMode.UNSIGNED, bo);
    }

    //TODO 3 Documentar
    
    /**
     * Returns a Byte Array representing the specified int value
     * 
     * @param num
     * @param len
     * @param sm
     * @return a Byte Array representing <code>num</code>
     */
    
    public static byte[] toBA(int num, int olen, SignMode sm){

        return toBA(num, olen, sm, ByteOrder.LSB_MSB);
    }

    //TODO 3 Documentar
    
    /**
     * Returns a Byte Array representing the specified int value
     * 
     * @param num
     * @param len
     * @param bo
     * @param sm
     * 
     * @return a Byte Array representing <code>num</code>
     */
    
    public static byte[] toBA(int num, int olen, SignMode sm, ByteOrder bo){
        
        int len = DigitUtils.numOfBytes(num, sm);

        if(olen < len) throw new IllegalArgumentException("Invalid Byte Array Length: " + olen);
        
        String s = DigitUtils.toString(num, DigitFormat.HEX, sm, bo);
        
        byte[] auxba = toBA(s);
        byte[] ba = new byte[olen];
        boolean neg = DigitUtils.isNeg(s, bo) && (sm == SignMode.SIGNED);
              
        if(bo == ByteOrder.LSB_MSB){
        	for(int i = 0; i<len; i++ ){
        		ba[i] = auxba[i];       		
        	}
        	if(neg){
        		for(int i = len; i<olen; i++){
        			ba[i] = (byte)0xFF;
        		}
        	}
        }
        else{
        	for(int i = olen - len; i<olen; i++){
        		ba[i] = auxba[i - (olen - len)];       		
        	}
        	if(neg){
        		for(int i = 0; i<olen - len; i++){
        			ba[i] = (byte)0xFF;
        		}
        	}
        }
 
        return ba;

    }
    
    /**
     * Returns a Byte Array in Less Significant Byte to Most Significant Byte DigitUtils.ByteOrder
     * representing the specified Unsigned int value
     * 
     * @param num the integer to be converted
     * 
     * @return a Byte Array representing <code>num</code>
     * @throws IllegalArgumentException
     */
       
    public static byte[] toBA(int num){

        return toBA(DigitUtils.toString(num, DigitFormat.HEX, SignMode.UNSIGNED, ByteOrder.LSB_MSB));

    }

    //TODO 3 Documentar
    
    /**
     * Returns a Byte Array representing the specified int value
     * 
     * @param num
     * @param bo
     * @return a Byte Array representing <code>num</code>
     */
    
    public static byte[] toBA(int num, ByteOrder bo){

        return toBA(DigitUtils.toString(num, DigitFormat.HEX, SignMode.UNSIGNED, bo));

    }
    
    //TODO 3 Documentar
    
    /**
     * Returns a Byte Array representing the specified int value
     * 
     * @param num
     * @param sm
     * @return a Byte Array representing <code>num</code>
     */
    
    public static byte[] toBA(int num, SignMode sm){

        return toBA(DigitUtils.toString(num, DigitFormat.HEX, sm, ByteOrder.LSB_MSB));
        
        
    }
    
    //TODO 3 Documentar
    
    /**
     * Returns a Byte Array representing the specified int value
     * 
     * @param num
     * @param bo
     * @param sm
     * 
     * @return a Byte Array representing <code>num</code>
     */
    
    public static byte[] toBA(int num, SignMode sm, ByteOrder bo){

        return toBA(DigitUtils.toString(num, DigitFormat.HEX, sm, bo));

    }

    /**
     * Returns a Byte Array representing the specified boolean value
     * 
     * @param b a boolean value
     * @return a Byte Array representing <code>b</code>
     * @throws IllegalArgumentException
     */
    
    public static byte[] toBA(boolean bool){

        if(bool) return BAUtils.toBA(1);
        else return BAUtils.toBA(0, 1);

    }
    
    /**
     * Returns a Byte Array of <code>olen</code> bytes representing the boolean value <code>b</code>
     * 
     * @param b a boolean value
     * @param olen number of bytes of the returned Byte Array
     * @return a Byte Array representing <code>b</code>
     */
    
    public static byte[] toBA(boolean bool, int olen){

        if(bool) return BAUtils.toBA(1, olen);
        else return BAUtils.toBA(0, olen);

    }

    //TODO 3 Documentar
    
    /**
     * 
     * @param b
     * @param olen
     * @param bo
     * @return
     */
    public static byte[] toBA(boolean bool, int olen, ByteOrder bo){

        if(bool) return BAUtils.toBA(1, olen, bo);
        else return BAUtils.toBA(0, olen, bo);

    }
    
    //TODO 3 Documentar

    /**
     * Returns a hexadecimal String representing the Byte Array <code>ba</code>
     * 
     * @param ba 
     * @return a String representing the Byte Array <code>ba</code>
     */
    
    public static String toString(byte[] ba){
    	
    	return toString(ba, DigitFormat.HEX);
    	
    }
   
    //TODO 3 Documentar
    
    /**
     * 
     * 
     * @param ba
     * @param df
     * @return
     */
    public static String toString(byte[] ba, DigitFormat df){
    	
    	StringBuilder sb = new StringBuilder();
    	
    	for(int i = 0; i<ba.length; i++){
    		sb.append(DigitUtils.toString(ba[i]));
    		
    	}
    	
    	return sb.toString();
    	
    }
    
    /**
     * 
     * @param ba2d
     * @return
     */
    public static String toString(byte[][] ba2d){
    	
    	int numOfRows = ba2d.length; 
    	
    	if(numOfRows < 1) return "";
    	
    	String s = toString(ba2d[0]);
    	
    	for(int i = 1; i < numOfRows; i++){
    		
    		s = s + "\n" + toString(ba2d[i]);
    	}
    	
    	return s;
    	
    }
    
    //TODO 3 Documentar
    
    /**
     * 
     * 
     * @param ba
     * @return a int number representing <code>ba</code>
     */
   
    public static int toInt(byte[] ba){

        return toInt(ba, SignMode.UNSIGNED, ByteOrder.LSB_MSB);

    }

    //TODO 3 Documentar
    
    /**
     * 
     * 
     * @param ba
     * @param bo
     * @return a int number representing <code>ba</code>
     */
    
    public static int toInt(byte[] ba, ByteOrder bo){

        return toInt(ba, SignMode.UNSIGNED, bo);

    }

    //TODO 3 Documentar
    
    /**
     * 
     * 
     * @param ba
     * @param sm
     * @return a int number representing <code>ba</code>
     */
    
    public static int toInt(byte[] ba, SignMode sm){

        return toInt(ba, sm, ByteOrder.LSB_MSB);

    }

    //TODO 3 Documentar
    
    /**
     * 
     * 
     * @param ba
     * @param bo
     * @param sm
     * @return a int number representing <code>ba</code>
     */
    
    public static int toInt(byte[] ba, SignMode sm, ByteOrder bo){

    	return DigitUtils.toInt(toString(ba), DigitFormat.HEX, sm, bo);
    	
    }
    
    //TODO 3 Documentar
    
    /**
     * 
     * 
     * @param ba
     * @return a boolean representing <code>ba</code>
     */
    
    public static boolean toBoolean(byte[] ba){

        return toBoolean(ba, ByteOrder.LSB_MSB);

    }
    
    //TODO 3 Documentar
    
    /**
     * 
     * @param ba
     * @param bo
     * @return
     */
    public static boolean toBoolean(byte[] ba, ByteOrder bo){
    	
    	int lsb;
    	
    	if(bo == ByteOrder.LSB_MSB) lsb = 0;
    	else lsb = ba.length - 1;
    	
    	return !compareBAs(and(extractSubBA(ba, lsb, 1), toBA("01")), new byte[1]);
    	
    	
    }
   
    //TODO 3 Documentar
    
    /**
     * 
     * 
     * @param ba
     * @return a Byte Array representing the two's complement of <code>ba</code>
     */
    
    public static byte[] twosComp(byte[] ba){

        return twosComp(ba, ByteOrder.LSB_MSB);
    }
    
    //TODO 3 Documentar
    
    /**
     * 
     * 
     * @param bo
     * @param ba
     * @return a Byte Array representing the two's complement of <code>ba</code>
     */        
    
    public static byte[] twosComp(byte[] ba, ByteOrder bo){

    	int len = ba.length;
    	DigitFormat df = DigitFormat.HEX;    	
    	String s = toString(ba, df);
    	String tc = DigitUtils.twosComp(s, df, bo);
    	
        return toBA(tc, len, df, bo);

    }
   
    //TODO 3 Documentar
    
    /**
     * 
     * 
     * @param bas
     * @return a Byte Array representing the XOR operation of <code>bas</code> Byte Arrays
     */
    
    public static byte[] xor(byte[]...bas){

        return xor(ByteOrder.LSB_MSB, bas);

    }
    
    //TODO 3 Documentar
    
    /**
     * 
     * 
     * @param bo
     * @param bas
     * @return a Byte Array representing the XOR operation of <code>bas</code> Byte Arrays
     */
    
    public static byte[] xor(ByteOrder bo, byte[]...bas){

        byte[][] baMatrix = alignBAs(bo, bas);

        int len = baMatrix[0].length;
        byte[] ba = new byte[len];

        for(int i = 0; i<bas.length; i++){
            for(int j = 0; j < len; j++){

                ba[j] = (byte)((int)ba[j] ^ (int)baMatrix[i][j]);

            }
        }

        return ba;

    }
    
    /**
     * Returns the logical conjunction of all the Byte Arrays included in <code>bas</code>
     * considering all of them being stored in Less Significant Byte to Most Significant Byte
     * Order.
     * 
     * @param bas the Byte Arrays from which to calculate the logical conjunction
     * @return the conjuncted Byte Array
     */
    
    public static byte[] and(byte[]...bas){

        return and(ByteOrder.LSB_MSB, bas);

    }
    
    /**
     * Returns the logical conjunction of all the Byte Arrays included in <code>bas</code>
     * taking into account the Byte Order specified in <code>bo</code>.
     * 
     * @param bo the Byte Order of the Byte Arrays in <code>bas</code>
     * @param bas the Byte Arrays from which to calculate the logical conjunction
     * @return the conjuncted Byte Array
     */
    
    public static byte[] and(ByteOrder bo, byte[]...bas){

        byte[][] baMatrix = alignBAs(bo, bas);

        int len = baMatrix[0].length;
        byte[] ba = new byte[0];
        
        for(int i = 0; i<len; i++){

            ba = concatenateBAs(ba, BAUtils.toBA("FF"));

        }

        for(int i = 0; i<bas.length; i++){
            for(int j = 0; j < len; j++){

                ba[j] = (byte)((int)ba[j] & (int)baMatrix[i][j]);

            }
        }

        return ba;

    }
    
    //TODO 3 Documentar
    
    /**
     * Returns the logical negation of <code>ba</code>.
     * 
     * @param ba Byte Array to be inverted
     * 
     * @return the inverted Byte Array
     */
    
    public static byte[] not(byte[] ba){

        byte[] auxba = new byte[ba.length];

        for(int i = 0; i<ba.length; i++){

            auxba[i] = (byte)~ba[i];

        }

        return auxba;

    }
    
    //TODO 4 Ampliación Documentación: Ejemplo
    
    /**
     * Returns a new Byte Array of <code>len</code> bytes that is the result of
     * expanding <code>ba</code> with zero-filled bytes.
     * 
     * @param ba the original Byte Array
     * @param len number of bytes of the expanded Byte Array
     * 
     * @return the expanded Byte Array
     */
     
    public static byte[] padding(byte[] ba, int olen){

    	return padding(ba, olen, ByteOrder.LSB_MSB);
        
    }
    
    //TODO 3 Documentar
    
    /**
     * 
     * @param ba
     * @param len
     * @param bo
     * @return
     */
    public static byte[] padding(byte[] ba, int olen, ByteOrder bo){
    	
    	if((ba.length == 0) || ba.length > olen) 
        	throw new IllegalArgumentException("Invalid Byte Array Length: " + toString(ba));
        
       byte[] auxba = new byte[olen - ba.length];
       
       if(bo == ByteOrder.LSB_MSB){
    	   ba = concatenateBAs(ba, auxba);
       }
       else{
    	   ba = concatenateBAs(auxba, ba);
       }
       return ba;
    	
    	
    }
    
    //TODO 4 Documentación: mejora y ejemplos
       
    /**
     * Returns a new Byte Array that is the result of a circular movement of the
     * parameter <code>ba</ba>. 
     * 
     * @param ba Byte Array whose bytes are moved
     * @param count number of positions to move each byte
     * @param dir Direction in which the bytes move
     * 
     * @return the rotated Byte Array
     * 
     * @see  Direction
     */
    public static byte[] rotateBA(byte[] ba, int count, Direction dir){

        byte[] auxba = new byte[ba.length];
        int i, j;

        if(dir == Direction.LEFT) count=count*(-1);
        for(i=0; i<ba.length;i++){

            j = ((i+count)%ba.length);
            if(j<0) j=(j+ba.length);
            auxba[j] = ba[i];
        }

        return auxba;

    }

    
    /**
     * 
     * @param ba
     * @return
     */
    public static byte[] reverseBA(byte[] ba){
    	
    	byte[] aux = new byte[ba.length];
    	
    	for(int i = 0; i < aux.length; i++){
    		
    		System.arraycopy(ba, i, aux, aux.length - i - 1, 1);
    		
    	}
    	
    	return aux;
    }
    
    /*
    public static byte[] lShift(byte[] ba, int bcount){
    	
    	boolean msb = false;
    	byte[] aux;
    	byte[] res = extractSubBA(ba, 0, ba.length);
    	
    	for(int i = 0; i < ba.length; i++){
    		
    		res[i] = (byte) ((int) res[i] << bcount);
    		
    		if(msb) res[i] = (byte) ((int)res[i] | (byte)1);
    		
    		
    		aux = and(ba, toBA("80"));
    		if(compareBAs(aux, new byte[1])) msb = false;
    		else msb = true;
    		
    		
    	}
    	
    	return res;
    	
    }
    
    */
    
    
    /**
     * Returns a new Byte Array that is a subarray of the <code>ba</code> parameter. 
     * The subarray begins at the position specified by <code>beginIndex</code> 
     * and it has <code>length</length> bytes.
     * 
     * @param ba the Byte Array 
     * @param beginIndex position where the subarray begins inside <code>ba</code>
     * @param length the number of bytes of the returned Byte Array
     * 
     * @return the extracted Byte Array
     */

    
    public static byte[] extractSubBA(byte[] ba, int begin, int olen){
    	
    	if((begin + olen)>ba.length) throw new IllegalArgumentException("Length Parameter too high");

        byte[] auxba = new byte[olen];

        System.arraycopy(ba, begin, auxba, 0, olen);

        return auxba;
    }
 
    //TODO 4 Documentación: mejora y ejemplos
    
    /**
     * Returns a new Byte Array that is the result of joining the Byte Arrays
     * received in <code>bas</code>. 
     * 
     * @param bas the Byte Arrays to join
     * 
     * @return the concatenated Byte Array
     */
    
    
    public static byte[] concatenateBAs(byte[]...bas){

        int len = 0;

        for(int i=0; i<bas.length;i++){
            len += bas[i].length;
        }

        byte[] ba = new byte[len];
        int begin = 0;

        for(int i=0; i<bas.length;i++){
            System.arraycopy(bas[i], 0 , ba, begin, bas[i].length);
            begin += bas[i].length;
        }

        return ba;
    }   
    
    
    /**
     * Generates a matrix where each row contains one of the Byte Arrays received as
     * parameters, all of them aligned considering a Less Significant Byte to
     * Most Significant Byte Order.
     * Therefore, bytes with the same relevance will end located at the same column
     * regardless of the size of its array.
     *  
     * @param bas Byte Arrays to be aligned
     * 
     * @return a matrix containing aligned Byte Arrays
     * 
     * @see #alignBAs(ByteOrder, byte[])
     * 
     */
    
    public static byte[][] alignBAs(byte[]...bas){

        return alignBAs(ByteOrder.LSB_MSB, bas);

    }
    
    //TODO 4 Documentación: ejemplo
    
    /**
     * Returns a matrix where each row contains one of the Byte Arrays received as
     * parameters, all of them aligned taking into account the Byte Order specified.
     * Therefore, bytes with the same relevance will end located at the same column
     * regardless of the size of its array.
     *  
     * @param bo the order of the bytes in the Byte Arrays
     * @param bas Byte Arrays to be aligned
     * 
     * @return a matrix containing aligned Byte Arrays
     * 
     */
    
    public static byte[][] alignBAs(ByteOrder bo, byte[]...bas){


        int len = 0;

        for(int i = 0; i < bas.length; i++){
                    if(bas[i].length > len) len = bas[i].length;
        }

        byte[][] baMatrix = new byte[bas.length][len];

        for(int i = 0; i < bas.length; i++){

            if(bo == ByteOrder.LSB_MSB){
                System.arraycopy(bas[i], 0, baMatrix[i], 0, bas[i].length);

            }
            else{
                System.arraycopy(bas[i], 0, baMatrix[i], len - bas[i].length,
                        bas[i].length);

            }
        }

        return baMatrix;

    }

    //TODO 4 Documentación: revisar @see
    
    /**
     * Returns <code>true</code> if two Byte Arrays are equal to each other
     * 
     * @param ba1 the first byte[] to be compared
     * @param ba2 the byte[] to be compared with ba2
     * 
     * @return <code>true</code> if ba1 and ba2 are identical; <code>false</code> otherwise
     * 
     * @see java.util.Arrays#equals(byte[], byte[])
     */

    public static boolean compareBAs(byte[] ba1, byte[] ba2){

        return Arrays.equals(ba1, ba2);

    }
  
    /**
     * 
     * @param bas
     * @return
     */
    public static byte[][] create2dBA(byte[]...bas){
    	
    	int numOfRows = bas.length;
    	
    	byte[][] res = new byte[numOfRows][];
    	
    	for(int i = 0; i < numOfRows; i++){
    		
    		res[i] = bas[i];
    		
    	}		
    	
    	return res;		
    }
    
    /**
     * 
     * @param ba2d
     * @param bas
     * @return
     */
    public static byte[][] create2dBA(byte[][] ba2d, byte[]...bas){
    	
    	byte[][] aux = create2dBA(bas);
    	return join2dBAs(ba2d, bas);  	
    	
    }
    
    /**
     * 
     * @param bas
     * @return
     */
    public static byte[][] join2dBAs(byte[][]...bas){
    	
    	int numOfRows = 0;
    	
    	for(int i = 0; i < bas.length; i++){
    		
    		numOfRows = numOfRows + bas[i].length;
    		
    	}
    	
    	byte[][] res = new byte[numOfRows][];
    	int pos = 0;
    	
    	for (int i = 0; i < bas.length; i++){
    		
    		System.arraycopy(bas[i], 0, res, pos, bas[i].length);
    		pos = pos + bas[i].length;
    		
    	}
    	
    	return res;
    	
    }
    
    
    //TODO 4 Documentación: revisar @see
    
    /**
     * Returns a randomized Byte Array of the specified length generating
     * with SHA1PRNG algorithm. 
     * As the resulting array is generated using the SecureRandom class,
     * it is suitable for cryptographic purposes.
     * 
     * @param length the length (in bytes) of the Byte Array to be generated
     *  
     * @return the random Byte Array
     *  
     * @see java.security.SecureRandom#getInstance(String)
     *    
     */
    
    public static byte[] getRandomBA(int length){


       byte[] randomBytes = new byte[length];

       try{
    	   SecureRandom rnd = SecureRandom.getInstance("SHA1PRNG");
    	   rnd.nextBytes(randomBytes);
       }catch(NoSuchAlgorithmException e){
    	   return new byte[length];
       }
       
    	   
       return randomBytes;
    }
    
    /**
     * Returns a randomized Byte Array of the specified length. 
     * As the resulting array is generated using the SecureRandom class,
     * it is suitable for cryptographic purposes.
     * 
     * @param length the length (in bytes) of the Byte Array to be generated
     * @param alg String containing the algorithm that will be used for generating
     * the randomized Byte Array.  
     * @return the random Byte Array 
     * @throws NoSuchAlgorithmException  
     * @see java.security.SecureRandom#getInstance(String)
     *    
     */
    
    public static byte[] getRandomBA(int length, String alg) throws NoSuchAlgorithmException{


        byte[] randomBytes = new byte[length];

        try{

            SecureRandom rnd = SecureRandom.getInstance(alg);
            rnd.nextBytes(randomBytes);

        }catch(NoSuchAlgorithmException e){
            throw e;
       }

       return randomBytes;
    }
    
    //TODO 3 Documentar
    
    /**
     * 
     * @param ba
     * @return
     */
    public static boolean isNeg(byte[] ba){
    	
    	return(DigitUtils.isNeg(toString(ba)));
    	
    }
    
    //TODO 3 Documentar
    
    /**
     * 
     * @param ba
     * @param bo
     * @return
     */
    public static boolean isNeg(byte[] ba, ByteOrder bo){
    	
    	return(DigitUtils.isNeg(toString(ba), bo));
    	
    }
    
    /**
     * Orientation in which a Byte Array should be rotated.
     */
	public static enum Direction {
    	/**
    	 * Rotation will be done by the LEFT edge of the array.
    	 * For example, if the array AFC811 was rotated one byte left,
    	 * the resulting byte array would be C811AF.
    	 */
    	LEFT{
			public String toString(){
				return "Left";
			}
			
		}, 
    	/**
    	 * Rotation will be done by the RIGHT edge of the array.
    	 * For example, if the array AFC811 was rotated one byte right,
    	 * the resulting byte array would be 11AFC8.
    	 */
    	RIGHT{
			public String toString(){
				return "Right";
			}
			
		};
    }
	
	
	public static void main(String[] args){
		
		String s = "A7";
		
		byte[] ba = toBA(s);
		
		System.out.println(toString(ba));
		
		
	}
	
    
}

