package br.com.xavier.suricate.dbms.impl.table;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import br.com.xavier.suricate.dbms.abstractions.table.AbstractTable;
import br.com.xavier.suricate.dbms.interfaces.table.data.ITableDataBlock;
import br.com.xavier.suricate.dbms.interfaces.table.header.ITableHeaderBlock;

public final class Table
		extends AbstractTable {

	private static final long serialVersionUID = 4864024392240777959L;
	
	public Table(ITableHeaderBlock headerBlock, Collection<ITableDataBlock> dataBlocks) throws IOException {
		super(headerBlock, dataBlocks);
	}
	
	public Table(File file) throws IOException {
		super(file);
	}
	
	public Table(File file, boolean lazyLoadDataBlocks) throws IOException {
		super(file, lazyLoadDataBlocks);
	}
	
}
