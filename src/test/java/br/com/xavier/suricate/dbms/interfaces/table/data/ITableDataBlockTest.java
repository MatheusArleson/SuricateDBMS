package br.com.xavier.suricate.dbms.interfaces.table.data;

import org.junit.After;
import org.junit.Before;

public abstract class ITableDataBlockTest {
	
	//XXX TEST SUBJECT
	private ITableDataBlock instance;
	
	//XXX TEST PROPERTIES
	
	//XXX CONSTRUCTOR
	public ITableDataBlockTest() {}
	
	//XXX ABSTRACT METHODS
	protected abstract ITableDataBlock getInstance();

	//XXX BEFORE METHODS
	@Before
	public void setup() {
		setupInstance();
		setupProperties();
		setupOtherProperties();
	}

	private void setupInstance() {
		this.instance = getInstance();
	}

	private void setupProperties() {
		// TODO Auto-generated method stub
		
	}
	
	private void setupOtherProperties() {
		// TODO Auto-generated method stub
		
	}

	//XXX AFTER METHODS
	@After
	public void destroy() {
		instance = null;
		
	}
	
	//XXX TEST METHODS
	//-------------
	// HEADER
	//-------------
	
	//-------------
	// ROWS
	//-------------
	
	//-------------
	// TO BYTE ARRAY
	//-------------
	
	//-------------
	// FROM BYTE ARRAY
	//-------------

}
