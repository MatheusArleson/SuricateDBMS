package br.com.xavier.suricate.dbms.abstractions.table.header;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import br.com.xavier.suricate.dbms.enums.ColumnsTypes;
import br.com.xavier.suricate.dbms.interfaces.table.data.ITableDataBlockHeader;
import br.com.xavier.suricate.dbms.interfaces.table.header.IColumnDescriptor;
import br.com.xavier.suricate.dbms.interfaces.table.header.ITableHeaderBlock;
import br.com.xavier.suricate.dbms.interfaces.table.header.ITableHeaderBlockContent;
import br.com.xavier.util.ObjectsUtils;

public abstract class AbstractTableHeaderBlock
		implements ITableHeaderBlock {
	
	private static final long serialVersionUID = -5842811196037749943L;
	
	//XXX PROPERTIES
	private ITableHeaderBlockContent headerContent;
	private Collection<IColumnDescriptor> columnsDescriptors;
	
	//XXX CONSTRUCTORS
	public AbstractTableHeaderBlock() {
		super();
	}
	
	public AbstractTableHeaderBlock(ITableHeaderBlockContent headerContent,	Collection<IColumnDescriptor> columnsDescriptors) {
		super();
		setHeaderContent(headerContent);
		setColumnsDescriptor(columnsDescriptors);
	}
	
	public AbstractTableHeaderBlock(byte[] bytes) throws IOException {
		try {
			fromByteArray(bytes);
		} catch(IOException e){
			throw e;
		} catch(IllegalArgumentException e){
			throw new IOException("Illegal value detected.", e);
		}
	}

	//XXX OVERRIDE METHODS
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result	+ ((headerContent == null) ? 0 : headerContent.hashCode());
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
		AbstractTableHeaderBlock other = (AbstractTableHeaderBlock) obj;
		if (headerContent == null) {
			if (other.headerContent != null)
				return false;
		} else if (!headerContent.equals(other.headerContent))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "AbstractTableHeaderBlock [" 
			+ "headerContent=" + headerContent
		+ "]";
	}
	
	@Override
	public Integer getRowSize() {
		if(columnsDescriptors == null || columnsDescriptors.isEmpty()){
			return null;
		}
		
		int rowSize = 0;
		for (IColumnDescriptor columnDescriptor : columnsDescriptors) {
			ColumnsTypes columnType = columnDescriptor.getType();
			switch (columnType) {
			case INTEGER:
				rowSize = rowSize + Integer.BYTES;
				break;
			case STRING:
				rowSize = rowSize + (2 * columnDescriptor.getSize());
				break;
			default:
				throw new IllegalArgumentException("Unknow column type.");
			}
		}
		
		return rowSize;
	}
	
	@Override
	public Integer getNumberOfRowsPerBlock() {
		ITableHeaderBlockContent headerContent = getHeaderContent();
		if(headerContent == null){
			return null;
		}
		
		Integer blockSize = headerContent.getBlockSize().getValue();
		Integer dataBlockHeaderBytesSize = ITableDataBlockHeader.BYTES_SIZE;
		Integer rowSize = getRowSize();
		
		Integer numberOfRowPerBlock = (blockSize - dataBlockHeaderBytesSize) / rowSize;
		return numberOfRowPerBlock;
	}
	
	//XXX GETTERS/SETTERS
	@Override
	public ITableHeaderBlockContent getHeaderContent() {
		return headerContent;
	}
	
	@Override
	public void setHeaderContent(ITableHeaderBlockContent headerContent) {
		if(headerContent == null){
			throw new IllegalArgumentException("Table header content instance must not be null.");
		}
		
		this.headerContent = headerContent;
	}

	@Override
	public Collection<IColumnDescriptor> getColumnsDescriptors() {
		if(columnsDescriptors == null){
			return null;
		}
		
		return new ArrayList<>(columnsDescriptors);
	}
	
	@Override
	public void setColumnsDescriptor(Collection<IColumnDescriptor> columnsDescriptors) {
		if(columnsDescriptors == null){
			throw new IllegalArgumentException("Columns descriptors collection instance must not be null.");
		}
		
		if(columnsDescriptors.isEmpty()){
			throw new IllegalArgumentException("Columns descriptors collection must not be empty.");
		}
		
		boolean anyNull = ObjectsUtils.anyNull(columnsDescriptors.toArray());
		if(anyNull){
			throw new IllegalArgumentException("Columns descriptors collections must not have null values.");
		}
		
		if(this.columnsDescriptors == null){
			this.columnsDescriptors = new ArrayList<>();
		}
		
		this.columnsDescriptors.clear();
		this.columnsDescriptors.addAll(columnsDescriptors);
		
		Integer rowsPerBlock = getNumberOfRowsPerBlock();
		if(rowsPerBlock < 1){
			throw new IllegalArgumentException("Invalid block size : each block must contain at least one row.");
		}
	}

}
