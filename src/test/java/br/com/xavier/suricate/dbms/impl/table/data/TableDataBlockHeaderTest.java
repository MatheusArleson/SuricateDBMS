package br.com.xavier.suricate.dbms.impl.table.data;

import br.com.xavier.suricate.dbms.abstractions.table.data.AbstractTableDataBlockHeaderTest;
import br.com.xavier.suricate.dbms.interfaces.table.data.ITableDataBlockHeader;

public class TableDataBlockHeaderTest 
		extends AbstractTableDataBlockHeaderTest {

	@Override
	protected ITableDataBlockHeader getInstance() {
		return new TableDataBlockHeader();
	}

}
