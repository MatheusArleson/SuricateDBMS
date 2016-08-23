package br.com.xavier.suricate.dbms.interfaces;

import static org.junit.Assert.*;
import static org.hamcrest.core.Is.*;
import static org.hamcrest.core.IsNot.*;
import static org.hamcrest.core.IsEqual.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public abstract class IThreeBytesValueTest {
	
	//XXX TEST SUBJECT
	protected IThreeByteValue instance;
	
	//XXX TEST PROPERTIES
	protected Integer maxValue;
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
		
		maxValue = IThreeByteValue.MAX_VALUE;
		number = 99;
		numberBinary = new byte[3];
		
		byte[] array = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(number).array();
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
		Integer value = maxValue + 1;
		
		instance.setValue(value);
	}
	
	@Test
	public void mustNotThrowIllegalArgumentExceptionIfValueIsEqualThanMaxValue(){
		instance.setValue(maxValue);
	}
	
	@Test
	public void mustNotThrowIllegalArgumentExceptionIfValueIsLessThanMaxValue(){
		Integer value = IThreeByteValue.MAX_VALUE - 1;
		
		instance.setValue(value);
	}
	
	@Test
	public void mustWorkWithMaxValue(){
		instance.setValue(maxValue);
		
		assertEquals(maxValue, instance.getValue());
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
		
		assertThat(numberBinary, is(equalTo(instance.getValueBinary())));
		
	}
	
	@Test
	public void mustNotReturnDifferentValueBinaryForValueSetted(){
		instance.setValue(number);
		numberBinary[0] = 0;
		
		assertThat(numberBinary, not(equalTo(instance.getValueBinary())));
	}
	
}
