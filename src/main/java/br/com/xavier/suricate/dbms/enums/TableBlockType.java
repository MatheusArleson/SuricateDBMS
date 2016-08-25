package br.com.xavier.suricate.dbms.enums;

public enum TableBlockType {
	
	;
	
	//XXX PROPERTIES
	private final String description;
	private final Byte value;
	
	//XXX CONSTRUCTOR
	private TableBlockType(String description, Byte value) {
		this.description = description;
		this.value = value;
	}
	
	//XXX GETTERS
	public String getDescription() {
		return description;
	}
	
	public Byte getValue() {
		return value;
	}

}
