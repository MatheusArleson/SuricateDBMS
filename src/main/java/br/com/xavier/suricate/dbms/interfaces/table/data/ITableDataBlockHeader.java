package br.com.xavier.suricate.dbms.interfaces.table.data;

import br.com.xavier.suricate.dbms.enums.TableBlockType;
import br.com.xavier.suricate.dbms.interfaces.IBinarizable;
import br.com.xavier.suricate.dbms.interfaces.IThreeByteValue;

public interface ITableDataBlockHeader
		extends IBinarizable {

	Byte getTableId();
	void setTableId(Byte id);
	IThreeByteValue getBlockId();
	void setBlockId(IThreeByteValue id);
	TableBlockType getType();
	void setType(TableBlockType type);
	IThreeByteValue getBytesUsedInBlock();
	void setBytesUsedInBlock(IThreeByteValue bytesUsedInBlock);
	
}
