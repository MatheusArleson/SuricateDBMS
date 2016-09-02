package br.com.xavier.suricate.dbms.interfaces.table.data;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public abstract class IColumnEntryTest {

	//XXX TEST SUBJECT
	private IColumnEntry instance;
	
	//XXX TEST PROPERTIES
	private byte[] content;
	private Short contentSize;
	private byte[] propertiesBytes;
	
	private byte[] otherContent;
	private Short otherContentSize;
	
	//XXX CONSTRUCTOR
	public IColumnEntryTest() {}
	
	//XXX ABSTRACT METHODS
	protected abstract IColumnEntry getInstance();

	//XXX BEFORE METHODS
	@Before
	public void setup() {
		setupInstance();
		setupProperties();
		setupOtherProperties();
	}

	private void setupInstance() {
		instance = getInstance();
	}

	private void setupProperties() {
		 content = new String("content").getBytes(StandardCharsets.UTF_16BE);
		 contentSize = new Integer(content.length).shortValue();
		 
		 int size = Short.BYTES + contentSize;
		 ByteBuffer bb = ByteBuffer.allocate(size);
		 
		 bb.putShort(contentSize);
		 bb.put(content);
		 
		 propertiesBytes = bb.array();
	}

	private void setupOtherProperties() {
		otherContent = new String("otherContent").getBytes(StandardCharsets.UTF_16BE);
		otherContentSize = new Integer(otherContent.length).shortValue();
	}

	//XXX AFTER METHODS
	@After
	public void destroy() {
		instance = null;
		
		content = null;
		contentSize = null;
		propertiesBytes = null;
		
		otherContent = null;
		otherContentSize = null;
	}
	
	//XXX TEST METHODS
	//-------------
	// CONTENT SIZE
	//-------------
	@Test
	public void getContentSizeTest(){
		instance.setContent(content);
		
		assertEquals(contentSize, instance.getContentSize());
	}
	
	@Test
	public void getContentSizeReferenceMustBeAClone(){
		instance.setContent(content);
		Short contentSizeClone = instance.getContentSize();
		Short contentSizeOtherClone = instance.getContentSize();
		
		assertEquals(contentSize, contentSizeClone);
		assertNotSame(contentSize, contentSizeClone);
		assertNotSame(contentSize, contentSizeOtherClone);
		assertNotSame(contentSizeClone, contentSizeOtherClone);
	}
	
	//-------------
	// CONTENT
	//-------------
	@Test(expected = IllegalArgumentException.class)
	public void setContentMustThrowIllegalArgumentExceptionOnNullBytes(){
		instance.setContent(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void setContentMustThrowIllegalArgumentExceptionOnContentSizeLessThanMinimum(){
		byte[] content = new byte[IColumnEntry.CONTENT_MIN_LENGTH - 1];
		
		instance.setContent(content);
	}
	
	@Test
	public void setContentMustNotThrowIllegalArgumentExceptionOnContentSizeEqualThanMinimum(){
		byte[] content = new byte[IColumnEntry.CONTENT_MIN_LENGTH];
		
		instance.setContent(content);
	}
	
	@Test
	public void setContentMustNotThrowIllegalArgumentExceptionOnContentSizeGreatherThanMinimum(){
		byte[] content = new byte[IColumnEntry.CONTENT_MIN_LENGTH + 1];
		
		instance.setContent(content);
	}
	
	@Test
	public void setContentMustAlsoSetContentSize(){
		instance.setContent(content);
		
		assertEquals(contentSize, instance.getContentSize());
		assertNotEquals(otherContentSize, instance.getContentSize());
		
		instance.setContent(otherContent);
		
		assertEquals(otherContentSize, instance.getContentSize());
		assertNotEquals(contentSize, instance.getContentSize());
	}
	
	@Test
	public void getAndSetContentTest(){
		instance.setContent(content);
		
		assertArrayEquals(content, instance.getContent());
		assertThat(otherContent, not(equalTo(instance.getContent())));
		
		instance.setContent(otherContent);
		
		assertArrayEquals(otherContent, instance.getContent());
		assertNotEquals(content, instance.getContent());	
	}
	
	//-------------
	// TO BYTE ARRAY
	//-------------
	@Test(expected = IOException.class)
	public void mustThrowIOExceptionOnNullContent() throws IOException {
		instance.toByteArray();
	}
 	
	@Test
	public void toByteArrayTest() throws IOException {
		instance.setContent(content);
		
		byte[] bytes = instance.toByteArray();
		
		assertNotNull(bytes);
		assertEquals(propertiesBytes.length, bytes.length);
		assertArrayEquals(propertiesBytes, bytes);
	}
	
	//-------------
	// FROM BYTE ARRAY
	//-------------
	@Test(expected = IOException.class)
	public void fromByteArrayMustThrowNullPointerExceptionOnNullByteArray() throws IOException {
		byte[] nullBytes = null;
		
		instance.fromByteArray(nullBytes);
	}
	
	@Test
	public void fromByteArrayMustRestoreEqualProperties() throws IOException {
		instance.fromByteArray(propertiesBytes);
		
		assertEquals(contentSize, instance.getContentSize());
		assertArrayEquals(content, instance.getContent());
	}
	
	@Test
	public void fromByteArrayMustProduceEqualInstance() throws IOException{
		IColumnEntry otherInstance = getInstance();
		otherInstance.setContent(content);
		
		instance.fromByteArray(propertiesBytes);
		
		assertEquals(instance, otherInstance);
	}
	
}
