package br.com.xavier.suricate.dbms;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;

import br.com.xavier.suricate.dbms.impl.low.BigEndianThreeBytesValue;
import br.com.xavier.suricate.dbms.interfaces.low.IThreeByteValue;
import br.com.xavier.util.ByteArrayUtils;

public class Factory {
	
	//XXX CONSTRUCTOR
	private Factory(){}
	
	//XXX STRING METHODS
	public static String fromByteArray(byte[] bytes){
		return ByteArrayUtils.fromByteArray(bytes, StandardCharsets.UTF_16);
	}
	
	public static byte[] toByteArray(String str) throws IOException {
		return ByteArrayUtils.toByteArray(str, StandardCharsets.UTF_16);
	}
	
	//XXX FILE METHODS
	public static byte[] getTableHeaderBlockBytes(RandomAccessFile raf) throws IOException {
		IThreeByteValue blockSize = getBlockSize(raf);
		Integer value = blockSize.getValue();
		byte[] buffer = new byte[value];
		
		raf.seek(0);
		raf.read(buffer);
		raf.seek(0);
		
		return buffer;
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
	
	
	
}
