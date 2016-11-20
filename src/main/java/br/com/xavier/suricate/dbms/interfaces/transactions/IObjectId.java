package br.com.xavier.suricate.dbms.interfaces.transactions;

import java.io.Serializable;

import br.com.xavier.suricate.dbms.interfaces.low.IThreeByteValue;

public interface IObjectId extends Serializable {
	
	Long FULL_BYTE_OFFSET = -1L;
	
	Byte getTableId();
	void setTableId(Byte tableId);
	
	IThreeByteValue getBlockId();
	void setBlockId(IThreeByteValue blockId);
	
	Long getByteOffset();
	void setByteOffset(Long byteOffset);
	
	static void validate(IObjectId ObjectId) {
		if(ObjectId == null){
			throw new IllegalArgumentException("Null ObjectId instance.");
		}
		
		Byte tableId = ObjectId.getTableId();
		
		if(tableId == null){
			throw new IllegalArgumentException("ObjectId instance must have at least table id.");
		} else {
			if(tableId < 1){
				throw new IllegalArgumentException("ObjectId instance have an invalid table id.");
			}
		}
		
		IThreeByteValue blockId = ObjectId.getBlockId();
		if(blockId != null && blockId.getValue() < 1){
			throw new IllegalArgumentException("ObjectId instance have an invalid block id.");
		}
		
		Long byteOffset = ObjectId.getByteOffset();
		if(byteOffset != null && byteOffset < 0 &&  !byteOffset.equals(FULL_BYTE_OFFSET)){
			throw new IllegalArgumentException("ObjectId instance have an invalid byte offset id.");
		}
	}
	
}
