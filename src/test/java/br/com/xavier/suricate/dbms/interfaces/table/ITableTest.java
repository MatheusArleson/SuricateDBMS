package br.com.xavier.suricate.dbms.interfaces.table;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.xavier.suricate.dbms.enums.ColumnsTypes;
import br.com.xavier.suricate.dbms.enums.TableBlockType;
import br.com.xavier.suricate.dbms.enums.TableStatus;
import br.com.xavier.suricate.dbms.impl.low.BigEndianThreeBytesValue;
import br.com.xavier.suricate.dbms.impl.table.data.ColumnEntry;
import br.com.xavier.suricate.dbms.impl.table.data.RowEntry;
import br.com.xavier.suricate.dbms.impl.table.data.TableDataBlock;
import br.com.xavier.suricate.dbms.impl.table.data.TableDataBlockHeader;
import br.com.xavier.suricate.dbms.impl.table.header.ColumnDescriptor;
import br.com.xavier.suricate.dbms.impl.table.header.TableHeaderBlock;
import br.com.xavier.suricate.dbms.impl.table.header.TableHeaderBlockContent;
import br.com.xavier.suricate.dbms.interfaces.low.IThreeByteValue;
import br.com.xavier.suricate.dbms.interfaces.table.data.IColumnEntry;
import br.com.xavier.suricate.dbms.interfaces.table.data.IRowEntry;
import br.com.xavier.suricate.dbms.interfaces.table.data.ITableDataBlock;
import br.com.xavier.suricate.dbms.interfaces.table.data.ITableDataBlockHeader;
import br.com.xavier.suricate.dbms.interfaces.table.header.IColumnDescriptor;
import br.com.xavier.suricate.dbms.interfaces.table.header.ITableHeaderBlock;
import br.com.xavier.suricate.dbms.interfaces.table.header.ITableHeaderBlockContent;

public abstract class ITableTest {

	//XXX TEST SUBJECT
	private ITable instance;
	
	//XXX TEST PROPERTIES
	private File tableFile;
	private ITableHeaderBlock headerBlock;
	private Collection<ITableDataBlock> dataBlocks;
	private byte[] propertiesBytes;
	
	//private File otherTableFile;
	private ITableHeaderBlock otherHeaderBlock;
	private Collection<ITableDataBlock> otherDataBlocks;
	//private byte[] otherPropertiesBytes;
	
	//XXX CONSTRUCTOR
	public ITableTest() {}
	
	//XXX ABSTRACT METHODS
	protected abstract ITable getInstance() throws IOException;
	
	//XXX BEFORE METHODS
	@Before
	public void setup() throws IOException {
		setupInstance();
		setupProperties();
		setupOtherProperties();
	}
	
	private void setupInstance() throws IOException {
		instance = getInstance();
	}
	
	private void setupProperties() throws IOException {
		tableFile = new File("testTable.suricata");
		this.headerBlock = generateHeaderBlock();
		this.dataBlocks = generateDataBlocks();
	}

	private ITableHeaderBlock generateHeaderBlock() {
		Byte tableId = new Byte("1");
		IThreeByteValue blockSize = new BigEndianThreeBytesValue(4096);
		Short headerSize = new Short("75");
		Integer nextFreeBlockId = new Integer("2");
		TableStatus tableStatus = TableStatus.VALID;
		
		ITableHeaderBlockContent headerContent = new TableHeaderBlockContent(tableId, blockSize, headerSize, nextFreeBlockId, tableStatus);
		
		String name = new String("coluna");
		ColumnsTypes type = ColumnsTypes.STRING;
		Short size = new Short("2");
		IColumnDescriptor columnDescriptor = new ColumnDescriptor(name, type, size);
		
		Collection<IColumnDescriptor> columnsDescriptors = new ArrayList<>();
		columnsDescriptors.add(columnDescriptor);
		
		ITableHeaderBlock headerBlock = new TableHeaderBlock(headerContent, columnsDescriptors);
		return headerBlock;
	}
	
	private Collection<ITableDataBlock> generateDataBlocks() throws IOException {
		Byte tableId = new Byte("");
		IThreeByteValue blockId = new BigEndianThreeBytesValue(1);
		TableBlockType type = TableBlockType.DATA;
		IThreeByteValue bytesUsedInBlock = new BigEndianThreeBytesValue(1);
		ITableDataBlockHeader header = new TableDataBlockHeader(tableId, blockId, type, bytesUsedInBlock);
		
		byte[] columnEntryBytes = new byte[]{};
		IColumnEntry columnEntry = new ColumnEntry(columnEntryBytes);
		
		Collection<IColumnEntry> columnsEntries = new ArrayList<>();
		columnsEntries.add(columnEntry);
		
		IRowEntry row = new RowEntry(columnsEntries );
		
		Collection<IRowEntry> rows = new ArrayList<>();
		rows.add(row);
		ITableDataBlock dataBlock = new TableDataBlock(header, rows);
		
		Collection<ITableDataBlock> dataBlocks = new ArrayList<>();
		dataBlocks.add(dataBlock);
		return dataBlocks;
	}
	
	private void setupOtherProperties() {
		
	}
	
	//XXX AFTER METHODS
	@After
	public void destroy() {
		instance = null;
	}
	
	//XXX TEST METHODS
	
	//-------------
	// FILE
	//-------------
	@Test(expected = IOException.class)
	public void setFileMustThrowIOExceptionOnNullFile() throws IOException{
		instance.setFile(null);
	}
	
	@Test
	public void setFileMustNotThrowIOExceptionOnFileWithInvalidExtension() throws IOException{
		File f = new File("invalid.inv");
		instance.setFile(f);
	}
	
	@Test
	public void setFileMustNotThrowIOExceptionOnFileWithValidExtension() throws IOException{
		File f = new File("valid.suricata");
		instance.setFile(f);
	}
	
	@Test
	public void setFileMustNotThrowIOExceptionOnValidFile() throws IOException{
		instance.setFile(tableFile);
	}
	
	@Test(expected = IOException.class)
	public void setFileMustReloadTableHeaderBlock() throws IOException{
		instance.setHeaderBlock(headerBlock);
		
		assertSame(headerBlock, instance.getHeaderBlock());
		
		instance.setFile(tableFile);
		assertNotSame(headerBlock, instance.getHeaderBlock());
		assertEquals(headerBlock, instance.getHeaderBlock());
	}
	
	//-------------
	// HEADER BLOCK
	//-------------
	@Test(expected = IllegalArgumentException.class)
	public void setTableHeaderBlockMustThrowIllegalArgumentExceptionOnNullTableHeaderBlock(){
		instance.setHeaderBlock(null);
	}
	
	@Test
	public void setTableHeaderBlockMustNotThrowIllegalArgumentExceptionOnValidTableHeaderBlock(){
		instance.setHeaderBlock(headerBlock);
	}
	
	@Test
	public void getAndSetTableHeaderBlockTest(){
		instance.setHeaderBlock(headerBlock);
		
		assertSame(headerBlock, instance.getHeaderBlock());
		assertNotSame(otherHeaderBlock, instance.getHeaderBlock());
		
		instance.setHeaderBlock(otherHeaderBlock);
		
		assertSame(otherHeaderBlock, instance.getHeaderBlock());
		assertNotSame(headerBlock, instance.getHeaderBlock());
	}
	
	//-------------
	// DATA BLOCKS
	//-------------
	@Test(expected = IllegalArgumentException.class)
	public void setDataBlocksMustThrowIllegalArgumentExceptionOnNullCollection(){
		instance.setDataBlocks(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void setDataBlocksMustThrowIllegalArgumentExceptionOnEmptyCollection(){
		Collection<ITableDataBlock> empty = Collections.emptyList();
		
		instance.setDataBlocks(empty);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void setDataBlocksMustThrowIllegalArgumentOnCollectionWithNullValues(){
		dataBlocks.add(null);
		
		instance.setDataBlocks(dataBlocks);
	}
	
	@Test
	public void setDataBlocksMustNotShareReferenceWithCollectionSetted(){
		instance.setDataBlocks(dataBlocks);
		
		assertNotSame(dataBlocks, instance.getDataBlocks());
		
		instance.setDataBlocks(otherDataBlocks);
		
		assertNotSame(dataBlocks, instance.getDataBlocks());
		assertNotSame(otherDataBlocks, instance.getDataBlocks());
	}
	
	@Test
	public void getAndsetDataBlocksTest(){
		instance.setDataBlocks(dataBlocks);
		
		assertEquals(dataBlocks, instance.getDataBlocks());
		
		instance.setDataBlocks(otherDataBlocks);
		
		assertEquals(otherDataBlocks, instance.getDataBlocks());
	}
	
	//-------------
	// TO BYTE ARRAY
	//-------------
	@Test(expected = IOException.class)
	public void toByteArrayMustThrowIOExceptionOnNullTableHeaderBlock() throws IOException {
		//instance.setHeaderBlock(headerBlock);
		instance.setDataBlocks(dataBlocks);
		
		instance.toByteArray();
	}
	
	@Test(expected = IOException.class)
	public void toByteArrayMustThrowIOExceptionOnNullDataBlocksCollection() throws IOException {
		instance.setHeaderBlock(headerBlock);
		//instance.setDataBlocks(dataBlocks);
		
		instance.toByteArray();
	}
	
	@Test
	public void toByteArrayTest() throws IOException {
		instance.setHeaderBlock(headerBlock);
		instance.setDataBlocks(dataBlocks);
		
		byte[] bytes = instance.toByteArray();
		
		assertNotNull(bytes);
		assertArrayEquals(propertiesBytes, bytes);
	}
	
	//-------------
	// FROM BYTE ARRAY
	//-------------
	@Test(expected = NullPointerException.class)
	public void fromByteArrayMustThrowNullPointerExceptionOnNullByteArray() throws IOException {
		byte[] nullBytes = null;
		
		instance.fromByteArray(nullBytes);
	}
	
	@Test
	public void fromByteArrayMustRestoreEqualProperties() throws IOException {
		instance.fromByteArray(propertiesBytes);
		
		assertEquals(headerBlock, instance.getHeaderBlock());
		assertEquals(dataBlocks, instance.getDataBlocks());
	}
	
	@Test
	public void fromByteArrayMustProduceEqualInstance() throws IOException{
		ITable otherInstance = getInstance();
		otherInstance.setHeaderBlock(headerBlock);
		otherInstance.setDataBlocks(dataBlocks);
		
		instance.fromByteArray(propertiesBytes);
		
		assertEquals(instance, otherInstance);
	}
	
}
