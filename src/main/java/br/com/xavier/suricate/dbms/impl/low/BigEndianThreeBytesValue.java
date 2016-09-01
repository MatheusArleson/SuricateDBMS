package br.com.xavier.suricate.dbms.impl.low;

import java.nio.ByteBuffer;
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

	public static void main(String[] args) {
//		ByteBuffer bb1 = ByteBuffer.allocate(4);
//		bb1.order(ByteOrder.BIG_ENDIAN);
//		bb1.putInt(-1);
//		bb1.rewind();
//		
//		Integer int1 = bb1.getInt();
//		
//	    System.out.println(bb1.order());
//		System.out.println(int1);
//	    System.out.println(int1 == -1);
//	    
//	    
//	    ByteBuffer bb2 = ByteBuffer.allocate(4);
//		bb2.order(ByteOrder.LITTLE_ENDIAN);
//		
//		bb2.putInt(-1);
//		
//		bb2.rewind();
//		
//		Integer int2 = bb2.getInt();
//		
//	    System.out.println(bb2.order());
//	    System.out.println(int2);
//		System.out.println(int2 == -1);
		
		IThreeByteValue tbv3 = new BigEndianThreeBytesValue(-1);
		Integer value = tbv3.getValue();
		System.out.println(value);
		
		
//		BigEndianThreeBytesValue tbv = new BigEndianThreeBytesValue();
//		tbv.setValue(-1);
//		
//		Integer value = tbv.getValue();
//		byte[] valueBinary = tbv.getValueBinary();
//		
//		System.out.println(value);
//		System.out.println(Arrays.toString(valueBinary));
	}

	@Override
	public IThreeByteValue clone() {
		return new BigEndianThreeBytesValue(getValueBinary());
	}

}
