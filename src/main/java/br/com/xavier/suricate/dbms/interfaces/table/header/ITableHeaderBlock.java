package br.com.xavier.suricate.dbms.interfaces.table.header;

import java.util.Collection;

import br.com.xavier.suricate.dbms.interfaces.IDeserializable;
import br.com.xavier.suricate.dbms.interfaces.ISerializable;

public interface ITableHeaderBlock
		extends ISerializable, IDeserializable<ITableHeaderBlock> {
	
	ITableHeaderBlockContent getHeaderContent();
	void setHeaderContent(ITableHeaderBlockContent headerData);
	Collection<IColumnDescriptor> getColumnsDescriptors();
	void setColumnsDescriptor(Collection<IColumnDescriptor> columnsDescriptors);
	
}
