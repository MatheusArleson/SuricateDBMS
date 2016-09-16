package br.com.xavier.suricate.dbms.interfaces.table;

import org.junit.After;
import org.junit.Before;

import br.com.xavier.suricate.dbms.interfaces.table.header.ITableHeaderBlockContent;

public abstract class ITableTest {

	//XXX TEST SUBJECT
	private ITable instance;
	
	//XXX TEST PROPERTIES
	
	//XXX CONSTRUCTOR
	public ITableTest() {}
	
	//XXX ABSTRACT METHODS
	protected abstract ITable getInstance();
	
	//XXX BEFORE METHODS
	@Before
	public void setup() {
		setupInstance();
		setupProperties();
		setupOtherProperties();
	}
	
	private void setupInstance() {
		instance = getInstance();
	}
	
	private void setupProperties() {
		
	}
	
	private void setupOtherProperties() {
		
	}
	
	//XXX AFTER METHODS
	@After
	public void destroy() {
		instance = null;
	}
	
	//XXX TEST METHODS
	
	//-------------
	// FILE
	//-------------
	
	//-------------
	// HEADER BLOCK
	//-------------
	
	//-------------
	// DATA BLOCKS
	//-------------
	
	//-------------
	// TO BYTE ARRAY
	//-------------
	
	//-------------
	// FROM BYTE ARRAY
	//-------------
}
