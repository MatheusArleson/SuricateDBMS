package br.com.xavier.suricate.dbms.interfaces;

import java.util.ArrayList;

public interface ITableColumnsDescriptor 
		extends IBinarizable<ITableColumnsDescriptor> {
	
	ArrayList<IColumnDescriptor> getColumnsDescriptors();
	void setColumnsDescriptors(ArrayList<IColumnDescriptor> columnsDescriptors);

}
