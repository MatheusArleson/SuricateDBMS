package br.com.xavier.suricate.dbms.interfaces;

public interface IThreeByteValue {
	
	Integer getValue();
	void setValue(Integer value);
	byte[] getValueBinary();
	void setValueBinary(byte[] value);

}
