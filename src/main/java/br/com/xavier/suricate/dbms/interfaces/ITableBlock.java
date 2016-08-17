package br.com.xavier.suricate.dbms.interfaces;

import java.util.Collection;

import br.com.xavier.suricate.dbms.enums.TableBlockType;

public interface ITableBlock
		extends IBinarizable<ITableBlock> {
	
	Byte getTableId();
	void setTableId(Byte id);
	Integer getBlockId();
	void setBlockId(Integer id);
	TableBlockType getType();
	void setType(TableBlockType type);
	Integer getBytesUsedInBlock();
	void setBytesUsedInBlock(Integer bytesUsedInBlock);
	Collection<IRow> getRows();
	void setRows(Collection<IRow> rows);
	
}
