package br.com.xavier.suricate.dbms.interfaces.table.data;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public abstract class IColumnEntryTest {

	//XXX TEST SUBJECT
	private IColumnEntry instance;
	
	//XXX TEST PROPERTIES
	byte[] content;
	Short contentSize;
	
	byte[] otherContent;
	Short otherContentSize;
	
	//XXX CONSTRUCTOR
	public IColumnEntryTest() {}
	
	//XXX ABSTRACT METHODS
	protected abstract IColumnEntry getInstance();

	//XXX BEFORE METHODS
	@Before
	public void setup() {
		setupInstance();
		setupProperties();
	}

	private void setupInstance() {
		instance = getInstance();
	}

	private void setupProperties() {
		 content = new String("content").getBytes(StandardCharsets.UTF_16BE);
		 contentSize = new Integer(content.length).shortValue();
		 
		 otherContent = new String("otherContent").getBytes(StandardCharsets.UTF_16BE);
		 contentSize = new Integer(content.length).shortValue();
	}

	//XXX AFTER METHODS
	@After
	public void destroy() {
		instance = null;
	}
	
	//XXX TEST METHODS
	@Test
	public void getContentSizeTestMustReturnNullIfContentIsNull(){
		Short contentSize = instance.getContentSize();
		
		assertNull(contentSize);
	}
	
	@Test
	public void getAndSetContentTest(){
		instance.setContent(content);
		
		assertSame(content, instance.getContent());
		assertNotSame(otherContent, instance.getContent());
		
		instance.setContent(otherContent);
		
		assertSame(otherContent, instance.getContent());
		assertNotSame(content, instance.getContent());	
	}
	
	@Test(expected = NullPointerException.class)
	public void setContentMustThrowNullPointerExceptionOnNullBytes(){
		
		instance.setContent(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void setContentMustThrowIllegalArgumentExceptionOnBytesLengthGreatherThanShortMaxValue(){
		byte[] content = new byte[Short.MAX_VALUE + 1];
		
		instance.setContent(content);
	}
	
	@Test
	public void setContentMustNotThrowIllegalArgumentExceptionOnBytesLengthEqualThanShortMaxValue(){
		byte[] content = new byte[Short.MAX_VALUE];
		
		instance.setContent(content);
	}
	
	@Test
	public void setContentMustNotThrowIllegalArgumentExceptionOnBytesLengthLessThanShortMaxValue(){
		byte[] content = new byte[Short.MAX_VALUE - 1];
		
		instance.setContent(content);
	}
	
	@Test
	public void toByteArrayTest() throws IOException {
		instance.setContent(content);
		
		byte[] bytes = instance.toByteArray();
		
		assertNotNull(bytes);
		assertEquals(content.length + Short.BYTES, bytes.length);
	}
	
	@Test(expected = IOException.class)
	public void mustThrowIOExceptionOnNullContent() throws IOException {
		instance.toByteArray();
	}
	
	@Test(expected = IOException.class)
	public void mustThrowIOExceptionOnNullContentSize() throws IOException {
		
		instance.toByteArray();
	}
	
	@Test
	public void fromByteArrayTest() throws IOException {
		instance.setContent(content);
		byte[] bytes = instance.toByteArray();
		
		instance.fromByteArray(bytes);
		
		assertArrayEquals(content, instance.getContent());
		assertEquals(contentSize, instance.getContentSize());
	}

	@Test(expected = NullPointerException.class)
	public void fromByteArrayMustThrowNullPointerExceptionOnNullByteArray() throws IOException {
		byte[] nullBytes = null;
		
		instance.fromByteArray(nullBytes);
	}
}
