package br.com.xavier.suricate.dbms.impl.table.access;

import br.com.xavier.suricate.dbms.abstractions.table.access.AbstractRowId;
import br.com.xavier.suricate.dbms.interfaces.low.IThreeByteValue;

public class RowId
		extends AbstractRowId {

	private static final long serialVersionUID = 771793322225788785L;

	public RowId(Byte tableId, IThreeByteValue blockId, Long byteOffset) {
		super(tableId, blockId, byteOffset);
	}

}
