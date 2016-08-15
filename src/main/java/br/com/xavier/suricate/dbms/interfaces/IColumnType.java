package br.com.xavier.suricate.dbms.interfaces;

public interface IColumnType
		extends IBinarizable<IColumnType> {
	
	Short getId();
	Short getSize();
	void setSize(Short size);
	String getName();
	
}
