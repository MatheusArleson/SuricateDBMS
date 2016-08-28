package br.com.xavier.suricate.dbms.abstractions.dbms;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import br.com.xavier.suricate.dbms.Factory;
import br.com.xavier.suricate.dbms.enums.FileModes;
import br.com.xavier.suricate.dbms.impl.table.Table;
import br.com.xavier.suricate.dbms.interfaces.dbms.IDbms;
import br.com.xavier.suricate.dbms.interfaces.table.ITable;
import br.com.xavier.suricate.dbms.interfaces.table.access.IRowId;
import br.com.xavier.suricate.dbms.interfaces.table.data.IRowEntry;

public class AbstractDbms
		implements IDbms {

	private static final long serialVersionUID = 664946624331325685L;
	
	//XXX DEPENDENCIES

	//XXX WORKSPACE PROPERTIES
	private FilenameFilter fileNameFilter;
	private File workspaceFolder;
	private Collection<ITable> tables; 
	
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
		
		if(files != null && files.length != 0){
			processWorkspace(files);
		}
	}

	private void processWorkspace(File[] files) throws IOException {
		this.tables = new ArrayList<>();
		
		for (File file : files) {
			RandomAccessFile raf = new RandomAccessFile(file, FileModes.READ_WRITE.getMode());
			Factory.getTableHeaderBlockBytes(raf);
			Table table = new Table(raf);
			tables.add(table);
		}
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

	//XXX DELEGATE TABLE MANAGER METHODS
	@Override
	public Collection<ITable> getAllTables() {
		return new ArrayList<>(tables);
	}

	@Override
	public void createTable(ITable table) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeTable(ITable table) {
		// TODO Auto-generated method stub
		
	}
	
	//XXX ROW MANAGER METHODS
	@Override
	public Long getRowCount(ITable table) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<IRowEntry> getAllRows(ITable table) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createRow(IRowEntry rowEntry) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IRowEntry getRow(IRowId rowId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteRow(IRowId rowId) {
		// TODO Auto-generated method stub
		
	}

}
