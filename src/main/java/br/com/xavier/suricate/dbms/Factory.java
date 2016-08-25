package br.com.xavier.suricate.dbms;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.io.IOUtils;

import br.com.xavier.suricate.dbms.interfaces.low.IBinarizable;

public class Factory {
	
	//XXX CONSTRUCTOR
	private Factory(){}
	
	//XXX METHODS
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
	
	
	
}
