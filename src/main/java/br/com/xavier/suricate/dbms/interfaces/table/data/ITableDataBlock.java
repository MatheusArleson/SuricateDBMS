package br.com.xavier.suricate.dbms.interfaces.table.data;

import java.util.Collection;

import br.com.xavier.suricate.dbms.interfaces.low.IBinarizable;

public interface ITableDataBlock
		extends IBinarizable {
	
	ITableDataBlockHeader getHeader();
	void getHeader(ITableDataBlockHeader header);
	Collection<IRowEntry> getRows();
	void setRows(Collection<IRowEntry> rows);
	
}
