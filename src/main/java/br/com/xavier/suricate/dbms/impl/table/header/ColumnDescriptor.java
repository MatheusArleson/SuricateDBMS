package br.com.xavier.suricate.dbms.impl.table.header;

import java.io.IOException;

import br.com.xavier.suricate.dbms.abstractions.table.header.AbstractColumnDescriptor;
import br.com.xavier.suricate.dbms.enums.ColumnsTypes;

public final class ColumnDescriptor
		extends AbstractColumnDescriptor {
	
	private static final long serialVersionUID = -1276506675217862799L;
	
	public ColumnDescriptor() {
		super();
	}

	public ColumnDescriptor(String name, ColumnsTypes type, Short size) {
		super(name, type, size);
	}
	
	public ColumnDescriptor(byte[] bytes) throws IOException {
		super(bytes);
	}
}
