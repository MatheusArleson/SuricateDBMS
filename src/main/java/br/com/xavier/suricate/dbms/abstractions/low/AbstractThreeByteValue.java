package br.com.xavier.suricate.dbms.abstractions.low;

import java.nio.ByteOrder;
import java.util.Arrays;

import br.com.xavier.suricate.dbms.interfaces.low.IThreeByteValue;

public abstract class AbstractThreeByteValue
		implements IThreeByteValue {
	
	private static final long serialVersionUID = -3824032907485983367L;
	
	//XXX PROPERTIES
	private static final int FULL_BYTE = 0xFF;
	
	private final ByteOrder byteEndianess;
	
	private boolean negative;
	private byte[] value;
	
	//XXX CONSTRUCTOR
	public AbstractThreeByteValue(ByteOrder byteEndianness) {
		this.byteEndianess = byteEndianness;
	}
	
	public AbstractThreeByteValue(ByteOrder byteEndianness, Integer value) {
		this(byteEndianness);
		setValue(value);
	}
	
	public AbstractThreeByteValue(ByteOrder byteEndianness, byte[] value) {
		this(byteEndianness);
		setValueBinary(value);
	}
	
	//XXX OVERRIDE METHODS
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(value);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractThreeByteValue other = (AbstractThreeByteValue) obj;
		if (!Arrays.equals(value, other.value))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "AbstractThreeByteValue [" 
			+ "byteEndianess=" + byteEndianess
			+ ", value=" + Arrays.toString(value) 
		+ "]";
	}
	
	public abstract IThreeByteValue clone();

	@Override
	public Integer getValue() {
		if(value == null){
			return null;
		}
		
		Integer number = null;
		if(byteEndianess.equals(ByteOrder.BIG_ENDIAN)){
			number = getValueBigEndian();
		} else {
			number = getValueLittleEndian();
		}
		
		if(negative){
			number = number | (FULL_BYTE << 24);
		}
		
		return number;
	}

	private Integer getValueLittleEndian() {
		return (value[0] & FULL_BYTE) | ((value[1] & FULL_BYTE) << 8) | ((value[2] & FULL_BYTE) << 16);
	}

	private Integer getValueBigEndian() {
		return (value[2] & FULL_BYTE) | ((value[1] & FULL_BYTE) << 8) | ((value[0] & FULL_BYTE) << 16);
	}

	@Override
	public void setValue(Integer value) {
		if(value == null){
			this.value = null;
			return;
		}
		
		if(value < MIN_VALUE){
			throw new IllegalArgumentException("Number is less than the minimum value of " + MIN_VALUE);
		}
		
		if(value > MAX_VALUE){
			throw new IllegalArgumentException("Number is greather than the maximum value of " + MAX_VALUE);
		}
		
		negative = value < 0;
		
		if(this.value == null){
			this.value = new byte[3];
		}
		
		if(byteEndianess.equals(ByteOrder.BIG_ENDIAN)){
			setValueBigEndian(value);
		} else {
			setValueLittleEndian(value);
		}
		
	}

	private void setValueBigEndian(Integer value) {
		this.value[2] = (byte) (value & FULL_BYTE);
		this.value[1] = (byte) ((value >> 8) & FULL_BYTE);
		this.value[0] = (byte) ((value >> 16) & FULL_BYTE);
	}
	
	private void setValueLittleEndian(Integer value) {
		this.value[0] = (byte) (value & FULL_BYTE);
		this.value[1] = (byte) ((value >> 8) & FULL_BYTE);
		this.value[2] = (byte) ((value >> 16) & FULL_BYTE);
	}
	
	@Override
	public void setValueBinary(byte[] value) {
		this.value = value;
	}

	//XXX GETTERS/SETTERS
	@Override
	public ByteOrder getByteEndianness() {
		return byteEndianess;
	}
	
	@Override
	public byte[] getValueBinary() {
		return value;
	}

}