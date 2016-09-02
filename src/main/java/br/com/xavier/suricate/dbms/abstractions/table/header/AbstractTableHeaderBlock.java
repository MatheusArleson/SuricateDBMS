package br.com.xavier.suricate.dbms.abstractions.table.header;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;

import br.com.xavier.suricate.dbms.impl.table.header.ColumnDescriptor;
import br.com.xavier.suricate.dbms.impl.table.header.TableHeaderBlockContent;
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
		this.headerContent = headerContent;
		this.columnsDescriptors = columnsDescriptors;
	}
	
	public AbstractTableHeaderBlock(byte[] bytes) throws IOException {
		super();
		this.headerContent = generateTableHeaderContent(bytes);
		this.columnsDescriptors = generateColumnsDescriptors(bytes);
	}

	private TableHeaderBlockContent generateTableHeaderContent(byte[] bytes) throws IOException {
		return new TableHeaderBlockContent(bytes);
	}
	
	private Collection<IColumnDescriptor> generateColumnsDescriptors(byte[] bytes) throws IOException {		
		ArrayList<IColumnDescriptor> columnsDescriptors = new ArrayList<>();
		try{
			ByteBuffer bb = ByteBuffer.wrap(bytes);
			bb.position(ITableHeaderBlockContent.BYTES_SIZE);
			
			byte[] buffer = new byte[IColumnDescriptor.BYTES_SIZE];
			while(bb.hasRemaining()){
				bb.get(buffer);
				IColumnDescriptor columnDesc = new ColumnDescriptor(buffer);
				columnsDescriptors.add(columnDesc);
			}
			
			return columnsDescriptors;
			
		} catch(Exception e){
			throw new IOException("Error while parsing columns descriptors.", e);
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
	}

}
