package br.com.xavier.suricate.dbms.interfaces.low;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public abstract class IThreeBytesValueTest {
	
	//XXX TEST SUBJECT
	private IThreeByteValue instance;
	
	//XXX TEST PROPERTIES
	private final Integer maxValue;
	private final Integer minValue;
	private Integer number;
	private byte[] numberBinarySameEndianness;
	
	//XXX CONSTRUCTOR
	public IThreeBytesValueTest() {	
		this.maxValue = IThreeByteValue.MAX_VALUE;
		this.minValue = IThreeByteValue.MIN_VALUE;
	}
	
	public IThreeBytesValueTest(Integer number) {
		this();
		this.number = number;
	}
	
	//XXX ABSTRACT METHODS
	protected abstract IThreeByteValue getInstance();
	
	//XXX BEFORE METHODS
	@Before
	public void setup() {
		instance = getInstance();
		if(number == null){
			number = 511;
		}
		
		
		numberBinarySameEndianness = generateSameEndiannessByteArray(instance, number);
	}

	private byte[] generateSameEndiannessByteArray(IThreeByteValue instance, Integer number) {
		byte[] numberBinarySameEndianness = new byte[3];
		ByteOrder byteEndianness = instance.getByteEndianness();
		
		byte[] array = ByteBuffer.allocate(4).order(byteEndianness).putInt(number).array();
		ByteBuffer buffer = ByteBuffer.wrap(array).order(byteEndianness);
		
		if(byteEndianness.equals(ByteOrder.BIG_ENDIAN)){
			buffer.get();
		}
		
		buffer.get(numberBinarySameEndianness);
		
		return numberBinarySameEndianness;
	}

	//XXX AFTER METHODS
	@After
	public void destroy() {
		instance = null;
		number = null;
		numberBinarySameEndianness = null;
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
	
	@Test(expected = IllegalArgumentException.class)
	public void mustThrowIllegalArgumentExceptionIfValueIsLessThanMinValue(){
		instance.setValue(minValue - 1);
	}
	
	@Test
	public void mustNotThrowIllegalArgumentExceptionIfValueIsEqualThanMinValue(){
		instance.setValue(minValue);
	}
	
	@Test
	public void mustNotThrowIllegalArgumentExceptionIfValueIsGreatherThanMinValue(){
		instance.setValue(minValue + 1);
	}
	
	@Test
	public void mustWorkWithMinValue(){
		instance.setValue(minValue);
		
		assertEquals(minValue, instance.getValue());
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
		
		assertThat(numberBinarySameEndianness, is(equalTo(instance.getValueBinary())));
		
	}
	
	@Test
	public void mustNotReturnDifferentValueBinaryForValueSetted(){
		byte swapValue = (byte) -1;
		instance.setValue(number);
		
		Byte inPlace = numberBinarySameEndianness[1];
		if(!inPlace.equals(swapValue)){
			numberBinarySameEndianness[1] = -1;
		} else {
			numberBinarySameEndianness[1] = 0;
		}
		
		assertThat(numberBinarySameEndianness, not(equalTo(instance.getValueBinary())));
	}
	
}
