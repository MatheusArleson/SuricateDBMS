package br.com.xavier.suricate.dbms.interfaces;

public interface IColumnDescriptor
		extends IBinarizable<IColumnDescriptor> {
	
	String getName();
	void setName(String name);
	IColumnType getType();
	void setType(IColumnType type);

}
