package br.com.xavier.suricate.dbms.interfaces.table.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public abstract class IColumnEntryTest {

	//XXX TEST SUBJECT
	private IColumnEntry instance;
	
	//XXX TEST PROPERTIES
	
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
	}

	//XXX AFTER METHODS
	@After
	public void destroy() {
		instance = null;
	}
	
	//XXX TEST METHODS
	@Test
	public void getContentSizeTestMustReturnNullIfContentIsNull(){
		instance.setContent(null);
		
		Short contentSize = instance.getContentSize();
		
		assertNull(contentSize);
	}
	
	@Test
	public void getAndSetContentTest(){
		byte[] content = new String("c").getBytes();
		byte[] otherContent = new String("o").getBytes();
		
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
		//TODO FIXME TERMINAR METHOD
		instance.setContent(null);
	}
	
	@Test
	public void setContentMustSetContentSizeToNullOnNonNullBytes(){
		byte[] content = new String("c").getBytes();
		Integer length = content.length;
		Short contentSize = length.shortValue();
		
		instance.setContent(content);
		
		assertEquals(contentSize, instance.getContentSize());
		
		byte[] otherContent = new String("ccc").getBytes();
		Short otherContentSize = new Short(String.valueOf(otherContent.length));
		
		instance.setContent(otherContent);
		
		assertEquals(otherContentSize, instance.getContentSize());
		assertNotEquals(contentSize, instance.getContentSize());
	}
	
}
