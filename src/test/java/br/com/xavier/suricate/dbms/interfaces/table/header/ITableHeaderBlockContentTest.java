package br.com.xavier.suricate.dbms.interfaces.table.header;

import static org.junit.Assert.*;

import java.io.IOException;

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
	
	//XXX CONSTRUCTOR
	public ITableHeaderBlockContentTest() {	}
	
	//XXX ABSTRACT METHODS
	protected abstract ITableHeaderBlockContent getInstance();
	
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
		this.tableId = new Byte("1");
		this.blockSize = new BigEndianThreeBytesValue(ITableHeaderBlockContent.MINUMUN_BLOCK_SIZE);
		this.headerSize = new Short("3");
		this.nextFreeBlockId = new Integer("4");
		this.tableStatus = TableStatus.VALID;
	}
	
	//XXX AFTER METHODS
	@After
	public void destroy() {
		instance = null;
	}
	
	//XXX TEST METHODS
	@Test
	public void getAndSetTableIdTest(){
		Byte otherTableId = new Byte("2");
		
		instance.setTableId(tableId);
		
		assertSame(tableId, instance.getTableId());
		assertNotSame(otherTableId, instance.getTableId());
		
		instance.setTableId(otherTableId);
		
		assertSame(otherTableId, instance.getTableId());
		assertNotSame(tableId, instance.getTableId());
	}
	
	@Test
	public void getAndSetBlockSizeTest(){
		IThreeByteValue otherBlockSize = new BigEndianThreeBytesValue(8192);
		
		instance.setBlockSize(blockSize);
		
		assertSame(blockSize, instance.getBlockSize());
		assertNotSame(otherBlockSize, instance.getTableId());
		
		instance.setBlockSize(otherBlockSize);
		
		assertSame(otherBlockSize, instance.getBlockSize());
		assertNotSame(blockSize, instance.getBlockSize());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void mustThrowIllegalArgumentExceptionOnSetBlockSizeLessThanMinimunBlockSize(){
		IThreeByteValue blockSize = new BigEndianThreeBytesValue(ITableHeaderBlockContent.MINUMUN_BLOCK_SIZE - 1);
		
		instance.setBlockSize(blockSize);
	}
	
	@Test
	public void mustSetBlockSizeEqualsThanMinimunBlockSize(){
		IThreeByteValue blockSize = new BigEndianThreeBytesValue(ITableHeaderBlockContent.MINUMUN_BLOCK_SIZE);
		
		instance.setBlockSize(blockSize);
		
		assertSame(blockSize, instance.getBlockSize());
	}
	
	@Test
	public void mustSetBlockSizeGreatherThanMinimunBlockSize(){
		IThreeByteValue blockSize = new BigEndianThreeBytesValue(ITableHeaderBlockContent.MINUMUN_BLOCK_SIZE + 1);
		
		instance.setBlockSize(blockSize);
		
		assertSame(blockSize, instance.getBlockSize());
	}
	
	@Test
	public void getAndSetHeaderSizeTest(){
		Short otherHeaderSize = new Short("2");
		
		instance.setHeaderSize(headerSize);
		
		assertSame(headerSize, instance.getHeaderSize());
		assertNotSame(otherHeaderSize, instance.getHeaderSize());
		
		instance.setHeaderSize(otherHeaderSize);
		
		assertSame(otherHeaderSize, instance.getHeaderSize());
		assertNotSame(headerSize, instance.getHeaderSize());
	}
	
	@Test
	public void getAndSetNextFreeBlockTest(){
		Integer otherNextFreeBlocke = new Integer("2");
		
		instance.setNextFreeBlockId(nextFreeBlockId);
		
		assertSame(nextFreeBlockId, instance.getNextFreeBlockId());
		assertNotSame(otherNextFreeBlocke, instance.getNextFreeBlockId());
		
		instance.setNextFreeBlockId(otherNextFreeBlocke);
		
		assertSame(otherNextFreeBlocke, instance.getNextFreeBlockId());
		assertNotSame(nextFreeBlockId, instance.getNextFreeBlockId());
	}
	
	@Test
	public void getAndSetStatusTest(){
		TableStatus otherStatus = TableStatus.INVALID;
		
		instance.setStatus(tableStatus);
		
		assertSame(tableStatus, instance.getStatus());
		assertNotSame(otherStatus, instance.getStatus());
		
		instance.setStatus(otherStatus);
		
		assertSame(otherStatus, instance.getStatus());
		assertNotSame(tableStatus, instance.getStatus());
	}
	
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
	}
	
	@Test(expected = IOException.class)
	public void mustThrowIOExceptionOnNullTableId() throws IOException {
		instance.setTableId(null);
		instance.setBlockSize(blockSize);
		instance.setHeaderSize(headerSize);
		instance.setNextFreeBlockId(nextFreeBlockId);
		instance.setStatus(tableStatus);
		
		instance.toByteArray();
	}
	
	@Test(expected = IOException.class)
	public void mustThrowIOExceptionOnNullBlockSize() throws IOException {
		instance.setTableId(tableId);
		instance.setBlockSize(null);
		instance.setHeaderSize(headerSize);
		instance.setNextFreeBlockId(nextFreeBlockId);
		instance.setStatus(tableStatus);
		
		instance.toByteArray();
	}
	
	@Test(expected = IOException.class)
	public void mustThrowIOExceptionOnNullHeaderSize() throws IOException {
		instance.setTableId(tableId);
		instance.setBlockSize(blockSize);
		instance.setHeaderSize(null);
		instance.setNextFreeBlockId(nextFreeBlockId);
		instance.setStatus(tableStatus);
		
		instance.toByteArray();
	}
	
	@Test(expected = IOException.class)
	public void mustThrowIOExceptionOnNullNextFreeBlockId() throws IOException {
		instance.setTableId(tableId);
		instance.setBlockSize(blockSize);
		instance.setHeaderSize(headerSize);
		instance.setNextFreeBlockId(null);
		instance.setStatus(tableStatus);
		
		instance.toByteArray();
	}
	
	@Test(expected = IOException.class)
	public void mustThrowIOExceptionOnNullTableStatus() throws IOException {
		instance.setTableId(tableId);
		instance.setBlockSize(blockSize);
		instance.setHeaderSize(headerSize);
		instance.setNextFreeBlockId(nextFreeBlockId);
		instance.setStatus(null);
		
		instance.toByteArray();
	}
	
	@Test
	public void fromByteArrayTest() throws IOException {
		instance.setTableId(tableId);
		instance.setBlockSize(blockSize);
		instance.setHeaderSize(headerSize);
		instance.setNextFreeBlockId(nextFreeBlockId);
		instance.setStatus(tableStatus);
		byte[] bytes = instance.toByteArray();
		
		instance.fromByteArray(bytes);
		
		assertEquals(tableId, instance.getTableId());
		assertEquals(blockSize, instance.getBlockSize());
		assertEquals(headerSize, instance.getHeaderSize());
		assertEquals(nextFreeBlockId, instance.getNextFreeBlockId());
		assertEquals(tableStatus, instance.getStatus());
	}
	
	@Test(expected = NullPointerException.class)
	public void fromByteArrayMustThrowNullPointerExceptionOnNullByteArray() throws IOException {
		byte[] nullBytes = null;
		
		instance.fromByteArray(nullBytes);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void fromByteArrayMustThrowIllegalArgumentExceptionOnByteArrayWithLessThanBytesSize() throws IOException {
		byte[] bytes = new byte[ITableHeaderBlockContent.BYTES_SIZE - 1];
		
		instance.fromByteArray(bytes);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void fromByteArrayMustThrowIllegalArgumentExceptionOnByteArrayWithGreatherThanBytesSize() throws IOException {
		byte[] bytes = new byte[ITableHeaderBlockContent.BYTES_SIZE + 1];
		
		instance.fromByteArray(bytes);
	}
	
	@Test
	public void fromByteArrayMustNotThrowIllegalArgumentExceptionOnByteArrayWithExactBytesSize() throws IOException {
		byte[] bytes = new byte[ITableHeaderBlockContent.BYTES_SIZE];
		
		IThreeByteValue blockSize = new BigEndianThreeBytesValue(75);
		byte[] valueBinary = blockSize.getValueBinary();
		bytes[1] = valueBinary[0];
		bytes[2] = valueBinary[1];
		bytes[3] = valueBinary[2];
		
		bytes[4] = TableStatus.VALID.getValue();
		
		instance.fromByteArray(bytes);
	}
	
	@Test
	public void toAndFromByteArrayTest() throws IOException {
		instance.setTableId(tableId);
		instance.setBlockSize(blockSize);
		instance.setHeaderSize(headerSize);
		instance.setNextFreeBlockId(nextFreeBlockId);
		instance.setStatus(tableStatus);
		
		byte[] bytes = instance.toByteArray();
		instance.fromByteArray(bytes);
		
		assertEquals(tableId, instance.getTableId());
		assertEquals(blockSize, instance.getBlockSize());
		assertEquals(headerSize, instance.getHeaderSize());
		assertEquals(nextFreeBlockId, instance.getNextFreeBlockId());
		assertEquals(tableStatus, instance.getStatus());
	}
	
}
