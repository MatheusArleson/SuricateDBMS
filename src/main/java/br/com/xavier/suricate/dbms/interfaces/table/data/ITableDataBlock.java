package br.com.xavier.suricate.dbms.interfaces.table.data;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import br.com.xavier.suricate.dbms.impl.table.data.RowEntry;
import br.com.xavier.suricate.dbms.impl.table.data.TableDataBlockHeader;
import br.com.xavier.suricate.dbms.interfaces.low.IBinarizable;
import br.com.xavier.suricate.dbms.interfaces.services.ITextSeparators;
import br.com.xavier.suricate.dbms.interfaces.table.header.IColumnDescriptor;
import br.com.xavier.util.ByteArrayUtils;
import br.com.xavier.util.ObjectsUtils;

public interface ITableDataBlock
		extends IBinarizable {
	
	ITableDataBlockHeader getHeader();
	void setHeader(ITableDataBlockHeader header);
	Collection<IRowEntry> getRows();
	void setRows(Collection<IRowEntry> rows);
	
	String printData(Collection<IColumnDescriptor> columnsDescriptors, ITextSeparators separators);
	
	@Override
	default byte[] toByteArray() throws IOException {
		ITableDataBlockHeader header = getHeader();
		Collection<IRowEntry> rows = getRows();
		
		boolean anyNull = ObjectsUtils.anyNull(header, rows);
		if(anyNull){
			throw new IOException("To transform to byte[] all properties must not be null.");
		}
		
		byte[] headerBytes = header.toByteArray();
		byte[] rowsBytes = ByteArrayUtils.toByteArray(rows);
		
		byte[] byteArray = ByteArrayUtils.toByteArray(headerBytes, rowsBytes);
		
		return byteArray;
	}
	
	@Override
	default void fromByteArray(byte[] bytes) throws IOException {
		Objects.requireNonNull(bytes, "bytes cannot be null");
		
		int minimunSize = ITableDataBlockHeader.BYTES_SIZE;
		if(bytes.length < minimunSize){
			throw new IOException("bytes length must be at least of size : " + minimunSize);
		}
		
		ByteBuffer bb = ByteBuffer.wrap(bytes);
		
		byte[] tableDataBlockHeaderBuffer = new byte[ITableDataBlockHeader.BYTES_SIZE];
		bb.get(tableDataBlockHeaderBuffer);
		ITableDataBlockHeader tableDataBlockHeader = new TableDataBlockHeader(tableDataBlockHeaderBuffer);
		setHeader(tableDataBlockHeader);
		
		Integer bytesUsedInBlock = tableDataBlockHeader.getBytesUsedInBlock().getValue();
		if(bb.remaining() < bytesUsedInBlock){
			throw new IOException("Row entries : Byte underflow : must be of lenght : " + bytesUsedInBlock);
		}
		
		byte[] rowEntriesBytes = new byte[bytesUsedInBlock];
		bb.get(rowEntriesBytes);
		
		ByteBuffer rowsEntriesBuffer = ByteBuffer.wrap(rowEntriesBytes);
		Collection<IRowEntry> rowEntries = new ArrayList<>();
		while(rowsEntriesBuffer.hasRemaining()){
			Integer rowSize = rowsEntriesBuffer.getInt();
			Integer rowEntrySize = Integer.BYTES + rowSize;
			byte[] rowEntryBuffer = new byte[rowEntrySize];
			
			rowsEntriesBuffer.position(rowsEntriesBuffer.position() - Integer.BYTES);
			rowsEntriesBuffer.get(rowEntryBuffer);
			
			IRowEntry rowEntry = new RowEntry(rowEntryBuffer);
			rowEntries.add(rowEntry);
		}
		
		setRows(rowEntries);
	}
	
}
