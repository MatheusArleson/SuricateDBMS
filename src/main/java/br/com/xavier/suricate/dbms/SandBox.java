package br.com.xavier.suricate.dbms;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

import br.com.xavier.suricate.dbms.enums.ColumnsTypes;
import br.com.xavier.suricate.dbms.enums.TableBlockType;
import br.com.xavier.suricate.dbms.enums.TableStatus;
import br.com.xavier.suricate.dbms.impl.low.BigEndianThreeBytesValue;
import br.com.xavier.suricate.dbms.impl.table.Table;
import br.com.xavier.suricate.dbms.impl.table.data.ColumnEntry;
import br.com.xavier.suricate.dbms.impl.table.data.RowEntry;
import br.com.xavier.suricate.dbms.impl.table.data.TableDataBlock;
import br.com.xavier.suricate.dbms.impl.table.data.TableDataBlockHeader;
import br.com.xavier.suricate.dbms.impl.table.header.ColumnDescriptor;
import br.com.xavier.suricate.dbms.impl.table.header.TableHeaderBlock;
import br.com.xavier.suricate.dbms.impl.table.header.TableHeaderBlockContent;
import br.com.xavier.suricate.dbms.interfaces.low.IThreeByteValue;
import br.com.xavier.suricate.dbms.interfaces.table.ITable;
import br.com.xavier.suricate.dbms.interfaces.table.data.IColumnEntry;
import br.com.xavier.suricate.dbms.interfaces.table.data.IRowEntry;
import br.com.xavier.suricate.dbms.interfaces.table.data.ITableDataBlock;
import br.com.xavier.suricate.dbms.interfaces.table.data.ITableDataBlockHeader;
import br.com.xavier.suricate.dbms.interfaces.table.header.IColumnDescriptor;
import br.com.xavier.suricate.dbms.interfaces.table.header.ITableHeaderBlock;
import br.com.xavier.suricate.dbms.interfaces.table.header.ITableHeaderBlockContent;

public class SandBox {
	
	public static void main(String[] args) throws IOException {
		ITable table = generateTableInstance();
		byte[] byteArray = table.toByteArray();
		
		File f = new File("tb_teste.suricata");
		FileOutputStream fos = new FileOutputStream(f);
		fos.write(byteArray);
		fos.flush();
		fos.close();
	}

	//XXX HEADER BLOCK METHODS
	public static ITableHeaderBlock generateHeaderBlockInstance() throws IOException {
		ITableHeaderBlockContent tableHeaderBlockContent = generateTableHeaderBlockContentInstance();
		IColumnDescriptor columnDescriptor = generateColumnDescriptorInstance();
		
		ITableHeaderBlock tableHeaderBlock = generateTableHeaderBlock(tableHeaderBlockContent, columnDescriptor);
		return tableHeaderBlock;
	}

	public static ITableHeaderBlockContent generateTableHeaderBlockContentInstance() throws IOException{
		Byte tableId = new Byte("1");
		IThreeByteValue blockSize = new BigEndianThreeBytesValue(128);
		TableStatus tableStatus = TableStatus.VALID;
		Integer nextFreeBlockId = new Integer(2);
		Short headerSize = new Short("64");
		
		ITableHeaderBlockContent headerBlockContent = new TableHeaderBlockContent(
			tableId, blockSize, headerSize, nextFreeBlockId, tableStatus
		);
		
		byte[] headerBlockContentBytes = headerBlockContent.toByteArray();
		System.out.println("#> HEADER BLOCK CONTENT BYTES > ");
		System.out.println(Arrays.toString(headerBlockContentBytes));
		
		return headerBlockContent;
	}
	
	public static IColumnDescriptor generateColumnDescriptorInstance() throws IOException{
		String columnDescriptorName = new String("c");
		ColumnsTypes type = ColumnsTypes.STRING;
		Short size = new Short("1");
		
		IColumnDescriptor columnDescriptor = new ColumnDescriptor(columnDescriptorName, type, size);
		
		byte[] columnDescriptorBytes = columnDescriptor.toByteArray();
		System.out.println("#> COLUMN DESCRIPTOR BYTES > ");
		System.out.println(Arrays.toString(columnDescriptorBytes));
		
		return columnDescriptor;
	}

	public static ITableHeaderBlock generateTableHeaderBlock(
			ITableHeaderBlockContent tableHeaderBlockContent,
			IColumnDescriptor columnDescriptor
	) throws IOException {
		
			Collection<IColumnDescriptor> columnsDescriptors = new ArrayList<>();
			columnsDescriptors.add(columnDescriptor);
			
			ITableHeaderBlock tableHeaderBlock = new TableHeaderBlock(tableHeaderBlockContent, columnsDescriptors );
			
			byte[] tableHeaderBytes = tableHeaderBlock.toByteArray();
			
			System.out.println("#> TABLE HEADER BLOCK BYTES > ");
			System.out.println(Arrays.toString(tableHeaderBytes));
			
			return tableHeaderBlock;
		}

	//XXX DATA BLOCK METHODS
	public static ITableDataBlock generateDataBlockInstance() throws IOException {
		ITableDataBlockHeader dataBlockHeader = generateDataBlockHeaderInstance();
		
		IColumnEntry columnEntry = generatecColumnEntryInstance();		
		IRowEntry rowEntry = generateRowEntryInstance(columnEntry);
		
		ITableDataBlock dataBlock = generateDataBlockInstance(dataBlockHeader, rowEntry);
		return dataBlock;
	}
	
	public static ITableDataBlockHeader generateDataBlockHeaderInstance() throws IOException {
		Byte tableId = new Byte("1");
		IThreeByteValue blockId = new BigEndianThreeBytesValue(1);
		TableBlockType type = TableBlockType.DATA;
		IThreeByteValue bytesUsedInBlock = new BigEndianThreeBytesValue(8);
		
		ITableDataBlockHeader dataBlockHeader = new TableDataBlockHeader(tableId, blockId, type, bytesUsedInBlock);
		
		byte[] dataBlockHeaderBytes = dataBlockHeader.toByteArray();
		
		System.out.println("#> DATA BLOCK > HEADER > BYTES > ");
		System.out.println(Arrays.toString(dataBlockHeaderBytes));
		
		return dataBlockHeader;
	}

	public static IColumnEntry generatecColumnEntryInstance() throws IOException {
		byte[] dataBytes = Factory.toByteArray("d");
		Integer dataSizeInt = dataBytes.length;
		Short dataSize = dataSizeInt.shortValue();
		
		ByteBuffer bb = ByteBuffer.allocate(dataSizeInt + Short.BYTES);
		bb.putShort(dataSize);
		bb.put(dataBytes);
		
		IColumnEntry columnEntry = new ColumnEntry(bb.array());
		byte[] columnEntryBytes = columnEntry.toByteArray();
		
		System.out.println("#> COLUMN ENTRY BYTES > ");
		System.out.println(Arrays.toString(columnEntryBytes));
		
		return columnEntry;
	}

	public static IRowEntry generateRowEntryInstance(IColumnEntry columnEntry) throws IOException {
		Collection<IColumnEntry> columnsEntries = new LinkedList<>();
		columnsEntries.add(columnEntry);
		
		IRowEntry rowEntry = new RowEntry(columnsEntries);
		byte[] rowEntryBytes = rowEntry.toByteArray();
		
		System.out.println("#> ROW ENTRY BYTES > ");
		System.out.println(Arrays.toString(rowEntryBytes));
		
		return rowEntry;
	}

	public static ITableDataBlock generateDataBlockInstance(ITableDataBlockHeader dataBlockHeader, IRowEntry rowEntry) throws IOException {
		Collection<IRowEntry> rows = new LinkedList<>();
		rows.add(rowEntry);
		
		ITableDataBlock dataBlock = new TableDataBlock(dataBlockHeader, rows);
		byte[] dataBlockBytes = dataBlock.toByteArray();
		
		System.out.println("#> DATA BLOCK BYTES > ");
		System.out.println(Arrays.toString(dataBlockBytes));
		
		return dataBlock;
	}

	//XXX TABLE METHODS
	public static ITable generateTableInstance() throws IOException {
		ITableHeaderBlock tableHeaderBlock = generateHeaderBlockInstance();
		ITableDataBlock dataBlock = generateDataBlockInstance();
		ITable table = generateTableInstance(tableHeaderBlock, dataBlock);
		return table;
	}
	
	public static ITable generateTableInstance(ITableHeaderBlock tableHeaderBlock, ITableDataBlock dataBlock) throws IOException {
		Collection<ITableDataBlock> dataBlocks = new LinkedList<>();
		dataBlocks.add(dataBlock );
		
		ITable table = new Table(tableHeaderBlock, dataBlocks);
		byte[] tableBytes = table.toByteArray();
		
		System.out.println("##> TABLE BYTES > ");
		System.out.println(Arrays.toString(tableBytes));
		
		return table;
	}

}
