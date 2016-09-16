package br.com.xavier.suricate.dbms.impl.table;

import java.io.File;
import java.io.IOException;

import br.com.xavier.suricate.dbms.abstractions.table.AbstractTable;

public final class Table
		extends AbstractTable {

	private static final long serialVersionUID = 4864024392240777959L;
	
	public Table(File file) throws IOException {
		super(file);
	}
	
	public Table(byte[] bytes) throws IOException {
		super(bytes);
	}
	
}
