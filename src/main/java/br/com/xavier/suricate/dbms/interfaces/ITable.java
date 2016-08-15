package br.com.xavier.suricate.dbms.interfaces;

import java.io.RandomAccessFile;
import java.util.Collection;

public interface ITable 
		extends IBinarizable<ITable> {
	
	RandomAccessFile getFile();
	void setFile(RandomAccessFile file);
	ITableHeader getHeader();
	void setHeader(ITableHeader header);
	Collection<ITableBlock> getBlocks();
	void setBlocks(Collection<ITableBlock> blocks);

}
