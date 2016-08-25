package br.com.xavier.suricate.dbms.impl.low;

import java.nio.ByteOrder;

import br.com.xavier.suricate.dbms.abstractions.low.AbstractThreeByteValue;

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
	
	public static void main(String[] args) {
		BigEndianThreeBytesValue tbv = new BigEndianThreeBytesValue();
		tbv.setValue(511);
		System.out.println(tbv.getValue());
	}

}
