package br.com.xavier.suricate.dbms.interfaces.table.data;

import java.util.Collection;

import br.com.xavier.suricate.dbms.interfaces.IBinarizable;

public interface IRowEntry 
		extends IBinarizable<IRowEntry> {
	
	Integer getSize();
	void setSize(Integer size);
	Collection<IColumnEntry> getColumnsEntries();
	void setColumnsEntries(Collection<IColumnEntry> columnsEntries);

}
