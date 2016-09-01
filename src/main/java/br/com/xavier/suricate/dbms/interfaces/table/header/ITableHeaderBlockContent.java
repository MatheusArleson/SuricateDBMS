package br.com.xavier.suricate.dbms.interfaces.table.header;

import java.io.IOException;
import java.nio.ByteBuffer;

import br.com.xavier.suricate.dbms.enums.TableStatus;
import br.com.xavier.suricate.dbms.impl.low.BigEndianThreeBytesValue;
import br.com.xavier.suricate.dbms.interfaces.low.IBinarizable;
import br.com.xavier.suricate.dbms.interfaces.low.IThreeByteValue;
import br.com.xavier.util.ObjectsUtils;

public interface ITableHeaderBlockContent
		extends IBinarizable {
	
	public static final int BYTES_SIZE = 11;
	public static final int MINUMUN_BLOCK_SIZE = 75;
	
	Byte getTableId();
	void setTableId(Byte tableId);
	IThreeByteValue getBlockSize();
	void setBlockSize(IThreeByteValue blockSize);
	Short getHeaderSize();
	void setHeaderSize(Short headerSize);
	Integer getNextFreeBlockId();
	void setNextFreeBlockId(Integer nextFreeBlockId);
	TableStatus getStatus();
	void setStatus(TableStatus tableStatus);
	
	@Override
	default byte[] toByteArray() throws IOException {
		ByteBuffer bb = ByteBuffer.allocate(BYTES_SIZE);
			
		Byte tableId = getTableId();
		IThreeByteValue blockSize = getBlockSize();
		TableStatus status = getStatus();
		Integer nextFreeBlockId = getNextFreeBlockId();
		Short headerSize = getHeaderSize();
		
		boolean anyNull = ObjectsUtils.anyNull(tableId, blockSize, status, nextFreeBlockId, headerSize);
		if(anyNull){
			throw new IOException("To transform to byte[] all properties must not be null.");
		}
		
		bb.put(tableId);
		bb.put(blockSize.getValueBinary());
		bb.put(status.getValue());
		bb.putInt(nextFreeBlockId);
		bb.putShort(headerSize);
			
		return bb.array();
	}
	
	default void fromByteArray(byte[] bytes) throws IOException {
		try {
			if(bytes == null){
				throw new IOException("bytes cannot be null");
			}
			
			if(bytes.length != BYTES_SIZE){
				throw new IllegalArgumentException("bytes length must be of size : " + BYTES_SIZE);
			}
			
			ByteBuffer bb = ByteBuffer.wrap(bytes);
			
			setTableId(bb.get());
			
			byte[] blockSize = new byte[3];
			bb.get(blockSize);
			setBlockSize(new BigEndianThreeBytesValue(blockSize));
			
			Byte statusByte = bb.get();
			TableStatus tableStatus = TableStatus.getStatusByValue(statusByte);
			if(tableStatus == null){
				throw new IOException("Unknow table status id : " + statusByte);
			} else {
				setStatus(tableStatus);
			}
			
			Integer nextFreeBlockId = bb.getInt();
			setNextFreeBlockId(nextFreeBlockId);
			
			Short headerSize = bb.getShort();		
			setHeaderSize(headerSize);
		} catch(IOException e){
			throw e;
		} catch(Exception e){
			throw new IOException("Error while parsing bytes.", e);
		}
	}
	
}
