package br.com.xavier.suricate.dbms.interfaces;

import static org.junit.Assert.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public abstract class IThreeBytesValueTest {
	
	//XXX TEST SUBJECT
	protected IThreeByteValue instance;
	
	//XXX TEST PROPERTIES
	protected Integer number;
	protected byte[] numberBinary;
	
	//XXX CONSTRUCTOR
	public IThreeBytesValueTest() {	}
	
	//XXX ABSTRACT METHODS
	protected abstract IThreeByteValue getInstance();
	
	//XXX BEFORE METHODS
	@Before
	public void setup() {
		instance = getInstance();
		number = 99;
		numberBinary = new byte[3];
		
		byte[] array = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(256).array();
		ByteBuffer.wrap(array).order(ByteOrder.LITTLE_ENDIAN).get(numberBinary);
	}

	//XXX AFTER METHODS
	@After
	public void destroy() {
		instance = null;
		number = null;
		numberBinary = null;
	}
	
	// XXX TEST METHODS
	
	// ------------------------------------------
	// INTEGER METODS
	// ------------------------------------------
	@Test
	public void mustReturnNullIfSetNull(){
		instance.setValue(null);
		
		assertNull(instance.getValue());
	}
	
	@Test
	public void mustReturnNotNullIfSetNotNull(){
		instance.setValue(number);
		
		assertNotNull(instance.getValue());
	}
	
	@Test
	public void mustReturnSameValueForValueSetted(){
		instance.setValue(number);
		
		assertEquals(number, instance.getValue());
	}
	
	@Test
	public void mustNotReturnDifferentValueForValueSetted(){
		instance.setValue(number);
		
		Integer unexpected = number + 1;
		assertNotEquals(unexpected, instance.getValue());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void mustThrowIllegalArgumentExceptionIfValueIsGreatherThanMaxValue(){
		int value = IThreeByteValue.MAX_VALUE + 1;
		instance.setValue(value);
	}
	
	@Test
	public void mustNotThrowIllegalArgumentExceptionIfValueIsEqualThanMaxValue(){
		int value = IThreeByteValue.MAX_VALUE;
		instance.setValue(value);
	}
	
	@Test
	public void mustNotThrowIllegalArgumentExceptionIfValueIsLessThanMaxValue(){
		int value = IThreeByteValue.MAX_VALUE - 1;
		instance.setValue(value);
	}
	
	// ------------------------------------------
	// BINARY METODS
	// ------------------------------------------
	@Test
	public void mustReturnNullBinaryIfSetNull(){
		instance.setValue(null);
		
		assertNull(instance.getValueBinary());
	}
	
	@Test
	public void mustReturnNotNullBinaryIfSetNotNull(){
		instance.setValue(number);
		
		assertNotNull(instance.getValueBinary());
	}
	
	@Test
	public void mustReturnSameValueBinaryForValueSetted(){
		instance.setValue(number);
		
		assertEquals(numberBinary, instance.getValue());
	}
	
	@Test
	public void mustNotReturnDifferentValueBinaryForValueSetted(){
		instance.setValue(number);
		numberBinary[0] = 0;
		
		assertNotEquals(numberBinary, instance.getValue());
	}
	
}
