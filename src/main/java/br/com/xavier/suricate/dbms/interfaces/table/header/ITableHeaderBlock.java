package br.com.xavier.suricate.dbms.interfaces.table.header;

import java.util.Collection;

import br.com.xavier.suricate.dbms.interfaces.low.IBinarizable;

public interface ITableHeaderBlock
		extends IBinarizable {
	
	ITableHeaderBlockContent getHeaderContent();
	void setHeaderContent(ITableHeaderBlockContent headerData);
	Collection<IColumnDescriptor> getColumnsDescriptors();
	void setColumnsDescriptor(Collection<IColumnDescriptor> columnsDescriptors);
	
}
