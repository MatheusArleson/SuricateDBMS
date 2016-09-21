package br.com.xavier.suricate.dbms.abstractions.services;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import br.com.xavier.suricate.dbms.enums.FileModes;
import br.com.xavier.suricate.dbms.impl.table.Table;
import br.com.xavier.suricate.dbms.impl.table.data.TableDataBlock;
import br.com.xavier.suricate.dbms.interfaces.low.IThreeByteValue;
import br.com.xavier.suricate.dbms.interfaces.services.IFileSystemManager;
import br.com.xavier.suricate.dbms.interfaces.table.ITable;
import br.com.xavier.suricate.dbms.interfaces.table.access.IRowId;
import br.com.xavier.suricate.dbms.interfaces.table.data.ITableDataBlock;
import br.com.xavier.suricate.dbms.interfaces.table.header.ITableHeaderBlock;
import br.com.xavier.suricate.dbms.interfaces.table.header.ITableHeaderBlockContent;
import br.com.xavier.util.FileUtils;

public abstract class AbstractFileSystemManager
			implements IFileSystemManager {

	private static final long serialVersionUID = 7345768551099818984L;

	//XXX PROPERTIES
	private File workspaceFolder;
	private FilenameFilter fileNameFilter;
	private Map<Byte, ITable> workspaceMap;
	
	//XXX CONSTRUCTOR
	public AbstractFileSystemManager(File workspaceFolder, FilenameFilter fileNameFilter) throws IOException {
		FileUtils.validateInstance(workspaceFolder, true, true);
		
		this.workspaceFolder = workspaceFolder;
		this.workspaceMap = new LinkedHashMap<>();
		
		analyzeWorkspace();
	}
	
	private void analyzeWorkspace() throws IOException{
		File[] files = workspaceFolder.listFiles(fileNameFilter);
		
		if(files != null && files.length != 0){
			for (File file : files) {
				ITable table = new Table(file);
				
				ITableHeaderBlock headerBlock = table.getHeaderBlock();
				ITableHeaderBlockContent headerContent = headerBlock.getHeaderContent();
				Byte tableId = headerContent.getTableId();
				
				workspaceMap.put(tableId, table);
			}
		}
	}

	@Override
	public ITableDataBlock readDataBlock(IRowId rowId) throws IOException {
		IRowId.validate(rowId);
		
		Byte tableId = rowId.getTableId();
		ITable table = fetchTableFromWorkspace(tableId);
		
		Integer blockId = rowId.getBlockId().getValue();
		validateBlockId(table, blockId);
		
		Integer blockSize = fetchBlockSize(table);
		long startPosition = blockSize * blockId;
		
		byte[] dataBlockBytes = new byte[blockSize];
		File tableFile = fetchTableFile(table);		
		
		RandomAccessFile raf = null;		
		try {
			raf = new RandomAccessFile(tableFile, FileModes.READ_ONLY.getMode());
			
			raf.seek(startPosition);
			raf.readFully(dataBlockBytes);
			
			ITableDataBlock dataBlock = new TableDataBlock(dataBlockBytes);
			return dataBlock;
			
		} catch (IOException e) {
			throw e;
		} finally {
			if(raf != null){
				IOUtils.closeQuietly(raf);
			}
		}
	}

	@Override
	public void writeDataBlock(ITableDataBlock dataBlock) throws IOException {
		Byte tableId = dataBlock.getHeader().getTableId();
		ITable table = fetchTableFromWorkspace(tableId);
		
		Integer blockId = dataBlock.getHeader().getBlockId().getValue();
		validateBlockId(table, blockId);
		
		byte[] dataBlockBytes = dataBlock.toByteArray();
		File tableFile = fetchTableFile(table);
		
		Integer blockSize = fetchBlockSize(table);
		long startPosition = blockSize  * blockId;
		
		RandomAccessFile raf = null;		
		try {
			raf = new RandomAccessFile(tableFile, FileModes.READ_WRITE_CONTENT_SYNC.getMode());
			
			raf.seek(startPosition);
			raf.write(dataBlockBytes);
			
		} catch (IOException e) {
			throw e;
		} finally {
			if(raf != null){
				IOUtils.closeQuietly(raf);
			}
		}
	}

	@Override
	public void deleteDataBlock(ITableDataBlock dataBlock) {
		// TODO Auto-generated method stub
		
	}
	
	//XXX PRIVATE METHODS
	private ITable fetchTableFromWorkspace(Byte tableId) throws IOException {
		ITable table = workspaceMap.get(tableId);
		if(table == null){
			throw new IOException("Table not exists for id: " + tableId);
		}
		return table;
	}
	
	private File fetchTableFile(ITable table) {
		return table.getFile();
	}
	
	private Integer fetchBlockSize(ITable table){
		return table.getHeaderBlock().getHeaderContent().getBlockSize().getValue();
	}
	
	private Integer fetchNextFreeBlockId(ITable table){
		return table.getHeaderBlock().getHeaderContent().getNextFreeBlockId();
	}
	
	private void validateBlockId(ITable table, Integer blockId) throws IOException{
		Integer nextFreeBlockId = fetchNextFreeBlockId(table);
		if(blockId > nextFreeBlockId){
			throw new IOException("Block not exists for id: " + blockId);
		}
	}

}
