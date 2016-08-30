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
	
	//XXX CONSTRUCTOR
	public ITableHeaderBlockContentTest() {	}
	
	//XXX ABSTRACT METHODS
	protected abstract ITableHeaderBlockContent getInstance();
	
	//XXX BEFORE METHODS
	@Before
	public void setup() {
		instance = getInstance();
	}
	
	//XXX AFTER METHODS
	@After
	public void destroy() {
		instance = null;
	}
	
	//XXX TEST METHODS
	@Test
	public void getAndSetTableIdTest(){
		Byte tableId = new Byte("1");
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
		IThreeByteValue blockSize = new BigEndianThreeBytesValue(4096);
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
		Short headerSize = new Short("1");
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
		Integer nextFreeBlock = new Integer("1");
		Integer otherNextFreeBlocke = new Integer("2");
		
		instance.setNextFreeBlockId(nextFreeBlock);
		
		assertSame(nextFreeBlock, instance.getNextFreeBlockId());
		assertNotSame(otherNextFreeBlocke, instance.getNextFreeBlockId());
		
		instance.setNextFreeBlockId(otherNextFreeBlocke);
		
		assertSame(otherNextFreeBlocke, instance.getNextFreeBlockId());
		assertNotSame(nextFreeBlock, instance.getNextFreeBlockId());
	}
	
	@Test
	public void getAndSetStatusTest(){
		TableStatus status = TableStatus.VALID;
		TableStatus otherStatus = TableStatus.INVALID;
		
		instance.setStatus(status);
		
		assertSame(status, instance.getStatus());
		assertNotSame(otherStatus, instance.getStatus());
		
		instance.setStatus(otherStatus);
		
		assertSame(otherStatus, instance.getStatus());
		assertNotSame(status, instance.getStatus());
	}
	
	@Test
	public void toByteArrayTest() throws IOException {
		Byte tableId = new Byte("1");
		IThreeByteValue blockSize = new BigEndianThreeBytesValue(ITableHeaderBlockContent.MINUMUN_BLOCK_SIZE);
		Short headerSize = new Short("3");
		Integer nextFreeBlockId = new Integer("4");
		TableStatus tableStatus = TableStatus.VALID;
		
		instance.setTableId(tableId);
		instance.setBlockSize(blockSize);
		instance.setHeaderSize(headerSize);
		instance.setNextFreeBlockId(nextFreeBlockId);
		instance.setStatus(tableStatus);
		
		byte[] bytes = instance.toByteArray();
		
		assertNotNull(bytes);
		assertEquals(ITableHeaderBlockContent.BYTES_SIZE, bytes.length);
	}
	
	@Test
	public void fromByteArrayTest(){
		
	}
	
	@Test
	public void toAndFromByteArrayTest(){
		
	}
	
	
	
}
