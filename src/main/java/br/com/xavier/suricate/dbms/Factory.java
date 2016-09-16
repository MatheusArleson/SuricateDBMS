package br.com.xavier.suricate.dbms;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;

import br.com.xavier.suricate.dbms.enums.FileModes;
import br.com.xavier.suricate.dbms.impl.low.BigEndianThreeBytesValue;
import br.com.xavier.suricate.dbms.interfaces.low.IThreeByteValue;
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
	
}
