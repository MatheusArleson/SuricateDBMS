package br.com.xavier.suricate.dbms.abstractions.table;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collection;

import br.com.xavier.suricate.dbms.Factory;
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
		parseHeaderBlock();
	}

	public AbstractTable(RandomAccessFile file, ITableHeaderBlock headerBlock) {
		super();
		this.file = file;
		this.headerBlock = headerBlock;
		this.dataBlocks = new ArrayList<>();
	}
	
	public AbstractTable(RandomAccessFile file, ITableHeaderBlock headerBlock, Collection<ITableDataBlock> dataBlocks) {
		this(file, headerBlock);
		this.dataBlocks.addAll(dataBlocks);
	}
	
	//XXX CONSTRUCTOR METHODS
	private void parseHeaderBlock() {
		// TODO Auto-generated method stub
	}

	//XXX OVERRIDE METHODS
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((headerBlock == null) ? 0 : headerBlock.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractTable other = (AbstractTable) obj;
		if (headerBlock == null) {
			if (other.headerBlock != null)
				return false;
		} else if (!headerBlock.equals(other.headerBlock))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "AbstractTable [" 
			+ "file=" + file 
			+ ", headerBlock=" + headerBlock 
		+ "]";
	}

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
