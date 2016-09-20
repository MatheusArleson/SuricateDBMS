package br.com.xavier.suricate.dbms.abstractions.services;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedHashMap;
import java.util.Map;

import br.com.xavier.suricate.dbms.impl.table.Table;
import br.com.xavier.suricate.dbms.interfaces.services.IFileSystemManager;
import br.com.xavier.suricate.dbms.interfaces.table.ITable;
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
	public RandomAccessFile createFile(String fileAbsolutePath) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RandomAccessFile createFile(String fileAbsolutePath, byte[] fileContent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] readBlock(RandomAccessFile file, Long blockId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void writeBlock(RandomAccessFile file, Long blockId, byte[] content) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteBlock(RandomAccessFile file, Long blockId) {
		// TODO Auto-generated method stub
		
	}

}
