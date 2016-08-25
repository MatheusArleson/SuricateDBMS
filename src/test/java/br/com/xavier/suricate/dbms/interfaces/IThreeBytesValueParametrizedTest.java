package br.com.xavier.suricate.dbms.interfaces;

import java.util.Arrays;
import java.util.Collection;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import br.com.xavier.suricate.dbms.interfaces.low.IThreeByteValue;

@RunWith(Parameterized.class)
public abstract class IThreeBytesValueParametrizedTest extends IThreeBytesValueTest {

	@Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] { { 0 }, { 1 }, { 2 }, { 5 }, { 255 }, { 256 }, { 511 }, { 512 }, { 1024 }, { IThreeByteValue.MAX_VALUE } });
	}
	
	public IThreeBytesValueParametrizedTest(Integer value) {
		super(value);
	}

}
