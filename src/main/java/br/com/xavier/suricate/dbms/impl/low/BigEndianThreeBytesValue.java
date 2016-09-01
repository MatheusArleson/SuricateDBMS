package br.com.xavier.suricate.dbms.impl.low;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.BitSet;

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
		
//		IThreeByteValue tbv3 = new BigEndianThreeBytesValue(-1);
//		Integer value3 = tbv3.getValue();
//		System.out.println(value3);
		
		BitSet bs = new BitSet(24);
		bs.flip(0, 24);
		byte[] bytes = bs.toByteArray();
		
		System.out.println("#> BYTES NATURAL > " + Arrays.toString(bytes));
		
//		byte[] bytesLE = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).put(bytes).array();
//		System.out.println("#> BYTES LE > " + Arrays.toString(bytesLE));
//		
//		byte[] bytesBE = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN).put(bytes).array();
//		System.out.println("#> BYTES BE > " + Arrays.toString(bytesBE));
		
		
		IThreeByteValue tbv4 = new BigEndianThreeBytesValue(bytes);
		Integer value4 = tbv4.getValue();
		System.out.println(value4);
		
		
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
