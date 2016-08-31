package br.com.xavier.suricate.dbms.interfaces.table.data;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import br.com.xavier.suricate.dbms.impl.table.data.ColumnEntry;
import br.com.xavier.suricate.dbms.interfaces.low.IBinarizable;
import br.com.xavier.util.ByteArrayUtils;

public interface IRowEntry 
		extends IBinarizable {
	
	Integer getSize();
	Collection<IColumnEntry> getColumnsEntries();
	void setColumnsEntries(Collection<IColumnEntry> columnsEntries);
	
	@Override
	default byte[] toByteArray() throws IOException {
		byte[] columnsEntriesBytes = ByteArrayUtils.toByteArray(getColumnsEntries());
		Integer size = columnsEntriesBytes.length + 4;
		
		ByteBuffer bb = ByteBuffer.allocate(size);
		
		bb.putInt(getSize());
		bb.put(columnsEntriesBytes);
		
		return bb.array();
	}
	
	@Override
	default void fromByteArray(byte[] bytes) throws IOException {
		Objects.requireNonNull(bytes, "bytes cannot be null");
		
		if(bytes.length < 4){
			throw new IOException("bytes length must be at least of size : " + 4);
		}
		
		ByteBuffer bb = ByteBuffer.wrap(bytes);
		
		Collection<IColumnEntry> columnEntries = new ArrayList<>();
		while(bb.hasRemaining()){
			Short contentSize = bb.getShort();
			
			byte[] contentBuffer = new byte[contentSize];
			bb.get(contentBuffer);
			
			IColumnEntry columnEntry = new ColumnEntry(contentSize, contentBuffer);
			columnEntries.add(columnEntry);
		}
		
		setColumnsEntries(columnEntries);
	}
	
}
