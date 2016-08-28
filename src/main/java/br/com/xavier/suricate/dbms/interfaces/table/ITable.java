package br.com.xavier.suricate.dbms.interfaces.table;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Collection;

import br.com.xavier.suricate.dbms.Factory;
import br.com.xavier.suricate.dbms.interfaces.low.IBinarizable;
import br.com.xavier.suricate.dbms.interfaces.table.data.ITableDataBlock;
import br.com.xavier.suricate.dbms.interfaces.table.header.ITableHeaderBlock;

public interface ITable 
		extends IBinarizable {
	
	RandomAccessFile getFile();
	ITableHeaderBlock getHeaderBlock();
	void setHeaderBlock(ITableHeaderBlock header);
	Collection<ITableDataBlock> getDataBlocks();
	void setDataBlocks(Collection<ITableDataBlock> dataBlocks);

	@Override
	default byte[] toByteArray() throws IOException {
		byte[] headerBlockBytes = getHeaderBlock().toByteArray();
		byte[] dataBlocksBytes = Factory.toByteArray(getDataBlocks());
		
		byte[] byteArray = Factory.toByteArray(headerBlockBytes, dataBlocksBytes);
		return byteArray;
	}
	
	@Override
	default void fromByteArray(byte[] bytes) throws IOException {
		throw new IOException("Cant setup table instances from byte array");
	}
}
