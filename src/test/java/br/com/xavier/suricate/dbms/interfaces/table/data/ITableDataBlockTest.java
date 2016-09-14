package br.com.xavier.suricate.dbms.interfaces.table.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.After;
import org.junit.Before;

import br.com.xavier.suricate.dbms.enums.TableBlockType;
import br.com.xavier.suricate.dbms.impl.low.BigEndianThreeBytesValue;
import br.com.xavier.suricate.dbms.impl.table.data.ColumnEntry;
import br.com.xavier.suricate.dbms.impl.table.data.RowEntry;
import br.com.xavier.suricate.dbms.impl.table.data.TableDataBlockHeader;
import br.com.xavier.suricate.dbms.interfaces.low.IThreeByteValue;

public abstract class ITableDataBlockTest {
	
	//XXX TEST SUBJECT
	private ITableDataBlock instance;
	
	//XXX TEST PROPERTIES
	private ITableDataBlockHeader header;
	private Collection<IRowEntry> rows;
	
	private ITableDataBlockHeader otherHeader;
	private Collection<IRowEntry> otherRows;
	
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
	
	//-------------
	// ROWS
	//-------------
	
	//-------------
	// TO BYTE ARRAY
	//-------------
	
	//-------------
	// FROM BYTE ARRAY
	//-------------

}
