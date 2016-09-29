package br.com.xavier.suricate.dbms.interfaces.services;

import java.io.IOException;

import br.com.xavier.suricate.dbms.interfaces.table.ITable;
import br.com.xavier.suricate.dbms.interfaces.table.access.IRowId;
import br.com.xavier.suricate.dbms.interfaces.table.data.ITableDataBlock;

public interface IBufferManager {
	
	ITableDataBlock getDataBlock(IRowId rowId) throws IOException;
	void purge(ITable table);
	void flush() throws IOException;
	void shutdown();
	String getStatistics();

}
