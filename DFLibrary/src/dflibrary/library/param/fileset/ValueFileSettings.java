package dflibrary.library.param.fileset;

import dflibrary.utils.ba.*;
import dflibrary.utils.ba.DigitUtils.SignMode;
import dflibrary.library.param.*;

/**
 * 
 * @author Francisco Rodríguez Algarra
 *
 */
public class ValueFileSettings extends FileSettings{
	
	/**
	 * 
	 */
	public ValueFileSettings(){		
		
		this(ComSet.PLAIN, new AccessRights(), new Value(), new Value(), new Value(), false, false);
		
	}	
	
	/**
	 * 
	 * @param fileType
	 * @param comSet
	 * @param accessRights
	 */
	public ValueFileSettings(ComSet comSet, AccessRights accessRights){
		
		this(comSet, accessRights, new Value(), new Value(), new Value(), false, false);
		
	}	
	
	/**
	 * 
	 * @param fileType
	 * @param comSet
	 * @param accessRights
	 * @param lowerLimit
	 * @param upperLimit
	 * @param limitedCreditValue
	 * @param LimitedCreditEnabled
	 */
	public ValueFileSettings(ComSet comSet, AccessRights accessRights,
			Value lowerLimit, Value upperLimit, Value limitedCreditValue, boolean limitedCreditEnabled){
		
		this(comSet, accessRights, lowerLimit, upperLimit, limitedCreditValue, limitedCreditEnabled, false);
		
	}
	
	/**
	 * 
	 * @param fileType
	 * @param comSet
	 * @param accessRights
	 * @param lowerLimit
	 * @param upperLimit
	 * @param limitedCreditValue
	 * @param limitedCreditEnabled
	 * @param getFreeValueEnabled
	 */
	public ValueFileSettings(ComSet comSet, AccessRights accessRights,
			Value lowerLimit, Value upperLimit, Value limitedCreditValue, boolean limitedCreditEnabled,
			boolean getFreeValueEnabled){
		
		super(FileType.VALUE, comSet, accessRights);
		
		setLimits(lowerLimit, upperLimit);
		setLimitedCredit(limitedCreditValue, limitedCreditEnabled);
		setGetFreeValueEnabled(getFreeValueEnabled);
		
	}
	
	
	/**
	 * 
	 * @param fileSet
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
	 * 
	 * @return
	 */
	public Value getLowerLimit(){
		
		return this.lowerLimit;
		
	}
	
	/**
	 * 
	 * @return
	 */
	public Value getUpperLimit(){
		
		return this.upperLimit;
		
	}
	
	/**
	 * 
	 * @return
	 */
	public Value getLimitedCreditValue(){
		
		return this.limitedCreditValue;
		
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean getLimitedCreditEnabled(){
		
		return this.limitedCreditEnabled;
		
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean getFreeValueEnabled(){
		
		return this.getFreeValueEnabled;
		
	}
	
	/**
	 * 
	 * @param lowerLimit
	 */
	public void setLowerLimit(byte[] lowerLimit){
		
		if(lowerLimit == null) throw new NullPointerException();
		if(lowerLimit.length != 4) throw new IllegalArgumentException();
		
		setLowerLimit(new Value(lowerLimit));
		
	}
	
	/**
	 * 
	 * @param lowerLimit
	 */
	public void setLowerLimit(Value lowerLimit){
		
		if(lowerLimit == null) throw new NullPointerException();
		if(lowerLimit.getValue() > getUpperLimit().getValue()) throw new IllegalArgumentException();
		
		this.lowerLimit = lowerLimit;
		
	}
	
	/**
	 * 
	 * @param upperLimit
	 */
	public void setUpperLimit(byte[] upperLimit){
		
		if(upperLimit == null) throw new NullPointerException();
		if(upperLimit.length != 4) throw new IllegalArgumentException();
		
		setUpperLimit(new Value(upperLimit));
		
	}
	
	/**
	 * 
	 * @param upperLimit
	 */
	public void setUpperLimit(Value upperLimit){
		
		if(upperLimit == null) throw new NullPointerException();
		if(upperLimit.getValue() < getLowerLimit().getValue()) throw new IllegalArgumentException();
		
		this.upperLimit = upperLimit;
		
	}
	
	/**
	 * 
	 * @param lowerLimit
	 * @param upperLimit
	 */
	public void setLimits(byte[] lowerLimit, byte[] upperLimit){
		
		if((lowerLimit == null) || (upperLimit == null)) throw new NullPointerException();
		if((lowerLimit.length != 4) || (upperLimit.length != 4)) throw new IllegalArgumentException();
		
		setLimits(new Value(lowerLimit), new Value(upperLimit));
	}
	
	/**
	 * 
	 * @param lowerLimit
	 * @param upperLimit
	 */
	public void setLimits(Value lowerLimit, Value upperLimit){
		
		if((upperLimit == null) || (lowerLimit == null)) throw new NullPointerException();
		if(lowerLimit.getValue() > upperLimit.getValue()) throw new IllegalArgumentException();
		
		this.lowerLimit = lowerLimit;
		this.upperLimit = upperLimit;
		
	}
	
	/**
	 * 
	 * @param value
	 */
	public void setLimitedCreditValue(byte[] value){
		
		if(value == null) throw new NullPointerException();
		if(value.length != 4) throw new IllegalArgumentException();
		
		setLimitedCreditValue(new Value(value));
		
		
	}
	
	/**
	 * 
	 * @param value
	 */
	public void setLimitedCreditValue(Value value){
		
		this.limitedCreditValue = value;
		
	}
	
	/**
	 * 
	 * @param enabled
	 */
	public void setLimitedCreditEnabled(byte[] enabled){
		
		if(enabled == null) throw new NullPointerException();
		if(enabled.length != 1) throw new IllegalArgumentException();
		
		setLimitedCreditEnabled(BAUtils.toBoolean(BAUtils.and(enabled, BAUtils.toBA("01"))));
		
		
	}
	
	/**
	 * 
	 * @param enabled
	 */
	public void setLimitedCreditEnabled(boolean enabled){
		
		if((!enabled) && (getLimitedCreditValue().getValue() != 0)) throw new IllegalArgumentException();
		
		this.limitedCreditEnabled = enabled;
		
	}
	
	/**
	 * 
	 * @param value
	 * @param enabled
	 */
	public void setLimitedCredit(byte[] value, byte[] enabled){
		
		if((value == null) || (enabled == null)) throw new NullPointerException();
		if((value.length != 4) || (enabled.length != 1)) throw new IllegalArgumentException();
		
		Value val = new Value(value);
		boolean en = BAUtils.toBoolean(enabled);
		
		setLimitedCredit(val, en);		
		
	}
	
	/**
	 * 
	 * @param value
	 * @param enabled
	 */
	public void setLimitedCredit(Value value, boolean enabled){
		
		if(value == null) throw new NullPointerException();
		
		this.limitedCreditValue = value;
		this.limitedCreditEnabled = enabled;
		
	}

	/**
	 * 
	 * @param limCredEnGetFreeValEn
	 */
	public void setGetFreeValueEnabled(byte[] limCredEnGetFreeValEn){
		
		if(limCredEnGetFreeValEn == null) throw new NullPointerException();
		byte[] aux = BAUtils.and(limCredEnGetFreeValEn, BAUtils.toBA("02"));
		
		this.getFreeValueEnabled = !BAUtils.compareBAs(aux, new byte[1]);
	}
	
	/**
	 * 
	 * @param enabled
	 */
	public void setGetFreeValueEnabled(boolean enabled){
		
		this.getFreeValueEnabled = enabled;
	}
	
	/**
	 * 
	 */
	public byte[] toBA(){
		
		byte[] lowerLimitBA = getLowerLimit().toBA();
		byte[] upperLimitBA = getUpperLimit().toBA();
		byte[] limitedCreditValueBA = getLimitedCreditValue().toBA();
		
		byte[] limitedCreditEnabledBA = BAUtils.toBA(getLimitedCreditEnabled());
		
		if(getFreeValueEnabled) limitedCreditEnabledBA = BAUtils.xor(limitedCreditEnabledBA, BAUtils.toBA("02"));
		
		return BAUtils.concatenateBAs(super.toBA(), lowerLimitBA, upperLimitBA, limitedCreditValueBA, limitedCreditEnabledBA);
		
	}
	
	/**
	 * 
	 */
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
