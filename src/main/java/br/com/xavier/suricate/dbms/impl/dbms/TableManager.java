package br.com.xavier.suricate.dbms.impl.dbms;

import java.util.Collection;

import br.com.xavier.suricate.dbms.abstractions.dbms.AbstractTableManager;
import br.com.xavier.suricate.dbms.interfaces.table.ITable;

public class TableManager
		extends AbstractTableManager {

	private static final long serialVersionUID = 4864827273099488009L;
	
	public TableManager(Collection<ITable> tables) {
		super(tables);
	}

}
