package br.com.xavier.suricate.dbms.interfaces.dbms;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Collection;

import br.com.xavier.suricate.dbms.interfaces.low.IThreeByteValue;
import br.com.xavier.suricate.dbms.interfaces.services.ITextSeparators;
import br.com.xavier.suricate.dbms.interfaces.table.ITable;
import br.com.xavier.suricate.dbms.interfaces.table.access.IRowId;
import br.com.xavier.suricate.dbms.interfaces.table.data.ITableDataBlock;

public interface ITableManager 
		extends Serializable {
	
	Collection<ITable> getAllTables();
	void removeTable(ITable table) throws IOException;
	
	ITable importTableFile(File file, Charset charset, ITextSeparators separators, IThreeByteValue blockSize) throws IOException;
	String printData(ITable table, ITextSeparators separators) throws IOException;
	ITableDataBlock getDataBlock(IRowId rowId) throws IOException;
	
}
