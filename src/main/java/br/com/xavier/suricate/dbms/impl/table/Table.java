package br.com.xavier.suricate.dbms.impl.table;

import java.io.IOException;
import java.io.RandomAccessFile;

import br.com.xavier.suricate.dbms.abstractions.table.AbstractTable;

public final class Table
		extends AbstractTable {

	private static final long serialVersionUID = 4864024392240777959L;
	
	public Table(RandomAccessFile file) throws IOException {
		super(file);
	}
	
}
