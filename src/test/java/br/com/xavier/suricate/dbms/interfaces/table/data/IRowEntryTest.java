package br.com.xavier.suricate.dbms.interfaces.table.data;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.After;
import org.junit.Before;

import br.com.xavier.suricate.dbms.impl.table.data.ColumnEntry;

public abstract class IRowEntryTest {
	
	//XXX TEST SUBJECT
	private IRowEntry instance;
	
	//XXX TEST PROPERTIES
	Integer entrySize;
	Collection<IColumnEntry> columnsEntries;
	
	Integer otherEntrySize;
	Collection<IColumnEntry> otherColumnsEntries;
	
	//XXX CONSTRUCTOR
	public IRowEntryTest() {}
	
	//XXX ABSTRACT METHODS
	protected abstract IRowEntry getInstance();

	//XXX BEFORE METHODS
	@Before
	public void setup() {
		setupInstance();
		setupProperties();
	}

	private void setupInstance() {
		instance = getInstance();
	}

	private void setupProperties() {
		generateProperties();
		generateOtherProperties();
	}

	private void generateProperties() {
		byte[] content = new String("content").getBytes(StandardCharsets.UTF_16BE);
		Short contentSize = new Integer(content.length).shortValue();
		IColumnEntry columnEntry = new ColumnEntry(contentSize, content);
		
		columnsEntries = new ArrayList<>();
		columnsEntries.add(columnEntry);
		
		entrySize = new Integer(0);
		for (IColumnEntry c : columnsEntries) {
			entrySize = entrySize + c.getContentSize(); 
		}
	}
	
	private void generateOtherProperties() {
		byte[] content = new String("otherContent").getBytes(StandardCharsets.UTF_16BE);
		Short contentSize = new Integer(content.length).shortValue();
		IColumnEntry columnEntry = new ColumnEntry(contentSize, content);
		
		otherColumnsEntries = new ArrayList<>();
		otherColumnsEntries.add(columnEntry);
		
		otherEntrySize = new Integer(0);
		for (IColumnEntry c : otherColumnsEntries) {
			otherEntrySize = otherEntrySize + c.getContentSize(); 
		}
	}

	//XXX AFTER METHODS
	@After
	public void destroy() {
		instance = null;
		entrySize = null;
		columnsEntries = null;
		otherEntrySize = null;
		otherColumnsEntries = null;
	}
	
	//XXX TEST METHODS
	
	
}
