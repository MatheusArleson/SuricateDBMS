package br.com.xavier.suricate.dbms;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;

import br.com.xavier.suricate.dbms.enums.ColumnsTypes;
import br.com.xavier.suricate.dbms.enums.FileModes;
import br.com.xavier.suricate.dbms.impl.low.BigEndianThreeBytesValue;
import br.com.xavier.suricate.dbms.impl.table.data.ColumnEntry;
import br.com.xavier.suricate.dbms.interfaces.low.IThreeByteValue;
import br.com.xavier.suricate.dbms.interfaces.table.data.IColumnEntry;
import br.com.xavier.suricate.dbms.interfaces.table.header.IColumnDescriptor;
import br.com.xavier.util.ByteArrayUtils;

public final class Factory {
	
	//XXX CONSTRUCTOR
	private Factory(){}
	
	//XXX STRING METHODS
	public static String fromByteArray(byte[] bytes){
		return ByteArrayUtils.fromByteArray(bytes, StandardCharsets.UTF_16BE);
	}
	
	public static byte[] toByteArray(String str) {
		return ByteArrayUtils.toByteArray(str, StandardCharsets.UTF_16BE);
	}
	
	//XXX FILE METHODS
	public static byte[] getTableHeaderBlockBytes(File f) throws IOException {
		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile(f, FileModes.READ_ONLY.getMode());
			
			IThreeByteValue blockSize = getBlockSize(raf);
			Integer value = blockSize.getValue();
			byte[] buffer = new byte[value];
			
			raf.seek(0);
			raf.read(buffer);
			raf.seek(0);
			
			return buffer;
		} catch(Exception e){
			throw new IOException(e);
		} finally {
			if(raf != null){
				IOUtils.closeQuietly(raf);
			}
		}
		
	}
	
	private static IThreeByteValue getBlockSize(RandomAccessFile raf) throws IOException{
		byte[] bytes = new byte[3];
		
		raf.seek(0);
		raf.seek(1);
		raf.readFully(bytes);
		raf.seek(0);
		
		IThreeByteValue tbv = new BigEndianThreeBytesValue(bytes);
		return tbv;
	}
	
	//XXX PADDING METHODS
	public static byte[] leftPad(byte[] bytes, int size){
		return ByteArrayUtils.leftPad(bytes, size);
	}

	//XXX COLUMN DESCRIPTOR METHODS
	public static String getAsString(IColumnDescriptor columnDescriptor, byte[] content) {
		ColumnsTypes columnType = columnDescriptor.getType();
		switch (columnType) {
		case INTEGER:
			ByteBuffer bb = ByteBuffer.allocateDirect(4).put(content);
			Integer value = bb.getInt();
			return String.valueOf(value);
		case STRING:
			return Factory.fromByteArray(content);
		default:
			throw new IllegalArgumentException("UNKNOW COLUMN TYPE");
		}
	}
	
	public static byte[] getAsBytes(IColumnDescriptor columnDescriptor, String data) {
		ColumnsTypes columnType = columnDescriptor.getType();
		switch (columnType) {
		case INTEGER:
			Integer value =  Integer.valueOf(data);
			ByteBuffer bb = ByteBuffer.allocate(Integer.BYTES);
			bb.putInt(value);
			return bb.array();
		case STRING:
			return Factory.toByteArray(data);
		default:
			throw new IllegalArgumentException("UNKNOW COLUMN TYPE");
		}
	}

	//XXX COLUMN ENTRY METHODS
	public static IColumnEntry toColumnEntry(byte[] data) throws IOException{
		Integer dataSizeInt = data.length;
		ByteBuffer bb = ByteBuffer.allocate(Short.BYTES + dataSizeInt);
		bb.putShort(dataSizeInt.shortValue());
		bb.put(data);
		
		IColumnEntry columnEntry = new ColumnEntry(bb.array());
		return columnEntry;
	}
}
