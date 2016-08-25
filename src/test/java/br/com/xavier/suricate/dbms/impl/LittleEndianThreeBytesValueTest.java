package br.com.xavier.suricate.dbms.impl;

import br.com.xavier.suricate.dbms.abstractions.AbstractThreeBytesValueTest;
import br.com.xavier.suricate.dbms.impl.low.LittleEndianThreeBytesValue;
import br.com.xavier.suricate.dbms.interfaces.low.IThreeByteValue;

public class LittleEndianThreeBytesValueTest 
		extends AbstractThreeBytesValueTest {

	@Override
	protected IThreeByteValue getInstance() {
		return new LittleEndianThreeBytesValue();
	}

}
