package br.com.xavier.suricate.dbms.interfaces.table.header;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Objects;

import br.com.xavier.suricate.dbms.enums.TableStatus;
import br.com.xavier.suricate.dbms.impl.low.BigEndianThreeBytesValue;
import br.com.xavier.suricate.dbms.interfaces.low.IBinarizable;
import br.com.xavier.suricate.dbms.interfaces.low.IThreeByteValue;

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
			
		bb.put(getTableId());
		bb.put(getBlockSize().getValueBinary());
		bb.put(getStatus().getValue());
		bb.putInt(getNextFreeBlockId());
		bb.putShort(getHeaderSize());
			
		return bb.array();
	}
	
	default void fromByteArray(byte[] bytes) throws IOException {
		Objects.requireNonNull(bytes, "bytes cannot be null");
		
		if(bytes.length < BYTES_SIZE){
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
	}
	
}
