package br.com.xavier.suricate.dbms.abstractions.table.data;

import java.io.IOException;

import br.com.xavier.suricate.dbms.enums.TableBlockType;
import br.com.xavier.suricate.dbms.interfaces.low.IThreeByteValue;
import br.com.xavier.suricate.dbms.interfaces.table.data.ITableDataBlockHeader;

public abstract class AbstractTableDataBlockHeader 
		implements ITableDataBlockHeader {

	private static final long serialVersionUID = 5415718742346223052L;

	//XXX PROPERTIES
	private Byte tableId;
	private IThreeByteValue blockId;
	private TableBlockType type;
	private IThreeByteValue bytesUsedInBlock;
	
	//XXX CONSTRUCTORS
	public AbstractTableDataBlockHeader() {
		super();
	}
	
	public AbstractTableDataBlockHeader(
		Byte tableId, 
		IThreeByteValue blockId, 
		TableBlockType type,
		IThreeByteValue bytesUsedInBlock
	) {
		super();
		setTableId(tableId);
		setBlockId(blockId);
		setType(type);
		setBytesUsedInBlock(bytesUsedInBlock);
	}
	
	public AbstractTableDataBlockHeader(byte[] bytes) throws IOException {
		super();
		fromByteArray(bytes);
	}

	//XXX OVERRIDE METHODS
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((blockId == null) ? 0 : blockId.hashCode());
		result = prime * result + ((bytesUsedInBlock == null) ? 0 : bytesUsedInBlock.hashCode());
		result = prime * result + ((tableId == null) ? 0 : tableId.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		AbstractTableDataBlockHeader other = (AbstractTableDataBlockHeader) obj;
		if (blockId == null) {
			if (other.blockId != null)
				return false;
		} else if (!blockId.equals(other.blockId))
			return false;
		if (bytesUsedInBlock == null) {
			if (other.bytesUsedInBlock != null)
				return false;
		} else if (!bytesUsedInBlock.equals(other.bytesUsedInBlock))
			return false;
		if (tableId == null) {
			if (other.tableId != null)
				return false;
		} else if (!tableId.equals(other.tableId))
			return false;
		if (type != other.type)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "AbstractTableDataBlockHeader [" 
			+ "tableId=" + tableId 
			+ ", blockId=" + blockId 
			+ ", type=" + type
			+ ", bytesUsedInBlock=" + bytesUsedInBlock 
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
	public void setTableId(Byte id) {
		if(id == null || id < 1){
			throw new IllegalArgumentException("Table ID must be a positive non zero number.");
		}
		
		this.tableId = id;
	}

	@Override
	public IThreeByteValue getBlockId() {
		if(blockId == null){
			return null;
		}
		
		return blockId.clone();
	}

	@Override
	public void setBlockId(IThreeByteValue id) {
		if(id == null || id.getValue() < 1){
			throw new IllegalArgumentException("Block ID must be a positive non zero number.");
		}
		
		this.blockId = id;
	}

	@Override
	public TableBlockType getType() {
		return type;
	}

	@Override
	public void setType(TableBlockType type) {
		if(type == null){
			throw new IllegalArgumentException("Block type must be not null.");
		}
		
		this.type = type;
	}

	@Override
	public IThreeByteValue getBytesUsedInBlock() {
		if(bytesUsedInBlock == null){
			return null;
		}
		
		return bytesUsedInBlock.clone();
	}

	@Override
	public void setBytesUsedInBlock(IThreeByteValue bytesUsedInBlock) {
		if(bytesUsedInBlock == null || bytesUsedInBlock.getValue() < 1){
			throw new IllegalArgumentException("Bytes used in block must be a positive non zero number.");
		}
		
		this.bytesUsedInBlock = bytesUsedInBlock;
	}

}
