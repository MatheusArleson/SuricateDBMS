package br.com.xavier.suricate.dbms.impl.column.type;

import br.com.xavier.suricate.dbms.abstractions.column.type.AbstractColumnSizeVariable;
import br.com.xavier.suricate.dbms.enums.ColumnsTypesInfo;

public class StringColumnType 
	extends AbstractColumnSizeVariable {

	private static final long serialVersionUID = 586036255847332572L;

	public StringColumnType(Short size) {
		super(ColumnsTypesInfo.STRING.getId(), size, ColumnsTypesInfo.STRING.getName());
	}

}
