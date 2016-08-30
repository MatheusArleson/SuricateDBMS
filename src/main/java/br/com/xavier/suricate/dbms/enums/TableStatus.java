package br.com.xavier.suricate.dbms.enums;

public enum TableStatus {
	
	//XXX ENUM MEMBERS
	VALID("VALID", "1"),
	INVALID("INVALID", "2");
	
	//XXX PROPERTIES
	private final String description;
	private final Byte value;
	
	//XXX CONSTRUCTOR
	private TableStatus(String description, String value) {
		this.description = description;
		this.value = new Byte(value);
	}
	
	//XXX METHODS
	public static TableStatus getStatusByValue(Byte value){
		if(value == null){
			return null;
		}
		
		for (TableStatus status : values()) {
			if(status.getValue().equals(value)){
				return status;
			}
		}
		
		return null;
	}
	
	//XXX GETTERS
	public String getDescription() {
		return description;
	}
	
	public Byte getValue() {
		return new Byte(value);
	}

}
