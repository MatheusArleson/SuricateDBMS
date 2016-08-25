package br.com.xavier.suricate.dbms.abstractions.table.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import br.com.xavier.suricate.dbms.Factory;
import br.com.xavier.suricate.dbms.interfaces.table.data.IRowEntry;
import br.com.xavier.suricate.dbms.interfaces.table.data.ITableDataBlock;
import br.com.xavier.suricate.dbms.interfaces.table.data.ITableDataBlockHeader;

public abstract class AbstractTableDataBlock
		implements ITableDataBlock {

	private static final long serialVersionUID = -7764984396629018489L;

	//XXX PROPERTIES
	private ITableDataBlockHeader header;
	private Collection<IRowEntry> rows;
	
	//XXX CONSTRUCTORS
	public AbstractTableDataBlock(ITableDataBlockHeader header) {
		super();
		this.header = header;
		this.rows = new ArrayList<>();
	}
	
	public AbstractTableDataBlock(ITableDataBlockHeader header, Collection<IRowEntry> rows) {
		this(header);
		this.rows.addAll(rows);
	}
	
	//XXX OVERRIDE METHODS
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((header == null) ? 0 : header.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractTableDataBlock other = (AbstractTableDataBlock) obj;
		if (header == null) {
			if (other.header != null)
				return false;
		} else if (!header.equals(other.header))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "AbstractTableDataBlock [" 
			+ "header=" + header 
		+ "]";
	}

	@Override
	public byte[] toByteArray() throws IOException {
		byte[] headerBytes = header.toByteArray();
		byte[] rowsBytes = Factory.toByteArray(rows);
		
		byte[] byteArray = Factory.toByteArray(headerBytes, rowsBytes);
		return byteArray;
	}

	//XXX GETTERS/SETTERS
	@Override
	public ITableDataBlockHeader getHeader() {
		return header;
	}

	@Override
	public void getHeader(ITableDataBlockHeader header) {
		this.header = header;
	}

	@Override
	public Collection<IRowEntry> getRows() {
		return rows;
	}

	@Override
	public void setRows(Collection<IRowEntry> rows) {
		this.rows = rows;
	}

}
