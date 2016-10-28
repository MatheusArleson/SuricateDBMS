package br.com.xavier.suricate.dbms.abstractions.transactions;

import br.com.xavier.suricate.dbms.interfaces.low.IThreeByteValue;
import br.com.xavier.suricate.dbms.interfaces.table.access.IRowId;

public abstract class AbstractObjectId 
			implements IRowId {

	private static final long serialVersionUID = 3157969748202106314L;
	
	//XXX PROPERTIES
	private Byte tableId;
	private IThreeByteValue blockId;
	private Long byteOffset;
	
	//XXX CONSTRUCTOR
	public AbstractObjectId(Byte tableId, IThreeByteValue blockId, Long byteOffset) {
		super();
		setTableId(tableId);
		setBlockId(blockId);
		setByteOffset(byteOffset);
	}
	
	//XXX OVERRIDE METHODS
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((blockId == null) ? 0 : blockId.hashCode());
		result = prime * result + ((byteOffset == null) ? 0 : byteOffset.hashCode());
		result = prime * result + ((tableId == null) ? 0 : tableId.hashCode());
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
		AbstractObjectId other = (AbstractObjectId) obj;
		if (tableId == null) {
			if (other.tableId != null)
				return false;
		} else if (!tableId.equals(other.tableId))
			return false;
		if (blockId == null) {
			if (other.blockId != null)
				return false;
		} else if (!blockId.equals(other.blockId))
			return false;
		if (byteOffset == null) {
			if (other.byteOffset != null)
				return false;
		} else if (!byteOffset.equals(other.byteOffset))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "AbstractRowId [" 
			+ "tableId=" + tableId 
			+ ", blockId=" + blockId 
			+ ", byteOffset=" + byteOffset 
		+ "]";
	}

	//XXX GETTERS/SETTERS
	@Override
	public Byte getTableId() {
		return new Byte(tableId);
	}

	@Override
	public void setTableId(Byte tableId) {
		if(tableId == null || tableId < 0){
			throw new IllegalArgumentException("Invalid table id.");
		}
		
		this.tableId = new Byte(tableId);
	}

	@Override
	public IThreeByteValue getBlockId() {
		return blockId.clone();
	}

	@Override
	public void setBlockId(IThreeByteValue blockId) {
		if(blockId == null || blockId.getValue() < 0){
			throw new IllegalArgumentException("Invalid block id.");
		}
		
		this.blockId = blockId.clone();
	}

	@Override
	public Long getByteOffset() {
		return new Long(byteOffset);
	}

	@Override
	public void setByteOffset(Long byteOffset) {
		if(byteOffset == null || (byteOffset < 0 && !byteOffset.equals(IRowId.FULL_BYTE_OFFSET)) ){
			throw new IllegalArgumentException("Invalid byte offset.");
		}
		
		this.byteOffset = new Long(byteOffset);
	}
	
}
