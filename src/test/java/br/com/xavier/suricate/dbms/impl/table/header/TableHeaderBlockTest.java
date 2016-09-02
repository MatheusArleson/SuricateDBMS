package br.com.xavier.suricate.dbms.impl.table.header;

import br.com.xavier.suricate.dbms.abstractions.table.header.AbstractTableHeaderBlockTest;
import br.com.xavier.suricate.dbms.interfaces.table.header.ITableHeaderBlock;

public class TableHeaderBlockTest 
		extends AbstractTableHeaderBlockTest {

	@Override
	protected ITableHeaderBlock getInstance() {
		return new TableHeaderBlock();
	}

}
