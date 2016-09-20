package br.com.xavier.suricate.dbms.interfaces.services;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;

import br.com.xavier.suricate.dbms.interfaces.table.access.IRowId;
import br.com.xavier.suricate.dbms.interfaces.table.data.ITableDataBlock;

public interface IFileSystemManager
		extends Serializable {
	
	RandomAccessFile createFile(String fileName);
	RandomAccessFile createFile(String fileAbsolutePath, byte[] fileContent);
	
	//ITableHeaderBlock readHeaderBlock();
	//void writeHeaderBlock(ITableHeaderBlock headerBlock);
	
	ITableDataBlock readDataBlock(IRowId rowId) throws IOException;
	void writeDataBlock(ITableDataBlock dataBlock) throws IOException;
	void deleteDataBlock(ITableDataBlock dataBlock) throws IOException;

}
