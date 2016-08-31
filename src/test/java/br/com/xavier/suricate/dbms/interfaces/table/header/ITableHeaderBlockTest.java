package br.com.xavier.suricate.dbms.interfaces.table.header;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.xavier.suricate.dbms.enums.ColumnsTypes;
import br.com.xavier.suricate.dbms.enums.TableStatus;
import br.com.xavier.suricate.dbms.impl.low.BigEndianThreeBytesValue;
import br.com.xavier.suricate.dbms.impl.table.header.ColumnDescriptor;
import br.com.xavier.suricate.dbms.impl.table.header.TableHeaderBlockContent;

public abstract class ITableHeaderBlockTest {
	
	//XXX TEST SUBJECT
	private ITableHeaderBlock instance;
	
	//XXX TEST PROPERTIES
	private ITableHeaderBlockContent headerContent;
	private Collection<IColumnDescriptor> columnsDescriptors;
	
	private ITableHeaderBlockContent otherHeaderContent;
	private Collection<IColumnDescriptor> otherColumnsDescriptors;
	
	//XXX CONSTRUCTOR
	public ITableHeaderBlockTest() {}
	
	//XXX ABSTRACT METHODS
	protected abstract ITableHeaderBlock getInstance();

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
		headerContent = new TableHeaderBlockContent();
		headerContent.setTableId(Byte.MIN_VALUE);
		headerContent.setBlockSize(new BigEndianThreeBytesValue(4096));
		headerContent.setStatus(TableStatus.VALID);
		headerContent.setNextFreeBlockId(0);
		
		columnsDescriptors = new ArrayList<>();
		
		IColumnDescriptor colDesc1 = new ColumnDescriptor();
		colDesc1.setName("col1");
		colDesc1.setType(ColumnsTypes.INTEGER);
		
		IColumnDescriptor colDesc2 = new ColumnDescriptor();
		colDesc2.setName("col2");
		colDesc2.setType(ColumnsTypes.STRING);
		colDesc2.setSize(new Short("3"));
		
		columnsDescriptors.add(colDesc1);
		columnsDescriptors.add(colDesc2);
		
		int columnsDescriptorsSize = columnsDescriptors.size() * 64;
		Short headerSize = new Integer(ITableHeaderBlockContent.BYTES_SIZE + columnsDescriptorsSize).shortValue();
		
		headerContent.setHeaderSize(headerSize);
	}

	private void generateOtherProperties() {
		otherHeaderContent = new TableHeaderBlockContent();
		otherHeaderContent.setTableId(Byte.MAX_VALUE);
		otherHeaderContent.setBlockSize(new BigEndianThreeBytesValue(8192));
		otherHeaderContent.setStatus(TableStatus.INVALID);
		otherHeaderContent.setNextFreeBlockId(1);
		
		otherColumnsDescriptors = new ArrayList<>();
		
		IColumnDescriptor colDesc1 = new ColumnDescriptor();
		colDesc1.setName("col3");
		colDesc1.setType(ColumnsTypes.STRING);
		colDesc1.setSize(new Short("4"));
		
		IColumnDescriptor colDesc2 = new ColumnDescriptor();
		colDesc2.setName("col4");
		colDesc2.setType(ColumnsTypes.INTEGER);
		
		otherColumnsDescriptors.add(colDesc1);
		otherColumnsDescriptors.add(colDesc2);
		
		int columnsDescriptorsSize = otherColumnsDescriptors.size() * 64;
		Short headerSize = new Integer(ITableHeaderBlockContent.BYTES_SIZE + columnsDescriptorsSize).shortValue();
		
		otherHeaderContent.setHeaderSize(headerSize);
	}

	//XXX AFTER METHODS
	@After
	public void destroy() {
		instance = null;
		headerContent = null;
		columnsDescriptors = null;
	}
	
	//XXX TEST METHODS
	@Test
	public void getAndSetHeaderContentTest(){
		instance.setHeaderContent(headerContent);
		
		assertSame(headerContent, instance.getHeaderContent());
		assertNotSame(otherHeaderContent, instance.getHeaderContent());
		
		instance.setHeaderContent(otherHeaderContent);
		
		assertSame(otherHeaderContent, instance.getHeaderContent());
		assertNotSame(headerContent, instance.getHeaderContent());
	}
	
	@Test(expected = NullPointerException.class)
	public void setHeaderContentMustThrowNullPointerExceptionOnNullValue(){
		
		instance.setHeaderContent(null);
		
	}
	
	@Test
	public void getAndSetColumnsDescriptorsTest(){
		instance.setColumnsDescriptor(columnsDescriptors);
		
		assertSame(columnsDescriptors, instance.getColumnsDescriptors());
		assertNotSame(otherColumnsDescriptors, instance.getColumnsDescriptors());
		
		instance.setColumnsDescriptor(otherColumnsDescriptors);
		
		assertSame(otherColumnsDescriptors, instance.getColumnsDescriptors());
		assertNotSame(columnsDescriptors, instance.getColumnsDescriptors());
	}
	
	@Test(expected = NullPointerException.class)
	public void setColumnsDescriptorsMustThrowNullPointerExceptionOnNullCollection(){
		
		instance.setColumnsDescriptor(null);
		
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void setColumnsDescriptorsMustThrowIllegalArgumentExceptionOnEmptyCollection(){
		ArrayList<IColumnDescriptor> columnsDescriptors = new ArrayList<>();
		
		instance.setColumnsDescriptor(columnsDescriptors);
		
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void setColumnsDescriptorsMustThrowIllegalArgumentOnCollectionWithNullValues(){
		columnsDescriptors.add(null);
		
		instance.setColumnsDescriptor(columnsDescriptors);
	}
	
	@Test
	public void toByteArrayTest() throws IOException {
		instance.setHeaderContent(headerContent);
		instance.setColumnsDescriptor(columnsDescriptors);
		
		byte[] bytes = instance.toByteArray();
		
		assertNotNull(bytes);
		assertThat(bytes.length, is(not(0)));
	}
	
	@Test(expected = IOException.class)
	public void toByteArrayMustThrowIOExceptionOnNullTableHeaderBlockContent() throws IOException {
		instance.setColumnsDescriptor(columnsDescriptors);
		
		instance.toByteArray();
	}
	
	@Test(expected = IOException.class)
	public void toByteArrayMustThrowIOExceptionOnNullColumnsDescriptorsCollection() throws IOException {
		instance.setHeaderContent(headerContent);
		
		instance.toByteArray();
	}
	
	@Test
	public void fromByteArrayTest() throws IOException {
		instance.setHeaderContent(headerContent);
		instance.setColumnsDescriptor(columnsDescriptors);
		byte[] bytes = instance.toByteArray();
		
		instance = getInstance();
		instance.fromByteArray(bytes);
		
		assertEquals(headerContent, instance.getHeaderContent());
		assertEquals(columnsDescriptors, instance.getColumnsDescriptors());
	}
	
	@Test(expected = NullPointerException.class)
	public void fromByteArrayMustThrowNullPointerExceptionOnNullByteArray() throws IOException {
		byte[] nullBytes = null;
		
		instance.fromByteArray(nullBytes);
	}
	
}
