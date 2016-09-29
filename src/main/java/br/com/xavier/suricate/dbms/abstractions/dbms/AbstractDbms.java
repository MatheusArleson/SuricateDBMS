package br.com.xavier.suricate.dbms.abstractions.dbms;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.io.FilenameUtils;

import br.com.xavier.suricate.dbms.impl.low.BigEndianThreeBytesValue;
import br.com.xavier.suricate.dbms.impl.services.BufferManager;
import br.com.xavier.suricate.dbms.impl.services.FileSystemManager;
import br.com.xavier.suricate.dbms.impl.table.access.RowId;
import br.com.xavier.suricate.dbms.interfaces.dbms.IDbms;
import br.com.xavier.suricate.dbms.interfaces.low.IThreeByteValue;
import br.com.xavier.suricate.dbms.interfaces.services.IBufferManager;
import br.com.xavier.suricate.dbms.interfaces.services.IFileNameFilter;
import br.com.xavier.suricate.dbms.interfaces.services.IFileSystemManager;
import br.com.xavier.suricate.dbms.interfaces.services.ITextSeparators;
import br.com.xavier.suricate.dbms.interfaces.table.ITable;
import br.com.xavier.suricate.dbms.interfaces.table.access.IRowId;
import br.com.xavier.suricate.dbms.interfaces.table.data.IRowEntry;
import br.com.xavier.suricate.dbms.interfaces.table.data.ITableDataBlock;
import br.com.xavier.suricate.dbms.interfaces.table.data.ITableDataBlockHeader;
import br.com.xavier.suricate.dbms.interfaces.table.header.IColumnDescriptor;
import br.com.xavier.suricate.dbms.interfaces.table.header.ITableHeaderBlockContent;

public abstract class AbstractDbms
		implements IDbms {

	private static final long serialVersionUID = 664946624331325685L;
	
	//XXX DEPENDENCIES

	//XXX WORKSPACE PROPERTIES
	private File workspaceFolder;
	private IFileNameFilter fileNameFilter;
	private int bufferDataBlockSlots;
	
	private IBufferManager bufferManager;
	private IFileSystemManager fileSystemManager;
	
	//XXX CONSTRUCTOR
	public AbstractDbms(File workspaceFolder, IFileNameFilter fileNameFilter, int bufferDataBlockSlots) throws IOException {
		this.workspaceFolder = Objects.requireNonNull(workspaceFolder, "Workspace folder must not be null.");
		this.fileNameFilter = Objects.requireNonNull(fileNameFilter, "File name filter must not be null.");
		this.bufferDataBlockSlots = bufferDataBlockSlots;
		
		initialize();
	}
	
	private void initialize() throws IOException {
		this.fileSystemManager = new FileSystemManager(workspaceFolder, fileNameFilter);
		this.bufferManager = new BufferManager(fileSystemManager, bufferDataBlockSlots);
	}
	
	//XXX OVERRIDE METHODS
	@Override
	public File getWorkspace() throws IOException {
		return workspaceFolder;
	}

	@Override
	public void setWorkspace(File workspaceFolder) throws IOException {
		this.workspaceFolder = workspaceFolder;
		initialize();
	}

	@Override
	public String getBufferStatistics() {
		return bufferManager.getStatistics();
	}
	
	@Override
	public void shutdown() {
		try {
			bufferManager.shutdown();
			fileSystemManager.shutdown();
		}catch (Exception e) {
			//TODO FIXME log exception...
		}
	}
	
	//XXX TABLE MANAGER METHODS
	@Override
	public Collection<ITable> getAllTables() {
		return fileSystemManager.getAllTables();
	}
	
	@Override
	public void removeTable(ITable table) throws IOException {
		fileSystemManager.removeTable(table);
		bufferManager.purge(table);
	}

	@Override
	public ITable importTableFile(File file, Charset charset, ITextSeparators separators, IThreeByteValue blockSize) throws IOException {
		return fileSystemManager.importFile(file, charset, separators, blockSize);
	}
	
	@Override
	public String dumpAllTablesData(ITextSeparators separators) throws IOException {
		Collection<ITable> allTables = getAllTables();
		if(allTables == null || allTables.isEmpty()){
			return "";
		}
		
		StringBuffer sb = new StringBuffer();
		for (ITable table : allTables) {
			String tableData = printData(table, separators);
			sb.append(tableData);
			sb.append(separators.getEndLineSeparator());
		}
		
		return sb.toString();
	}
	
	public Collection<ITableDataBlock> getAllDataBlocks(ITable table) throws IOException {
		ITableHeaderBlockContent headerContent = table.getHeaderBlock().getHeaderContent();
		Integer nextFreeBlockId = headerContent.getNextFreeBlockId();
		if(nextFreeBlockId.equals(0)){
			return Collections.emptyList();
		}
		
		List<Integer> blockIds = IntStream.range(1, nextFreeBlockId).boxed().collect(Collectors.toList());
		
		Byte tableId = headerContent.getTableId();
		Long byteOffset = IRowId.FULL_BYTE_OFFSET;
		
		Collection<ITableDataBlock> dataBlocks = new LinkedList<>();
		for (Integer id : blockIds) {
			IThreeByteValue blockId = new BigEndianThreeBytesValue(id);
			IRowId rowId = new RowId(tableId, blockId, byteOffset);
			
			ITableDataBlock dataBlock = bufferManager.getDataBlock(rowId);
			dataBlocks.add(dataBlock);
		}
		
		return dataBlocks;
	}
	
	//XXX ROW MANAGER METHODS
	@Override
	public Long getRowCount(ITable table) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<IRowEntry> getAllRows(ITable table) throws IOException {
		if(table == null){
			return null;
		}
		
		Collection<ITableDataBlock> dataBlocks = getAllDataBlocks(table);
		
		Collection<IRowEntry> rowsBuffer = new LinkedList<>();
		for (ITableDataBlock dataBlock : dataBlocks) {
			Collection<IRowEntry> rows = dataBlock.getRows();
			rowsBuffer.addAll(rows);
		}
		
		return rowsBuffer;
	}
	
	@Override
	public Collection<IRowId> getRowIds(ITable table) throws IOException {
		if(table == null){
			return null;
		}
		
		ITableHeaderBlockContent headerContent = table.getHeaderBlock().getHeaderContent();
		Byte tableId = headerContent.getTableId();
		Integer blockSize = headerContent.getBlockSize().getValue();
		
		Collection<IRowId> rowIds = new LinkedList<>();
		Collection<ITableDataBlock> dataBlocks = getAllDataBlocks(table);
		for (ITableDataBlock dataBlock : dataBlocks) {
			IThreeByteValue blockId = dataBlock.getHeader().getBlockId();
			
			Collection<IRowEntry> rows = dataBlock.getRows();
			Long offset = new Long(blockSize + ITableDataBlockHeader.BYTES_SIZE);
			for (IRowEntry row : rows) {
				IRowId rowId = new RowId(tableId, blockId, offset);
				rowIds.add(rowId);
				
				Integer rowSize = row.getColumnsEntrySize();
				offset = offset + rowSize;
			}
		}
		
		return rowIds;
	}
	
	@Override
	public ITableDataBlock getDataBlock(IRowId rowId) throws IOException {
		IRowId.validate(rowId);
		return bufferManager.getDataBlock(rowId);
	}
	
	@Override
	public IRowEntry getRow(IRowId rowId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createRow(IRowEntry rowEntry) {
		// TODO Auto-generated method stub
		
	}

	
	@Override
	public void deleteRow(IRowId rowId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String printData(ITable table, ITextSeparators separators) throws IOException {
		if(table == null){
			throw new IOException("Null table.");
		}
		
		if(separators == null){
			throw new IOException("Null text separators.");
		}
		
		StringBuffer sb = new StringBuffer();
		
		String tableName = FilenameUtils.getBaseName( table.getFile().getName() );
		sb.append(tableName);
		sb.append(separators.getEndLineSeparator());
		
		Collection<IColumnDescriptor> columnsDescriptors = table.getHeaderBlock().getColumnsDescriptors();
		if(columnsDescriptors == null || columnsDescriptors.isEmpty()){
			throw new IOException("Invalid columns descriptors");
		}
		
		columnsDescriptors.forEach(cd -> {
			String descriptorStr = cd.printData(separators);
			sb.append(descriptorStr);
		});
		
		sb.append(separators.getEndLineSeparator());
		
		Collection<IRowEntry> allRows = getAllRows(table);
		allRows.forEach(r -> {
			String rowData = r.printData(columnsDescriptors, separators);
			sb.append(rowData);
		});
		
		return sb.toString();
	}
}
