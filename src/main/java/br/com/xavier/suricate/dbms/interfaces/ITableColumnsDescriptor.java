package br.com.xavier.suricate.dbms.interfaces;

import java.util.Collection;

public interface ITableColumnsDescriptor 
		extends IBinarizable<ITableColumnsDescriptor> {
	
	Collection<IColumnDescriptor> getColumnsDescriptors();
	void setColumnsDescriptors(Collection<IColumnDescriptor> columnsDescriptors);

}
