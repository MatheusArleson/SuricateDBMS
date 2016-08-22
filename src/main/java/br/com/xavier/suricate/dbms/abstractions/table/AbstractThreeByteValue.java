package br.com.xavier.suricate.dbms.abstractions.table;

import br.com.xavier.suricate.dbms.interfaces.IThreeByteValue;

public class AbstractThreeByteValue
		implements IThreeByteValue {
	
	private static final long serialVersionUID = -3824032907485983367L;
	
	//XXX PROPERTIES
	private byte[] value;
	
	//XXX CONSTRUCTOR
	public AbstractThreeByteValue() {}
	
	public AbstractThreeByteValue(Integer value) {
		setValue(value);
	}
	
	//XXX OVERRIDE METHODS
	@Override
	public Integer getValue() {
		if(value == null){
			return null;
		}
		
		int number = (value[0] & 0xFF) | ((value[1] & 0xFF) << 8) | ((value[2] & 0x0F) << 16);
		return number;
	}

	@Override
	public void setValue(Integer value) {
		if(value == null){
			this.value = null;
			return;
		}
		
		if(value > MAX_VALUE){
			throw new IllegalArgumentException("Number is greather than the maximun value of " + MAX_VALUE);
		}
		
		this.value = new byte[3];
		
		this.value[0] = (byte) (value & 0xFF);
		this.value[1] = (byte) ((value >> 8) & 0xFF);
		this.value[2] = (byte) ((value >> 16) & 0xFF);
		
	}

	@Override
	public byte[] getValueBinary() {
		return value;
	}

	@Override
	public void setValueBinary(byte[] value) {
		this.value = value;
	}

	@Override
	public byte[] toByteArray() {
		return getValueBinary();
	}

}
