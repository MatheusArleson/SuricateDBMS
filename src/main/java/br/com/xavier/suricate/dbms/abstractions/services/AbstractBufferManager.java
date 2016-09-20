package br.com.xavier.suricate.dbms.abstractions.services;

import java.io.IOException;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Objects;

import br.com.xavier.suricate.dbms.impl.services.FileSystemManager;
import br.com.xavier.suricate.dbms.interfaces.low.IThreeByteValue;
import br.com.xavier.suricate.dbms.interfaces.services.IBufferManager;
import br.com.xavier.suricate.dbms.interfaces.services.IFileSystemManager;
import br.com.xavier.suricate.dbms.interfaces.table.access.IRowId;
import br.com.xavier.suricate.dbms.interfaces.table.data.ITableDataBlock;

public class AbstractBufferManager
		implements IBufferManager {
	
	//XXX DEPENDENCIES PROPERTIES
	private IFileSystemManager fileSystemManager;
	
	//XXX BUFFER PROPERTIES
	private int bufferSlots;
	private Deque<ITableDataBlock> bufferDeque;
	
	//XXX STATISTICS PROPERTIES
	private Long acessCount;
	private Long hitCount;
	
	//XXX CONSTRUCTOR
	public AbstractBufferManager(IFileSystemManager fileSystemManager, int bufferSlots) {
		this.fileSystemManager = Objects.requireNonNull(fileSystemManager);
		this.bufferDeque = new LinkedList<>();
		this.acessCount = new Long(0L);
		this.hitCount = new Long(0L);
	}
	
	//XXX OVERRIDE METHODS
	@Override
	public ITableDataBlock getDataBlock(IRowId rowId) {
		validateRowID(rowId);
		acessCount++;
		
		ITableDataBlock dataBlock = isBlockInMemory(rowId);
		boolean hit = false;
		if(dataBlock != null){
			hit = true;
			hitCount++;
			return dataBlock;
		} 
			
		dataBlock = fetchBlockFromFileSystem(rowId);
		
		swapOut(dataBlock, hit);
		swapIn(dataBlock);
		
		return dataBlock;
	}
	
	@Override
	public void flush() throws IOException {
		for (ITableDataBlock dataBlock : bufferDeque) {
			fileSystemManager.writeDataBlock(dataBlock);
		}
	}

	//XXX PRIVATE METHODS
	private void validateRowID(IRowId rowId) {
		if(rowId == null){
			throw new IllegalArgumentException("Null rowId instance.");
		}
		
		Byte tableId = rowId.getTableId();
		if(tableId == null){
			throw new IllegalArgumentException("RowId instance has null table Id.");
		}
		
		IThreeByteValue blockId = rowId.getBlockId();
		if(blockId == null){
			throw new IllegalArgumentException("RowId instance has null block Id.");
		}
		
		Long byteOffset = rowId.getByteOffset();
		if(byteOffset == null){
			throw new IllegalArgumentException("RowId instance has null byte offset.");
		}
	}
	
	private ITableDataBlock isBlockInMemory(IRowId rowId) {
		if(bufferDeque.isEmpty()){
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
	
	private ITableDataBlock fetchBlockFromFileSystem(IRowId rowId) {
		return fileSystemManager.readDataBlock(rowId);
	}
	
	private void swapIn(ITableDataBlock dataBlock) {
		bufferDeque.addFirst(dataBlock);
	}

	private void swapOut(ITableDataBlock dataBlock, boolean hit) {
		if(hit){
			bufferDeque.remove(dataBlock);
		} else {
			ITableDataBlock dataBlockRemoved = bufferDeque.removeLast();
			fileSystemManager.writeDataBlock(dataBlockRemoved);
		}
	}

}
