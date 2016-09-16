package br.com.xavier.suricate.dbms.abstractions.table;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collection;

import br.com.xavier.suricate.dbms.Factory;
import br.com.xavier.suricate.dbms.enums.FileModes;
import br.com.xavier.suricate.dbms.impl.table.header.TableHeaderBlock;
import br.com.xavier.suricate.dbms.interfaces.table.ITable;
import br.com.xavier.suricate.dbms.interfaces.table.data.ITableDataBlock;
import br.com.xavier.suricate.dbms.interfaces.table.header.ITableHeaderBlock;
import br.com.xavier.util.FileUtils;

public class AbstractTable 
		implements ITable {

	private static final long serialVersionUID = 4883170740788262539L;

	//XXX PROPERTIES
	private RandomAccessFile file;
	
	private ITableHeaderBlock headerBlock;
	private Collection<ITableDataBlock> dataBlocks;
	
	//XXX CONSTRUCTORS
	public AbstractTable(File file) throws IOException {
		super();
		
		FileUtils.validateInstance(file, false, true);
		RandomAccessFile raf = new RandomAccessFile(file, FileModes.READ_WRITE_CONTENT_SYNC.getMode());
		
		this.file = raf;
		
		byte[] bytes = Factory.getTableHeaderBlockBytes(raf);
		this.headerBlock = new TableHeaderBlock(bytes);
		
		//XXX FIXME terminar construcao do metodo
		this.dataBlocks = new ArrayList<>();
	}
	
	public AbstractTable(byte[] bytes) throws IOException {
		fromByteArray(bytes);
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

	//XXX GETTERS/SETTERS
	@Override
	public RandomAccessFile getFile() {
		return file;
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
