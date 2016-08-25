package br.com.xavier.suricate.dbms.impl;

import br.com.xavier.suricate.dbms.impl.low.BigEndianThreeBytesValue;
import br.com.xavier.suricate.dbms.interfaces.IThreeBytesValueParametrizedTest;
import br.com.xavier.suricate.dbms.interfaces.low.IThreeByteValue;

public class BigEndianThreeBytesValueParametrizedTest 
		extends IThreeBytesValueParametrizedTest {

	public BigEndianThreeBytesValueParametrizedTest(Integer value) {
		super(value);
	}

	@Override
	protected IThreeByteValue getInstance() {
		return new BigEndianThreeBytesValue();
	}
	
}
