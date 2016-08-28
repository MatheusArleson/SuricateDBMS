package br.com.xavier.suricate.dbms.interfaces.dbms;

import java.io.Serializable;
import java.util.Collection;

import br.com.xavier.suricate.dbms.interfaces.table.ITable;

public interface ITableManager 
		extends Serializable {
	
	Collection<ITable> getAllTables();
	void createTable(ITable table);
	void removeTable(ITable table);
	

}
