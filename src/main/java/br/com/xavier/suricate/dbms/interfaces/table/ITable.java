package br.com.xavier.suricate.dbms.interfaces.table;

import java.io.RandomAccessFile;
import java.util.Collection;

import br.com.xavier.suricate.dbms.interfaces.low.IBinarizable;
import br.com.xavier.suricate.dbms.interfaces.table.data.ITableDataBlock;
import br.com.xavier.suricate.dbms.interfaces.table.header.ITableHeaderBlock;

public interface ITable 
		extends IBinarizable {
	
	RandomAccessFile getFile();
	void setFile(RandomAccessFile file);
	ITableHeaderBlock getHeaderBlock();
	void setHeaderBlock(ITableHeaderBlock header);
	Collection<ITableDataBlock> getDataBlocks();
	void setDataBlocks(Collection<ITableDataBlock> dataBlocks);

}
