package br.com.xavier.suricate.dbms.abstractions.services;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import br.com.xavier.suricate.dbms.enums.FileModes;
import br.com.xavier.suricate.dbms.impl.services.FileParser;
import br.com.xavier.suricate.dbms.impl.table.Table;
import br.com.xavier.suricate.dbms.impl.table.data.TableDataBlock;
import br.com.xavier.suricate.dbms.interfaces.low.IThreeByteValue;
import br.com.xavier.suricate.dbms.interfaces.services.IFileNameFilter;
import br.com.xavier.suricate.dbms.interfaces.services.IFileParser;
import br.com.xavier.suricate.dbms.interfaces.services.IFileSystemManager;
import br.com.xavier.suricate.dbms.interfaces.services.ITextSeparators;
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
	private IFileNameFilter fileNameFilter;
	
	private Byte nextTableId = 1;
	private Map<Byte, ITable> workspaceMap;
	
	private IFileParser fileParser; 
	
	//XXX CONSTRUCTOR
	public AbstractFileSystemManager(File workspaceFolder, IFileNameFilter fileNameFilter) throws IOException {
		FileUtils.validateInstance(workspaceFolder, true, true);
		
		this.fileParser = new FileParser();
		this.fileNameFilter = fileNameFilter;
		this.workspaceFolder = workspaceFolder;
		this.workspaceMap = new LinkedHashMap<>();
		
		analyzeWorkspace();
	}
	
	private void analyzeWorkspace() throws IOException{
		File[] files = workspaceFolder.listFiles(fileNameFilter);
		
		if(files != null && files.length != 0){
			for (File file : files) {
				ITable table = new Table(file);
				addTableToWorkspaceMap(table); 
			}
		}
	}

	//XXX DATA BLOCK METHODS
	@Override
	public ITableDataBlock readDataBlock(IRowId rowId) throws IOException {
		IRowId.validate(rowId);
		
		Byte tableId = rowId.getTableId();
		ITable table = fetchTableFromWorkspace(tableId);
		if(table == null){
			throw new IOException("Table not exists for id: " + tableId);
		}
		
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
		if(table == null){
			throw new IOException("Table not exists for id: " + tableId);
		}
		
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
	
	//XXX TABLE METHODS
	@Override
	public Collection<ITable> getAllTables() {
		Collection<ITable> tables = new ArrayList<>(workspaceMap.values());
		return tables;
	}
	
	@Override
	public void removeTable(ITable table) throws IOException {
		if(table == null){
			return;
		}
		
		ITableHeaderBlock headerBlock = table.getHeaderBlock();
		ITableHeaderBlockContent headerContent = headerBlock.getHeaderContent();
		Byte tableId = headerContent.getTableId();
		
		ITable realTable = fetchTableFromWorkspace(tableId);
		if(realTable == null){
			return;
		}
		
		workspaceMap.remove(tableId);
		
	}
	
	@Override
	public ITable importFile(File file, Charset charset, ITextSeparators separators, IThreeByteValue blockSize) throws IOException {
		ITable presentTable = fetchTableByFile(file);
		if(presentTable != null){
			throw new IOException("Table already imported");
		}
		
		Byte tableId = fetchNextTableId();
		ITable newTable = fileParser.parse(file, charset, separators, tableId, blockSize);
		byte[] tableBytes = newTable.toByteArray();
		
		String newFileName = FilenameUtils.getBaseName(file.getName()) + fileNameFilter.getExtension();
		File newFile = new File(workspaceFolder, newFileName);
		
		createFile(newFile, tableBytes);
		
		newTable.setFile(newFile);
		addTableToWorkspaceMap(newTable);
		return newTable;
	}
	
	@Override
	public void createFile(File file, byte[] content) throws IOException {
		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile(file, FileModes.READ_WRITE_CONTENT_SYNC.getMode());
			raf.write(content);
		} catch (Exception e) {
			throw new IOException(e);
		} finally {
			if(raf != null){
				IOUtils.closeQuietly(raf);
			}
		}
	}

	@Override
	public void shutdown() {
		
	}
	
	//XXX PRIVATE METHODS
	private void addTableToWorkspaceMap(ITable table) throws IOException {
		ITableHeaderBlock headerBlock = table.getHeaderBlock();
		ITableHeaderBlockContent headerContent = headerBlock.getHeaderContent();
		Byte tableId = headerContent.getTableId();
		
		ITable previousTable = workspaceMap.put(tableId, table);
		if(previousTable != null){
			throw new IOException("Invalid workspace : Table ID must be unique : Id : " + tableId);
		}
		
		if(tableId >= nextTableId){
			nextTableId = (byte) (tableId + 1);
		}
	}
	
	private Byte fetchNextTableId(){
		Byte tableId = new Byte(nextTableId);
		nextTableId++;
		return tableId;
	}
	
	private ITable fetchTableFromWorkspace(Byte tableId) throws IOException {
		ITable table = workspaceMap.get(tableId);
		return table;
	}
	
	private File fetchTableFile(ITable table) {
		return table.getFile();
	}
	
	private ITable fetchTableByFile(File file) {
		return workspaceMap.entrySet()
				.parallelStream()
				.filter( e -> FilenameUtils.getBaseName( e.getValue().getFile().getName() ).equals( FilenameUtils.getBaseName(file.getName()) ) )
				.map(Map.Entry::getValue)
				.findFirst()
				.orElse(null);
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
