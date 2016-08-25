package br.com.xavier.suricate.dbms.interfaces.low;

import java.nio.ByteOrder;

public interface IThreeByteValue 
		extends IBinarizable {
	
	static final Integer MAX_VALUE = 8388607;
	
	ByteOrder getByteEndianness();
	Integer getValue();
	void setValue(Integer value);
	byte[] getValueBinary();
	void setValueBinary(byte[] value);

}
