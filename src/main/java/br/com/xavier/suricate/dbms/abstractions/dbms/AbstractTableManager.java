package br.com.xavier.suricate.dbms.abstractions.dbms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import br.com.xavier.suricate.dbms.interfaces.dbms.ITableManager;
import br.com.xavier.suricate.dbms.interfaces.table.ITable;

public abstract class AbstractTableManager 
		implements ITableManager {

	private static final long serialVersionUID = -6675239047256411715L;
	
	private Collection<ITable> tables;
	
	public AbstractTableManager(Collection<ITable> tables) {
		this.tables = Objects.requireNonNull(tables, "Tables collection must not be null.");
	}
	
	@Override
	public Collection<ITable> getAllTables() {
		Collection<ITable> collection = new ArrayList<>(tables);
		return collection;
	}

	@Override
	public void createTable(ITable table) {
		// TODO Auto-generated method stub
		tables.add(table);
	}

	@Override
	public void removeTable(ITable table) {
		// TODO Auto-generated method stub
		tables.remove(table);
	}

}
