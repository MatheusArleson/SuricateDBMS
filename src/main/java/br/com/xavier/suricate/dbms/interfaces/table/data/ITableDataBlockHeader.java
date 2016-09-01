package br.com.xavier.suricate.dbms.interfaces.table.data;

import java.io.IOException;
import java.nio.ByteBuffer;

import br.com.xavier.suricate.dbms.enums.TableBlockType;
import br.com.xavier.suricate.dbms.impl.low.BigEndianThreeBytesValue;
import br.com.xavier.suricate.dbms.interfaces.low.IBinarizable;
import br.com.xavier.suricate.dbms.interfaces.low.IThreeByteValue;
import br.com.xavier.util.ObjectsUtils;

public interface ITableDataBlockHeader
		extends IBinarizable {

	public static final int BYTES_SIZE = 8;

	Byte getTableId();
	void setTableId(Byte id);
	IThreeByteValue getBlockId();
	void setBlockId(IThreeByteValue id);
	TableBlockType getType();
	void setType(TableBlockType type);
	IThreeByteValue getBytesUsedInBlock();
	void setBytesUsedInBlock(IThreeByteValue bytesUsedInBlock);
	
	@Override
	default byte[] toByteArray() throws IOException {
		ByteBuffer bb = ByteBuffer.allocate(BYTES_SIZE);
		
		Byte tableId = getTableId();
		IThreeByteValue blockId = getBlockId();
		TableBlockType type = getType();
		IThreeByteValue bytesUsedInBlock = getBytesUsedInBlock();
		
		boolean anyNull = ObjectsUtils.anyNull(tableId, blockId, type, bytesUsedInBlock);
		if(anyNull){
			throw new IOException("To transform to byte[] all properties must not be null.");
		}
		
		bb.put(tableId);
		bb.put(blockId.getValueBinary());
		bb.put(type.getId());
		bb.put(bytesUsedInBlock.getValueBinary());
		
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
			
			Byte tableId = bb.get();
			setTableId(tableId);
			
			byte[] blockIdBuffer = new byte[3];
			bb.get(blockIdBuffer);
			IThreeByteValue blockId = new BigEndianThreeBytesValue(blockIdBuffer);
			setBlockId(blockId);
			
			Byte blockTypeId = bb.get();
			TableBlockType type = TableBlockType.getTypeById(blockTypeId);
			setType(type);
			
			byte[] bytesUserInBlockBuffer = new byte[3];
			bb.get(bytesUserInBlockBuffer);
			IThreeByteValue bytesUsedInBlock = new BigEndianThreeBytesValue(bytesUserInBlockBuffer);
			setBytesUsedInBlock(bytesUsedInBlock);
			
		} catch(IOException e){
			throw e;
		} catch (Exception e) {
			throw new IOException("Error while parsing bytes.", e);
		}
	}
}
