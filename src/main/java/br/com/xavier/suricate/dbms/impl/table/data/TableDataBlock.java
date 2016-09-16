package br.com.xavier.suricate.dbms.impl.table.data;

import java.io.IOException;
import java.util.Collection;

import br.com.xavier.suricate.dbms.abstractions.table.data.AbstractTableDataBlock;
import br.com.xavier.suricate.dbms.interfaces.table.data.IRowEntry;
import br.com.xavier.suricate.dbms.interfaces.table.data.ITableDataBlockHeader;

public final class TableDataBlock 
		extends AbstractTableDataBlock {
	
	private static final long serialVersionUID = 7289131324732164038L;

	public TableDataBlock() {
		super();
	}
	
	public TableDataBlock(ITableDataBlockHeader header, Collection<IRowEntry> rows) {
		super(header, rows);
	}
	
	public TableDataBlock(byte[] bytes) throws IOException {
		super(bytes);
	}

}
