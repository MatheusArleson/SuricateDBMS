package br.com.xavier.suricate.dbms.interfaces;

import java.util.Collection;

import br.com.xavier.suricate.dbms.enums.TableBlockType;

public interface ITableBlock
		extends IBinarizable<ITableBlock> {
	
	Byte getTableId();
	void setTableId(Byte id);
	IThreeByteValue getBlockId();
	void setBlockId(IThreeByteValue id);
	TableBlockType getType();
	void setType(TableBlockType type);
	IThreeByteValue getBytesUsedInBlock();
	void setBytesUsedInBlock(IThreeByteValue bytesUsedInBlock);
	Collection<IRow> getRows();
	void setRows(Collection<IRow> rows);
	
}
