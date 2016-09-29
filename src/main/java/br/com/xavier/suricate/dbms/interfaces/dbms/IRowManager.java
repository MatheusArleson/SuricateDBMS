package br.com.xavier.suricate.dbms.interfaces.dbms;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;

import br.com.xavier.suricate.dbms.interfaces.table.ITable;
import br.com.xavier.suricate.dbms.interfaces.table.access.IRowId;
import br.com.xavier.suricate.dbms.interfaces.table.data.IRowEntry;

public interface IRowManager 
		extends Serializable {
	
	Long getRowCount(ITable table);
	Collection<IRowEntry> getAllRows(ITable table) throws IOException;
	void createRow(IRowEntry rowEntry);
	IRowEntry getRow(IRowId rowId);
	void deleteRow(IRowId rowId);
	
	Collection<IRowId> getRowIds(ITable table) throws IOException;
	
}
