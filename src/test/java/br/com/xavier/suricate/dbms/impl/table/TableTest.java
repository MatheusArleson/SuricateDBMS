package br.com.xavier.suricate.dbms.impl.table;

import java.io.File;
import java.io.IOException;

import br.com.xavier.suricate.dbms.abstractions.table.AbstractTableTest;
import br.com.xavier.suricate.dbms.interfaces.table.ITable;

public class TableTest 
		extends AbstractTableTest {

	@Override
	protected ITable getInstance() throws IOException {
		File f = new File("testTable.suricata");
		return new Table(f);
	}

}
