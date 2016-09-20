package br.com.xavier.suricate.dbms.abstractions.dbms;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import br.com.xavier.suricate.dbms.impl.services.BufferManager;
import br.com.xavier.suricate.dbms.impl.services.FileSystemManager;
import br.com.xavier.suricate.dbms.interfaces.dbms.IDbms;
import br.com.xavier.suricate.dbms.interfaces.services.IBufferManager;
import br.com.xavier.suricate.dbms.interfaces.services.IFileSystemManager;
import br.com.xavier.suricate.dbms.interfaces.table.ITable;
import br.com.xavier.suricate.dbms.interfaces.table.access.IRowId;
import br.com.xavier.suricate.dbms.interfaces.table.data.IRowEntry;

public class AbstractDbms
		implements IDbms {

	private static final long serialVersionUID = 664946624331325685L;
	
	//XXX DEPENDENCIES

	//XXX WORKSPACE PROPERTIES
	private File workspaceFolder;
	private FilenameFilter fileNameFilter;
	private int bufferDataBlockSlots;
	
	private IBufferManager bufferManager;
	private IFileSystemManager fileSystemManager;
	
	//XXX CONSTRUCTOR
	public AbstractDbms(File workspaceFolder, FilenameFilter fileNameFilter, int bufferDataBlockSlots) throws IOException {
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

	//XXX DELEGATE TABLE MANAGER METHODS
	@Override
	public Collection<ITable> getAllTables() {
		return new ArrayList<>();
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
