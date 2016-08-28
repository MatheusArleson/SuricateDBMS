package br.com.xavier.suricate.dbms.interfaces.low;

import java.io.IOException;
import java.nio.ByteOrder;

public interface IThreeByteValue 
		extends IBinarizable {
	
	static final Integer MAX_VALUE = 8388607;
	
	ByteOrder getByteEndianness();
	Integer getValue();
	void setValue(Integer value);
	byte[] getValueBinary();
	void setValueBinary(byte[] value);
	
	@Override
	default byte[] toByteArray() {
		return getValueBinary();
	}
	
	@Override
	default void fromByteArray(byte[] bytes) throws IOException {
		setValueBinary(bytes);
	}
	
}
