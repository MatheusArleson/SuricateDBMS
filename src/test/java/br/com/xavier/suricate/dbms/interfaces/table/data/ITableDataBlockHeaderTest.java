package br.com.xavier.suricate.dbms.interfaces.table.data;

import static org.junit.Assert.*;

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
	private TableBlockType blockType;
	private IThreeByteValue blockId;
	private IThreeByteValue bytesUsedInBlock;
	private byte[] propertiesBytes;
	
	private Byte otherTableId;
	private TableBlockType otherBlockType;
	private IThreeByteValue otherBlockId;
	private IThreeByteValue otherBytesUsedInBlock;
	private byte[] otherPropertiesBytes;
	
	//XXX CONSTRUCTOR
	public ITableDataBlockHeaderTest() {}
	
	//XXX ABSTRACT METHODS
	protected abstract ITableDataBlockHeader getInstance();

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
		this.blockType = TableBlockType.DATA;
		this.blockId = new BigEndianThreeBytesValue(2);
		this.bytesUsedInBlock = new BigEndianThreeBytesValue(3);
		
		propertiesBytes = new byte[ITableDataBlockHeader.BYTES_SIZE];
		propertiesBytes[0] = tableId;
		
		byte[] blockIdBytes = blockId.getValueBinary();
		propertiesBytes[1] = blockIdBytes[0];
		propertiesBytes[2] = blockIdBytes[1];
		propertiesBytes[3] = blockIdBytes[2];
		
		propertiesBytes[4] = blockType.getId();
		
		byte[] bytesUsedInblockBytes = bytesUsedInBlock.getValueBinary();
		propertiesBytes[5] = bytesUsedInblockBytes[0];
		propertiesBytes[6] = bytesUsedInblockBytes[1];
		propertiesBytes[7] = bytesUsedInblockBytes[2];
	}
	
	private void setupOtherProperties() {
		this.otherTableId = new Byte("2");
		this.otherBlockType = TableBlockType.INDEX;
		this.otherBlockId = new BigEndianThreeBytesValue(3);
		this.otherBytesUsedInBlock = new BigEndianThreeBytesValue(4);
		
		otherPropertiesBytes = new byte[ITableDataBlockHeader.BYTES_SIZE];
		otherPropertiesBytes[0] = otherTableId;
		
		byte[] blockIdBytes = otherBlockId.getValueBinary();
		otherPropertiesBytes[1] = blockIdBytes[0];
		otherPropertiesBytes[2] = blockIdBytes[1];
		otherPropertiesBytes[3] = blockIdBytes[2];
		
		otherPropertiesBytes[4] = otherBlockType.getId();
		
		byte[] bytesUsedInblockBytes = otherBytesUsedInBlock.getValueBinary();
		otherPropertiesBytes[5] = bytesUsedInblockBytes[0];
		otherPropertiesBytes[6] = bytesUsedInblockBytes[1];
		otherPropertiesBytes[7] = bytesUsedInblockBytes[2];
	}

	//XXX AFTER METHODS
	@After
	public void destroy() {
		instance = null;
		
		tableId = null;
		blockType = null;
		blockId = null;
		bytesUsedInBlock = null;
		propertiesBytes = null;
		
		otherTableId = null;
		otherBlockType = null;
		otherBlockId = null;
		otherBytesUsedInBlock = null;
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
		
		assertEquals(tableId, instance.getTableId());
		assertNotEquals(otherTableId, instance.getTableId());
		
		instance.setTableId(otherTableId);
		
		assertEquals(otherTableId, instance.getTableId());
		assertNotEquals(tableId, instance.getTableId());
	}
	
	//-------------
	// BLOCK ID
	//-------------
	@Test(expected = IllegalArgumentException.class)
	public void setBlockIdMustThrowIllegalArgumentExceptionOnNullBlockId(){
		instance.setBlockId(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void setBlockIdMustThrowIllegalArgumentExceptionOnNegativeBlockId(){
		instance.setBlockId(new BigEndianThreeBytesValue(-1));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void setBlockIdMustThrowIllegalArgumentExceptionOnZeroBlockId(){
		instance.setBlockId(new BigEndianThreeBytesValue(0));
	}
	
	@Test
	public void setBloackIdMustNotThrowIllegalArgumentExceptionValidBlockId(){
		instance.setBlockId(blockId);
	}
	
	@Test
	public void getBlockIdReferenceMustBeAClone(){
		instance.setBlockId(blockId);
		
		assertNotSame(blockId, instance.getBlockId());
		assertEquals(blockId, instance.getBlockId());
	}
	
	@Test
	public void getAndSetBlockIdTest(){
		instance.setBlockId(blockId);
		
		assertEquals(blockId, instance.getBlockId());
		assertNotEquals(otherBlockId, instance.getBlockId());
		
		instance.setBlockId(otherBlockId);
		
		assertEquals(otherBlockId, instance.getBlockId());
		assertNotEquals(blockId, instance.getBlockId());
	}
	
	//-------------
	// BLOCK TYPE
	//-------------
	@Test(expected = IllegalArgumentException.class)
	public void setTypeMustThrowIllegalArgumentExceptionOnNullType(){
		instance.setType(null);
	}
	
	@Test
	public void setTypeMustNotThrowIllegalArgumentExceptionOnValidType(){
		instance.setType(blockType);
	}
	
	@Test
	public void getAndSetTypeTest(){
		instance.setType(blockType);
		
		assertSame(blockType, instance.getType());
		assertNotSame(otherBlockType, instance.getType());
		
		instance.setType(otherBlockType);
		
		assertSame(otherBlockType, instance.getType());
		assertNotSame(blockType, instance.getType());
	}
	
	//-------------
	// BYTES USED IN BLOCK
	//-------------
	@Test(expected = IllegalArgumentException.class)
	public void setBytesUsedInBlockMustThrowIllegalArgumentExceptionOnNull(){
		instance.setBytesUsedInBlock(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void setBytesUsedInBlockMustThrowIllegalArgumentExceptionOnNegative(){
		instance.setBytesUsedInBlock(new BigEndianThreeBytesValue(-1));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void setBytesUsedInBlockMustThrowIllegalArgumentExceptionOnZero(){
		instance.setBytesUsedInBlock(new BigEndianThreeBytesValue(0));
	}
	
	@Test
	public void setBytesUsedInBlockMustNotThrowIllegalArgumentExceptionOnValid(){
		instance.setBytesUsedInBlock(bytesUsedInBlock);
	}
	
	@Test
	public void getBytesUsedInBlockReferenceMustBeAClone(){
		instance.setBytesUsedInBlock(bytesUsedInBlock);
		
		assertNotSame(bytesUsedInBlock, instance.getBytesUsedInBlock());
		assertEquals(bytesUsedInBlock, instance.getBytesUsedInBlock());
	}
	
	@Test
	public void getAndSetBytesUsedInBlockTest(){
		instance.setBytesUsedInBlock(bytesUsedInBlock);
		
		assertEquals(bytesUsedInBlock, instance.getBytesUsedInBlock());
		assertNotEquals(otherBytesUsedInBlock, instance.getBytesUsedInBlock());
		
		instance.setBytesUsedInBlock(otherBytesUsedInBlock);
		
		assertEquals(otherBytesUsedInBlock, instance.getBytesUsedInBlock());
		assertNotEquals(bytesUsedInBlock, instance.getBytesUsedInBlock());
	}

	//-------------
	// TO BYTE ARRAY
	//-------------
	@Test(expected = IOException.class)
	public void mustThrowIOExceptionOnNullTableId() throws IOException {
		//instance.setTableId(null);
		instance.setBlockId(blockId);
		instance.setBytesUsedInBlock(bytesUsedInBlock);
		instance.setType(blockType);
		
		instance.toByteArray();
	}
	
	@Test(expected = IOException.class)
	public void mustThrowIOExceptionOnNullBlockId() throws IOException {
		instance.setTableId(tableId);
		//instance.setBlockId(null);
		instance.setBytesUsedInBlock(bytesUsedInBlock);
		instance.setType(blockType);
		
		instance.toByteArray();
	}
	
	@Test(expected = IOException.class)
	public void mustThrowIOExceptionOnNullBytesUsedInBlock() throws IOException {
		instance.setTableId(tableId);
		instance.setBlockId(blockId);
		//instance.setBytesUsedInBlock(null);
		instance.setType(blockType);
		
		instance.toByteArray();
	}
	
	@Test(expected = IOException.class)
	public void mustThrowIOExceptionOnNullBlockType() throws IOException {
		instance.setTableId(tableId);
		instance.setBlockId(blockId);
		instance.setBytesUsedInBlock(bytesUsedInBlock);
		//instance.setType(null);
		
		instance.toByteArray();
	}
	
	@Test
	public void toByteArrayTest() throws IOException {
		instance.setTableId(tableId);
		instance.setBlockId(blockId);
		instance.setBytesUsedInBlock(bytesUsedInBlock);
		instance.setType(blockType);
		
		byte[] bytes = instance.toByteArray();
		
		assertNotNull(bytes);
		assertEquals(ITableDataBlockHeader.BYTES_SIZE, bytes.length);
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
		byte[] bytes = new byte[ITableDataBlockHeader.BYTES_SIZE - 1];
		
		instance.fromByteArray(bytes);
	}
	
	@Test(expected = IOException.class)
	public void fromByteArrayMustThrowIOExceptionOnByteArrayWithGreatherThanBytesSize() throws IOException {
		byte[] bytes = new byte[ITableDataBlockHeader.BYTES_SIZE + 1];
		
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
		assertEquals(blockId, instance.getBlockId());
		assertEquals(bytesUsedInBlock, instance.getBytesUsedInBlock());
		assertEquals(blockType, instance.getType());
	}

	@Test
	public void fromByteArrayTest() throws IOException {
		ITableDataBlockHeader otherInstance = getInstance();
		otherInstance.setTableId(tableId);
		otherInstance.setBlockId(blockId);
		otherInstance.setBytesUsedInBlock(bytesUsedInBlock);
		otherInstance.setType(TableBlockType.DATA);
		
		instance.fromByteArray(propertiesBytes);
		
		assertEquals(instance, otherInstance);
	}
}
