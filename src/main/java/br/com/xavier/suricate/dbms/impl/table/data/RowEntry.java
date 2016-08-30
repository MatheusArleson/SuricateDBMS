package br.com.xavier.suricate.dbms.impl.table.data;

import java.io.IOException;
import java.util.Collection;

import br.com.xavier.suricate.dbms.abstractions.table.data.AbstractRowEntry;
import br.com.xavier.suricate.dbms.interfaces.table.data.IColumnEntry;

public class RowEntry 
		extends AbstractRowEntry {
	
	private static final long serialVersionUID = -1618950376653615597L;

	public RowEntry(Integer size) {
		super(size);
	}
	
	public RowEntry(Integer size, Collection<IColumnEntry> columnsEntries) {
		super(size, columnsEntries);
	}
	
	public RowEntry(byte[] bytes) throws IOException {
		super(bytes);
	}
	
}