package br.com.xavier.suricate.dbms.interfaces.table;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import br.com.xavier.suricate.dbms.interfaces.low.IBinarizable;
import br.com.xavier.suricate.dbms.interfaces.table.data.ITableDataBlock;
import br.com.xavier.suricate.dbms.interfaces.table.header.ITableHeaderBlock;
import br.com.xavier.util.ByteArrayUtils;

public interface ITable 
		extends IBinarizable {
	
	File getFile();
	void setFile(File file) throws IOException;
	ITableHeaderBlock getHeaderBlock();
	void setHeaderBlock(ITableHeaderBlock header);
	Collection<ITableDataBlock> getDataBlocks();
	void setDataBlocks(Collection<ITableDataBlock> dataBlocks);

	@Override
	default byte[] toByteArray() throws IOException {
		byte[] headerBlockBytes = getHeaderBlock().toByteArray();
		byte[] dataBlocksBytes = ByteArrayUtils.toByteArray(getDataBlocks());
		
		byte[] byteArray = ByteArrayUtils.toByteArray(headerBlockBytes, dataBlocksBytes);
		return byteArray;
	}
	
	@Override
	default void fromByteArray(byte[] bytes) throws IOException {
		//TODO FIXME finish method
		throw new IOException("Cant setup table instances from byte array");
	}
}
