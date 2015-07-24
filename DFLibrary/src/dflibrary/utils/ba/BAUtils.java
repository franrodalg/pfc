package dflibrary.utils.ba;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import dflibrary.utils.ba.DigitUtils.*;

/**
 * A collection of methods designed for providing help in byte array treatment.
 * @author Francisco Rodriguez Algarra
 */
public class BAUtils {
    
    // Type Conversion Methods

    /**
     * Generates a byte array corresponding to the one represented in
     * the hexadecimal string <code>s</code>
     * @param s the string to be converted
     * @return a byte array representing the string <code>s</code>
     */
    public static byte[] toBA(String s){
    	
    	return toBA(s, DigitFormat.HEX);
   	
    }
    
    /**
     * Generates a byte array corresponding to the one represented in
     * the string <code>s</code>
     * @param s the string to be converted
     * @param df an instance of class <code>DigitFormat</code> indicating
     * the digit format in which <code>s</code> is represented
     * @return a byte array representing the string <code>s</code>
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
    	
    	else throw new IllegalArgumentException(df + 
                " Digit Format Not Supported in " +
    		"toBA(String s, DigitFormat df):byte[]");
    	
    	return ba;
    	
    }

    /**
     * Generates a byte array corresponding to the one represented in
     * the unsigned hexadecimal string <code>s</code>, from less significant 
     * byte to most significant byte
     * @param s the string to be converted
     * @param olen the desired length of the output byte array
     * @return a byte array of length <code>olen</code> representing
     * the hexadecimal string <code>s</code>
     */
    public static byte[] toBA(String s, int olen){
    	
    	return toBA(s, olen, DigitFormat.HEX, 
                SignMode.UNSIGNED, ByteOrder.LSB_MSB);
    	
    }
    
    /**
     * Generates a byte array corresponding to the one represented in
     * the unsigned string <code>s</code>, from less significant 
     * byte to most significant byte
     * @param s the string to be converted
     * @param olen the desired length of the output byte array
     * @param df an instance of class <code>DigitFormat</code> indicating
     * the digit format in which <code>s</code> is represented
     * @return a byte array of length <code>olen</code> representing
     * the string <code>s</code>
     */
    public static byte[] toBA(String s, int olen, DigitFormat df){
    	
    	return toBA(s, olen, df, SignMode.UNSIGNED, ByteOrder.LSB_MSB);
    	
    }
    
   /**
     * Generates a byte array corresponding to the one represented in
     * the hexadecimal string <code>s</code>, from less significant byte
     * to most significant byte
     * @param s the string to be converted
     * @param olen the desired length of the output byte array
     * @param sm an instance of class <code>SignMode</code> indicating
     * whether the represented number should be interpreted as
     * signed or unsigned
     * @return a byte array of length <code>olen</code> representing
     * the string <code>s</code>
    */
    public static byte[] toBA(String s, int olen, SignMode sm){
    	
    	return toBA(s, olen, DigitFormat.HEX, sm, ByteOrder.LSB_MSB);
    	
    }
    
    /**
     * Generates a byte array corresponding to the one represented in
     * the unisgned hexadecimal string <code>s</code>
     * @param s the string to be converted
     * @param olen the desired length of the output byte array
     * @param bo an instance of class <code>ByteOrder</code> indicating
     * the order in which the bytes are presented
     * @return a byte array of length <code>olen</code> representing
     * the string <code>s</code>
     */
    public static byte[] toBA(String s, int olen, ByteOrder bo){
    	
    	return toBA(s, olen, DigitFormat.HEX, SignMode.UNSIGNED, bo);
    	
    }
    
    /**
     * Generates a byte array corresponding to the one represented in
     * the unsigned string <code>s</code>, from less significant 
     * byte to most significant byte
     * @param s the string to be converted
     * @param olen the desired length of the output byte array
     * @param df an instance of class <code>DigitFormat</code> indicating
     * the digit format in which <code>s</code> is represented
     * @param sm an instance of class <code>SignMode</code> indicating
     * whether the represented number should be interpreted as
     * signed or unsigned
     * @return a byte array of length <code>olen</code> representing
     * the string <code>s</code>
     */
    public static byte[] toBA(String s, int olen, DigitFormat df, 
            SignMode sm){
    	
    	return toBA(s, olen, df, sm, ByteOrder.LSB_MSB);
    	
    }
    
    /**
     * Generates a byte array corresponding to the one represented in
     * the hexadecimal string <code>s</code>
     * @param s the string to be converted
     * @param olen the desired length of the output byte array
     * @param sm an instance of class <code>SignMode</code> indicating
     * whether the represented number should be interpreted as
     * signed or unsigned
     * @param bo an instance of class <code>ByteOrder</code> indicating
     * the order in which the bytes are presented
     * @return a byte array of length <code>olen</code> representing
     * the string <code>s</code>
     */
    public static byte[] toBA(String s, int olen, SignMode sm, 
            ByteOrder bo){
    	
    	return toBA(s, olen, DigitFormat.HEX, sm, bo);
    	
    }

    /**
     * Generates a byte array corresponding to the one represented in
     * the unisgned string <code>s</code>
     * @param s the string to be converted
     * @param olen the desired length of the output byte array
     * @param df an instance of class <code>DigitFormat</code> indicating
     * the digit format in which <code>s</code> is represented
     * @param bo an instance of class <code>ByteOrder</code> indicating
     * the order in which the bytes are presented
     * @return a byte array of length <code>olen</code> representing
     * the string <code>s</code>
     */
    public static byte[] toBA(String s, int olen, DigitFormat df, 
            ByteOrder bo){
    	
    	return toBA(s, olen, df, SignMode.UNSIGNED, bo);
    	
    }
    
    /**
     * Generates a byte array corresponding to the one represented in
     * the string <code>s</code>
     * @param s the string to be converted
     * @param olen the desired length of the output byte array
     * @param df an instance of class <code>DigitFormat</code> indicating
     * the digit format in which <code>s</code> is represented
     * @param sm an instance of class <code>SignMode</code> indicating
     * whether the represented number should be interpreted as
     * signed or unsigned
     * @param bo an instance of class <code>ByteOrder</code> indicating
     * the order in which the bytes are presented
     * @return a byte array of length <code>olen</code> representing
     * the string <code>s</code>
     */
    public static byte[] toBA(String s, int olen, DigitFormat df, 
            SignMode sm, ByteOrder bo){
    	
    	int len = DigitUtils.numOfBytes(s, df);

        if(olen < len) 
            throw new IllegalArgumentException(
                    "Invalid Byte Array Length: " + olen
                    );
                
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
     * Generates a byte array corresponding to the value represented in
     * the unsigned int <code>num</code>, from less significant 
     * byte to most significant byte 
     * @param num the integer to be converted 
     * @return a byte array representing the value <code>num</code>
     */
    public static byte[] toBA(int num){

        return toBA(DigitUtils.toString(num, DigitFormat.HEX, 
                    SignMode.UNSIGNED, ByteOrder.LSB_MSB));

    }
    
    /**
     * Generates a byte array corresponding to the value represented in
     * the unsigned int <code>num</code>, from less significant 
     * byte to most significant byte 
     * @param num the integer to be converted 
     * @param olen the desired length of the output byte array
     * @return a byte array representing the value <code>num</code>
     */
    public static byte[] toBA(int num,  int olen){

        return toBA(num, olen, SignMode.UNSIGNED, ByteOrder.LSB_MSB);
    }

    /**
     * Generates a byte array corresponding to the value represented in
     * the unsigned int <code>num</code>
     * @param num the integer to be converted 
     * @param bo an instance of class <code>ByteOrder</code> indicating
     * the order in which the bytes are presented
     * @return a byte array representing the value <code>num</code>
     */
    public static byte[] toBA(int num, ByteOrder bo){

        return toBA(DigitUtils.toString(num, DigitFormat.HEX,
                    SignMode.UNSIGNED, bo));

    }
    
    /**
     * Generates a byte array corresponding to the value represented in
     * the int <code>num</code>, from less significant 
     * byte to most significant byte 
     * @param num the integer to be converted 
     * @param sm an instance of class <code>SignMode</code> indicating
     * whether the represented number should be interpreted as
     * signed or unsigned
     * @return a byte array representing the value <code>num</code>
     */
    public static byte[] toBA(int num, SignMode sm){

        return toBA(DigitUtils.toString(num, DigitFormat.HEX, sm, 
                    ByteOrder.LSB_MSB));
        
    }
    
    /**
     * Generates a byte array corresponding to the value represented in
     * the int <code>num</code> 
     * @param num the integer to be converted 
     * @param bo an instance of class <code>ByteOrder</code> indicating
     * the order in which the bytes are presented
     * @param sm an instance of class <code>SignMode</code> indicating
     * whether the represented number should be interpreted as
     * signed or unsigned
     * @return a byte array representing the value <code>num</code>
     */
    public static byte[] toBA(int num, SignMode sm, ByteOrder bo){

        return toBA(DigitUtils.toString(num, DigitFormat.HEX, sm, bo));

    }

    /**
     * Generates a byte array corresponding to the value represented in
     * the unsigned int <code>num</code>, from less significant 
     * byte to most significant byte 
     * @param num the integer to be converted 
     * @param olen the desired length of the output byte array     
     * @param bo an instance of class <code>ByteOrder</code> indicating
     * the order in which the bytes should be presented
     * @return a byte array representing the value <code>num</code>
     */
    public static byte[] toBA(int num, int olen, ByteOrder bo){

        return toBA(num, olen, SignMode.UNSIGNED, bo);
    }
    
    /**
     * Generates a byte array corresponding to the value represented in
     * the unsigned int <code>num</code>, from less significant 
     * byte to most significant byte 
     * @param num the integer to be converted 
     * @param olen the desired length of the output byte array     
     * @param sm an instance of class <code>SignMode</code> indicating
     * whether the represented number should be interpreted as
     * signed or unsigned
     * @return a byte array representing the value <code>num</code>
     */
    public static byte[] toBA(int num, int olen, SignMode sm){

        return toBA(num, olen, sm, ByteOrder.LSB_MSB);
    }
    
    /**
     * Generates a byte array corresponding to the value represented in
     * the unsigned int <code>num</code>, from less significant 
     * byte to most significant byte 
     * @param num the integer to be converted 
     * @param olen the desired length of the output byte array     
     * @param sm an instance of class <code>SignMode</code> indicating
     * whether the represented number should be interpreted as
     * signed or unsigned
     * @param bo an instance of class <code>ByteOrder</code> indicating
     * the order in which the bytes should be presented
     * @return a byte array representing the value <code>num</code>
     */
    public static byte[] toBA(int num, int olen, SignMode sm, ByteOrder bo){
        
        int len = DigitUtils.numOfBytes(num, sm);

        if(olen < len)
            throw new IllegalArgumentException(
                    "Invalid Byte Array Length: " + olen);
        
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
     * Generates a byte array corresponding to the logical value represented in
     * the boolean <code>bool</code>
     * @param bool the boolean to be converted 
     * @return a byte array representing the value <code>bool</code>
     */
    public static byte[] toBA(boolean bool){

        if(bool) return BAUtils.toBA(1);
        else return BAUtils.toBA(0, 1);

    }
    
    /**
     * Generates a byte array corresponding to the logical value represented in
     * the boolean <code>bool</code>
     * @param bool the boolean to be converted 
     * @param olen the desired length of the output byte array
     * @return a byte array representing the value <code>bool</code>
     */
    public static byte[] toBA(boolean bool, int olen){

        if(bool) return BAUtils.toBA(1, olen);
        else return BAUtils.toBA(0, olen);

    }

    /**
     * Generates a byte array corresponding to the logical value represented in
     * the boolean <code>bool</code>
     * @param bool the boolean to be converted 
     * @param olen the desired length of the output byte array
     * @param bo an instance of class <code>ByteOrder</code> indicating
     * the order in which the bytes should be presented
     * @return a byte array representing the value <code>bool</code>
     */
    public static byte[] toBA(boolean bool, int olen, ByteOrder bo){

        if(bool) return BAUtils.toBA(1, olen, bo);
        else return BAUtils.toBA(0, olen, bo);

    }

    /**
     * Generates an hexadecimal string corresponding to
     * the byte array <code>ba</code>
     * @param ba the byte array to be converted
     * @return a string representing the value of the 
     * byte array <code>ba</code>
     */
    public static String toString(byte[] ba){
    	
    	return toString(ba, DigitFormat.HEX);
    	
    }
    
    /**
     * Generates a string corresponding to
     * the byte array <code>ba</code> in <code>df</code> digit format
     * @param ba the byte array to be converted
     * @param df an instance of class <code>DigitFormat</code> indicating
     * the digit format in which the output should be represented
     * @return a string representing the value of the 
     * byte array <code>ba</code>
     */
    public static String toString(byte[] ba, DigitFormat df){
    	
    	StringBuilder sb = new StringBuilder();
    	
    	for(int i = 0; i<ba.length; i++){
    		sb.append(DigitUtils.toString(ba[i]));
    		
    	}
    	
    	return sb.toString();
    	
    }
    
    /**
     * Generates a string corresponding to
     * the two-dimensional byte array <code>ba2d</code>
     * @param ba2d the byte array to be converted
     * @return a string representing the value of the 
     * two-dimensional byte array <code>ba</code>
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

    /**
     * Generates an int number corresponding to
     * the unsigned byte array <code>ba</code>, from less significant 
     * byte to most significant byte
     * @param ba the byte array to be converted
     * @return an int representing the value of the 
     * byte array <code>ba</code>
     */
    public static int toInt(byte[] ba){

        return toInt(ba, SignMode.UNSIGNED, ByteOrder.LSB_MSB);

    }
    
    /**
     * Generates an int number corresponding to
     * the unsigned byte array <code>ba</code>
     * @param ba the byte array to be converted
     * @param bo an instance of class <code>ByteOrder</code> indicating
     * the order in which the bytes of <code>ba</code>
     * should be interpreted
     * @return an int representing the value of the 
     * byte array <code>ba</code>
     */
    public static int toInt(byte[] ba, ByteOrder bo){

        return toInt(ba, SignMode.UNSIGNED, bo);

    }
    
    /**
     * Generates an int number corresponding to
     * the byte array <code>ba</code>, from less significant 
     * byte to most significant byte
     * @param ba the byte array to be converted
     * @param sm an instance of class <code>SignMode</code> indicating
     * whether the represented number should be interpreted as
     * signed or unsigned
     * @return an int representing the value of the 
     * byte array <code>ba</code>
     */
    public static int toInt(byte[] ba, SignMode sm){

        return toInt(ba, sm, ByteOrder.LSB_MSB);

    }
    
    /**
     * Generates an int number corresponding to
     * the byte array <code>ba</code>, from less significant 
     * byte to most significant byte
     * @param ba the byte array to be converted
     * @param sm an instance of class <code>SignMode</code> indicating
     * whether the represented number should be interpreted as
     * signed or unsigned
     * @param bo an instance of class <code>ByteOrder</code> indicating
     * the order in which the bytes of <code>ba</code>
     * should be interpreted
     * @return an int representing the value of the 
     * byte array <code>ba</code>
     */
    public static int toInt(byte[] ba, SignMode sm, ByteOrder bo){

    	return DigitUtils.toInt(toString(ba), DigitFormat.HEX, sm, bo);
    	
    }
    
    /**
     * Generates a boolean value corresponding to the byte array
     * <code>ba</code>, from less significant byte to most significant byte
     * @param ba the byte array to be converted
     * @return a boolean representing the value of the
     * byte array <code>ba</code>
     */
    public static boolean toBoolean(byte[] ba){

        return toBoolean(ba, ByteOrder.LSB_MSB);

    }
    
    /**
     * Generates a boolean value corresponding to the byte array
     * <code>ba</code>, from less significant byte to most significant byte
     * @param ba the byte array to be converted
     * @param bo an instance of class <code>ByteOrder</code> indicating
     * the order in which the bytes of <code>ba</code>
     * should be interpreted
     * @return a boolean representing the value of the
     * byte array <code>ba</code>
     */
    public static boolean toBoolean(byte[] ba, ByteOrder bo){
    	
    	int lsb;
    	
    	if(bo == ByteOrder.LSB_MSB) lsb = 0;
    	else lsb = ba.length - 1;
    	
    	return !compareBAs(and(extractSubBA(ba, lsb, 1), 
                    toBA("01")),
                new byte[1]);
    	
    }
  
    // Binary Computation Methods
  
    /**
     * Computes the two's complement of the value stored in the
     * byte array <code>ba</code>, from less significant byte
     * to most significant byte
     * @param ba a byte array
     * @return a byte array representing the two's complement 
     * of <code>ba</code>
     */
    public static byte[] twosComp(byte[] ba){

        return twosComp(ba, ByteOrder.LSB_MSB);
    }
    
    /**
     * Computes the two's complement of the value stored in the
     * byte array <code>ba</code>, from less significant byte
     * to most significant byte
     * @param ba a byte array
     * @param bo an instance of class <code>ByteOrder</code> indicating
     * the order in which the bytes of <code>ba</code>
     * should be interpreted
     * @return a byte array representing the two's complement 
     * of <code>ba</code>    
     */        
    
    public static byte[] twosComp(byte[] ba, ByteOrder bo){

    	int len = ba.length;
    	DigitFormat df = DigitFormat.HEX;    	
    	String s = toString(ba, df);
    	String tc = DigitUtils.twosComp(s, df, bo);
    	
        return toBA(tc, len, df, bo);

    }
  
    /**
     * Computes the exclusive OR operation (XOR) between multiple byte arrays 
     * @param bas an undetermined number of byte arrays, represented in
     * less significant byte to most significant byte order
     * @return a byte array representing the XOR operation 
     * of <code>bas</code> byte arrays
     */
    public static byte[] xor(byte[]...bas){

        return xor(ByteOrder.LSB_MSB, bas);

    }
     
    /**
     * Computes the exclusive OR operation (XOR) between multiple byte arrays 
     * @param bas an undetermined number of byte arrays
     * @param bo an instance of class <code>ByteOrder</code> indicating
     * the order in which the bytes of <code>bas</code>
     * should be interpreted
     * @return a byte array representing the XOR operation 
     * of <code>bas</code> byte arrays
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
     * Computes the logical conjunction operation (AND)
     * between multiple byte arrays 
     * @param bas an undetermined number of byte arrays, represented in
     * less significant byte to most significant byte order
     * @return a byte array representing the AND operation 
     * of <code>bas</code> byte arrays
     */
    public static byte[] and(byte[]...bas){

        return and(ByteOrder.LSB_MSB, bas);

    }
    
    /**
     * Computes the logical conjunction operation (AND)
     * between multiple byte arrays 
     * @param bas an undetermined number of byte arrays
     * @param bo an instance of class <code>ByteOrder</code> indicating
     * the order in which the bytes of <code>bas</code>
     * should be interpreted
     * @return a byte array representing the AND operation 
     * of <code>bas</code> byte arrays
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
     * @param ba byte array to be inverted
     * 
     * @return the inverted byte array
     */
    
    public static byte[] not(byte[] ba){

        byte[] auxba = new byte[ba.length];

        for(int i = 0; i<ba.length; i++){

            auxba[i] = (byte)~ba[i];

        }

        return auxba;

    }
    
    // Array Transformation Methods
    
    /**
     * Returns a new byte array of <code>len</code> bytes that is the result of
     * expanding <code>ba</code> with zero-filled bytes.
     * 
     * @param ba the original byte array
     * @param len number of bytes of the expanded byte array
     * 
     * @return the expanded byte array
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
     * Returns a new byte array that is the result of a circular movement of the
     * parameter <code>ba</ba>. 
     * 
     * @param ba byte array whose bytes are moved
     * @param count number of positions to move each byte
     * @param dir Direction in which the bytes move
     * 
     * @return the rotated byte array
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
    
    /**
     * Returns a new byte array that is a subarray of the <code>ba</code> parameter. 
     * The subarray begins at the position specified by <code>beginIndex</code> 
     * and it has <code>length</length> bytes.
     * 
     * @param ba the byte array 
     * @param beginIndex position where the subarray begins inside <code>ba</code>
     * @param length the number of bytes of the returned byte array
     * 
     * @return the extracted byte array
     */

    
    public static byte[] extractSubBA(byte[] ba, int begin, int olen){
    	
    	if((begin + olen)>ba.length) throw new IllegalArgumentException("Length Parameter too high");

        byte[] auxba = new byte[olen];

        System.arraycopy(ba, begin, auxba, 0, olen);

        return auxba;
    }
 
    // Array Combination Methods

    /**
     * Returns a new byte array that is the result of joining the byte arrays
     * received in <code>bas</code>. 
     * 
     * @param bas the byte arrays to join
     * 
     * @return the concatenated byte array
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
     * Generates a matrix where each row contains one of the byte arrays received as
     * parameters, all of them aligned considering a Less Significant Byte to
     * Most Significant Byte Order.
     * Therefore, bytes with the same relevance will end located at the same column
     * regardless of the size of its array.
     *  
     * @param bas byte arrays to be aligned
     * 
     * @return a matrix containing aligned byte arrays
     * 
     * @see #alignBAs(ByteOrder, byte[])
     * 
     */
    public static byte[][] alignBAs(byte[]...bas){

        return alignBAs(ByteOrder.LSB_MSB, bas);

    }
    
    //TODO 4 Documentación: ejemplo
    
    /**
     * Returns a matrix where each row contains one of the byte arrays received as
     * parameters, all of them aligned taking into account the Byte Order specified.
     * Therefore, bytes with the same relevance will end located at the same column
     * regardless of the size of its array.
     *  
     * @param bo the order of the bytes in the byte arrays
     * @param bas byte arrays to be aligned
     * 
     * @return a matrix containing aligned byte arrays
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
    
    // Array Check Methods

    /**
     * Returns <code>true</code> if two byte arrays are equal to each other
     * 
     * @param ba1 the first byte[] to be compared
     * @param ba2 the byte[] to be compared with ba2
     * 
     * @return <code>true</code> if ba1 and ba2 are identical; 
     * <code>false</code> otherwise
     * 
     * @see java.util.Arrays#equals(byte[], byte[])
     */
    public static boolean compareBAs(byte[] ba1, byte[] ba2){

        return Arrays.equals(ba1, ba2);

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



    
    // Array Generation Methods

    /**
     * Returns a randomized byte array of the specified length generating
     * with SHA1PRNG algorithm. 
     * As the resulting array is generated using the SecureRandom class,
     * it is suitable for cryptographic purposes.
     * 
     * @param length the length (in bytes) of the byte array to be generated
     *  
     * @return the random byte array
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
     * Returns a randomized byte array of the specified length. 
     * As the resulting array is generated using the SecureRandom class,
     * it is suitable for cryptographic purposes.
     * @param length an int representing the length (in bytes) of the byte array 
     * to be generated
     * @param alg a String containing the algorithm that will be used for generating
     * the randomized byte array.  
     * @return the random byte array 
     * @throws NoSuchAlgorithmException  
     * @see java.security.SecureRandom#getInstance(String)
     */
    public static byte[] getRandomBA(int length, String alg) 
    		throws NoSuchAlgorithmException{

        byte[] randomBytes = new byte[length];

        try{

            SecureRandom rnd = SecureRandom.getInstance(alg);
            rnd.nextBytes(randomBytes);

        }catch(NoSuchAlgorithmException e){
            throw e;
       }

       return randomBytes;
    }
    

    
    /**
     * Orientation in which a byte array should be rotated.
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
		
}
