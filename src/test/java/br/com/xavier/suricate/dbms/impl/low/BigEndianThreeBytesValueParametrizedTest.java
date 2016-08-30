package br.com.xavier.suricate.dbms.impl.low;

import br.com.xavier.suricate.dbms.interfaces.low.IThreeByteValue;
import br.com.xavier.suricate.dbms.interfaces.low.IThreeBytesValueParametrizedTest;

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
