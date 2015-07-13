package dflibrary.library.param.fileset;

import dflibrary.utils.ba.*;
import dflibrary.utils.ba.DigitUtils.SignMode;
import dflibrary.library.param.*;

/**
 * Provides an encapsulation for the file settings of value files
 * @author Francisco Rodríguez Algarra
 */
public class ValueFileSettings extends FileSettings{
	
	/**
	 * Creates a default instance of class <code>ValueFileSettings</code>
	 */
	public ValueFileSettings(){		
		
		this(ComSet.PLAIN, new AccessRights(), new Value(), new Value(), 
				new Value(), false, false);
		
	}	
	
	/**
	 * Creates an instance of class <code>ValueFileSettings</code>
	 * @param comSet an instance of class <code>ComSet</code> representing 
	 * the file's communication settings
	 * @param accessRights an instance of class <code>AccessRights</code> 
	 * representing the file's access rights
	 */
	public ValueFileSettings(ComSet comSet, AccessRights accessRights){
		
		this(comSet, accessRights, new Value(), new Value(), new Value(), 
				false, false);
		
	}	
	
	/**
	 * Creates an instance of class <code>ValueFileSettings</code>
	 * @param comSet an instance of class <code>ComSet</code> representing 
	 * the file's communication settings
	 * @param accessRights an instance of class <code>AccessRights</code> 
	 * representing the file's access rights
	 * @param lowerLimit an instance of class <code>Value</code> representing
	 * the lower possible integer that can be stored in the file
	 * @param upperLimit an instance of class <code>Value</code> representing
	 * the higher possible integer that can be stored in the file
	 * @param limitedCreditValue an instance of class <code>Value</code>
	 * representing the maximum possible amount that can be increased via
	 * a call to <code>limitedCredit</code>
	 * @param limitedCreditEnabled a boolean indicating whether limited 
	 * credit operations are allowed or not
	 */
	public ValueFileSettings(ComSet comSet, AccessRights accessRights,
			Value lowerLimit, Value upperLimit, Value limitedCreditValue, 
			boolean limitedCreditEnabled){
		
		this(comSet, accessRights, lowerLimit, upperLimit, limitedCreditValue, 
				limitedCreditEnabled, false);
		
	}
	
	/**
	 * Creates an instance of class <code>ValueFileSettings</code>
	 * @param comSet an instance of class <code>ComSet</code> representing 
	 * the file's communication settings
	 * @param accessRights an instance of class <code>AccessRights</code> 
	 * representing the file's access rights
	 * @param lowerLimit an instance of class <code>Value</code> representing
	 * the lower possible integer that can be stored in the file
	 * @param upperLimit an instance of class <code>Value</code> representing
	 * the higher possible integer that can be stored in the file
	 * @param limitedCreditValue an instance of class <code>Value</code>
	 * representing the maximum possible amount that can be increased via
	 * a call to <code>limitedCredit</code>
	 * @param limitedCreditEnabled a boolean indicating whether limited 
	 * credit operations are allowed or not
	 * @param getFreeValueEnabled a boolean indicating whether get value
	 * operations are allowed without previous authentication
	 */
	public ValueFileSettings(ComSet comSet, AccessRights accessRights,
			Value lowerLimit, Value upperLimit, Value limitedCreditValue, 
			boolean limitedCreditEnabled, boolean getFreeValueEnabled){
		
		super(FileType.VALUE, comSet, accessRights);
		
		setLimits(lowerLimit, upperLimit);
		setLimitedCredit(limitedCreditValue, limitedCreditEnabled);
		setGetFreeValueEnabled(getFreeValueEnabled);
		
	}
	
	/**
	 * Creates an instance of class <code>ValueFileSettings</code>
	 * @param fileSet a byte array containing the file settings
	 */
	public ValueFileSettings(byte[] fileSet){
		
		super(fileSet);
		
		if(fileSet.length < 17) throw new IllegalArgumentException();
		
		byte[] lowerLimitBA = BAUtils.extractSubBA(fileSet, 4, 4);
		byte[] upperLimitBA = BAUtils.extractSubBA(fileSet, 8, 4);
		byte[] limitedCreditValueBA = BAUtils.extractSubBA(fileSet, 12, 4);
		byte[] limCredEnGetFreeValBA = BAUtils.extractSubBA(fileSet, 16, 1);
		
		setLimits(lowerLimitBA, upperLimitBA);
		setLimitedCredit(limitedCreditValueBA, limCredEnGetFreeValBA);
		setGetFreeValueEnabled(limCredEnGetFreeValBA);
				
	}
	
	/**
	 * @return an instance of class <code>Value</code> representing
	 * the lower possible integer that can be stored in the file
	 */
	public Value getLowerLimit(){
		
		return this.lowerLimit;
		
	}
	
	/**
	 * @return an instance of class <code>Value</code> representing
	 * the higher possible integer that can be stored in the file
	 */
	public Value getUpperLimit(){
		
		return this.upperLimit;
		
	}
	
	/** 
	 * @return an instance of class <code>Value</code>
	 * representing the maximum possible value that can be increased via
	 * a call to <code>limitedCredit</code>
	 */
	public Value getLimitedCreditValue(){
		
		return this.limitedCreditValue;
		
	}
	
	/** 
	 * @return <code>true</code> if limited credit operations are allowed;
	 * <code>false</code> otherwise
	 */
	public boolean getLimitedCreditEnabled(){
		
		return this.limitedCreditEnabled;
		
	}
	
	/**
	 * @return <code>true</code> if get value
	 * operations are allowed without previous authentication;
	 * <code>false</code> otherwise
	 */
	public boolean getFreeValueEnabled(){
		
		return this.getFreeValueEnabled;
		
	}
	
	/**
	 * @param lowerLimit a byte array 
	 * representing the lower possible integer that can be 
	 * stored in the file
	 */
	public void setLowerLimit(byte[] lowerLimit){
		
		if(lowerLimit == null) throw new NullPointerException();
		if(lowerLimit.length != 4) throw new IllegalArgumentException();
		
		setLowerLimit(new Value(lowerLimit));
		
	}
	
	/**
	 * @param lowerLimit an instance of class <code>Value</code> 
	 * representing the lower possible integer that can be stored 
	 * in the file
	 */
	public void setLowerLimit(Value lowerLimit){
		
		if(lowerLimit == null) throw new NullPointerException();
		if(lowerLimit.getValue() > getUpperLimit().getValue()) 
			throw new IllegalArgumentException();
		
		this.lowerLimit = lowerLimit;
		
	}
	
	/**
	 * @param upperLimit a byte array 
	 * representing the higher possible integer that can be 
	 * stored in the file
	 */
	public void setUpperLimit(byte[] upperLimit){
		
		if(upperLimit == null) throw new NullPointerException();
		if(upperLimit.length != 4) throw new IllegalArgumentException();
		
		setUpperLimit(new Value(upperLimit));
		
	}
	
	/**
	 * @param upperLimit an instance of class <code>Value</code> 
	 * representing the higher possible integer that can be stored 
	 * in the file
	 */
	public void setUpperLimit(Value upperLimit){
		
		if(upperLimit == null) throw new NullPointerException();
		if(upperLimit.getValue() < getLowerLimit().getValue()) 
			throw new IllegalArgumentException();
		
		this.upperLimit = upperLimit;
		
	}
	
	/**
	 * Sets both the lower and upper limits of the value file
	 * @param lowerLimit a byte array 
	 * representing the lower possible integer that can be 
	 * stored in the file
	 * @param upperLimit a byte array 
	 * representing the higher possible integer that can be 
	 * stored in the file
	 */
	public void setLimits(byte[] lowerLimit, byte[] upperLimit){
		
		if((lowerLimit == null) || (upperLimit == null)) 
			throw new NullPointerException();
		if((lowerLimit.length != 4) || (upperLimit.length != 4)) 
			throw new IllegalArgumentException();
		
		setLimits(new Value(lowerLimit), new Value(upperLimit));
	}
	
	/**
	 * Sets both the lower and upper limits of the value file
	 * @param lowerLimit an instance of class <code>Value</code> 
	 * representing the lower possible integer that can be stored 
	 * in the file
	 * @param upperLimit an instance of class <code>Value</code> 
	 * representing the higher possible integer that can be stored 
	 * in the file
	 */
	public void setLimits(Value lowerLimit, Value upperLimit){
		
		if((upperLimit == null) || (lowerLimit == null)) 
			throw new NullPointerException();
		if(lowerLimit.getValue() > upperLimit.getValue()) 
			throw new IllegalArgumentException();
		
		this.lowerLimit = lowerLimit;
		this.upperLimit = upperLimit;
		
	}
	
	/**
	 * @param value a byte array
	 * representing the maximum possible amount that can be increased via
	 * a call to <code>limitedCredit</code>
	 */
	public void setLimitedCreditValue(byte[] value){
		
		if(value == null) throw new NullPointerException();
		if(value.length != 4) throw new IllegalArgumentException();
		
		setLimitedCreditValue(new Value(value));
		
	}
	
	/** 
	 * @param value an instance of class <code>Value</code>
	 * representing the maximum possible amount that can be increased via
	 * a call to <code>limitedCredit</code>
	 */
	public void setLimitedCreditValue(Value value){
		
		this.limitedCreditValue = value;
		
	}
	
	/**
	 * @param enabled a byte array representing whether limited 
	 * credit operations are allowed or not
	 */
	public void setLimitedCreditEnabled(byte[] enabled){
		
		if(enabled == null) throw new NullPointerException();
		if(enabled.length != 1) throw new IllegalArgumentException();
		
		setLimitedCreditEnabled(BAUtils.toBoolean(
				BAUtils.and(enabled, BAUtils.toBA("01"))));
			
	}
	
	/**
	 * @param enabled a boolean indicating whether limited 
	 * credit operations are allowed or not
	 */
	public void setLimitedCreditEnabled(boolean enabled){
		
		if((!enabled) && (getLimitedCreditValue().getValue() != 0))
			throw new IllegalArgumentException();
		
		this.limitedCreditEnabled = enabled;
		
	}
	
	/**
	 * Sets both the amount and permission corresponding to
	 * the limited credit operations
	 * @param value a byte array
	 * representing the maximum possible amount that can be increased via
	 * a call to <code>limitedCredit</code>
	 * @param enabled a byte array indicating whether get value
	 * operations are allowed without previous authentication
	 */
	public void setLimitedCredit(byte[] value, byte[] enabled){
		
		if((value == null) || (enabled == null))
			throw new NullPointerException();
		if((value.length != 4) || (enabled.length != 1))
			throw new IllegalArgumentException();
		
		Value val = new Value(value);
		boolean en = BAUtils.toBoolean(enabled);
		
		setLimitedCredit(val, en);		
		
	}
	
	/**
	 * Sets both the amount and permission corresponding to
	 * the limited credit operations
	 * @param value an instance of class <code>Value</code>
	 * representing the maximum possible amount that can be increased via
	 * a call to <code>limitedCredit</code>
	 * @param enabled a boolean indicating whether get value
	 * operations are allowed without previous authentication
	 */
	public void setLimitedCredit(Value value, boolean enabled){
		
		if(value == null) throw new NullPointerException();
		
		this.limitedCreditValue = value;
		this.limitedCreditEnabled = enabled;
		
	}

	/**
	 * @param limCredEnGetFreeValEn a byte array containing both
	 * the limited credit and the free get value permissions
	 */
	public void setGetFreeValueEnabled(byte[] limCredEnGetFreeValEn){
		
		if(limCredEnGetFreeValEn == null) throw new NullPointerException();
		byte[] aux = BAUtils.and(limCredEnGetFreeValEn, BAUtils.toBA("02"));
		
		this.getFreeValueEnabled = !BAUtils.compareBAs(aux, new byte[1]);
	}
	
	/**
	 * @param enabled a boolean indicating whether get value
	 * operations are allowed without previous authentication
	 */
	public void setGetFreeValueEnabled(boolean enabled){
		
		this.getFreeValueEnabled = enabled;
	}
	
    @Override
	public byte[] toBA(){
		
		byte[] lowerLimitBA = getLowerLimit().toBA();
		byte[] upperLimitBA = getUpperLimit().toBA();
		byte[] limitedCreditValueBA = getLimitedCreditValue().toBA();
		
		byte[] limitedCreditEnabledBA = BAUtils.toBA(
				getLimitedCreditEnabled());
		
		if(getFreeValueEnabled) 
			limitedCreditEnabledBA = BAUtils.xor(
					limitedCreditEnabledBA, BAUtils.toBA("02"));
		
		return BAUtils.concatenateBAs(super.toBA(), lowerLimitBA, 
				upperLimitBA, limitedCreditValueBA, limitedCreditEnabledBA);
		
	}
	
	@Override
	public String toString(){
		
		String s = super.toString() + "\n";
		
		s = s + "Lower Limit: " + this.getLowerLimit() + "\n";
		s = s +	"Upper Limit: " + this.getUpperLimit() + "\n";
		s = s +	"Limited Credit Value: " + this.getLimitedCreditValue() + "\n";
		s = s +	"Limited Credit Enabled: " + this.getLimitedCreditEnabled() + "\n";
		s = s +	"Get Free Value Enabled: " + this.getFreeValueEnabled() + "\n";
		return s;
		
	}
	
	private Value lowerLimit;
	private Value upperLimit;
	private Value limitedCreditValue;
	private boolean limitedCreditEnabled;
	private boolean getFreeValueEnabled;
	
}
