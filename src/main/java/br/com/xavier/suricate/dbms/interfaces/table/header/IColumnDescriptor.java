package br.com.xavier.suricate.dbms.interfaces.table.header;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Objects;

import br.com.xavier.suricate.dbms.Factory;
import br.com.xavier.suricate.dbms.enums.ColumnsTypes;
import br.com.xavier.suricate.dbms.interfaces.low.IBinarizable;
import br.com.xavier.util.StringUtils;

public interface IColumnDescriptor
		extends IBinarizable {
	
	public static final int BYTES_SIZE = 64;
	
	String getName();
	void setName(String name);
	ColumnsTypes getType();
	void setType(ColumnsTypes type);
	Short getSize();
	void setSize(Short size);

	@Override
	default byte[] toByteArray() throws IOException {
		ByteBuffer bb = ByteBuffer.allocate(64);
		
		byte[] byteArray = Factory.toByteArray(getName());
		bb.put(byteArray);
		
		bb.position(61);
		
		bb.putShort(getType().getId());
		bb.putShort(getSize());
		
		return bb.array();
	}
	
	@Override
	default void fromByteArray(byte[] bytes) throws IOException {
		Objects.requireNonNull(bytes, "bytes cannot be null");
		
		if(bytes.length < BYTES_SIZE){
			throw new IllegalArgumentException("bytes length must be of size : " + BYTES_SIZE);
		}
		
		ByteBuffer bb = ByteBuffer.wrap(bytes);
		
		byte[] nameBuffer = new byte[60];
		bb.get(nameBuffer);
		String name = Factory.fromByteArray(nameBuffer);
		if(StringUtils.isNullOrEmpty(name)){
			throw new IOException("Null name for column descriptor");
		} else {
			setName(name);
		}
		
		bb.position(61);
		
		Short columnTypeId = bb.getShort();
		ColumnsTypes type = ColumnsTypes.getById(columnTypeId);
		if(type == null){
			throw new IOException("Unknown column type for id : " + columnTypeId);
		} else {
			setType(type);
		}
		
		Short size = bb.getShort();
		setSize(size);
	}
}
