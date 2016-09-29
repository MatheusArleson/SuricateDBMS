package br.com.xavier.suricate.dbms.interfaces.services;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Collection;

import br.com.xavier.suricate.dbms.interfaces.low.IThreeByteValue;
import br.com.xavier.suricate.dbms.interfaces.table.ITable;
import br.com.xavier.suricate.dbms.interfaces.table.access.IRowId;
import br.com.xavier.suricate.dbms.interfaces.table.data.ITableDataBlock;

public interface IFileSystemManager
		extends Serializable {
	
	Collection<ITable> getAllTables();
	void removeTable(ITable table) throws IOException;
	ITable importFile(File file, Charset charset, ITextSeparators separators, IThreeByteValue blockSize) throws IOException;
	void createFile(File file, byte[] content) throws IOException;
	
	//ITableHeaderBlock readHeaderBlock();
	//void writeHeaderBlock(ITableHeaderBlock headerBlock);
	
	ITableDataBlock readDataBlock(IRowId rowId) throws IOException;
	void writeDataBlock(ITableDataBlock dataBlock) throws IOException;
	void deleteDataBlock(ITableDataBlock dataBlock) throws IOException;

}
