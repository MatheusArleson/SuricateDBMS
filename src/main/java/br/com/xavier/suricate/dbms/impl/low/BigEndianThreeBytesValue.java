package br.com.xavier.suricate.dbms.impl.low;

import java.nio.ByteOrder;

import br.com.xavier.suricate.dbms.abstractions.low.AbstractThreeByteValue;
import br.com.xavier.suricate.dbms.interfaces.low.IThreeByteValue;

public class BigEndianThreeBytesValue 
		extends AbstractThreeByteValue {

	private static final long serialVersionUID = 5549909069846860140L;
	
	public BigEndianThreeBytesValue() {
		super(ByteOrder.BIG_ENDIAN);
	}
	
	public BigEndianThreeBytesValue(Integer value){
		super(ByteOrder.BIG_ENDIAN, value);
	}
	
	public BigEndianThreeBytesValue(byte[] value){
		super(ByteOrder.BIG_ENDIAN, value);
	}

	@Override
	public IThreeByteValue clone() {
		return new BigEndianThreeBytesValue(getValueBinary());
	}

}
