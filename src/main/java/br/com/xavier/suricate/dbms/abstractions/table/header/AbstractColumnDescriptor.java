package br.com.xavier.suricate.dbms.abstractions.table.header;

import java.io.IOException;

import br.com.xavier.suricate.dbms.enums.ColumnsTypes;
import br.com.xavier.suricate.dbms.interfaces.table.header.IColumnDescriptor;
import br.com.xavier.util.StringUtils;

public abstract class AbstractColumnDescriptor 
		implements IColumnDescriptor {

	private static final long serialVersionUID = -3743957379502298125L;
	
	//XXX PROPERTIES
	private String name;
	private ColumnsTypes type;
	private Short size;
	
	//XXX CONSTRUCTORS
	public AbstractColumnDescriptor() {
		super();
	}
	
	public AbstractColumnDescriptor(String name, ColumnsTypes type, Short size) {
		super();
		setName(name);
		setType(type);
		setSize(size);
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
		if(name == null){
			return null;
		}
		
		return new String(name);
	}
	
	@Override
	public void setName(String name) {
		if(StringUtils.isNullOrEmpty(name)){
			throw new IllegalArgumentException("Column name must be a not empty string.");
		}
		
		if(name.length() > IColumnDescriptor.MAX_COLUMN_NAME_LENGTH){
			throw new IllegalArgumentException("Maximum length for column name is " + IColumnDescriptor.MAX_COLUMN_NAME_LENGTH + " characters.");
		}
		
		this.name = name.trim();
	}

	@Override
	public ColumnsTypes getType() {
		return type;
	}
	
	@Override
	public void setType(ColumnsTypes type) {
		if(type == null){
			throw new IllegalArgumentException("Column type must be not null.");
		}
		
		this.type = type;
		switch (type) {
		case INTEGER:
			this.size = new Short("4");
			return;
		
		case STRING:
		default:
			this.size = null;
			return;
		}
	}

	@Override
	public Short getSize() {
		if(size == null){
			return null;
		}
		
		return new Short(size);
	}
	
	@Override
	public void setSize(Short size) {
		if(size == null || size < 1){
			throw new IllegalArgumentException("Size must be a positive number.");
		}
		
		if(type == null){
			this.size = size;
			return;
		}
		
		switch (type) {
		case INTEGER:
			this.size = new Short("4");
			return;
		
		case STRING:
		default:
			this.size = size;
			return;
		}
	}

}
