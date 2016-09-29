package br.com.xavier.suricate.dbms.interfaces.table.access;

import java.io.Serializable;

import br.com.xavier.suricate.dbms.interfaces.low.IThreeByteValue;

public interface IRowId
		extends Serializable {
	
	Long FULL_BYTE_OFFSET = -1L;
	
	Byte getTableId();
	void setTableId(Byte tableId);
	IThreeByteValue getBlockId();
	void setBlockId(IThreeByteValue blockId);
	Long getByteOffset();
	void setByteOffset(Long byteOffset);
	
	static void validate(IRowId rowId) {
		if(rowId == null){
			throw new IllegalArgumentException("Null rowId instance.");
		}
		
		Byte tableId = rowId.getTableId();
		if(tableId == null){
			throw new IllegalArgumentException("RowId instance has null table Id.");
		}
		
		IThreeByteValue blockId = rowId.getBlockId();
		if(blockId == null){
			throw new IllegalArgumentException("RowId instance has null block Id.");
		}
		
		Long byteOffset = rowId.getByteOffset();
		if(byteOffset == null){
			throw new IllegalArgumentException("RowId instance has null byte offset.");
		}
	}

}
