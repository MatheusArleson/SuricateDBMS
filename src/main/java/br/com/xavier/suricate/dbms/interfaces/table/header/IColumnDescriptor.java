package br.com.xavier.suricate.dbms.interfaces.table.header;

import br.com.xavier.suricate.dbms.enums.ColumnsTypes;
import br.com.xavier.suricate.dbms.interfaces.IDeserializableCollection;
import br.com.xavier.suricate.dbms.interfaces.ISerializable;

public interface IColumnDescriptor
		extends ISerializable, IDeserializableCollection<IColumnDescriptor> {
	
	String getName();
	void setName(String name);
	ColumnsTypes getType();
	void setType(ColumnsTypes type);
	Short getSize();
	void setSize(Short size);

}
