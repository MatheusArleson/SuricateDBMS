package br.com.xavier.suricate.dbms.impl;

import br.com.xavier.suricate.dbms.interfaces.IThreeByteValue;
import br.com.xavier.suricate.dbms.interfaces.IThreeBytesValueParametrizedTest;

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
