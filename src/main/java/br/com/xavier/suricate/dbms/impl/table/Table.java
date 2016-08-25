package br.com.xavier.suricate.dbms.impl.table;

import java.io.RandomAccessFile;
import java.util.Collection;

import br.com.xavier.suricate.dbms.abstractions.table.AbstractTable;
import br.com.xavier.suricate.dbms.interfaces.table.data.ITableDataBlock;
import br.com.xavier.suricate.dbms.interfaces.table.header.ITableHeaderBlock;

public final class Table
		extends AbstractTable {

	private static final long serialVersionUID = 4864024392240777959L;

	public Table(RandomAccessFile file) {
		super(file);
	}
	
	public Table(RandomAccessFile file, ITableHeaderBlock headerBlock) {
		super(file, headerBlock);
	}
	
	public Table(RandomAccessFile file, ITableHeaderBlock headerBlock, Collection<ITableDataBlock> dataBlocks) {
		super(file, headerBlock, dataBlocks);
	}

}
