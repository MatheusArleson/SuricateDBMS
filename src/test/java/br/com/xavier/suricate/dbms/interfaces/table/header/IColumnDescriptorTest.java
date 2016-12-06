package br.com.xavier.suricate.dbms.interfaces.table.header;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.xavier.suricate.dbms.Factory;
import br.com.xavier.suricate.dbms.enums.ColumnsTypes;

public abstract class IColumnDescriptorTest {
	
	//XXX TEST SUBJECT
	private IColumnDescriptor instance;
	
	//XXX TEST PROPERTIES
	private String columnName;
	private Short columnSize;
	private ColumnsTypes columnType;
	private byte[] propertiesBytes;
	
	private String otherColumnName;
	private Short otherColumnSize;
	private ColumnsTypes otherColumnType;
	private byte[] otherPropertiesBytes;
	
	//XXX CONSTRUCTOR
	public IColumnDescriptorTest() {}
	
	//XXX ABSTRACT METHODS
	protected abstract IColumnDescriptor getInstance();

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
		columnName = new String("columnName");
		columnSize = new Short("2");
		columnType = ColumnsTypes.STRING;
		
		propertiesBytes = new byte[IColumnDescriptor.BYTES_SIZE];
		byte[] columnNameBytes = Factory.toByteArray(columnName);
		ByteBuffer bb = ByteBuffer.wrap(propertiesBytes);
		bb.put(columnNameBytes);
		bb.position(60);
		
		bb.putShort(columnType.getId());
		bb.putShort(columnSize);
	}
	
	private void setupOtherProperties() {
		otherColumnName = new String("otherColumnName");
		otherColumnSize = new Short("4");
		otherColumnType = ColumnsTypes.INTEGER;
		
		otherPropertiesBytes = new byte[IColumnDescriptor.BYTES_SIZE];
		byte[] columnNameBytes = Factory.toByteArray(otherColumnName);
		ByteBuffer bb = ByteBuffer.wrap(otherPropertiesBytes);
		bb.put(columnNameBytes);
		bb.position(60);
		
		bb.putShort(otherColumnType.getId());
		bb.putShort(otherColumnSize);
	}

	//XXX AFTER METHODS
	@After
	public void destroy() {
		instance = null;
		
		columnName = null;
		columnSize = null;
		columnType = null;
		propertiesBytes = null;
		
		otherColumnName = null;
		otherColumnSize = null;
		otherColumnType = null;
		otherPropertiesBytes = null;
	}
	
	//XXX TEST METHODS
	
	//-------------
	// NAME
	//-------------
	@Test(expected = IllegalArgumentException.class)
	public void setNameMustThrowIllegalArgumentExceptionOnNullName(){
		instance.setName(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void setNameMustThrowIllegalArgumentExceptionOnEmptyName(){
		instance.setName("");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void setNameMustThrowIllegalArgumentExceptionOnEmptySpacesName(){
		instance.setName("  ");
	}
	
	@Test
	public void setNameMustNotThrowIllegalArgumentExceptionOnValidName(){
		instance.setName("c");
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
		
		assertEquals(columnName, instance.getName());
	}
	
	@Test
	public void setNameMustNotThrowIllegalArgumentExceptionOnNameLenghtLessThanMaximumSize(){
		String columnName = new String("01234567890123456789012345678");
		assertEquals(IColumnDescriptor.MAX_COLUMN_NAME_LENGTH - 1, columnName.length());
		
		instance.setName(columnName);
		
		assertEquals(columnName, instance.getName());
	}
	
	@Test
	public void getNameReferenceMustBeAClone(){
		instance.setName(columnName);
		
		assertNotSame(columnName, instance.getName());
		assertEquals(columnName, instance.getName());
	}
	
	@Test
	public void getAndSetNameTest(){
		instance.setName(columnName);
		
		assertEquals(columnName, instance.getName());
		assertNotEquals(otherColumnName, instance.getName());
		
		instance.setName(otherColumnName);
		
		assertEquals(otherColumnName, instance.getName());
		assertNotEquals(columnName, instance.getName());
	}

	//-------------
	// TYPE
	//-------------
	@Test(expected = IllegalArgumentException.class)
	public void setTypeMustThrowIllegalArgumentExceptionOnNullType(){
		instance.setType(null);
	}
	
	@Test
	public void setTypeMustNotThrowIllegalArgumentExceptionOnValidType(){
		instance.setType(ColumnsTypes.INTEGER);
	}
	
	@Test
	public void setTypeForIntegerMustAlsoSetSizeToFourBytes(){
		instance.setType(ColumnsTypes.INTEGER);
		
		assertSame(ColumnsTypes.INTEGER, instance.getType());
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
	public void getAndSetTypeTest(){
		instance.setType(columnType);
		
		assertSame(columnType, instance.getType());
		assertNotSame(otherColumnType, instance.getType());
		
		instance.setType(otherColumnType);
		
		assertSame(otherColumnType, instance.getType());
		assertNotSame(columnType, instance.getType());
	}
	
	//-------------
	// SIZE
	//-------------
	@Test(expected = IllegalArgumentException.class)
	public void setSizeMustThrowIllegalArgumentExceptionOnNullSize(){
		instance.setSize(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void setSizeMustThrowIllegalArgumentExceptionOnNegativeSize(){
		instance.setSize(Short.MIN_VALUE);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void setSizeMustThrowIllegalArgumentExceptionOnZeroSize(){
		instance.setSize(new Short("0"));
	}
	
	@Test
	public void setSizeMustNotThrowIllegalArgumentExceptionOnValidSize(){
		instance.setSize(columnSize);
	}
	
	@Test
	public void setSizeOnIntegerTypeMustNotChangeIt(){
		instance.setType(ColumnsTypes.INTEGER);
		instance.setSize(columnSize);
		
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
		assertEquals(size, instance.getSize());
		
		Short otherSize = new Short("2");
			
		instance.setSize(otherSize);
		
		assertNotNull(instance.getSize());
		assertEquals(otherSize, instance.getSize());
	}
		
	@Test
	public void getAndSetSizeTest(){
		instance.setSize(columnSize);
		
		assertEquals(columnSize, instance.getSize());
		assertNotEquals(otherColumnSize, instance.getSize());
		
		instance.setSize(otherColumnSize);
		
		assertEquals(otherColumnSize, instance.getSize());
		assertNotEquals(columnSize, instance.getSize());
	}
	
	//-------------
	// TO BYTE ARRAY
	//-------------
	@Test(expected = IOException.class)
	public void toByteArrayMustThrowIOExceptionOnNullColumnName() throws IOException {
		//instance.setName(null);
		instance.setType(columnType);
		instance.setSize(columnSize);
		
		instance.toByteArray();
	}
	
	@Test(expected = IOException.class)
	public void toByteArrayMustThrowIOExceptionOnNullColumnType() throws IOException {
		instance.setName(columnName);
		//instance.setType(null);
		instance.setSize(columnSize);
		
		instance.toByteArray();
	}
	
	@Test(expected = IOException.class)
	public void toByteArrayMustThrowIOExceptionOnNullColumnSize() throws IOException {
		instance.setName(columnName);
		instance.setType(columnType);
		//instance.setSize(null);
		
		instance.toByteArray();
	}
	
	@Test
	public void toByteArrayTest() throws IOException {
		instance.setName(columnName);
		instance.setType(columnType);
		instance.setSize(columnSize);
		
		byte[] bytes = instance.toByteArray();
		
		assertNotNull(bytes);
		assertEquals(IColumnDescriptor.BYTES_SIZE, bytes.length);
		assertArrayEquals(propertiesBytes, bytes);
	}
	
	//-------------
	// FROM BYTE ARRAY
	//-------------
	@Test(expected = IOException.class)
	public void fromByteArrayMustThrowIOExceptionOnNullByteArray() throws IOException {
		byte[] nullBytes = null;
		
		instance.fromByteArray(nullBytes);
	}
	
	@Test(expected = IOException.class)
	public void fromByteArrayMustThrowIOExceptionOnByteArrayWithLessThanBytesSize() throws IOException {
		byte[] bytes = new byte[IColumnDescriptor.BYTES_SIZE - 1];
		
		instance.fromByteArray(bytes);
	}
	
	@Test(expected = IOException.class)
	public void fromByteArrayMustThrowIOExceptionOnByteArrayWithGreatherThanBytesSize() throws IOException {
		byte[] bytes = new byte[IColumnDescriptor.BYTES_SIZE + 1];
		
		instance.fromByteArray(bytes);
	}
	
	@Test
	public void fromByteArrayMustNotThrowIOExceptionOnByteArrayWithExactBytesSize() throws IOException {
		instance.fromByteArray(propertiesBytes);
	}

	@Test
	public void fromByteArrayMustRestoreEqualProperties() throws IOException {
		instance.fromByteArray(propertiesBytes);
		
		assertEquals(columnName, instance.getName());
		assertEquals(columnSize, instance.getSize());
		assertEquals(columnType, instance.getType());
	}
	
	@Test
	public void fromByteArrayMustProduceEqualInstance() throws IOException{
		IColumnDescriptor otherInstance = getInstance();
		otherInstance.setName(columnName);
		otherInstance.setType(columnType);
		otherInstance.setSize(columnSize);
		
		instance.fromByteArray(propertiesBytes);
		
		assertEquals(instance, otherInstance);
	}
	
}
