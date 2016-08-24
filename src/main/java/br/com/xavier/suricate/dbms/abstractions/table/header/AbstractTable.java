package br.com.xavier.suricate.dbms.abstractions.table.header;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Collection;

import br.com.xavier.suricate.dbms.impl.Factory;
import br.com.xavier.suricate.dbms.interfaces.table.ITable;
import br.com.xavier.suricate.dbms.interfaces.table.data.ITableDataBlock;
import br.com.xavier.suricate.dbms.interfaces.table.header.ITableHeaderBlock;

public class AbstractTable 
		implements ITable {

	private static final long serialVersionUID = 4883170740788262539L;

	//XXX PROPERTIES
	private RandomAccessFile file;
	private ITableHeaderBlock headerBlock;
	private Collection<ITableDataBlock> dataBlocks;
	
	//XXX CONSTRUCTORS
	public AbstractTable(RandomAccessFile file) {
		super();
		this.file = file;
	}
	
	public AbstractTable(RandomAccessFile file, ITableHeaderBlock headerBlock) {
		this(file);
		this.headerBlock = headerBlock;
	}
	
	public AbstractTable(RandomAccessFile file, ITableHeaderBlock headerBlock, Collection<ITableDataBlock> dataBlocks) {
		this(file, headerBlock);
		this.dataBlocks = dataBlocks;
	}

	//XXX OVERRIDE METHODS
	@Override
	public byte[] toByteArray() throws IOException {
		byte[] headerBlockBytes = headerBlock.toByteArray();
		byte[] dataBlocksBytes = Factory.toByteArray(dataBlocks);
		
		byte[] byteArray = Factory.toByteArray(headerBlockBytes, dataBlocksBytes);
		return byteArray;
	}

	//XXX GETTERS/SETTERS
	@Override
	public RandomAccessFile getFile() {
		return file;
	}

	@Override
	public void setFile(RandomAccessFile file) {
		this.file = file;
	}

	@Override
	public ITableHeaderBlock getHeaderBlock() {
		return headerBlock;
	}

	@Override
	public void setHeaderBlock(ITableHeaderBlock headerBlock) {
		this.headerBlock = headerBlock;
	}

	@Override
	public Collection<ITableDataBlock> getDataBlocks() {
		return dataBlocks;
	}

	@Override
	public void setDataBlocks(Collection<ITableDataBlock> dataBlocks) {
		this.dataBlocks = dataBlocks;
	}

}
