package br.com.xavier.suricate.dbms.interfaces.table.header;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import br.com.xavier.suricate.dbms.impl.table.header.ColumnDescriptor;
import br.com.xavier.suricate.dbms.impl.table.header.TableHeaderBlockContent;
import br.com.xavier.suricate.dbms.interfaces.low.IBinarizable;
import br.com.xavier.util.ByteArrayUtils;
import br.com.xavier.util.ObjectsUtils;

public interface ITableHeaderBlock
		extends IBinarizable {
	
	ITableHeaderBlockContent getHeaderContent();
	void setHeaderContent(ITableHeaderBlockContent headerData);
	Collection<IColumnDescriptor> getColumnsDescriptors();
	void setColumnsDescriptor(Collection<IColumnDescriptor> columnsDescriptors);
	
	Integer getRowSize();
	Integer getNumberOfRowsPerBlock();
	
	@Override
	default byte[] toByteArray() throws IOException {
		ITableHeaderBlockContent headerContent = getHeaderContent();
		Collection<IColumnDescriptor> columnsDescriptors = getColumnsDescriptors();
		
		boolean anyNull = ObjectsUtils.anyNull(headerContent, columnsDescriptors);
		if(anyNull){
			throw new IOException("To transform to byte[] all properties must not be null.");
		}
		
		byte[] headerContentBytes = headerContent.toByteArray();
		byte[] columnsDescriptorsBytes = ByteArrayUtils.toByteArray(columnsDescriptors);
		
		byte[] byteArray = ByteArrayUtils.toByteArray(headerContentBytes, columnsDescriptorsBytes);
		return byteArray;
	}
	
	@Override
	default void fromByteArray(byte[] bytes) throws IOException {
		Objects.requireNonNull(bytes, "bytes cannot be null");
		
		int minimunSize = ITableHeaderBlockContent.BYTES_SIZE + IColumnDescriptor.BYTES_SIZE;
		if(bytes.length < minimunSize){
			throw new IOException("bytes length must be at least of size : " + minimunSize);
		}
		
		byte[] headerContentBuffer = new byte[ITableHeaderBlockContent.BYTES_SIZE];
		ByteBuffer bb = ByteBuffer.wrap(bytes);
		bb.get(headerContentBuffer);
		
		ITableHeaderBlockContent headerBlockContent = new TableHeaderBlockContent(headerContentBuffer);
		setHeaderContent(headerBlockContent);
		
		Short columnsDescriptorsSize = headerBlockContent.getHeaderSize();
		if(bb.remaining() < columnsDescriptorsSize){
			throw new IOException("Columns descriptors : Byte underflow : must be of lenght : " + columnsDescriptorsSize);
		}
		
		int columnDescriptorByteSize = IColumnDescriptor.BYTES_SIZE;
		int descriptorsQtd = columnsDescriptorsSize / columnDescriptorByteSize;
		byte[] columnDescriptorBuffer = new byte[columnDescriptorByteSize];
		
		Collection<IColumnDescriptor> columnsDescriptors = new ArrayList<>();
		while(descriptorsQtd > 0){
			bb.get(columnDescriptorBuffer);
			
			IColumnDescriptor columnDescriptor = new ColumnDescriptor(columnDescriptorBuffer);
			columnsDescriptors.add(columnDescriptor);
			
			descriptorsQtd--;
		}
		
		setColumnsDescriptor(columnsDescriptors);
	}
	
}
