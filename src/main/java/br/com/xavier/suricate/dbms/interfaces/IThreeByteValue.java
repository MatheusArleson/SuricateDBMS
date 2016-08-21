package br.com.xavier.suricate.dbms.interfaces;

public interface IThreeByteValue {
	
	static final Integer MAX_VALUE = 16777215;
	
	Integer getValue();
	void setValue(Integer value);
	byte[] getValueBinary();
	void setValueBinary(byte[] value);

}
