package test;

import dflibrary.utils.ba.*;

public class BATest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		byte[] b1 = BAUtils.toBA("AABB");
		byte[] b2 = BAUtils.toBA("0011223344");
		byte[] b3 = BAUtils.toBA("CC55DD");
		
		byte[][] ba2d1 = BAUtils.create2dBA(b1, b2, b3);
		
		System.out.println(BAUtils.toString(ba2d1));
		System.out.println("");
		
		byte[] b4 = BAUtils.toBA("EEFF");
		byte[] b5 = BAUtils.toBA("66778899");
		
		byte[][] ba2d2 = BAUtils.create2dBA(b4, b5);
		
		System.out.println(BAUtils.toString(ba2d2));
		System.out.println("");
		
		byte[][] jba2d = BAUtils.join2dBAs(ba2d1, ba2d2);
		System.out.println(BAUtils.toString(jba2d));
		System.out.println("");
		
		byte[] b6 = BAUtils.toBA("12345678");
		
		byte[][] jba2d2 = BAUtils.create2dBA(jba2d, b6);
		System.out.println(BAUtils.toString(jba2d2));
		System.out.println("");
		
		System.out.println("PRUEBA");
		
	}

}
