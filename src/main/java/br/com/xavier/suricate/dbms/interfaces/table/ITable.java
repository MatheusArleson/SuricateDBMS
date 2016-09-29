package br.com.xavier.suricate.dbms.interfaces.table;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Collection;

import br.com.xavier.suricate.dbms.Factory;
import br.com.xavier.suricate.dbms.interfaces.low.IBinarizable;
import br.com.xavier.suricate.dbms.interfaces.table.data.ITableDataBlock;
import br.com.xavier.suricate.dbms.interfaces.table.header.ITableHeaderBlock;
import br.com.xavier.util.ByteArrayUtils;
import br.com.xavier.util.ObjectsUtils;

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
		ITableHeaderBlock headerBlock = getHeaderBlock();
		Collection<ITableDataBlock> dataBlocks = getDataBlocks();
		
		boolean anyNull = ObjectsUtils.anyNull(headerBlock, dataBlocks);
		if(anyNull){
			throw new IOException("To transform to byte[] all properties must not be null.");
		}
		
		Integer blockSize = headerBlock.getHeaderContent().getBlockSize().getValue();
		
		byte[] headerBlockBytes = headerBlock.toByteArray();
		headerBlockBytes = Factory.leftPad(headerBlockBytes, blockSize);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		for (ITableDataBlock dataBlock : dataBlocks) {
			byte[] dataBlockBytes = dataBlock.toByteArray();
			dataBlockBytes = Factory.leftPad(dataBlockBytes, blockSize);
			baos.write(dataBlockBytes);
		}
		
		byte[] dataBlocksBytes = baos.toByteArray();
		
		byte[] byteArray = ByteArrayUtils.toByteArray(headerBlockBytes, dataBlocksBytes);
		return byteArray;
	}
	
	@Override
	default void fromByteArray(byte[] bytes) throws IOException {
		//TODO FIXME finish method
		throw new IOException("Cant setup table instances from byte array");
	}
}
