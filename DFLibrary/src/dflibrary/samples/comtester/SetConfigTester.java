package dflibrary.samples.comtester;

import dflibrary.library.*;
import dflibrary.library.param.*;
import dflibrary.library.security.*;
import dflibrary.utils.ba.BAUtils;

public class SetConfigTester {

	
	public SetConfigTester(){
		
		this.session = new DFSession();
		
	}

public DFResponse setConfiguration(ConfigOption opt){
		
		byte[] optBA = opt.getOpt().toBA();
		byte[] encOptData = DFCrypto.getEncOptData(opt, getSession());
		
		System.out.println(BAUtils.toString(encOptData));
		
		byte[] res = SetConfiguration(optBA, encOptData);
		
		if(!SC.isOk(res)){
			session.resetAuth();
			return new DFResponse(res);
		}
		
		DFCrypto.checkCMAC(res, session);
		
		
		return new DFResponse(res);
		
	}
	
	
	public static byte[] SetConfiguration(byte[] optBA, byte [] encOptData){
		
		byte[] res = new byte[1];
		
		return res;
		
		
	}

	public DFSession getSession(){ return this.session;}
	
	public static void main(String[] args){
		
		SetConfigTester tester = new SetConfigTester();
		byte[] sk = BAUtils.toBA("A117814D54FC78BDA117814D54FC78BD");
		tester.getSession().setAuth(AuthType.TDEA_NATIVE, 0, sk, true);
		
		byte[] atsData = BAUtils.toBA("0D757781024E58502054455354");
		
		DFResponse res = tester.setConfiguration(new ConfigOption(atsData));
		System.out.println(res);
		System.out.println(tester.getSession());
		
	}
	
	private DFSession session;
	
	
}
