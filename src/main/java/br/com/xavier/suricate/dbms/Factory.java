package br.com.xavier.suricate.dbms;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.io.IOUtils;

import br.com.xavier.suricate.dbms.impl.low.BigEndianThreeBytesValue;
import br.com.xavier.suricate.dbms.interfaces.low.IBinarizable;
import br.com.xavier.suricate.dbms.interfaces.low.IThreeByteValue;

public class Factory {
	
	//XXX CONSTRUCTOR
	private Factory(){}
	
	//XXX BYTE ARRAY METHODS
	@SuppressWarnings("unchecked")
	public static byte[] toByteArray(Collection<? extends IBinarizable> collection) throws IOException{
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			
			Iterator<IBinarizable> iterator = (Iterator<IBinarizable>) collection.iterator();
			while(iterator.hasNext()){
				IBinarizable element = iterator.next();
				baos.write(element.toByteArray());
			}
			
			baos.flush();
			return baos.toByteArray();
			
		} catch (IOException e) {
			throw e;
		} finally {
			if(baos != null){
				IOUtils.closeQuietly(baos);
			}
		}
	}
	
	public static byte[] toByteArray(byte[]... bytes) throws IOException{
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			
			for (byte[] byteArray : bytes) {
				baos.write(byteArray);
			}
			
			baos.flush();
			return baos.toByteArray();
			
		} catch (IOException e) {
			throw e;
		} finally {
			if(baos != null){
				IOUtils.closeQuietly(baos);
			}
		}
	}
	
	public static byte[] toByteArray(String str) throws IOException {
		return str.getBytes(StandardCharsets.UTF_16);
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
