package br.com.xavier.suricate.dbms.impl;

import br.com.xavier.suricate.dbms.abstractions.AbstractThreeBytesValueTest;
import br.com.xavier.suricate.dbms.impl.low.BigEndianThreeBytesValue;
import br.com.xavier.suricate.dbms.interfaces.low.IThreeByteValue;

public class BigEndianThreeBytesValueTest 
		extends AbstractThreeBytesValueTest {

	@Override
	protected IThreeByteValue getInstance() {
		return new BigEndianThreeBytesValue();
	}
	
}
