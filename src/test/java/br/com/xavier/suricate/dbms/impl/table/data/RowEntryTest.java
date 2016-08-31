package br.com.xavier.suricate.dbms.impl.table.data;

import br.com.xavier.suricate.dbms.abstractions.table.data.AbstractRowEntryTest;
import br.com.xavier.suricate.dbms.interfaces.table.data.IRowEntry;

public class RowEntryTest
		extends AbstractRowEntryTest {

	@Override
	protected IRowEntry getInstance() {
		return new RowEntry();
	}

}
