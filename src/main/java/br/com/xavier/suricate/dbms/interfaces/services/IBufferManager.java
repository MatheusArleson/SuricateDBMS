package br.com.xavier.suricate.dbms.interfaces.services;

import br.com.xavier.suricate.dbms.interfaces.low.IThreeByteValue;
import br.com.xavier.suricate.dbms.interfaces.table.access.IRowId;
import br.com.xavier.suricate.dbms.interfaces.table.data.ITableDataBlock;

public interface IBufferManager {
	
	ITableDataBlock fetchBlock(IRowId rowId);
	void swapIn();
	void swapOut();
	boolean isBlockInMemory(Byte tableId, IThreeByteValue blockId);
	void flush();

}
