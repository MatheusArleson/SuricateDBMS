package br.com.xavier.suricate.dbms.abstractions.services;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

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
	private Deque<ITableDataBlock> bufferDeque;
	
	//XXX STATISTICS PROPERTIES
	private Long acessCount;
	private Long hitCount;
	
	//XXX CONSTRUCTOR
	public AbstractBufferManager(IFileSystemManager fileSystemManager, int bufferSlots) {
		this.fileSystemManager = Objects.requireNonNull(fileSystemManager);
		this.bufferDeque = new ArrayDeque<>(bufferSlots);
		this.acessCount = new Long(0L);
		this.hitCount = new Long(0L);
	}
	
	//XXX OVERRIDE METHODS
	@Override
	public ITableDataBlock fetchBlock(IRowId rowId) {
		validateRowID(rowId);
		
		acessCount++;
		
		boolean blockInMemory = isBlockInMemory(rowId.getTableId(), rowId.getBlockId());
		if(blockInMemory){
			hitCount++;
			//bufferDeque.remove
		}
		
		return null;
	}
	
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
	
	@Override
	public boolean isBlockInMemory(Byte tableId, IThreeByteValue blockId) {
		if(tableId == null){
			throw new IllegalArgumentException("Null table id.");
		}
		
		if(blockId == null || blockId.getValue() == null){
			throw new IllegalArgumentException("Null block id.");
		}
		
		if(bufferDeque.isEmpty()){
			return false;
		}
		
		ITableDataBlock bufferedDB = fetchBlock(tableId, blockId);
		if(bufferedDB != null){
			return true;
		}
		
		return false;
	}

	private ITableDataBlock fetchBlock(Byte tableId, IThreeByteValue blockId) {
		ITableDataBlock bufferedDB = bufferDeque.stream()
												.filter(p -> p.getHeader().getTableId().equals(tableId) && p.getHeader().getBlockId().equals(blockId))
												.findFirst()
												.orElse(null);
		return bufferedDB;
	}

	

	@Override
	public void swapIn() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void swapOut() {
		// TODO Auto-generated method stub
		
	}

	

	@Override
	public void flush() {
		// TODO Auto-generated method stub
		
	}

}
