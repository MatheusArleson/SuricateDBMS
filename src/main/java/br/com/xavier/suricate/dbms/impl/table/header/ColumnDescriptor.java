package br.com.xavier.suricate.dbms.impl.table.header;

import br.com.xavier.suricate.dbms.abstractions.table.header.AbstractColumnDescriptor;
import br.com.xavier.suricate.dbms.enums.ColumnsTypes;

public class ColumnDescriptor
		extends AbstractColumnDescriptor {
	
	private static final long serialVersionUID = -1276506675217862799L;

	public ColumnDescriptor(String name, ColumnsTypes type, Short size) {
		super(name, type, size);
	}

}
