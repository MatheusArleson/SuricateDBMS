package br.com.xavier.suricate.dbms.impl.table.header;

import java.util.Collection;

import br.com.xavier.suricate.dbms.abstractions.table.header.AbstractTableHeaderBlock;
import br.com.xavier.suricate.dbms.interfaces.table.header.IColumnDescriptor;
import br.com.xavier.suricate.dbms.interfaces.table.header.ITableHeaderBlockContent;

public final class TableHeaderBlock
		extends AbstractTableHeaderBlock {

	private static final long serialVersionUID = 3103600835022231237L;
	
	//XXX CONSTRUCTORS
	public TableHeaderBlock() {
		super();
	}
	
	public TableHeaderBlock(ITableHeaderBlockContent headerContent) {
		super(headerContent);
	}
	
	public TableHeaderBlock(ITableHeaderBlockContent headerContent,	Collection<IColumnDescriptor> columnsDescriptors) {
		super(headerContent, columnsDescriptors);
	}
	

}
