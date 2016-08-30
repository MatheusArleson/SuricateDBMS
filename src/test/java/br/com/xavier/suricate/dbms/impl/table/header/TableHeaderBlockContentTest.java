package br.com.xavier.suricate.dbms.impl.table.header;

import br.com.xavier.suricate.dbms.abstractions.table.header.AbstractTableHeaderBlockContentTest;
import br.com.xavier.suricate.dbms.interfaces.table.header.ITableHeaderBlockContent;

public class TableHeaderBlockContentTest
		extends AbstractTableHeaderBlockContentTest{

	@Override
	protected ITableHeaderBlockContent getInstance() {
		return new TableHeaderBlockContent();
	}

}
