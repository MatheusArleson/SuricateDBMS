package br.com.xavier.suricate.dbms.interfaces.table.data;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.xavier.suricate.dbms.impl.table.data.ColumnEntry;
import br.com.xavier.util.ByteArrayUtils;

public abstract class IRowEntryTest {
	
	//XXX TEST SUBJECT
	private IRowEntry instance;
	
	//XXX TEST PROPERTIES
	private Integer rowEntrySize;
	private Integer columnsEntriesSize;
	private Collection<IColumnEntry> columnsEntries;
	private byte[] propertiesBytes;
	
	private Integer otherEntrySize;
	private Collection<IColumnEntry> otherColumnsEntries;
	
	//XXX CONSTRUCTOR
	public IRowEntryTest() {}
	
	//XXX ABSTRACT METHODS
	protected abstract IRowEntry getInstance();

	//XXX BEFORE METHODS
	@Before
	public void setup() throws IOException {
		setupInstance();
		setupProperties();
	}

	private void setupInstance() {
		instance = getInstance();
	}

	private void setupProperties() throws IOException {
		generateProperties();
		generateOtherProperties();
	}

	private void generateProperties() throws IOException {
		byte[] content = new String("c").getBytes(StandardCharsets.UTF_16BE);
		Short contentSize = ((Integer) content.length).shortValue();
		
		ByteBuffer bb = ByteBuffer.allocate(contentSize + Short.BYTES);
		bb.putShort(contentSize);
		bb.put(content);
		
		IColumnEntry columnEntry = new ColumnEntry(bb.array());
		
		columnsEntries = new ArrayList<>();
		columnsEntries.add(columnEntry);
		
		columnsEntriesSize = new Integer(0);
		for (IColumnEntry c : columnsEntries) {
			columnsEntriesSize = columnsEntriesSize + (c.getEntrySize()); 
		}
		
		byte[] columnsEntriesBytes = ByteArrayUtils.toByteArray(columnsEntries);
		rowEntrySize = columnsEntriesBytes.length + Integer.BYTES;
		
		bb = ByteBuffer.allocate(rowEntrySize);
		
		bb.putInt(rowEntrySize);
		bb.put(columnsEntriesBytes);
		
		propertiesBytes = bb.array();
	}
	
	private void generateOtherProperties() throws IOException {
		byte[] content = new String("o").getBytes(StandardCharsets.UTF_16BE);
		Short contentSize = ((Integer) content.length).shortValue();
		
		ByteBuffer bb = ByteBuffer.allocate(contentSize + Short.BYTES);
		bb.putShort(contentSize);
		bb.put(content);
		
		IColumnEntry columnEntry = new ColumnEntry(bb.array());
		
		otherColumnsEntries = new ArrayList<>();
		otherColumnsEntries.add(columnEntry);
		
		otherEntrySize = new Integer(0);
		for (IColumnEntry c : otherColumnsEntries) {
			otherEntrySize = otherEntrySize + (c.getEntrySize()); 
		}
		
		//byte[] columnsEntriesBytes = ByteArrayUtils.toByteArray(otherColumnsEntries);
		//Integer size = columnsEntriesBytes.length + Integer.BYTES;
		
		//bb = ByteBuffer.allocate(size);
		
		//bb.putInt(otherEntrySize);
		//bb.put(columnsEntriesBytes);
		
		//otherPropertiesBytes = bb.array();
	}

	//XXX AFTER METHODS
	@After
	public void destroy() {
		instance = null;
		
		rowEntrySize = null;
		columnsEntriesSize = null;
		columnsEntries = null;
		propertiesBytes = null;
		
		otherEntrySize = null;
		otherColumnsEntries = null;
	}
	
	//XXX TEST METHODS
	
	//-------------
	// ENTRY SIZE
	//-------------
	@Test
	public void getEntrySizeTest(){
		instance.setColumnsEntries(columnsEntries);
		
		assertEquals(rowEntrySize, instance.getEntrySize());
	}
	
	//-------------
	// COLUMNS ENTRIES SIZE
	//-------------
	@Test
	public void getColumnsEntrySizeTest(){
		instance.setColumnsEntries(columnsEntries);
		
		assertEquals(columnsEntriesSize, instance.getColumnsEntrySize());
	}
	
	@Test
	public void getColumnsEntrySizeReferenceMustBeAClone(){
		instance.setColumnsEntries(columnsEntries);
		Integer sizeClone = instance.getColumnsEntrySize();
		
		assertNotSame(columnsEntriesSize, instance.getColumnsEntrySize());
		assertEquals(columnsEntriesSize, instance.getColumnsEntrySize());
		
		assertNotSame(sizeClone, instance.getColumnsEntrySize());
		assertEquals(sizeClone, instance.getColumnsEntrySize());
	}
	
	//-------------
	// COLUMNS ENTRIES
	//-------------
	@Test(expected = IllegalArgumentException.class)
	public void setColumnsEntriesMustThrowIllegalArgumentExceptionOnNullValue(){
		instance.setColumnsEntries(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void setColumnsEntriesMustThrowIllegalArgumentExceptionOnEmptyCollectionValue(){
		instance.setColumnsEntries(Collections.emptyList());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void setColumnsEntriesMustThrowIllegalArgumentExceptionOnCollectionWithNullValues(){
		columnsEntries.add(null);
		
		instance.setColumnsEntries(columnsEntries);
	}
	
	@Test
	public void setColumnsEntriesMustAlsoSetEntrySize(){
		instance.setColumnsEntries(columnsEntries);
		
		assertEquals(columnsEntriesSize, instance.getColumnsEntrySize());
	}
	
	@Test
	public void setColumnsEntriesTest(){
		instance.setColumnsEntries(columnsEntries);
	}
	
	@Test
	public void getColumnsEntriesReferenceMustBeAClone(){
		instance.setColumnsEntries(columnsEntries);
		
		assertNotSame(columnsEntries, instance.getColumnsEntries());
		assertEquals(columnsEntries, instance.getColumnsEntries());
	}
	
	@Test
	public void getColumnsEntriesTest(){
		instance.setColumnsEntries(columnsEntries);
		
		assertEquals(columnsEntries, instance.getColumnsEntries());
	}
	
	//-------------
	// TO BYTE ARRAY
	//-------------
	@Test(expected = IOException.class)
	public void toByteArrayMustThrowIOExceptionOnNullColumnsEntries() throws IOException {
		instance.toByteArray();
	}
	
	@Test
	public void toByteArrayTest() throws IOException {
		instance.setColumnsEntries(columnsEntries);
		
		byte[] bytes = instance.toByteArray();
		
		assertNotNull(bytes);
		assertEquals(propertiesBytes.length, bytes.length);
		assertArrayEquals(propertiesBytes, bytes);
	}
	
	//-------------
	// FROM BYTE ARRAY
	//-------------
	@Test(expected = IOException.class)
	public void fromByteArrayMustThrowIOExceptionOnNullByteArray() throws IOException {
		byte[] nullBytes = null;
		
		instance.fromByteArray(nullBytes);
	}
	
	@Test(expected = IOException.class)
	public void fromByteArrayMustThrowIOExceptionOnInvalidRowEntrySizeOnValue() throws IOException {
		propertiesBytes[0] = 0xf;
		
		instance.fromByteArray(propertiesBytes);
	}
	
	@Test
	public void fromByteArrayMustRestoreEqualProperties() throws IOException {
		instance.fromByteArray(propertiesBytes);
		
		assertEquals(columnsEntries, instance.getColumnsEntries());
		assertEquals(columnsEntriesSize, instance.getColumnsEntrySize());
	}
	
	@Test
	public void fromByteArrayMustProduceEqualInstance() throws IOException{
		IRowEntry otherInstance = getInstance();
		otherInstance.setColumnsEntries(columnsEntries);
		
		instance.fromByteArray(propertiesBytes);
		
		assertEquals(instance, otherInstance);
	}
		
}
