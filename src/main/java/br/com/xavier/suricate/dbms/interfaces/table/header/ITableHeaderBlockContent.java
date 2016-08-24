package br.com.xavier.suricate.dbms.interfaces.table.header;

import br.com.xavier.suricate.dbms.enums.TableStatus;
import br.com.xavier.suricate.dbms.interfaces.IBinarizable;
import br.com.xavier.suricate.dbms.interfaces.IThreeByteValue;

public interface ITableHeaderBlockContent
		extends IBinarizable {
	
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
	
}
