package br.com.xavier.suricate.dbms.abstractions.table.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import br.com.xavier.suricate.dbms.interfaces.services.ITextSeparators;
import br.com.xavier.suricate.dbms.interfaces.table.data.IRowEntry;
import br.com.xavier.suricate.dbms.interfaces.table.data.ITableDataBlock;
import br.com.xavier.suricate.dbms.interfaces.table.data.ITableDataBlockHeader;
import br.com.xavier.suricate.dbms.interfaces.table.header.IColumnDescriptor;
import br.com.xavier.util.ObjectsUtils;

public abstract class AbstractTableDataBlock
		implements ITableDataBlock {

	private static final long serialVersionUID = -7764984396629018489L;

	//XXX PROPERTIES
	private ITableDataBlockHeader header;
	private Collection<IRowEntry> rows;
	
	//XXX CONSTRUCTORS
	public AbstractTableDataBlock() {
		super();
	}
	
	public AbstractTableDataBlock(ITableDataBlockHeader header, Collection<IRowEntry> rows) {
		super();
		setHeader(header);
		
		if(rows != null && rows.isEmpty()){
			this.rows = new ArrayList<>();
		} else {
			setRows(rows);
		}
	}
	
	public AbstractTableDataBlock(byte[] bytes) throws IOException {
		super();
		fromByteArray(bytes);
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
	public String printData(Collection<IColumnDescriptor> columnsDescriptors, ITextSeparators separators) {
		if(separators == null){
			throw new IllegalArgumentException("Null separators.");
		}
		
		if(columnsDescriptors == null || columnsDescriptors.isEmpty()){
			throw new IllegalArgumentException("Null columns descriptors.");
		}
		
		if(rows == null || rows.isEmpty()){
			return "";
		}

		StringBuffer sb = new StringBuffer();
		for (IRowEntry row : rows) {
			String rowInfo = row.printData(columnsDescriptors, separators);
			sb.append(rowInfo);
		}
		
		return sb.toString();
	}
	
	//XXX GETTERS/SETTERS
	@Override
	public ITableDataBlockHeader getHeader() {
		return header;
	}

	@Override
	public void setHeader(ITableDataBlockHeader header) {
		if(header == null){
			throw new IllegalArgumentException("Table Data Block Header instance must not be null.");
		}
		
		this.header = header;
	}

	@Override
	public Collection<IRowEntry> getRows() {
		if(rows == null){
			return null;
		}
		
		return new ArrayList<>(rows);
	}

	@Override
	public void setRows(Collection<IRowEntry> rows) {
		if(rows == null){
			throw new IllegalArgumentException("Rows collection instance must not be null.");
		}
		
		if(rows.isEmpty()){
			throw new IllegalArgumentException("Rows collection must not be empty.");
		}
		
		boolean anyNull = ObjectsUtils.anyNull(rows.toArray());
		if(anyNull){
			throw new IllegalArgumentException("Rows collections must not have null values.");
		}
		
		if(this.rows == null){
			this.rows = new ArrayList<>();
		}
		
		this.rows.clear();
		this.rows.addAll(rows);
	}

}
