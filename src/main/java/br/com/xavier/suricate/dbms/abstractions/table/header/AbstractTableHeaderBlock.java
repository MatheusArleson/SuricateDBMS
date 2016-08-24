package br.com.xavier.suricate.dbms.abstractions.table.header;

import java.util.Collection;

import br.com.xavier.suricate.dbms.interfaces.table.header.IColumnDescriptor;
import br.com.xavier.suricate.dbms.interfaces.table.header.ITableHeaderBlock;
import br.com.xavier.suricate.dbms.interfaces.table.header.ITableHeaderBlockContent;

public class AbstractTableHeaderBlock
		implements ITableHeaderBlock {
	
	private static final long serialVersionUID = -5842811196037749943L;
	
	//XXX PROPERTIES
	private ITableHeaderBlockContent headerContent;
	private Collection<IColumnDescriptor> columnsDescriptors;
	
	//XXX CONSTRUCTOR
	public AbstractTableHeaderBlock() {
		super();
		this.headerContent = null;
		this.columnsDescriptors = null;
	}
	
	public AbstractTableHeaderBlock(ITableHeaderBlockContent headerContent,	Collection<IColumnDescriptor> columnsDescriptors) {
		super();
		this.headerContent = headerContent;
		this.columnsDescriptors = columnsDescriptors;
	}

	@Override
	public byte[] toByteArray() {
		return null;
	}
	
	@Override
	public ITableHeaderBlock fromByteArray(byte[] bytes) {
		this.headerContent = headerContent.fromByteArray(bytes);
		this.columnsDescriptors = ;
		
		return this;
	}

	//XXX GETTERS/SETTERS
	@Override
	public ITableHeaderBlockContent getHeaderContent() {
		return headerContent;
	}

	@Override
	public void setHeaderContent(ITableHeaderBlockContent headerContent) {
		this.headerContent = headerContent;
	}

	@Override
	public Collection<IColumnDescriptor> getColumnsDescriptors() {
		return columnsDescriptors;
	}

	@Override
	public void setColumnsDescriptor(Collection<IColumnDescriptor> columnsDescriptors) {
		this.columnsDescriptors = columnsDescriptors;
	}

}
