package br.com.xavier.suricate.dbms.interfaces.table.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.xavier.suricate.dbms.enums.TableBlockType;
import br.com.xavier.suricate.dbms.impl.low.BigEndianThreeBytesValue;
import br.com.xavier.suricate.dbms.interfaces.low.IThreeByteValue;

public abstract class ITableDataBlockHeaderTest {
	
	//XXX TEST SUBJECT
	private ITableDataBlockHeader instance;
	
	//XXX TEST PROPERTIES
	private Byte tableId;
	private IThreeByteValue blockId;
	private IThreeByteValue bytesUsedInBlock;
	
	//XXX CONSTRUCTOR
	public ITableDataBlockHeaderTest() {}
	
	//XXX ABSTRACT METHODS
	protected abstract ITableDataBlockHeader getInstance();

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
		this.blockId = new BigEndianThreeBytesValue(2);
		this.bytesUsedInBlock = new BigEndianThreeBytesValue(3);
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
	public void getAndSetBlockIdTest(){
		IThreeByteValue otherBlockId = new BigEndianThreeBytesValue(2);
		
		instance.setBlockId(blockId);
		
		assertSame(blockId, instance.getBlockId());
		assertNotSame(otherBlockId, instance.getBlockId());
		
		instance.setBlockId(otherBlockId);
		
		assertSame(otherBlockId, instance.getBlockId());
		assertNotSame(blockId, instance.getBlockId());
	}
	
	@Test
	public void getAndSetTypeTest(){
		TableBlockType blockType = TableBlockType.DATA;
		TableBlockType otherType = TableBlockType.INDEX;
		
		instance.setType(blockType);
		
		assertSame(blockType, instance.getType());
		assertNotSame(otherType, instance.getType());
		
		instance.setType(otherType);
		
		assertSame(otherType, instance.getType());
		assertNotSame(blockType, instance.getType());
	}
	
	@Test
	public void getAndSetBytesUsedInBlockTest(){
		IThreeByteValue otherValue = new BigEndianThreeBytesValue(2);
		
		instance.setBytesUsedInBlock(bytesUsedInBlock);
		
		assertSame(bytesUsedInBlock, instance.getBytesUsedInBlock());
		assertNotSame(otherValue, instance.getBytesUsedInBlock());
		
		instance.setBytesUsedInBlock(otherValue);
		
		assertSame(otherValue, instance.getBytesUsedInBlock());
		assertNotSame(bytesUsedInBlock, instance.getBytesUsedInBlock());
	}

	@Test
	public void toByteArrayTest() throws IOException {
		instance.setTableId(tableId);
		instance.setBlockId(blockId);
		instance.setBytesUsedInBlock(bytesUsedInBlock);
		instance.setType(TableBlockType.DATA);
		
		byte[] bytes = instance.toByteArray();
		
		assertNotNull(bytes);
		assertEquals(ITableDataBlockHeader.BYTES_SIZE, bytes.length);
	}
	
	@Test(expected = IOException.class)
	public void mustThrowIOExceptionOnNullTableId() throws IOException {
		instance.setTableId(null);
		instance.setBlockId(blockId);
		instance.setBytesUsedInBlock(bytesUsedInBlock);
		instance.setType(TableBlockType.DATA);
		
		instance.toByteArray();
	}
	
	@Test(expected = IOException.class)
	public void mustThrowIOExceptionOnNullBlockId() throws IOException {
		instance.setTableId(tableId);
		instance.setBlockId(null);
		instance.setBytesUsedInBlock(bytesUsedInBlock);
		instance.setType(TableBlockType.DATA);
		
		instance.toByteArray();
	}
	
	@Test(expected = IOException.class)
	public void mustThrowIOExceptionOnNullBytesUsedInBlock() throws IOException {
		instance.setTableId(tableId);
		instance.setBlockId(blockId);
		instance.setBytesUsedInBlock(null);
		instance.setType(TableBlockType.DATA);
		
		instance.toByteArray();
	}
	
	@Test(expected = IOException.class)
	public void mustThrowIOExceptionOnNullBlockType() throws IOException {
		instance.setTableId(tableId);
		instance.setBlockId(blockId);
		instance.setBytesUsedInBlock(bytesUsedInBlock);
		instance.setType(null);
		
		instance.toByteArray();
	}
	
	@Test
	public void fromByteArrayTest() throws IOException {
		instance.setTableId(tableId);
		instance.setBlockId(blockId);
		instance.setBytesUsedInBlock(bytesUsedInBlock);
		instance.setType(TableBlockType.DATA);
		
		byte[] bytes = instance.toByteArray();
		instance.fromByteArray(bytes);
		
		assertEquals(tableId, instance.getTableId());
		assertEquals(blockId, instance.getBlockId());
		assertEquals(bytesUsedInBlock, instance.getBytesUsedInBlock());
		assertEquals(TableBlockType.DATA, instance.getType());
	}
	
	@Test(expected = NullPointerException.class)
	public void fromByteArrayMustThrowNullPointerExceptionOnNullByteArray() throws IOException {
		byte[] nullBytes = null;
		
		instance.fromByteArray(nullBytes);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void fromByteArrayMustThrowIllegalArgumentExceptionOnByteArrayWithLessThanBytesSize() throws IOException {
		byte[] bytes = new byte[ITableDataBlockHeader.BYTES_SIZE - 1];
		
		instance.fromByteArray(bytes);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void fromByteArrayMustThrowIllegalArgumentExceptionOnByteArrayWithGreatherThanBytesSize() {
		byte[] bytes = new byte[ITableDataBlockHeader.BYTES_SIZE + 1];
		
		try {
			instance.fromByteArray(bytes);
		} catch (IOException e) {
		}
	}
	
	@Test
	public void fromByteArrayMustNotThrowIllegalArgumentExceptionOnByteArrayWithExactBytesSize() {
		byte[] bytes = new byte[ITableDataBlockHeader.BYTES_SIZE];
		
		try {
			instance.fromByteArray(bytes);
		} catch (IOException e) {
		}
	}

}
