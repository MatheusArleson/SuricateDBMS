package br.com.xavier.suricate.dbms.interfaces.table.access;

import java.io.Serializable;

import br.com.xavier.suricate.dbms.interfaces.low.IThreeByteValue;

public interface IRowId
		extends Serializable {
	
	Byte getTableId();
	void setTableId(Byte tableId);
	IThreeByteValue getBlockId();
	void setBlockId(IThreeByteValue blockId);
	Long getByteOffset();
	void setByteOffset(Long byteOffset);

}
