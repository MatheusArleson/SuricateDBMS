package br.com.xavier.suricate.dbms.impl.table.data;

import java.io.IOException;

import br.com.xavier.suricate.dbms.abstractions.table.data.AbstractColumnEntry;

public final class ColumnEntry
		extends AbstractColumnEntry {
	
	private static final long serialVersionUID = -1246714275187831700L;

	public ColumnEntry() {
		super();
	}
	
	public ColumnEntry(byte[] bytes) throws IOException {
		super(bytes);
	}

}
