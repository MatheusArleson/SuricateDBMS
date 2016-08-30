package br.com.xavier.suricate.dbms.impl.table.data;

import br.com.xavier.suricate.dbms.abstractions.table.data.AbstractColumnEntryTest;
import br.com.xavier.suricate.dbms.interfaces.table.data.IColumnEntry;

public class ColumnEntryTest 
		extends AbstractColumnEntryTest {

	@Override
	protected IColumnEntry getInstance() {
		return new ColumnEntry();
	}

}
