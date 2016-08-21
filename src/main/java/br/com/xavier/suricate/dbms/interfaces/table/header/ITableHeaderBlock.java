package br.com.xavier.suricate.dbms.interfaces.table.header;

import java.util.Collection;

import br.com.xavier.suricate.dbms.enums.TableStatus;
import br.com.xavier.suricate.dbms.interfaces.IBinarizable;
import br.com.xavier.suricate.dbms.interfaces.IThreeByteValue;

public interface ITableHeaderBlock
		extends IBinarizable<ITableHeaderBlock> {
	
	Byte getTableId();
	void setTableId(Byte tableId);
	IThreeByteValue getBlockSize();
	void setBlockSize(IThreeByteValue id);
	Short getHeaderSize();
	void setHeaderSize(Short headerSize);
	Integer getNextFreeBlockId();
	void setNextFreeBlockId(Integer nextFreeBlockId);
	TableStatus getStatus();
	void setStatus(TableStatus status);
	Collection<IColumnDescriptor> getColumnsDescriptors();
	void setColumnsDescriptor(Collection<IColumnDescriptor> columnsDescriptors);
	
}
