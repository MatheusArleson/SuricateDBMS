package br.com.xavier.suricate.dbms.interfaces.table.data;

import br.com.xavier.suricate.dbms.interfaces.IDeserializable;
import br.com.xavier.suricate.dbms.interfaces.ISerializable;

public interface IColumnEntry
		extends ISerializable, IDeserializable<IColumnEntry> {
	
	Short getContentSize();
	void setContentSize(Short size);
	byte[] getContent();
	void setContent(byte[] content);

}
