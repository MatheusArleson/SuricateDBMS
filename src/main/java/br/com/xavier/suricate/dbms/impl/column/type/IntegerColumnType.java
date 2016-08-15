package br.com.xavier.suricate.dbms.impl.column.type;

import br.com.xavier.suricate.dbms.abstractions.column.type.AbstractColumnSizeFixed;
import br.com.xavier.suricate.dbms.enums.ColumnsTypesInfo;

public class IntegerColumnType
		extends AbstractColumnSizeFixed {

	private static final long serialVersionUID = -1827281354556688365L;
	
	//XXX CONSTANTS
	private static final Short INTEGER_SIZE = 4;
	
	//XXX CONSTRUCTOR
	public IntegerColumnType() {
		super(ColumnsTypesInfo.INTEGER.getId(), ColumnsTypesInfo.INTEGER.getName());
	}
	
	//XXX OVERRIDE METHODS
	@Override
	public Short getSize() {
		return INTEGER_SIZE;
	}
	
}
