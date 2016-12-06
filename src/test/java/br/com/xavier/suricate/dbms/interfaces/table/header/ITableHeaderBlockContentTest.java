package br.com.xavier.suricate.dbms.interfaces.table.header;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.xavier.suricate.dbms.enums.TableStatus;
import br.com.xavier.suricate.dbms.impl.low.BigEndianThreeBytesValue;
import br.com.xavier.suricate.dbms.interfaces.low.IThreeByteValue;

public abstract class ITableHeaderBlockContentTest {
	
	//XXX TEST SUBJECT
	private ITableHeaderBlockContent instance;
	
	//XXX TEST PROPERTIES
	private Byte tableId;
	private IThreeByteValue blockSize;
	private Short headerSize;
	private Integer nextFreeBlockId;
	private TableStatus tableStatus;
	private byte[] propertiesBytes;
	
	private Byte otherTableId;
	private IThreeByteValue otherBlockSize;
	private Short otherHeaderSize;
	private Integer otherNextFreeBlockId;
	private TableStatus otherTableStatus;
	private byte[] otherPropertiesBytes;
	
	//XXX CONSTRUCTOR
	public ITableHeaderBlockContentTest() {	}
	
	//XXX ABSTRACT METHODS
	protected abstract ITableHeaderBlockContent getInstance();
	
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
		this.tableId = new Byte("1");
		this.blockSize = new BigEndianThreeBytesValue(ITableHeaderBlockContent.MINUMUN_BLOCK_SIZE);
		this.headerSize = new Short("2");
		this.nextFreeBlockId = new Integer("3");
		this.tableStatus = TableStatus.VALID;
		
		this.propertiesBytes = new byte[ITableHeaderBlockContent.BYTES_SIZE];
		
		propertiesBytes[0] = tableId;
		propertiesBytes[1] = blockSize.getValueBinary()[0];
		propertiesBytes[2] = blockSize.getValueBinary()[1];
		propertiesBytes[3] = blockSize.getValueBinary()[2];
		propertiesBytes[4] = tableStatus.getValue();
		
		byte[] nextFreeBlockIdBytes = ByteBuffer.allocate(4).putInt(nextFreeBlockId).array();
		propertiesBytes[5] = nextFreeBlockIdBytes[0];
		propertiesBytes[6] = nextFreeBlockIdBytes[1];
		propertiesBytes[7] = nextFreeBlockIdBytes[2];
		propertiesBytes[8] = nextFreeBlockIdBytes[3];
		
		byte[] headerSizeBytes = ByteBuffer.allocate(2).putShort(headerSize).array();
		propertiesBytes[9] = headerSizeBytes[0]; 
		propertiesBytes[10] = headerSizeBytes[1];		
	}
	
	private void setupOtherProperties() {
		this.otherTableId = new Byte(Byte.MAX_VALUE);
		this.otherBlockSize = new BigEndianThreeBytesValue(ITableHeaderBlockContent.MINUMUN_BLOCK_SIZE + 1);
		this.otherHeaderSize = new Short(Short.MAX_VALUE);
		this.otherNextFreeBlockId = new Integer(Integer.MAX_VALUE);
		this.otherTableStatus = TableStatus.INVALID;
		
		this.otherPropertiesBytes = new byte[ITableHeaderBlockContent.BYTES_SIZE];
		
		otherPropertiesBytes[0] = otherTableId;
		otherPropertiesBytes[1] = otherBlockSize.getValueBinary()[0];
		otherPropertiesBytes[2] = otherBlockSize.getValueBinary()[1];
		otherPropertiesBytes[3] = otherBlockSize.getValueBinary()[2];
		otherPropertiesBytes[4] = otherTableStatus.getValue();
		
		byte[] nextFreeBlockIdBytes = ByteBuffer.allocate(4).putInt(otherNextFreeBlockId).array();
		otherPropertiesBytes[5] = nextFreeBlockIdBytes[0];
		otherPropertiesBytes[6] = nextFreeBlockIdBytes[1];
		otherPropertiesBytes[7] = nextFreeBlockIdBytes[2];
		otherPropertiesBytes[8] = nextFreeBlockIdBytes[3];
		
		byte[] headerSizeBytes = ByteBuffer.allocate(2).putShort(otherHeaderSize).array();
		otherPropertiesBytes[9] = headerSizeBytes[0]; 
		otherPropertiesBytes[10] = headerSizeBytes[1];	
	}
	
	//XXX AFTER METHODS
	@After
	public void destroy() {
		instance = null;
		tableId = null;
		blockSize = null;
		headerSize = null;
		nextFreeBlockId = null;
		tableStatus = null;
		propertiesBytes = null;
		
		otherTableId = null;
		otherBlockSize = null;
		otherHeaderSize = null;
		otherNextFreeBlockId = null;
		otherTableStatus = null;
		otherPropertiesBytes = null;
	}
	
	//XXX TEST METHODS
	
	//-------------
	// TABLE ID
	//-------------
	@Test(expected = IllegalArgumentException.class)
	public void setTableIdMustThrowIllegalArgumentExceptionOnNullTableId(){
		instance.setTableId(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void setTableIdMustThrowIllegalArgumentExceptionOnNegativeTableId(){
		instance.setTableId(Byte.MIN_VALUE);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void setTableIdMustThrowIllegalArgumentExceptionOnZeroTableId(){
		instance.setTableId(new Byte("0"));
	}
	
	@Test
	public void setTableIdMustNotThrowIllegalArgumentExceptionValidTableId(){
		instance.setTableId(tableId);
	}
	
	@Test
	public void getTableIdReferenceMustBeAClone(){
		instance.setTableId(tableId);
		
		assertNotSame(tableId, instance.getTableId());
		assertEquals(tableId, instance.getTableId());
	}
	
	@Test
	public void getAndSetTableIdTest(){
		instance.setTableId(tableId);
		
		assertNotSame(tableId, instance.getTableId());
		assertEquals(tableId, instance.getTableId());
		assertNotEquals(otherTableId, instance.getTableId());
		
		instance.setTableId(otherTableId);
		
		assertNotSame(otherTableId, instance.getTableId());
		assertEquals(otherTableId, instance.getTableId());
		assertNotEquals(tableId, instance.getTableId());
	}
	
	//-------------
	// BLOCK SIZE
	//-------------
	@Test
	public void getBlockSizeReferenceMustBeAClone(){
		instance.setBlockSize(blockSize);
		
		assertNotSame(blockSize, instance.getBlockSize());
		assertEquals(blockSize, instance.getBlockSize());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void setBlockSizeMustThrowIllegalArgumentExceptionOnNullBlockSize(){
		instance.setBlockSize(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void setBlockSizeMustThrowIllegalArgumentExceptionOnNegativeBlockSize(){
		instance.setBlockSize(new BigEndianThreeBytesValue(Integer.MIN_VALUE));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void setBlockSizeMustThrowIllegalArgumentExceptionOnZeroBlockSize(){
		instance.setBlockSize(new BigEndianThreeBytesValue(0));
	}

	@Test(expected = IllegalArgumentException.class)
	public void setBlockMustThrowIllegalArgumentExceptionOnBlockSizeEqualsThanMinimunBlockSize(){
		IThreeByteValue blockSize = new BigEndianThreeBytesValue(ITableHeaderBlockContent.MINUMUN_BLOCK_SIZE - 1);
		
		instance.setBlockSize(blockSize);
	}
	
	@Test
	public void setBlockMustAcceptBlockSizeEqualsThanMinimunBlockSize(){
		IThreeByteValue blockSize = new BigEndianThreeBytesValue(ITableHeaderBlockContent.MINUMUN_BLOCK_SIZE);
		
		instance.setBlockSize(blockSize);
		
		assertEquals(blockSize, instance.getBlockSize());
	}
	
	@Test
	public void setBlockMustAcceptBlockSizeGreatherThanMinimunBlockSize(){
		IThreeByteValue blockSize = new BigEndianThreeBytesValue(ITableHeaderBlockContent.MINUMUN_BLOCK_SIZE + 1);
		
		instance.setBlockSize(blockSize);
		
		assertEquals(blockSize, instance.getBlockSize());
	}
	
	@Test
	public void getAndSetBlockSizeTest(){
		instance.setBlockSize(blockSize);
		
		assertEquals(blockSize, instance.getBlockSize());
		assertNotEquals(otherBlockSize, instance.getTableId());
		
		instance.setBlockSize(otherBlockSize);
		
		assertEquals(otherBlockSize, instance.getBlockSize());
		assertNotEquals(blockSize, instance.getBlockSize());
	}
	
	//-------------
	// HEADER SIZE
	//-------------
	@Test
	public void getHeaderSizeReferenceMustBeAClone(){
		instance.setHeaderSize(headerSize);
		
		assertNotSame(headerSize, instance.getHeaderSize());
		assertEquals(headerSize, instance.getHeaderSize());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void setHeaderSizeMustThrowIllegalArgumentExceptionOnNullHeaderSize(){
		instance.setHeaderSize(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void setHeaderSizeMustThrowIllegalArgumentExceptionOnNegativeHeaderSize(){
		instance.setHeaderSize(Short.MIN_VALUE);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void setHeaderSizeMustThrowIllegalArgumentExceptionOnZeroHeaderSize(){
		instance.setHeaderSize(new Short("0"));
	}
	
	@Test
	public void setHeaderSizeMustNotThrowIllegalArgumentExceptionOnValidHeaderSize(){
		instance.setHeaderSize(headerSize);
	}
	
	@Test
	public void getAndSetHeaderSizeTest(){
		instance.setHeaderSize(headerSize);
		
		assertEquals(headerSize, instance.getHeaderSize());
		assertNotEquals(otherHeaderSize, instance.getHeaderSize());
		
		instance.setHeaderSize(otherHeaderSize);
		
		assertEquals(otherHeaderSize, instance.getHeaderSize());
		assertNotEquals(headerSize, instance.getHeaderSize());
	}
	
	//-------------
	// NEXT FREE BLOCK
	//-------------
	@Test
	public void getNextFreeBlockReferenceMustBeAClone(){
		instance.setNextFreeBlockId(nextFreeBlockId);
		
		assertNotSame(nextFreeBlockId, instance.getNextFreeBlockId());
		assertEquals(nextFreeBlockId, instance.getNextFreeBlockId());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void setNextFreeBlockIdMustThrowIllegalArgumentExceptionOnNullNextFreeBlockId(){
		instance.setNextFreeBlockId(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void setNextFreeBlockIdMustThrowIllegalArgumentExceptionOnNegativeNextFreeBlockId(){
		instance.setNextFreeBlockId(-1);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void setNextFreeBlockIdMustThrowIllegalArgumentExceptionOnZeroNextFreeBlockId(){
		instance.setNextFreeBlockId(0);
	}
	
	@Test
	public void setNextFreeBlockIdMustNotThrowIllegalArgumentExceptionOnValidNextFreeBlockId(){
		instance.setNextFreeBlockId(nextFreeBlockId);
	}
	
	@Test
	public void getAndSetNextFreeBlockTest(){
		instance.setNextFreeBlockId(nextFreeBlockId);
		
		assertEquals(nextFreeBlockId, instance.getNextFreeBlockId());
		assertNotEquals(otherNextFreeBlockId, instance.getNextFreeBlockId());
		
		instance.setNextFreeBlockId(otherNextFreeBlockId);
		
		assertEquals(otherNextFreeBlockId, instance.getNextFreeBlockId());
		assertNotEquals(nextFreeBlockId, instance.getNextFreeBlockId());
	}

	//-------------
	// STATUS
	//-------------
	@Test(expected = IllegalArgumentException.class)
	public void setStatusMustThrowIllegalArgumentExceptionOnNullStatus(){
		instance.setStatus(null);
	}
	
	@Test
	public void setStatusMustNotThrowIllegalArgumentExceptionOnValidStatus(){
		instance.setStatus(tableStatus);
	}
	
	@Test
	public void getAndSetStatusTest(){
		instance.setStatus(tableStatus);
		
		assertSame(tableStatus, instance.getStatus());
		assertNotSame(otherTableStatus, instance.getStatus());
		
		instance.setStatus(otherTableStatus);
		
		assertSame(otherTableStatus, instance.getStatus());
		assertNotSame(tableStatus, instance.getStatus());
	}
	
	//-------------
	// TO BYTE ARRAY
	//-------------	
	@Test
	public void toByteArrayTest() throws IOException {
		instance.setTableId(tableId);
		instance.setBlockSize(blockSize);
		instance.setHeaderSize(headerSize);
		instance.setNextFreeBlockId(nextFreeBlockId);
		instance.setStatus(tableStatus);
		
		byte[] bytes = instance.toByteArray();
		
		assertNotNull(bytes);
		assertEquals(ITableHeaderBlockContent.BYTES_SIZE, bytes.length);
		assertArrayEquals(propertiesBytes, bytes);
	}
	
	@Test(expected = IOException.class)
	public void toByteArrayMustThrowIOExceptionOnNullTableId() throws IOException {
		//instance.setTableId(null);
		instance.setBlockSize(blockSize);
		instance.setHeaderSize(headerSize);
		instance.setNextFreeBlockId(nextFreeBlockId);
		instance.setStatus(tableStatus);
		
		instance.toByteArray();
	}
	
	@Test(expected = IOException.class)
	public void toByteArrayMustThrowIOExceptionOnNullBlockSize() throws IOException {
		instance.setTableId(tableId);
		//instance.setBlockSize(null);
		instance.setHeaderSize(headerSize);
		instance.setNextFreeBlockId(nextFreeBlockId);
		instance.setStatus(tableStatus);
		
		instance.toByteArray();
	}
	
	@Test(expected = IOException.class)
	public void toByteArrayMustThrowIOExceptionOnNullHeaderSize() throws IOException {
		instance.setTableId(tableId);
		instance.setBlockSize(blockSize);
		//instance.setHeaderSize(null);
		instance.setNextFreeBlockId(nextFreeBlockId);
		instance.setStatus(tableStatus);
		
		instance.toByteArray();
	}
	
	@Test(expected = IOException.class)
	public void toByteArrayMustThrowIOExceptionOnNullNextFreeBlockId() throws IOException {
		instance.setTableId(tableId);
		instance.setBlockSize(blockSize);
		instance.setHeaderSize(headerSize);
		//instance.setNextFreeBlockId(null);
		instance.setStatus(tableStatus);
		
		instance.toByteArray();
	}
	
	@Test(expected = IOException.class)
	public void toByteArrayMustThrowIOExceptionOnNullTableStatus() throws IOException {
		instance.setTableId(tableId);
		instance.setBlockSize(blockSize);
		instance.setHeaderSize(headerSize);
		instance.setNextFreeBlockId(nextFreeBlockId);
		//instance.setStatus(null);
		
		instance.toByteArray();
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
		byte[] bytes = new byte[ITableHeaderBlockContent.BYTES_SIZE - 1];
		
		instance.fromByteArray(bytes);
	}
	
	@Test(expected = IOException.class)
	public void fromByteArrayMustThrowIOExceptionOnByteArrayWithGreatherThanBytesSize() throws IOException {
		byte[] bytes = new byte[ITableHeaderBlockContent.BYTES_SIZE + 1];
		
		instance.fromByteArray(bytes);
	}
	
	@Test
	public void fromByteArrayMustNotThrowIOExceptionOnByteArrayWithExactBytesSize() throws IOException {
		instance.fromByteArray(propertiesBytes);
	}
	
	@Test
	public void fromByteArrayMustRestoreEqualProperties() throws IOException {
		instance.fromByteArray(propertiesBytes);
		
		assertEquals(tableId, instance.getTableId());
		assertEquals(blockSize, instance.getBlockSize());
		assertEquals(headerSize, instance.getHeaderSize());
		assertEquals(nextFreeBlockId, instance.getNextFreeBlockId());
		assertEquals(tableStatus, instance.getStatus());
	}
	
	@Test
	public void fromByteArrayMustProduceEqualInstance() throws IOException{
		ITableHeaderBlockContent otherInstance = getInstance();
		otherInstance.setTableId(tableId);
		otherInstance.setBlockSize(blockSize);
		otherInstance.setHeaderSize(headerSize);
		otherInstance.setNextFreeBlockId(nextFreeBlockId);
		otherInstance.setStatus(tableStatus);
		
		instance.fromByteArray(propertiesBytes);
		
		assertEquals(instance, otherInstance);
	}
	
}
