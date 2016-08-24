package br.com.xavier.suricate.dbms.impl;

import br.com.xavier.suricate.dbms.interfaces.IThreeByteValue;
import br.com.xavier.suricate.dbms.interfaces.IThreeBytesValueParametrizedTest;

public class LittleEndianThreeBytesValueParametrizedTest 
		extends IThreeBytesValueParametrizedTest {

	public LittleEndianThreeBytesValueParametrizedTest(Integer value) {
		super(value);
	}

	@Override
	protected IThreeByteValue getInstance() {
		return new LittleEndianThreeBytesValue();
	}

}
