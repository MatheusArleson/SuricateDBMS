package br.com.xavier.suricate.dbms.abstractions.services;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.io.LineIterator;

import br.com.xavier.suricate.dbms.Factory;
import br.com.xavier.suricate.dbms.enums.ColumnsTypes;
import br.com.xavier.suricate.dbms.enums.TableBlockType;
import br.com.xavier.suricate.dbms.enums.TableStatus;
import br.com.xavier.suricate.dbms.impl.low.BigEndianThreeBytesValue;
import br.com.xavier.suricate.dbms.impl.table.Table;
import br.com.xavier.suricate.dbms.impl.table.data.RowEntry;
import br.com.xavier.suricate.dbms.impl.table.data.TableDataBlock;
import br.com.xavier.suricate.dbms.impl.table.data.TableDataBlockHeader;
import br.com.xavier.suricate.dbms.impl.table.header.ColumnDescriptor;
import br.com.xavier.suricate.dbms.impl.table.header.TableHeaderBlock;
import br.com.xavier.suricate.dbms.impl.table.header.TableHeaderBlockContent;
import br.com.xavier.suricate.dbms.interfaces.low.IThreeByteValue;
import br.com.xavier.suricate.dbms.interfaces.services.IFileParser;
import br.com.xavier.suricate.dbms.interfaces.services.ITextSeparators;
import br.com.xavier.suricate.dbms.interfaces.table.ITable;
import br.com.xavier.suricate.dbms.interfaces.table.data.IColumnEntry;
import br.com.xavier.suricate.dbms.interfaces.table.data.IRowEntry;
import br.com.xavier.suricate.dbms.interfaces.table.data.ITableDataBlock;
import br.com.xavier.suricate.dbms.interfaces.table.data.ITableDataBlockHeader;
import br.com.xavier.suricate.dbms.interfaces.table.header.IColumnDescriptor;
import br.com.xavier.suricate.dbms.interfaces.table.header.ITableHeaderBlock;
import br.com.xavier.suricate.dbms.interfaces.table.header.ITableHeaderBlockContent;
import br.com.xavier.util.FileUtils;
import br.com.xavier.util.StringUtils;

public abstract class AbstractFileParser 
		implements IFileParser {

	@Override
	public ITable parse(File f, Charset charset, ITextSeparators separators, Byte tableId, IThreeByteValue blockSize) throws IOException {
		validate(f, charset, separators);
		
		LineIterator lineIterator = null;
		try {
			lineIterator = StringUtils.getLineIterator(f, charset);
			
			if(!lineIterator.hasNext()){
				throw new IOException("Error retrieving the first line.");
			}
			
			String firstLine = lineIterator.next();
			ITableHeaderBlock headerBlock = generateHeaderBlock(firstLine, separators, tableId, blockSize);
			
			Collection<IColumnDescriptor> columnsDescriptors = headerBlock.getColumnsDescriptors();
			if(columnsDescriptors == null || columnsDescriptors.isEmpty()){
				throw new IOException("Null columns descriptors");
			}
			
			List<IRowEntry> rowsEntries = new LinkedList<>();
			String line = null;
			while(lineIterator.hasNext()){
				line = lineIterator.next();
				IRowEntry rowEntry = parseRowEntry(line, separators, columnsDescriptors);
				if(rowEntry != null){
					rowsEntries.add(rowEntry);
				}
			}
			
			List<ITableDataBlock> dataBlocks = generateDataBlocks(headerBlock, rowsEntries);
			int nextFreeBlockId = dataBlocks.size() + 1;
			headerBlock.getHeaderContent().setNextFreeBlockId(nextFreeBlockId);
			
			ITable table = new Table(headerBlock, dataBlocks);
			return table;
			
		} catch (Exception e) {
			throw new IOException(e);
		} finally {
			if(lineIterator != null){
				LineIterator.closeQuietly(lineIterator);
			}
		}
	}

	private void validate(File f, Charset charset, ITextSeparators separators) throws IOException {
		FileUtils.validateInstance(f, false, false);
		
		if(separators == null){
			throw new IllegalArgumentException("Separators must be not null");
		}
		
		if(charset == null){
			throw new IllegalArgumentException("Charset must be not null");
		}
	}
	
	//XXX HEADER BLOCK METHODS
	private ITableHeaderBlock generateHeaderBlock(String firstLine, ITextSeparators separators, Byte tableId, IThreeByteValue blockSize) throws IOException {
		Collection<IColumnDescriptor> columnsDescriptors = parseTableMetadata(firstLine, separators);
		ITableHeaderBlockContent headerContent = generateHeaderContent(tableId, blockSize, columnsDescriptors);
		
		ITableHeaderBlock headerBlock = new TableHeaderBlock(headerContent, columnsDescriptors);
		return headerBlock;
	}

	private Collection<IColumnDescriptor> parseTableMetadata(String firstLine, ITextSeparators separators) throws IOException {
		String[] columnSplit = doColumnsSplit(separators, firstLine);
		Collection<IColumnDescriptor> columnDescriptors = parseColumnsDescriptors(separators, columnSplit);
		return columnDescriptors;
	}
	
	private Collection<IColumnDescriptor> parseColumnsDescriptors(ITextSeparators separators, String[] columnsArray) throws IOException{
		Collection<IColumnDescriptor> columnsDescriptors = new ArrayList<>();
		
		for (String columnStr : columnsArray) {
			String[] nameMetadataSplit = doNameMetadataSplit(separators, columnStr);
			String name = nameMetadataSplit[0];
			
			String typeSizeStr = nameMetadataSplit[1];
			String[] typeSizeSplit = doTypeSizeSplit(separators, typeSizeStr);
			
			String typeStr = typeSizeSplit[0];
			ColumnsTypes type = ColumnsTypes.getBySigla(typeStr);
			
			if(type == null){
				throw new IOException("Unknow column type : " + typeStr);
			}
			
			String sizeStr = typeSizeSplit[1];
			Short size = Short.valueOf(sizeStr);
			
			IColumnDescriptor columnDescriptor = new ColumnDescriptor(name, type, size);
			columnsDescriptors.add(columnDescriptor);
		}
		
		return columnsDescriptors;
	}
	
	private ITableHeaderBlockContent generateHeaderContent(Byte tableId, IThreeByteValue blockSize, Collection<IColumnDescriptor> columnsDescriptors) {
		Integer headerSizeInt = IColumnDescriptor.BYTES_SIZE * columnsDescriptors.size();
		Short headerSize = headerSizeInt.shortValue();
		Integer nextFreeBlockId = new Integer(1);
		
		ITableHeaderBlockContent tableHeaderBlockContent = new TableHeaderBlockContent(
			tableId, blockSize, headerSize, nextFreeBlockId, TableStatus.VALID
		);
		
		return tableHeaderBlockContent;
	}
	
	//SPLITS METHODS
	
	private String[] doColumnsSplit(ITextSeparators separators, String firstLine) throws IOException {
		String separator = separators.getColumnsSeparator();
		
		if(!firstLine.contains(separator)){
			throw new IOException("Invalid first line : not contain columns separator.");
		}
		
		if(firstLine.startsWith(separator)){
			firstLine = firstLine.substring(1, firstLine.length());
		}
		
		if(firstLine.endsWith(separator)){
			firstLine = firstLine.substring(0, firstLine.length() - 1);
		}
		
		String regexColumnsSeparator = StringUtils.getRegexPattern(separator);
		String[] columnsSplit = firstLine.split(regexColumnsSeparator);
		
		for (String columnStr : columnsSplit) {
			if(columnStr.startsWith(separators.getNameMetadataSeparator())){
				columnStr = columnStr.substring(1);
			}
			
			if(columnStr.endsWith(separators.getNameMetadataSeparator())){
				columnStr = columnStr.substring(0, columnStr.length() - 1);
			}
		}
		
		return columnsSplit;
	}

	private String[] doNameMetadataSplit(ITextSeparators separators, String columnStr) throws IOException {
		
		String separator = separators.getNameMetadataSeparator();
		
		if(!columnStr.contains(separator)){
			throw new IOException("Invalid first line : not contain name metadata separator.");
		}
		
		if(columnStr.startsWith(separator)){
			columnStr = columnStr.substring(1, columnStr.length());
		}
		
		if(columnStr.endsWith(separator)){
			columnStr = columnStr.substring(0, columnStr.length() - 1);
		}
		
		String nameMetadataRegexPattern = StringUtils.getRegexPattern(separator);
		String[] nameMetadataSplit = columnStr.split(nameMetadataRegexPattern);
		
		for (String nameMetadataStr : nameMetadataSplit) {
			if(nameMetadataStr.startsWith(separators.getTypeSizeSeparator())){
				nameMetadataStr = nameMetadataStr.substring(1);
			}
			
			if(nameMetadataStr.endsWith(separators.getTypeSizeSeparator())){
				nameMetadataStr = nameMetadataStr.substring(0, nameMetadataStr.length() - 1);
			}
		}
		
		return nameMetadataSplit;
	}
	
	private String[] doTypeSizeSplit(ITextSeparators separators, String typeSizeStr) throws IOException {
		
		String separator = separators.getTypeSizeSeparator();
		
		if(!typeSizeStr.contains(separator)){
			throw new IOException("Invalid first line : not contain type size separator.");
		}
		
		if(typeSizeStr.startsWith(separator)){
			typeSizeStr = typeSizeStr.substring(1, typeSizeStr.length());
		}
		
		if(typeSizeStr.endsWith(separator)){
			typeSizeStr = typeSizeStr.substring(0, typeSizeStr.length() - 1);
		}
		
		String typeSizeRegexPattern = StringUtils.getRegexPattern(separator);
		String[] typeSizeSplit = typeSizeStr.split(typeSizeRegexPattern);
		
		return typeSizeSplit;
	}
	
	//XXX ROW ENTRY METHODS
	private IRowEntry parseRowEntry(String line, ITextSeparators separators, Collection<IColumnDescriptor> columnsDescriptors) throws IOException {
		if(line == null || line.isEmpty()){
			return null;
		}
		
		String separator = separators.getColumnsSeparator();
		
		if(line.startsWith(separator )){
			line = line.substring(1, line.length());
		}
		
		if(line.endsWith(separator)){
			line = line.substring(0, line.length() - 1);
		}
		
		String regexColumnsSeparator = StringUtils.getRegexPattern(separator);
		List<String> columnsSplit = Arrays.asList(line.split(regexColumnsSeparator));
		
		if(columnsSplit.size() != columnsDescriptors.size()){
			throw new IOException("Columns data must be of same size of columns descriptors.");
		}
		
		Iterator<String> columnsSplitIterator = columnsSplit.iterator();
		Iterator<IColumnDescriptor> columnsDescriptorsIterator = columnsDescriptors.iterator();
		
		Collection<IColumnEntry> columnsEntries = new LinkedList<>();
		for (int i = 0; i < columnsSplit.size(); i++) {
			IColumnDescriptor columnDescriptor = columnsDescriptorsIterator.next();
			String dataStr = columnsSplitIterator.next();
			
			byte[] dataBytes = Factory.getAsBytes(columnDescriptor, dataStr);
			IColumnEntry columnEntry = Factory.toColumnEntry(dataBytes);
			columnsEntries.add(columnEntry);
		}
		
		IRowEntry rowEntry = new RowEntry(columnsEntries);
		return rowEntry;
	}
		
	private List<ITableDataBlock> generateDataBlocks(ITableHeaderBlock headerBlock, List<IRowEntry> rowsEntries) throws IOException {
		if(headerBlock == null){
			throw new IOException("Null header block.");
		}
		
		if(rowsEntries == null || rowsEntries.isEmpty()){
			throw new IOException("Null row entries.");
		}
		
		Byte tableId = headerBlock.getHeaderContent().getTableId();
		Integer rowSize = headerBlock.getRowSize();
		Integer rowsPerBlock = headerBlock.getNumberOfRowsPerBlock();
		
		Integer blockIdCount = new Integer(0);
		List<List<IRowEntry>> partitions = ListUtils.partition(rowsEntries, rowsPerBlock);
		
		List<ITableDataBlock> dataBlocks = new LinkedList<>();
		for (List<IRowEntry> partition : partitions) {
			blockIdCount++;
			ITableDataBlock dataBlock = generateTableDataBlock(tableId, blockIdCount, rowSize, partition);
			dataBlocks.add(dataBlock);
		}
		
		return dataBlocks;
	}

	private ITableDataBlock generateTableDataBlock(Byte tableId, Integer blockId, Integer rowSize, List<IRowEntry> partition) {
		IThreeByteValue blockIdTbv = new BigEndianThreeBytesValue(blockId);
		TableBlockType blockType = TableBlockType.DATA;
		
		Integer bytesUsedInBlock = rowSize * partition.size();
		IThreeByteValue bytesUsedInBlockTbv = new BigEndianThreeBytesValue(bytesUsedInBlock );
		
		ITableDataBlockHeader header = new TableDataBlockHeader(tableId, blockIdTbv, blockType, bytesUsedInBlockTbv);
		ITableDataBlock tableDataBlock = new TableDataBlock(header , partition);
		return tableDataBlock;
	}
}
