package br.com.xavier.suricate.dbms.enums;

public enum ColumnsTypes {
	
	//XXX ENUM MEMBERS
	INTEGER("1", "Integer"),
	STRING("2", "String");
	
	//XXX PROPERTIES
	private final Short id;
	private final String name;
	
	//XXX CONSTRUCTOR
	private ColumnsTypes(String id, String name) {
		this.id = new Short(id);
		this.name = name;
	}
	
	//XXX METHODS
	public static ColumnsTypes getById(Short id){
		if(id == null){
			return null;
		}
		
		for (ColumnsTypes typeInfo : values()) {
			if(typeInfo.getId().equals(id)){
				return typeInfo;
			}
		}
		
		return null;
	}
	
	//XXX GETTERS
	public Short getId() {
		return new Short(id);
	}
	
	public String getName() {
		return name;
	}
	
}
