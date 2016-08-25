package br.com.xavier.suricate.dbms.impl.table.header;

import br.com.xavier.suricate.dbms.abstractions.table.header.AbstractTableHeaderBlockContent;
import br.com.xavier.suricate.dbms.enums.TableStatus;
import br.com.xavier.suricate.dbms.interfaces.low.IThreeByteValue;

public final class TableHeaderBlockContent 
		extends AbstractTableHeaderBlockContent {
	
	private static final long serialVersionUID = -2148776571184121789L;

	public TableHeaderBlockContent(
		Byte tableId, 
		IThreeByteValue blockSize, 
		Short headerSize,
		Integer nextFreeBlockId, 
		TableStatus tableStatus
	) {
		super(tableId, blockSize, headerSize, nextFreeBlockId, tableStatus);
	}

}
