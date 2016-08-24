package br.com.xavier.suricate.dbms.interfaces.table.data;

import java.util.Collection;

import br.com.xavier.suricate.dbms.interfaces.IDeserializable;
import br.com.xavier.suricate.dbms.interfaces.ISerializable;

public interface ITableDataBlock
		extends ISerializable, IDeserializable<ITableDataBlock> {
	
	ITableDataBlockHeader getHeader();
	void getHeader(ITableDataBlockHeader header);
	Collection<IRowEntry> getRows();
	void setRows(Collection<IRowEntry> rows);
	
}
