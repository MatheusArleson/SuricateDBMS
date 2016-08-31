package br.com.xavier.suricate.dbms.interfaces.table.header;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.xavier.suricate.dbms.enums.ColumnsTypes;

public abstract class IColumnDescriptorTest {
	
	//XXX TEST SUBJECT
	private IColumnDescriptor instance;
	
	//XXX TEST PROPERTIES
	private String columnName;
	private Short size;
	
	//XXX CONSTRUCTOR
	public IColumnDescriptorTest() {}
	
	//XXX ABSTRACT METHODS
	protected abstract IColumnDescriptor getInstance();

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
		columnName = new String("c");
		size = new Short("1");
	}

	//XXX AFTER METHODS
	@After
	public void destroy() {
		instance = null;
		columnName = null;
		size = null;
	}
	
	//XXX TEST METHODS
	@Test
	public void getAndSetNameTest(){
		String otherColumnName = new String("other");
		
		instance.setName(columnName);
		
		assertSame(columnName, instance.getName());
		assertNotSame(otherColumnName, instance.getName());
		
		instance.setName(otherColumnName);
		
		assertSame(otherColumnName, instance.getName());
		assertNotSame(columnName, instance.getName());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void setNameMustThrowIllegalArgumentExceptionOnNameLenghtGreatherThanMaximumSize(){
		String columnName = new String("012345678901234567890123456789_");
		assertEquals(IColumnDescriptor.MAX_COLUMN_NAME_LENGTH + 1, columnName.length());
		
		instance.setName(columnName);
	}
	
	@Test
	public void setNameMustNotThrowIllegalArgumentExceptionOnNameLenghtEqualThanMaximumSize(){
		String columnName = new String("012345678901234567890123456789");
		assertEquals(IColumnDescriptor.MAX_COLUMN_NAME_LENGTH, columnName.length());
		
		instance.setName(columnName);
		
		assertSame(columnName, instance.getName());
	}
	
	@Test
	public void setNameMustNotThrowIllegalArgumentExceptionOnNameLenghtLessThanMaximumSize(){
		String columnName = new String("01234567890123456789012345678");
		assertEquals(IColumnDescriptor.MAX_COLUMN_NAME_LENGTH - 1, columnName.length());
		
		instance.setName(columnName);
		
		assertSame(columnName, instance.getName());
	}
	
	@Test
	public void getAndSetTypeTest(){
		ColumnsTypes otherType = ColumnsTypes.STRING;
		
		instance.setType(ColumnsTypes.INTEGER);
		
		assertSame(ColumnsTypes.INTEGER, instance.getType());
		assertNotSame(otherType, instance.getType());
		
		instance.setType(otherType);
		
		assertSame(otherType, instance.getType());
		assertNotSame(ColumnsTypes.INTEGER, instance.getType());
		
	}
	
	@Test
	public void setTypeForIntegerMustAlsoSetSizeToFourBytes(){
		instance.setType(ColumnsTypes.INTEGER);
		
		assertNotNull(instance.getSize());
		assertEquals(new Short("4"), instance.getSize());
	}
	
	@Test
	public void setTypeForIntegerAfterStringTypeMustAlsoSetSizeToFourBytes(){
		instance.setType(ColumnsTypes.INTEGER);
		
		assertNotNull(instance.getSize());
		assertEquals(new Short("4"), instance.getSize());
		
		instance.setType(ColumnsTypes.STRING);
		
		assertNull(instance.getSize());
		
		instance.setType(ColumnsTypes.INTEGER);
		
		assertNotNull(instance.getSize());
		assertEquals(new Short("4"), instance.getSize());
	}
	
	@Test
	public void setTypeForStringMustSetSizeToNull(){
		instance.setType(ColumnsTypes.STRING);
		
		assertNull(instance.getSize());
	}
	
	@Test
	public void setTypeForStringAfterIntegerTypeMustAlsoSetSizeToNull(){
		instance.setType(ColumnsTypes.STRING);
		
		assertNull(instance.getSize());		
		
		instance.setType(ColumnsTypes.INTEGER);
		assertNotNull(instance.getSize());
		assertEquals(new Short("4"), instance.getSize());
		
		instance.setType(ColumnsTypes.STRING);
		assertNull(instance.getSize());
	}
	
	@Test
	public void getAndSetSizeTest(){
		Short otherSize = new Short("2");
		
		instance.setSize(size);
		
		assertSame(size, instance.getSize());
		assertNotSame(otherSize, instance.getSize());
		
		instance.setSize(otherSize);
		
		assertSame(otherSize, instance.getSize());
		assertNotSame(size, instance.getSize());
	}
	
	@Test
	public void setSizeToIntegerTypeMustNotChangeIt(){
		instance.setType(ColumnsTypes.INTEGER);
		instance.setSize(size);
		
		assertNotNull(instance.getSize());
		assertEquals(new Short("4"), instance.getSize());
		
		Short otherSize = new Short("1");
			
		instance.setSize(otherSize);
		
		assertNotNull(instance.getSize());
		assertEquals(new Short("4"), instance.getSize());
	}
	
	@Test
	public void setSizeToStringTypeMustChangeIt(){
		instance.setType(ColumnsTypes.STRING);
		
		assertNull(instance.getSize());
		
		Short size = new Short("1");
		instance.setSize(size);
		
		assertNotNull(instance.getSize());
		assertSame(size, instance.getSize());
		
		Short otherSize = new Short("2");
			
		instance.setSize(otherSize);
		
		assertNotNull(instance.getSize());
		assertSame(otherSize, instance.getSize());
	}
	
	@Test
	public void setSizeToNullTypeMustChangeIt(){
		assertNull(instance.getType());
		
		Short size = new Short("1");
		instance.setSize(size);
		
		assertNotNull(instance.getSize());
		assertSame(size, instance.getSize());
		
		Short otherSize = new Short("2");
			
		instance.setSize(otherSize);
		
		assertNotNull(instance.getSize());
		assertSame(otherSize, instance.getSize());
	}
	
	@Test
	public void toByteArrayTest() throws IOException {
		instance.setName(columnName);
		instance.setType(ColumnsTypes.STRING);
		instance.setSize(size);
		
		byte[] bytes = instance.toByteArray();
		
		assertNotNull(bytes);
		assertEquals(IColumnDescriptor.BYTES_SIZE, bytes.length);
	}
	
	@Test(expected = IOException.class)
	public void toByteArrayMustThrowIOExceptionOnNullColumnName() throws IOException {
		instance.setName(null);
		instance.setSize(size);
		instance.setType(ColumnsTypes.STRING);
		
		instance.toByteArray();
	}
	
	@Test(expected = IOException.class)
	public void toByteArrayMustThrowIOExceptionOnNullColumnSize() throws IOException {
		instance.setName(columnName);
		instance.setSize(null);
		instance.setType(ColumnsTypes.STRING);
		
		instance.toByteArray();
	}
	
	@Test(expected = IOException.class)
	public void toByteArrayMustThrowIOExceptionOnNullColumnType() throws IOException {
		instance.setName(columnName);
		instance.setSize(size);
		instance.setType(null);
		
		instance.toByteArray();
	}
	
	@Test
	public void fromByteArrayTest() throws IOException {
		instance.setName(columnName);
		instance.setType(ColumnsTypes.STRING);
		instance.setSize(size);
		byte[] bytes = instance.toByteArray();
		
		instance = getInstance();
		instance.fromByteArray(bytes);
		
		assertEquals(columnName, instance.getName());
		assertEquals(ColumnsTypes.STRING, instance.getType());
		assertEquals(size, instance.getSize());
	}
	
	@Test(expected = NullPointerException.class)
	public void fromByteArrayMustThrowNullPointerExceptionOnNullByteArray() throws IOException {
		byte[] nullBytes = null;
		
		instance.fromByteArray(nullBytes);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void fromByteArrayMustThrowIllegalArgumentExceptionOnByteArrayWithLessThanBytesSize() throws IOException {
		byte[] bytes = new byte[IColumnDescriptor.BYTES_SIZE - 1];
		
		instance.fromByteArray(bytes);
	}
	
	@Test
	public void fromByteArrayMustNotThrowIllegalArgumentExceptionOnByteArrayWithGreatherThanBytesSize() {
		byte[] bytes = new byte[IColumnDescriptor.BYTES_SIZE + 1];
		
		try {
			instance.fromByteArray(bytes);
		} catch (IOException e) {
		}
	}
	
	@Test
	public void fromByteArrayMustNotThrowIllegalArgumentExceptionOnByteArrayWithExactBytesSize() {
		byte[] bytes = new byte[IColumnDescriptor.BYTES_SIZE];
		
		try {
			instance.fromByteArray(bytes);
		} catch (IOException e) {
		}
	}
}
