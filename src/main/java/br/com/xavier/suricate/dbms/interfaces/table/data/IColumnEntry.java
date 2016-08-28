package br.com.xavier.suricate.dbms.interfaces.table.data;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Objects;

import br.com.xavier.suricate.dbms.interfaces.low.IBinarizable;

public interface IColumnEntry
		extends IBinarizable {
	
	Short getContentSize();
	void setContentSize(Short size);
	byte[] getContent();
	void setContent(byte[] content);
	
	@Override
	default byte[] toByteArray() throws IOException {
		int size = getContent().length + 2;
		ByteBuffer bb = ByteBuffer.allocate(size);
		
		bb.putShort(getContentSize());
		bb.put(getContent());
		
		return bb.array();
	}
	
	@Override
	default void fromByteArray(byte[] bytes) throws IOException {
		Objects.requireNonNull(bytes, "bytes cannot be null");
		
		ByteBuffer bb = ByteBuffer.wrap(bytes);
		Short contentSize = bb.getShort();
		
		byte[] contentBuffer = new byte[contentSize];
		bb.get(contentBuffer);
		setContent(contentBuffer);
	}

}
