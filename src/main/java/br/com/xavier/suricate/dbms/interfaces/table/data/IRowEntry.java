package br.com.xavier.suricate.dbms.interfaces.table.data;

import java.util.Collection;

import br.com.xavier.suricate.dbms.interfaces.IDeserializable;
import br.com.xavier.suricate.dbms.interfaces.ISerializable;

public interface IRowEntry 
		extends ISerializable, IDeserializable<IRowEntry> {
	
	Integer getSize();
	void setSize(Integer size);
	Collection<IColumnEntry> getColumnsEntries();
	void setColumnsEntries(Collection<IColumnEntry> columnsEntries);

}
