package br.com.xavier.suricate.dbms.impl;

import br.com.xavier.suricate.dbms.impl.low.LittleEndianThreeBytesValue;
import br.com.xavier.suricate.dbms.interfaces.IThreeBytesValueParametrizedTest;
import br.com.xavier.suricate.dbms.interfaces.low.IThreeByteValue;

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
