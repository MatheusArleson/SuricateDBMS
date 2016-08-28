package br.com.xavier.suricate.dbms.abstractions.table.header;

import java.io.IOException;

import br.com.xavier.suricate.dbms.enums.ColumnsTypes;
import br.com.xavier.suricate.dbms.interfaces.table.header.IColumnDescriptor;

public abstract class AbstractColumnDescriptor 
		implements IColumnDescriptor {

	private static final long serialVersionUID = -3743957379502298125L;
	
	//XXX PROPERTIES
	private String name;
	private ColumnsTypes type;
	private Short size;
	
	//XXX CONSTRUCTORS
	public AbstractColumnDescriptor(String name, ColumnsTypes type, Short size) {
		super();
		this.name = name;
		this.type = type;
		this.size = size;
	}
	
	public AbstractColumnDescriptor(byte[] bytes) throws IOException {
		super();
		fromByteArray(bytes);
	}

	//XXX OVERRIDE METHODS
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((size == null) ? 0 : size.hashCode());
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
		AbstractColumnDescriptor other = (AbstractColumnDescriptor) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (size == null) {
			if (other.size != null)
				return false;
		} else if (!size.equals(other.size))
			return false;
		if (type != other.type)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "AbstractColumnDescriptor [" 
			+ "name=" + name 
			+ ", type=" + type 
			+ ", size=" + size 
		+ "]";
	}

	//XXX GETTERS/SETTERS
	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public ColumnsTypes getType() {
		return type;
	}

	@Override
	public void setType(ColumnsTypes type) {
		this.type = type;
	}

	@Override
	public Short getSize() {
		return size;
	}

	@Override
	public void setSize(Short size) {
		this.size = size;
	}

}
