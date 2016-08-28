package br.com.xavier.suricate.dbms.abstractions.dbms;

import java.util.Collection;

import br.com.xavier.suricate.dbms.interfaces.dbms.IRowManager;
import br.com.xavier.suricate.dbms.interfaces.table.ITable;
import br.com.xavier.suricate.dbms.interfaces.table.access.IRowId;
import br.com.xavier.suricate.dbms.interfaces.table.data.IRowEntry;

public abstract class AbstractRowManager
		implements IRowManager {

	private static final long serialVersionUID = 4457373853667921204L;

	@Override
	public Long getRowCount(ITable table) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<IRowEntry> getAllRows(ITable table) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createRow(IRowEntry rowEntry) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IRowEntry getRow(IRowId rowId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteRow(IRowId rowId) {
		// TODO Auto-generated method stub
		
	}

}
