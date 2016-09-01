package br.com.xavier.suricate.dbms.impl.low;

import java.nio.ByteOrder;

import br.com.xavier.suricate.dbms.abstractions.low.AbstractThreeByteValue;
import br.com.xavier.suricate.dbms.interfaces.low.IThreeByteValue;

public class LittleEndianThreeBytesValue 
		extends AbstractThreeByteValue {

	private static final long serialVersionUID = 5549909069846860140L;
	
	public LittleEndianThreeBytesValue() {
		super(ByteOrder.LITTLE_ENDIAN);
	}
	
	public LittleEndianThreeBytesValue(Integer value){
		super(ByteOrder.LITTLE_ENDIAN, value);
	}
	
	public LittleEndianThreeBytesValue(byte[] value){
		super(ByteOrder.LITTLE_ENDIAN, value);
	}

	@Override
	public IThreeByteValue clone() {
		return new LittleEndianThreeBytesValue(getValueBinary());
	}
	
}
