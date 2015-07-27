package dflibrary.utils.security;

import java.util.zip.CRC32;

import dflibrary.utils.ba.BAUtils;

/**
 * Provides methods for the computation of CRCs
 * @author Francisco Rodríguez Algarra
 */
public class CRC {

    /**
     * Computes the 32-bit CRC of <code>data</code>
     * @param data a byte array
     * @return the 32-bit CRC of <code>data</code>
     */
    public static byte[] CRC32(byte[] data){
		
    	if(data == null) throw new NullPointerException();
		
    	CRC32 crc = new CRC32();
		
        crc.update(data);
		
    	String res = Long.toHexString(~crc.getValue());

	return BAUtils.reverseBA(
                BAUtils.extractSubBA(BAUtils.toBA(res), 4, 4));

    }
	
    /**
     * Computes the 16-bit CRC of <code>data</code> 
     * @param data a byte array
     * @return the 16-bit array of <code>data</code>
     */
    public static byte[] CRC16(byte[] data){
		
        if(data == null) throw new NullPointerException();

        byte[] crc = BAUtils.toBA("6363");

        for(byte b: data){

            crc = updateCRC16(b, crc);

        }

        return crc;	

    }
	
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

    /**
     * The initial value for 32-bit CRC computation
     */
    public static final byte[] CRC32_PRESET = {
        (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF
    };

    /**
     * The polynomial for 32-bit CRC computation
     */
    public static final byte[] CRC32_POLY = {
        (byte)0xED, (byte)0xB8,  (byte)0x83, (byte)0x20
    };
		
}
