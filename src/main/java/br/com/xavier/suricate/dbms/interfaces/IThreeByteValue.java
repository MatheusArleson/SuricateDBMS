package br.com.xavier.suricate.dbms.interfaces;

public interface IThreeByteValue 
		extends IBinarizable<IThreeByteValue> {
	
	static final Integer MAX_VALUE = 8388607;
	
	Integer getValue();
	void setValue(Integer value);
	byte[] getValueBinary();
	void setValueBinary(byte[] value);

}
