package br.com.xavier.suricate.dbms.interfaces.table.data;

import java.io.IOException;
import java.nio.ByteBuffer;

import br.com.xavier.suricate.dbms.interfaces.low.IBinarizable;
import br.com.xavier.util.ObjectsUtils;

public interface IColumnEntry
		extends IBinarizable {
	
	public static final Short CONTENT_MIN_LENGTH = 4;
	public static final Short CONTENT_MAX_LENGTH = Short.MAX_VALUE;  
	
	Short getContentSize();
	byte[] getContent();
	void setContent(byte[] content);
	
	@Override
	default byte[] toByteArray() throws IOException {
		Short contentSize = getContentSize();
		byte[] content = getContent();
		
		boolean anyNull = ObjectsUtils.anyNull(contentSize, content);
		if(anyNull){
			throw new IOException("To transform to byte[] all properties must not be null.");
		}
		
		int size = content.length + Short.BYTES;
		ByteBuffer bb = ByteBuffer.allocate(size);
		
		bb.putShort(contentSize);
		bb.put(content);
		
		return bb.array();
	}
	
	@Override
	default void fromByteArray(byte[] bytes) throws IOException {
		try{
			if(bytes == null){
				throw new IOException("bytes cannot be null");
			}
			
			ByteBuffer bb = ByteBuffer.wrap(bytes);
			Short contentSize = bb.getShort();
			
			byte[] contentBuffer = new byte[contentSize];
			bb.get(contentBuffer);
			
			setContent(contentBuffer);
			
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			throw new IOException("Error while parsing the bytes.", e);
		}
	}

}
