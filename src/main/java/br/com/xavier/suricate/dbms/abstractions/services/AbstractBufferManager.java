package br.com.xavier.suricate.dbms.abstractions.services;

import java.io.IOException;
import java.util.Collection;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Objects;
import java.util.stream.Collectors;

import br.com.xavier.suricate.dbms.interfaces.low.IThreeByteValue;
import br.com.xavier.suricate.dbms.interfaces.services.IBufferManager;
import br.com.xavier.suricate.dbms.interfaces.services.IFileSystemManager;
import br.com.xavier.suricate.dbms.interfaces.table.ITable;
import br.com.xavier.suricate.dbms.interfaces.table.access.IRowId;
import br.com.xavier.suricate.dbms.interfaces.table.data.ITableDataBlock;
import br.com.xavier.suricate.dbms.interfaces.table.header.ITableHeaderBlock;
import br.com.xavier.suricate.dbms.interfaces.table.header.ITableHeaderBlockContent;

public abstract class AbstractBufferManager
		implements IBufferManager {
	
	//XXX DEPENDENCIES PROPERTIES
	private IFileSystemManager fileSystemManager;
	
	//XXX BUFFER PROPERTIES
	private Integer bufferSlots;
	private Integer slotsFilled;
	private Deque<ITableDataBlock> bufferDeque;
	
	//XXX STATISTICS PROPERTIES
	private Long acessCount;
	private Long hitCount;
	
	//XXX CONSTRUCTOR
	public AbstractBufferManager(IFileSystemManager fileSystemManager, Integer bufferSlots) {
		this.fileSystemManager = Objects.requireNonNull(fileSystemManager);
		this.bufferSlots = bufferSlots;
		this.slotsFilled = new Integer(0);
		this.bufferDeque = new LinkedList<>();
		this.acessCount = new Long(0L);
		this.hitCount = new Long(0L);
	}
	
	//XXX OVERRIDE METHODS
	@Override
	public ITableDataBlock getDataBlock(IRowId rowId) throws IOException {
		IRowId.validate(rowId);
		acessCount++;
		
		ITableDataBlock dataBlock = isBlockInMemory(rowId);
		if(dataBlock != null){
			hitCount++;
			swapOut(dataBlock);
		} else {
			dataBlock = fetchBlockFromFileSystem(rowId);
		} 
		
		swapIn(dataBlock);
		return dataBlock;
	}
	
	@Override
	public void purge(ITable table) {
		if(table == null){
			return;
		}
		
		if(isBufferDequeEmpty()){
			return;
		}
		
		ITableHeaderBlock headerBlock = table.getHeaderBlock();
		ITableHeaderBlockContent headerContent = headerBlock.getHeaderContent();
		Byte tableId = headerContent.getTableId();
		
		Collection<ITableDataBlock> toRemove = bufferDeque.stream()
			.filter(db -> db.getHeader().getTableId().equals(tableId))
			.collect(Collectors.toCollection(LinkedList::new));
		
		bufferDeque.removeAll(toRemove);
		slotsFilled = slotsFilled - toRemove.size();
	}
	
	@Override
	public void flush() throws IOException {
		for (ITableDataBlock dataBlock : bufferDeque) {
			fileSystemManager.writeDataBlock(dataBlock);
		}
	}

	//XXX PRIVATE METHODS
	private ITableDataBlock isBlockInMemory(IRowId rowId) {
		boolean isBufferEmpty = isBufferDequeEmpty();
		if(isBufferEmpty){
			return null;
		}
		
		ITableDataBlock bufferedDB = fetchBlockFromBuffer(rowId);
		return bufferedDB;
	}
	
	private ITableDataBlock fetchBlockFromBuffer(IRowId rowId) {
		Byte tableId = rowId.getTableId();
		IThreeByteValue blockId = rowId.getBlockId();
		
		ITableDataBlock bufferedDB = bufferDeque.stream()
												.filter(p -> p.getHeader().getTableId().equals(tableId) && p.getHeader().getBlockId().equals(blockId))
												.findFirst()
												.orElse(null);
		return bufferedDB;
	}
	
	private ITableDataBlock fetchBlockFromFileSystem(IRowId rowId) throws IOException {
		return fileSystemManager.readDataBlock(rowId);
	}
	
	private void swapIn(ITableDataBlock dataBlock) throws IOException {
		if(isBufferDequeFull()){
			swapOut(null);
		}
		
		bufferDeque.addFirst(dataBlock);
		slotsFilled++;
	}

	private void swapOut(ITableDataBlock dataBlock) throws IOException {
		boolean isBufferEmpty = isBufferDequeEmpty();
		if(isBufferEmpty){
			return;
		}
		
		if(dataBlock != null){
			bufferDeque.remove(dataBlock);
		} else {
			ITableDataBlock dataBlockRemoved = bufferDeque.removeLast();
			fileSystemManager.writeDataBlock(dataBlockRemoved);
		}
		
		slotsFilled--;
	}

	private boolean isBufferDequeEmpty() {
		return slotsFilled.equals(0);
	}
	
	private boolean isBufferDequeFull() {
		return slotsFilled.equals(bufferSlots);
	}

}
