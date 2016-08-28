package br.com.xavier.suricate.dbms.abstractions.dbms;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import br.com.xavier.suricate.dbms.enums.FileModes;
import br.com.xavier.suricate.dbms.impl.dbms.RowManager;
import br.com.xavier.suricate.dbms.impl.dbms.TableManager;
import br.com.xavier.suricate.dbms.impl.table.Table;
import br.com.xavier.suricate.dbms.interfaces.dbms.IDbms;
import br.com.xavier.suricate.dbms.interfaces.dbms.IRowManager;
import br.com.xavier.suricate.dbms.interfaces.dbms.ITableManager;
import br.com.xavier.suricate.dbms.interfaces.table.ITable;
import br.com.xavier.suricate.dbms.interfaces.table.access.IRowId;
import br.com.xavier.suricate.dbms.interfaces.table.data.IRowEntry;

public class AbstractDbms
		implements IDbms {

	private static final long serialVersionUID = 664946624331325685L;
	
	//XXX DEPENDENCIES
	private ITableManager tableManager;
	private IRowManager rowManager; 

	//XXX WORKSPACE PROPERTIES
	private FilenameFilter fileNameFilter;
	private File workspaceFolder;
	
	//XXX CONSTRUCTOR
	public AbstractDbms(FilenameFilter fileNameFilter, File workspaceFolder) throws IOException {
		this.fileNameFilter = Objects.requireNonNull(fileNameFilter, "File name filter must not be null.");
		this.workspaceFolder = Objects.requireNonNull(workspaceFolder, "Workspace folder must not be null.");
		
		initialize();
	}
	
	private void initialize() throws IOException {
		checkWorkspaceFolder();
		analyzeWorkspace();
	}
	
	private void checkWorkspaceFolder() throws IOException {
		String absolutePath = workspaceFolder.getAbsolutePath();
		if(!workspaceFolder.isDirectory()){
			throw new IllegalArgumentException("Workspace must be a folder : " + absolutePath);
		}
		
		if(!workspaceFolder.canRead()){
			throw new IOException("Permission denied to read on Workspace folder : " + absolutePath);
		}
		
		if(!workspaceFolder.canWrite()){
			throw new IOException("Permission denied to write on Workspace folder : " + absolutePath);
		}
	}
	
	private void analyzeWorkspace() throws IOException{
		File[] files = workspaceFolder.listFiles(fileNameFilter);
		
		if(files == null || files.length == 0){
			processEmptyWorkspace();
		} else {
			processWorkspace(files);
		}
		
	}
	
	private void processEmptyWorkspace() {
		this.tableManager = new TableManager(new ArrayList<>());
		this.rowManager = new RowManager();
	}

	private void processWorkspace(File[] files) throws IOException {
		for (File file : files) {
			RandomAccessFile raf = new RandomAccessFile(file, FileModes.READ_WRITE.getMode());
			Table table = new Table(raf);
		}
	}
	
	//XXX OVERRIDE METHODS
	@Override
	public File getWorkspace() {
		return workspaceFolder;
	}

	@Override
	public void setWorkspace(File workspaceFolder) {
		this.workspaceFolder = workspaceFolder;
	}
	
	//XXX DELEGATE TABLE MANAGER METHODS
	@Override
	public Collection<ITable> getAllTables() {
		return tableManager.getAllTables();
	}

	@Override
	public void createTable(ITable table) {
		tableManager.createTable(table);
	}

	@Override
	public void removeTable(ITable table) {
		tableManager.removeTable(table);
	}
	
	//XXX DELEGATE ROW MANAGER METHODS
	@Override
	public Long getRowCount(ITable table) {
		return rowManager.getRowCount(table);
	}

	@Override
	public Collection<IRowEntry> getAllRows(ITable table) {
		return rowManager.getAllRows(table);
	}

	@Override
	public void createRow(IRowEntry rowEntry) {
		rowManager.createRow(rowEntry);
	}

	@Override
	public IRowEntry getRow(IRowId rowId) {
		return rowManager.getRow(rowId);
	}

	@Override
	public void deleteRow(IRowId rowId) {
		rowManager.deleteRow(rowId);
	}
}
