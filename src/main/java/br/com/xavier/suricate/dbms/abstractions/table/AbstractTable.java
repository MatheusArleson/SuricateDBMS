package br.com.xavier.suricate.dbms.abstractions.table;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.io.FilenameUtils;

import br.com.xavier.suricate.dbms.Factory;
import br.com.xavier.suricate.dbms.impl.table.header.TableHeaderBlock;
import br.com.xavier.suricate.dbms.interfaces.table.ITable;
import br.com.xavier.suricate.dbms.interfaces.table.data.ITableDataBlock;
import br.com.xavier.suricate.dbms.interfaces.table.header.ITableHeaderBlock;
import br.com.xavier.util.FileUtils;
import br.com.xavier.util.ObjectsUtils;
import br.com.xavier.util.StringUtils;

public class AbstractTable 
		implements ITable {

	private static final long serialVersionUID = 4883170740788262539L;

	//XXX PROPERTIES
	private File file;
	private boolean lazyLoadDataBlocks;
	
	private ITableHeaderBlock headerBlock;
	private Collection<ITableDataBlock> dataBlocks;
	
	//XXX CONSTRUCTORS
	public AbstractTable(byte[] bytes) throws IOException {
		fromByteArray(bytes);
	}
	
	public AbstractTable(File file) throws IOException {
		this(file, true);
	}
	
	public AbstractTable(File file, boolean lazyLoadDataBlocks) throws IOException {
		super();
		setFile(file);
		setLazyLoadDataBlocks(lazyLoadDataBlocks);
		
		initialize();
	}
	
	private void initialize() throws IOException {
		initializeHeaderBlock();
		initializeDataBlocks();
	}
	
	private void initializeHeaderBlock() throws IOException {
		byte[] headerBytes = Factory.getTableHeaderBlockBytes(getFile());
		ITableHeaderBlock headerBlock = new TableHeaderBlock(headerBytes);
		setHeaderBlock(headerBlock);
	}

	private void initializeDataBlocks() {
		this.dataBlocks = new ArrayList<>();
		
		if(lazyLoadDataBlocks){
			return;
		} else {
			
		}
	}

	//XXX OVERRIDE METHODS
	

	//XXX GETTERS/SETTERS
	@Override
	public File getFile() {
		return file;
	}
	
	@Override
	public void setFile(File file) throws IOException {
		try {
			FileUtils.validateInstance(file, false, true);
			
			String extension = FilenameUtils.getExtension(file.getName());
			if(StringUtils.isNullOrEmpty(extension)){
				throw new IllegalArgumentException("Invalid file extension : " + extension);
			}
			
			this.file = file;
			
			byte[] bytes = Factory.getTableHeaderBlockBytes(file);
			this.headerBlock = new TableHeaderBlock(bytes);
			
		} catch(IOException e){
			throw e;
		} catch(Exception e){
			throw new IOException(e);
		}
	}
	
	public void setLazyLoadDataBlocks(boolean lazyLoadDataBlocks) {
		this.lazyLoadDataBlocks = lazyLoadDataBlocks;
	}
	
	public boolean isLazyLoadDataBlocks() {
		return lazyLoadDataBlocks;
	}

	@Override
	public ITableHeaderBlock getHeaderBlock() {
		return headerBlock;
	}

	@Override
	public void setHeaderBlock(ITableHeaderBlock headerBlock) {
		if(headerBlock == null){
			throw new IllegalArgumentException("Header block instance must not be null.");
		}
		
		this.headerBlock = headerBlock;
	}

	@Override
	public Collection<ITableDataBlock> getDataBlocks() {
		return new ArrayList<>(dataBlocks);
	}

	@Override
	public void setDataBlocks(Collection<ITableDataBlock> dataBlocks) {
		if(dataBlocks == null){
			throw new IllegalArgumentException("Data blocks collection instance must not be null.");
		}
		
		if(dataBlocks.isEmpty()){
			throw new IllegalArgumentException("Data blocks collection must not be empty.");
		}
		
		boolean anyNull = ObjectsUtils.anyNull(dataBlocks.toArray());
		if(anyNull){
			throw new IllegalArgumentException("Data blocks collections must not have null values.");
		}
		
		if(this.dataBlocks == null){
			this.dataBlocks = new ArrayList<>();
		}
		
		this.dataBlocks.clear();
		this.dataBlocks.addAll(dataBlocks);
	}

}
