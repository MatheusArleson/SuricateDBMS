package br.com.xavier.suricate.dbms.interfaces.table.data;

import br.com.xavier.suricate.dbms.interfaces.low.IBinarizable;

public interface IColumnEntry
		extends IBinarizable {
	
	Short getContentSize();
	void setContentSize(Short size);
	byte[] getContent();
	void setContent(byte[] content);

}
