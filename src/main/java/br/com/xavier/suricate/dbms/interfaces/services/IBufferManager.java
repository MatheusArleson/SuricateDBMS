package br.com.xavier.suricate.dbms.interfaces.services;

import java.io.IOException;

import br.com.xavier.suricate.dbms.interfaces.table.access.IRowId;
import br.com.xavier.suricate.dbms.interfaces.table.data.ITableDataBlock;

public interface IBufferManager {
	
	ITableDataBlock getDataBlock(IRowId rowId) throws IOException;
//	ITableDataBlock isBlockInMemory(IRowId rowId);
//	void swapIn();
//	void swapOut();
	void flush() throws IOException;

}
