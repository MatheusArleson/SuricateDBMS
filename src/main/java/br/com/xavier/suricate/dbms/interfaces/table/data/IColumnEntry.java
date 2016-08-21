package br.com.xavier.suricate.dbms.interfaces.table.data;

import br.com.xavier.suricate.dbms.interfaces.IBinarizable;

public interface IColumnEntry
		extends IBinarizable<IColumnEntry> {
	
	Short getContentSize();
	void setContentSize(Short size);
	byte[] getContent();
	void setContent(byte[] content);

}
