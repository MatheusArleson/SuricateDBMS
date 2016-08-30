package br.com.xavier.suricate.dbms.impl.low;

import br.com.xavier.suricate.dbms.abstractions.low.AbstractThreeBytesValueTest;
import br.com.xavier.suricate.dbms.interfaces.low.IThreeByteValue;

public class BigEndianThreeBytesValueTest 
		extends AbstractThreeBytesValueTest {

	@Override
	protected IThreeByteValue getInstance() {
		return new BigEndianThreeBytesValue();
	}
	
}
