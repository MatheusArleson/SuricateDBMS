package br.com.xavier.suricate.dbms.interfaces;

import br.com.xavier.suricate.dbms.enums.TableStatus;

public interface ITableHeader
		extends IBinarizable<ITableHeader> {
	
	Byte getTableId();
	void setTableId(Byte tableId);
	Integer getBlockSize();
	void setBlockSize(Integer id);
	Short getHeaderSize();
	void setHeaderSize(Short headerSize);
	Integer getNextFreeBlockId();
	void setNextFreeBlockId(Integer nextFreeBlockId);
	TableStatus getStatus();
	void setStatus(TableStatus status);
	ITableColumnsDescriptor getTableColumnsDescriptor();
	void setTableColumnsDescriptor(ITableColumnsDescriptor tableColumnsDescriptor);
	
}
