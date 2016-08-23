package br.com.xavier.suricate.dbms.interfaces;

import java.nio.ByteOrder;

public interface IThreeByteValue 
		extends IBinarizable<IThreeByteValue> {
	
	static final Integer MAX_VALUE = 8388607;
	
	ByteOrder getByteEndianness();
	Integer getValue();
	void setValue(Integer value);
	byte[] getValueBinary();
	void setValueBinary(byte[] value);

}
