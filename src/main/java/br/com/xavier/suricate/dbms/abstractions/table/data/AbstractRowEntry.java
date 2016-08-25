package br.com.xavier.suricate.dbms.abstractions.table.data;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;

import br.com.xavier.suricate.dbms.Factory;
import br.com.xavier.suricate.dbms.interfaces.table.data.IColumnEntry;
import br.com.xavier.suricate.dbms.interfaces.table.data.IRowEntry;

public class AbstractRowEntry 
		implements IRowEntry {

	private static final long serialVersionUID = -4174089309741988414L;
	
	//XXX PROPERTIES
	private Integer size;
	private Collection<IColumnEntry> columnsEntries;
	
	//XXX CONSTRUCTOR
	public AbstractRowEntry(Integer size) {
		super();
		this.size = size;
		this.columnsEntries = new ArrayList<>();
	}
	
	public AbstractRowEntry(Integer size, Collection<IColumnEntry> columnsEntries) {
		this(size);
		this.columnsEntries.addAll(columnsEntries);
	}
	
	//XXX OVERRIDE METHODS
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((columnsEntries == null) ? 0 : columnsEntries.hashCode());
		result = prime * result + ((size == null) ? 0 : size.hashCode());
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
		AbstractRowEntry other = (AbstractRowEntry) obj;
		if (columnsEntries == null) {
			if (other.columnsEntries != null)
				return false;
		} else if (!columnsEntries.equals(other.columnsEntries))
			return false;
		if (size == null) {
			if (other.size != null)
				return false;
		} else if (!size.equals(other.size))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "AbstractRowEntry [" 
			+ "size=" + size 
			+ ", columnsEntries=" + columnsEntries 
		+ "]";
	}

	@Override
	public byte[] toByteArray() throws IOException {
		ByteBuffer bb = null;
		
		try {
			byte[] columnsEntriesBytes = Factory.toByteArray(columnsEntries);
			Integer size = columnsEntriesBytes.length + 4;
			
			bb = ByteBuffer.allocate(size);
			
			bb.putInt(getSize());
			bb.put(columnsEntriesBytes);
			
			return bb.array();
			
		} catch (Exception e) {
			throw e;
		} 
		
	}

	//XXX GETTERS/SETTERS
	@Override
	public Integer getSize() {
		return size;
	}

	@Override
	public void setSize(Integer size) {
		this.size = size;
	}

	@Override
	public Collection<IColumnEntry> getColumnsEntries() {
		return columnsEntries;
	}

	@Override
	public void setColumnsEntries(Collection<IColumnEntry> columnsEntries) {
		this.columnsEntries = columnsEntries;
	}

}
