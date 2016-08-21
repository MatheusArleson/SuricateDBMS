package br.com.xavier.suricate.dbms.enums;

public enum TableStatus {
	
	//XXX ENUM MEMBERS
	VALID("VALID", "1");
	
	//XXX PROPERTIES
	private final String description;
	private final Byte value;
	
	//XXX CONSTRUCTOR
	private TableStatus(String description, String value) {
		this.description = description;
		this.value = new Byte(value);
	}
	
	public String getDescription() {
		return description;
	}
	
	public Byte getValue() {
		return new Byte(value);
	}

}
