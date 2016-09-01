package br.com.xavier.suricate.dbms.abstractions.table.header;

import java.io.IOException;

import br.com.xavier.suricate.dbms.enums.TableStatus;
import br.com.xavier.suricate.dbms.interfaces.low.IThreeByteValue;
import br.com.xavier.suricate.dbms.interfaces.table.header.ITableHeaderBlockContent;

public abstract class AbstractTableHeaderBlockContent 
		implements ITableHeaderBlockContent {

	private static final long serialVersionUID = 4951516297889090690L;
	
	//XXX PROPERTIES
	private Byte tableId;
	private IThreeByteValue blockSize;
	private TableStatus tableStatus;
	private Integer nextFreeBlockId;
	private Short headerSize;
	
	//XXX CONSTRUCTOR
	public AbstractTableHeaderBlockContent() {
		super();
	}
	
	public AbstractTableHeaderBlockContent(
		Byte tableId, 
		IThreeByteValue blockSize, 
		Short headerSize,
		Integer nextFreeBlockId, 
		TableStatus tableStatus
	) throws IllegalArgumentException {
		super();
		
		setTableId(tableId);
		setBlockSize(blockSize);
		setHeaderSize(headerSize);
		setNextFreeBlockId(nextFreeBlockId);
		setStatus(tableStatus);
	}
	
	public AbstractTableHeaderBlockContent(byte[] bytes) throws IOException {
		try	{
			fromByteArray(bytes);
		} catch(IOException e){
			throw e;
		} catch(IllegalArgumentException e){
			throw new IOException("Illegal value detected.", e);
		}
	}
	
	//XXX OVERRIDE METHODS
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((blockSize == null) ? 0 : blockSize.hashCode());
		result = prime * result + ((headerSize == null) ? 0 : headerSize.hashCode());
		result = prime * result + ((nextFreeBlockId == null) ? 0 : nextFreeBlockId.hashCode());
		result = prime * result + ((tableId == null) ? 0 : tableId.hashCode());
		result = prime * result + ((tableStatus == null) ? 0 : tableStatus.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractTableHeaderBlockContent other = (AbstractTableHeaderBlockContent) obj;
		if (tableId == null) {
			if (other.tableId != null)
				return false;
		} else if (!tableId.equals(other.tableId))
			return false;
		if (tableStatus != other.tableStatus)
			return false;
		if (nextFreeBlockId == null) {
			if (other.nextFreeBlockId != null)
				return false;
		} else if (!nextFreeBlockId.equals(other.nextFreeBlockId))
			return false;
		if (blockSize == null) {
			if (other.blockSize != null)
				return false;
		} else if (!blockSize.equals(other.blockSize))
			return false;
		if (headerSize == null) {
			if (other.headerSize != null)
				return false;
		} else if (!headerSize.equals(other.headerSize))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "AbstractTableHeaderBlockContent [" 
			+ "tableId=" + tableId 
			+ ", blockSize=" + blockSize 
			+ ", headerSize=" + headerSize 
			+ ", nextFreeBlockId=" + nextFreeBlockId 
			+ ", tableStatus=" + tableStatus 
		+ "]";
	}
	
	//XXX GETTERS/SETTERS
	@Override
	public Byte getTableId() {
		if(tableId == null){
			return null;
		}
		
		return new Byte(tableId);
	}

	@Override
	public void setTableId(Byte tableId) {
		if(tableId == null || tableId < 1){
			throw new IllegalArgumentException("Table ID must be a positive non zero number.");
		}
		
		this.tableId = tableId;
	}

	@Override
	public IThreeByteValue getBlockSize() {
		if(blockSize == null){
			return null;
		}
		
		return blockSize.clone();
	}

	@Override
	public void setBlockSize(IThreeByteValue blockSize) {
		if(blockSize == null){
			throw new IllegalArgumentException("Block size must be a positive non zero number.");
		} 
				
		if(blockSize.getValue() < ITableHeaderBlockContent.MINUMUN_BLOCK_SIZE){
			throw new IllegalArgumentException("Block size must be greather than " + ITableHeaderBlockContent.MINUMUN_BLOCK_SIZE + " bytes.");
		}
		
		this.blockSize = blockSize;
	}
	
	@Override
	public Short getHeaderSize() {
		if(headerSize == null){
			return null;
		}
		
		return new Short(headerSize);
	}

	@Override
	public void setHeaderSize(Short headerSize) {
		if(headerSize == null || headerSize < 1){
			throw new IllegalArgumentException("Header size must be a positive non zero number.");
		}
		
		this.headerSize = headerSize;
	}

	@Override
	public Integer getNextFreeBlockId() {
		if(nextFreeBlockId == null){
			return null;
		}
		
		return new Integer(nextFreeBlockId);
	}

	@Override
	public void setNextFreeBlockId(Integer nextFreeBlockId) {
		if(nextFreeBlockId == null || nextFreeBlockId < 1){
			throw new IllegalArgumentException("Next free block ID must be a positive non zero number.");
		}
		
		this.nextFreeBlockId = nextFreeBlockId;
	}

	@Override
	public TableStatus getStatus() {
		return tableStatus;
	}

	@Override
	public void setStatus(TableStatus tableStatus) {
		if(tableStatus == null){
			throw new IllegalArgumentException("Table status must be not null.");
		}
		
		this.tableStatus = tableStatus;
	}

}
