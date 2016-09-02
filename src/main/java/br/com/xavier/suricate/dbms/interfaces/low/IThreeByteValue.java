package br.com.xavier.suricate.dbms.interfaces.low;

import java.io.IOException;
import java.nio.ByteOrder;

public interface IThreeByteValue 
		extends IBinarizable, Cloneable {
	
	static final Integer MAX_VALUE = 8388607;
	//TODO FIXME not working with negative numbers but it's not a requirement right now.
	static final Integer MIN_VALUE = 0;
	
	ByteOrder getByteEndianness();
	boolean isBigEndian();
	Integer getValue();
	void setValue(Integer value);
	byte[] getValueBinary();
	void setValueBinary(byte[] value);
	
	IThreeByteValue clone();
	
	@Override
	default byte[] toByteArray() {
		return getValueBinary();
	}
	
	@Override
	default void fromByteArray(byte[] bytes) throws IOException {
		setValueBinary(bytes);
	}
	
	
}
