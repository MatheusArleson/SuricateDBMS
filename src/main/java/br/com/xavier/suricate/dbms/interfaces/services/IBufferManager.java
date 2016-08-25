package br.com.xavier.suricate.dbms.interfaces.services;

import br.com.xavier.suricate.dbms.interfaces.table.access.IRowId;
import br.com.xavier.suricate.dbms.interfaces.table.data.ITableDataBlock;

public interface IBufferManager {
	
	ITableDataBlock fetchBlock(IRowId rowId);
	void swapIn();
	void swapOut();
	boolean isBlockInMemory();
	boolean isBlockInDisk();
	void flush();

}
