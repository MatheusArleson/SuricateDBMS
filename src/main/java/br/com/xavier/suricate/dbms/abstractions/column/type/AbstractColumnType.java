package br.com.xavier.suricate.dbms.abstractions.column.type;

import java.nio.ByteBuffer;

import br.com.xavier.suricate.dbms.interfaces.IColumnType;

public class AbstractColumnType
		implements IColumnType {

	private static final long serialVersionUID = 7230787742719322303L;

	//XXX PROPERTIES
	private Short id;
	private String name;
	private Short size;
	
	//XXX CONSTRUCTOR
	protected AbstractColumnType(Short id, Short size, String name) {
		super();
		this.id = id;
		this.name = name;
		this.size = size;
	}
	
	//XXX OVERRIDE METHODS
	@Override
	public byte[] toByteArray() {
		ByteBuffer buffer = ByteBuffer.allocate(4);
		
		byte[] nameBytes = name.getBytes();
		buffer.put(nameBytes);
		buffer.position(61);
		
		buffer.putShort(id);
		buffer.putShort(size);
		
		return buffer.array();
	}
	
	protected void setSize(Short size) {
		this.size = size;
	}
	
	//XXX GETTERS/SETTERS
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public Short getId() {
		return id;
	}
	
	@Override
	public Short getSize() {
		return size;
	}
	
}
