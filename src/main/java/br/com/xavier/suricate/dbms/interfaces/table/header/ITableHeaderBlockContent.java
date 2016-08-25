package br.com.xavier.suricate.dbms.interfaces.table.header;

import br.com.xavier.suricate.dbms.enums.TableStatus;
import br.com.xavier.suricate.dbms.interfaces.low.IBinarizable;
import br.com.xavier.suricate.dbms.interfaces.low.IThreeByteValue;

public interface ITableHeaderBlockContent
		extends IBinarizable {
	
	Byte getTableId();
	void setTableId(Byte tableId);
	IThreeByteValue getBlockSize();
	void setBlockSize(IThreeByteValue blockSize);
	Short getHeaderSize();
	void setHeaderSize(Short headerSize);
	Integer getNextFreeBlockId();
	void setNextFreeBlockId(Integer nextFreeBlockId);
	TableStatus getStatus();
	void setStatus(TableStatus tableStatus);
	
}
