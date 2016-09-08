package br.com.xavier.suricate.dbms.interfaces.table.data;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;

import br.com.xavier.suricate.dbms.impl.table.data.ColumnEntry;
import br.com.xavier.suricate.dbms.interfaces.low.IBinarizable;
import br.com.xavier.util.ByteArrayUtils;
import br.com.xavier.util.ObjectsUtils;

public interface IRowEntry 
		extends IBinarizable {
	
	public static final Integer ENTRY_MIN_SIZE = Integer.BYTES + Short.BYTES + 2;
	
	Integer getEntrySize();
	Integer getColumnsEntrySize();
	Collection<IColumnEntry> getColumnsEntries();
	void setColumnsEntries(Collection<IColumnEntry> columnsEntries);
	
	@Override
	default byte[] toByteArray() throws IOException {
		Collection<IColumnEntry> columnsEntries = getColumnsEntries();
		Integer columnsEntriesSize = getColumnsEntrySize();
		Integer rowEntrySize = getEntrySize();
		
		boolean anyNull = ObjectsUtils.anyNull(rowEntrySize, columnsEntriesSize, columnsEntries);
		if(anyNull){
			throw new IOException("To transform to byte[] all properties must not be null.");
		}
		
		
		ByteBuffer bb = ByteBuffer.allocate(rowEntrySize);
		
		bb.putInt(rowEntrySize);
		
		byte[] columnsEntriesBytes = ByteArrayUtils.toByteArray(columnsEntries);
		bb.put(columnsEntriesBytes);
		
		return bb.array();
	}
	
	@Override
	default void fromByteArray(byte[] bytes) throws IOException {
		try{
			
			if(bytes == null){
				throw new IOException("bytes cannot be null");
			}
			
			if(bytes.length < ENTRY_MIN_SIZE){
				throw new IOException("bytes length must be at least of size : " + ENTRY_MIN_SIZE);
			}
			
			ByteBuffer bb = ByteBuffer.wrap(bytes);
			Integer rowEntrySize = bb.getInt();
			
			if(bb.remaining() > rowEntrySize){
				throw new IOException("bytes overflow.");
			}
			
			if(bb.remaining() < rowEntrySize){
				throw new IOException("bytes underflow.");
			}
			
			Collection<IColumnEntry> columnEntries = new ArrayList<>();
			while(bb.hasRemaining()){
				Short contentSize = bb.getShort();
				
				byte[] contentBuffer = new byte[contentSize];
				bb.get(contentBuffer);
				
				IColumnEntry columnEntry = new ColumnEntry(contentBuffer);
				columnEntries.add(columnEntry);
			}
			
			setColumnsEntries(columnEntries);
		
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			throw new IOException("Error while parsing the bytes.", e);
		}
	}
	
}
