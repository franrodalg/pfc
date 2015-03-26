package dflibrary.utils.security;

import java.util.zip.CRC32;

import dflibrary.utils.ba.BAUtils;

/**
 * 
 * @author Francisco Rodríguez Algarra
 *
 */
public class CRC {

	/**
	 * 
	 * @param data
	 * @return
	 */
	public static byte[] CRC32(byte[] data){
		
		if(data == null) throw new NullPointerException();
		
		CRC32 crc = new CRC32();
		
		crc.update(data);
		
		String res = Long.toHexString(~crc.getValue());
		
		return BAUtils.reverseBA(BAUtils.extractSubBA(BAUtils.toBA(res), 4, 4));

	}
	
	/**
	 * 
	 * @param data
	 * @return
	 */
	public static byte[] CRC16(byte[] data){
		
		if(data == null) throw new NullPointerException();

        byte[] crc = BAUtils.toBA("6363");

        for(byte b: data){

            crc = updateCRC16(b, crc);

        }

        return crc;
		
		
	}
	
	/**
	 * 
	 * @param crc
	 * @return
	 */
	private static byte[] updateCRC16(byte data, byte[] crc){
		
		if(crc == null) throw new NullPointerException();
        
        int b = (data ^ crc[0]) & 0xFF;

        b = (b ^ (b<<4)) & 0xFF;

        int b0 = crc[1] & 0x00FF;
        int b1 = (b<<8) & 0xFFFF;
        int b2 = (b<<3) & 0xFFFF;
        int b3 = (b>>>4) & 0xFFFF;

        int aux = (b0 ^ b1 ^ b2 ^ b3) & 0xFFFF;

        crc[1] = (byte)(aux>>>8);
        crc[0] = (byte)(aux & 0xFF);

        return crc;
		
	}
	
	/*

	
	private static byte[] rShift(byte[] ba){
		
		boolean lsb = false;
    	byte[] aux;
    	byte[] res = BAUtils.extractSubBA(ba, 0, ba.length);
   
    	
    	for(int i = 0; i < ba.length; i++){
    		
    		res[i] = (byte) ((int) res[i] >> 1);
    		
    		if(lsb) res[i] = (byte) ((int)res[i] | (byte)0x80);
    		
    		aux = BAUtils.and(BAUtils.extractSubBA(ba, i, 1), BAUtils.toBA("01"));
    		if(BAUtils.compareBAs(aux, new byte[1])) lsb = false;
    		else lsb = true;
    		
    	}
    	
    	return res;
    	
		
		
	}

	*/
	
	public static final byte[] CRC32_PRESET = {(byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF};
	public static final byte[] CRC32_POLY = {(byte)0xED, (byte)0xB8,  (byte)0x83, (byte)0x20};
	
	
	public static void main(String[] args){
		
		byte[] ba = BAUtils.toBA("01000000");
		
		System.out.println(BAUtils.toString(CRC16(ba)));
		

	}
	
}
