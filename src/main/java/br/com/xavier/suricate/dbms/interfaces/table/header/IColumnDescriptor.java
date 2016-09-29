package br.com.xavier.suricate.dbms.interfaces.table.header;

import java.io.IOException;
import java.nio.ByteBuffer;

import br.com.xavier.suricate.dbms.Factory;
import br.com.xavier.suricate.dbms.enums.ColumnsTypes;
import br.com.xavier.suricate.dbms.interfaces.low.IBinarizable;
import br.com.xavier.suricate.dbms.interfaces.services.ITextSeparators;
import br.com.xavier.util.ObjectsUtils;
import br.com.xavier.util.StringUtils;

public interface IColumnDescriptor
		extends IBinarizable {
	
	public static final int BYTES_SIZE = 64;
	public static final int MAX_COLUMN_NAME_LENGTH = 30;
	
	String getName();
	void setName(String name);
	ColumnsTypes getType();
	void setType(ColumnsTypes type);
	Short getSize();
	void setSize(Short size);

	String printData(ITextSeparators separators);
	
	@Override
	default byte[] toByteArray() throws IOException {
		ByteBuffer bb = ByteBuffer.allocate(64);
		
		String columnName = getName();
		ColumnsTypes columnType = getType();
		Short columnSize = getSize();
		
		boolean anyNull = ObjectsUtils.anyNull(columnName, columnType, columnSize);
		if(anyNull){
			throw new IOException("To transform to byte[] all properties must not be null.");
		}
		
		byte[] byteArray = Factory.toByteArray(columnName);
		bb.put(byteArray);
		
		bb.position(60);
		
		bb.putShort(columnType.getId());
		bb.putShort(columnSize);
		
		return bb.array();
	}
	
	@Override
	default void fromByteArray(byte[] bytes) throws IOException {
		try {
			if(bytes == null){
				throw new IOException("bytes cannot be null");
			}
			
			if(bytes.length != BYTES_SIZE){
				throw new IOException("bytes length must be of size : " + BYTES_SIZE);
			}
			
			ByteBuffer bb = ByteBuffer.wrap(bytes);
			
			byte[] nameBuffer = new byte[60];
			bb.get(nameBuffer);
			String name = Factory.fromByteArray(nameBuffer);
			
			if(StringUtils.isNullOrEmpty(name)){
				throw new IOException("Null name for column descriptor");
			} else {
				setName(name.trim());
			}
			
			bb.position(60);
			
			Short columnTypeId = bb.getShort();
			ColumnsTypes type = ColumnsTypes.getById(columnTypeId);
			if(type == null){
				throw new IOException("Unknown column type for id : " + columnTypeId);
			} else {
				setType(type);
			}
			
			Short size = bb.getShort();
			setSize(size);
		} catch(IOException e){
			throw e;
		} catch(Exception e){
			throw new IOException("Error while parsing bytes.", e);
		}
	}
	
}
