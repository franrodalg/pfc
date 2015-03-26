package dflibrary.samples.comtester;

import dflibrary.library.*;
import dflibrary.library.param.DFNamesRes;
import dflibrary.library.security.AuthType;
import dflibrary.library.security.DFCrypto;
import dflibrary.utils.ba.BAUtils;

public class GetDFNamesTester {
	
	public GetDFNamesTester(){
		
		this.session = new DFSession();
		
	}

	public DFResponse getDFNames(){
		
		//byte[][] res = GetDFNames();
		
		byte[][] res = get();
		
		if(!SC.isOk(res[0])){
			session.resetAuth();
			return new DFResponse(res[0]);
			
		}
		
		byte[] cmacData = SC.OPERATION_OK.toBA();
		byte[][] dfNames = new byte[res.length - 1][];
		
		for(int i = 1; i < res.length - 1; i ++){
			
			cmacData = BAUtils.concatenateBAs(cmacData, res[i]);
			dfNames[i-1] = res[i];
			
		}
		
		if(res.length > 1) {
						
			cmacData = BAUtils.concatenateBAs(cmacData, res[res.length - 1]); 
			
			byte[] lastFrame = DFCrypto.getData(BAUtils.concatenateBAs(new byte[1], res[res.length - 1]), session);
			if(lastFrame.length == 0){
				
				byte[][] aux = new byte[dfNames.length - 1][];
				System.arraycopy(dfNames, 0, aux, 0, dfNames.length-1);
				dfNames = aux;
				
			}
			else dfNames[dfNames.length - 1] = lastFrame;
		}
			
		DFCrypto.updateCmacIV(ComCode.GET_DF_NAMES, getSession());
		
		DFCrypto.checkCMAC(cmacData, getSession());
		
		return new DFResponse(SC.OPERATION_OK, new DFNamesRes(dfNames));
		
		
	}
	
	
	public static byte[][] get(){
		
		byte[][] res = new byte[4][];
		//byte[] cmac = new byte[0];
		byte[] cmac = BAUtils.toBA("123456789ABCDEF0");
		
		res[0] = new byte[1];
		
		res[1] = BAUtils.concatenateBAs(BAUtils.toBA("100000"), BAUtils.toBA("1000"), BAUtils.toBA("100000"));
		//res[1] = new byte[0];
		
		res[2] = BAUtils.concatenateBAs(BAUtils.toBA("200000"), BAUtils.toBA("2000"), BAUtils.toBA("20000000"));
		res[3] = BAUtils.concatenateBAs(BAUtils.toBA("300000"), BAUtils.toBA("3000"), BAUtils.toBA("3000"));
		
		res[res.length - 1] = BAUtils.concatenateBAs(res[res.length - 1], cmac);
		
		
		return res;
		
		
	}

	public DFSession getSession(){ return this.session;}
	
	public static void main(String[] args){
		
		GetDFNamesTester tester = new GetDFNamesTester();
		
		tester.getSession().setAuth(AuthType.TDEA_STANDARD, 0, new byte[16], true);
		
		DFResponse res = tester.getDFNames();
		System.out.println(res);
		System.out.println(tester.getSession());
		
	}
	
	private DFSession session;
	
}
