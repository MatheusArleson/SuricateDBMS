package br.com.xavier.suricate.dbms.impl.table.data;

import br.com.xavier.suricate.dbms.abstractions.table.data.AbstractTableDataBlockHeader;
import br.com.xavier.suricate.dbms.enums.TableBlockType;
import br.com.xavier.suricate.dbms.interfaces.low.IThreeByteValue;

public class TableDataBlockHeader 
		extends AbstractTableDataBlockHeader {
	
	private static final long serialVersionUID = 3506634766337003130L;

	public TableDataBlockHeader(
		Byte tableId, 
		IThreeByteValue blockId, 
		TableBlockType type,
		IThreeByteValue bytesUsedInBlock
	) {
		super(tableId, blockId, type, bytesUsedInBlock);
	}

}
