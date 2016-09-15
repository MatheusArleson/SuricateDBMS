package br.com.xavier.suricate.dbms.interfaces.table.data;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.xavier.suricate.dbms.enums.TableBlockType;
import br.com.xavier.suricate.dbms.impl.low.BigEndianThreeBytesValue;
import br.com.xavier.suricate.dbms.impl.table.data.ColumnEntry;
import br.com.xavier.suricate.dbms.impl.table.data.RowEntry;
import br.com.xavier.suricate.dbms.impl.table.data.TableDataBlockHeader;
import br.com.xavier.suricate.dbms.interfaces.low.IThreeByteValue;
import br.com.xavier.util.ByteArrayUtils;

public abstract class ITableDataBlockTest {
	
	//XXX TEST SUBJECT
	private ITableDataBlock instance;
	
	//XXX TEST PROPERTIES
	private ITableDataBlockHeader header;
	private Collection<IRowEntry> rows;
	private byte[] propertiesBytes;
	
	private ITableDataBlockHeader otherHeader;
	private Collection<IRowEntry> otherRows;
	//private byte[] otherPropertiesBytes;
	
	//XXX CONSTRUCTOR
	public ITableDataBlockTest() {}
	
	//XXX ABSTRACT METHODS
	protected abstract ITableDataBlock getInstance();

	//XXX BEFORE METHODS
	@Before
	public void setup() throws IOException {
		setupInstance();
		setupProperties();
		setupOtherProperties();
	}

	private void setupInstance() {
		this.instance = getInstance();
	}

	private void setupProperties() throws IOException {
		Byte tableId = new Byte("1");
		IThreeByteValue blockId = new BigEndianThreeBytesValue(2);
		TableBlockType type = TableBlockType.DATA;
		IThreeByteValue bytesUsedInBlock = new BigEndianThreeBytesValue(3);
		
		header = new TableDataBlockHeader(tableId, blockId, type, bytesUsedInBlock);
		
		byte[] columnEntryBytes = new byte[]{0,2,0,99};
		IColumnEntry columnEntry = new ColumnEntry(columnEntryBytes);
		
		Collection<IColumnEntry> columnsEntries = new ArrayList<>();
		columnsEntries.add(columnEntry);
		
		IRowEntry rowEntry = new RowEntry(columnsEntries);
		
		Collection<IRowEntry> rowsEntries = new ArrayList<>();
		rowsEntries.add(rowEntry);
	
		rows = rowsEntries;
		
		byte[] headerBytes = header.toByteArray();
		byte[] rowsBytes = ByteArrayUtils.toByteArray(rows);
		
		propertiesBytes = ByteArrayUtils.toByteArray(headerBytes, rowsBytes);
	}
	
	private void setupOtherProperties() throws IOException {
		Byte tableId = new Byte("2");
		IThreeByteValue blockId = new BigEndianThreeBytesValue(3);
		TableBlockType type = TableBlockType.INDEX;
		IThreeByteValue bytesUsedInBlock = new BigEndianThreeBytesValue(4);
		
		otherHeader = new TableDataBlockHeader(tableId, blockId, type, bytesUsedInBlock);
		
		byte[] columnEntryBytes = new byte[]{0,2,1,99};
		IColumnEntry columnEntry = new ColumnEntry(columnEntryBytes);
		
		Collection<IColumnEntry> columnsEntries = new ArrayList<>();
		columnsEntries.add(columnEntry);
		
		IRowEntry rowEntry = new RowEntry(columnsEntries);
		
		Collection<IRowEntry> rowsEntries = new ArrayList<>();
		rowsEntries.add(rowEntry);
	
		otherRows = rowsEntries;
		
//		byte[] headerBytes = otherHeader.toByteArray();
//		byte[] rowsBytes = ByteArrayUtils.toByteArray(otherRows);
//		
//		otherPropertiesBytes = ByteArrayUtils.toByteArray(headerBytes, rowsBytes);
	}

	//XXX AFTER METHODS
	@After
	public void destroy() {
		instance = null;
		
		header = null;
		rows = null;
		
		otherHeader = null;
		otherRows = null;
	}
	
	//XXX TEST METHODS
	//-------------
	// HEADER
	//-------------
	@Test(expected = IllegalArgumentException.class)
	public void setHeaderMustThrowIllegalArgumentExceptionOnNullTableHeader(){
		instance.setHeader(null);
	}
	
	@Test
	public void setHeaderMustNotThrowIllegalArgumentExceptionValidHeader(){
		instance.setHeader(header);
	}
	
	@Test
	public void getAndSetHeaderTest(){
		instance.setHeader(header);
		
		assertSame(header, instance.getHeader());
		assertNotSame(otherHeader, instance.getHeader());
		
		instance.setHeader(otherHeader);
		
		assertSame(otherHeader, instance.getHeader());
		assertNotSame(header, instance.getHeader());
	}
	
	//-------------
	// ROWS
	//-------------
	@Test(expected = IllegalArgumentException.class)
	public void setRowsMustThrowIllegalArgumentExceptionOnNullRowsCollection(){
		instance.setRows(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void setRowsMustThrowIllegalArgumentExceptionOnEmptyCollection(){
		Collection<IRowEntry> empty = Collections.emptyList();
		
		instance.setRows(empty);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void setRowsMustThrowIllegalArgumentOnCollectionWithNullValues(){
		rows.add(null);
		
		instance.setRows(rows);
	}
	
	@Test
	public void setRowsMustNotShareReferenceWithCollectionSetted(){
		instance.setRows(rows);
		
		assertNotSame(rows, instance.getRows());
		
		instance.setRows(otherRows);
		
		assertNotSame(rows, instance.getRows());
		assertNotSame(otherRows, instance.getRows());
	}
	
	@Test
	public void getAndSetRowsTest(){
		instance.setRows(rows);
		
		assertEquals(rows, instance.getRows());
		
		instance.setRows(otherRows);
		
		assertEquals(otherRows, instance.getRows());
	}
	
	@Test
	public void setRowsMustNotThrowIllegalArgumentExceptionValidRows(){
		instance.setRows(rows);
	}
	
	//-------------
	// TO BYTE ARRAY
	//-------------
	@Test(expected = IOException.class)
	public void toByteArrayMustThrowIOExceptionOnNullHeader() throws IOException {
		//instance.setHeader(header);
		instance.setRows(rows);
		
		instance.toByteArray();
	}
	
	@Test(expected = IOException.class)
	public void toByteArrayMustThrowIOExceptionOnNullRowsCollection() throws IOException {
		instance.setHeader(header);
		//instance.setRows(rows);
		
		instance.toByteArray();
	}
	
	@Test
	public void toByteArrayTest() throws IOException {
		instance.setHeader(header);
		instance.setRows(rows);
		
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
		
		assertEquals(header, instance.getHeader());
		assertEquals(rows, instance.getRows());
	}
	
	@Test
	public void fromByteArrayMustProduceEqualInstance() throws IOException{
		ITableDataBlock otherInstance = getInstance();
		otherInstance.setHeader(header);
		otherInstance.setRows(rows);
		
		instance.fromByteArray(propertiesBytes);
		
		assertEquals(instance, otherInstance);
	}

}
