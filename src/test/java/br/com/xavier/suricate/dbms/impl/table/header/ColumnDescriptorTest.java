package br.com.xavier.suricate.dbms.impl.table.header;

import br.com.xavier.suricate.dbms.abstractions.table.header.AbstractColumnDescriptorTest;
import br.com.xavier.suricate.dbms.interfaces.table.header.IColumnDescriptor;

public class ColumnDescriptorTest 
		extends AbstractColumnDescriptorTest {

	@Override
	protected IColumnDescriptor getInstance() {
		return new ColumnDescriptor();
	}

}
