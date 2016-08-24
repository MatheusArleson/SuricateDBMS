package br.com.xavier.suricate.dbms.impl;

import br.com.xavier.suricate.dbms.abstractions.AbstractThreeBytesValueTest;
import br.com.xavier.suricate.dbms.interfaces.IThreeByteValue;

public class BigEndianThreeBytesValueTest 
		extends AbstractThreeBytesValueTest {

	@Override
	protected IThreeByteValue getInstance() {
		return new BigEndianThreeBytesValue();
	}
	
}
