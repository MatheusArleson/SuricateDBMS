package br.com.xavier.suricate.dbms.impl.table.data;

import br.com.xavier.suricate.dbms.abstractions.table.data.AbstractTableDataBlockTest;
import br.com.xavier.suricate.dbms.interfaces.table.data.ITableDataBlock;

public class TableDataBlockTest
		extends AbstractTableDataBlockTest {

	@Override
	protected ITableDataBlock getInstance() {
		return new TableDataBlock();
	}

}
