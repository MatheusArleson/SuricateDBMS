package br.com.xavier.suricate.dbms.enums;

public enum TableBlockType {
	
	//XXX ENUM MEMBERS
	DATA("Data", "1"),
	INDEX("Index", "2");
	
	//XXX PROPERTIES
	private final String description;
	private final Byte id;
	
	//XXX CONSTRUCTOR
	private TableBlockType(String description, String id) {
		this.description = description;
		this.id = new Byte(id);
	}
	
	//XXX METHODS
	public static TableBlockType getTypeById(Byte id){
		if(id == null){
			return null;
		}
		
		for (TableBlockType type : values()) {
			if(type.getId().equals(id)){
				return type;
			}
		}
		
		return null;
	}
	
	//XXX GETTERS
	public String getDescription() {
		return description;
	}
	
	public Byte getId() {
		return id;
	}

}
