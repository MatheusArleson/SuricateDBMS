package br.com.xavier.suricate.dbms.enums;

import br.com.xavier.util.StringUtils;

public enum ColumnsTypes {
	
	//XXX ENUM MEMBERS
	INTEGER("1", "I", "Integer"),
	STRING("2", "S", "String");
	
	//XXX PROPERTIES
	private final Short id;
	private final String sigla;
	private final String name;
	
	//XXX CONSTRUCTOR
	private ColumnsTypes(String id, String sigla, String name) {
		this.id = new Short(id);
		this.sigla = sigla;
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
	
	public static ColumnsTypes getBySigla(String sigla){
		if(StringUtils.isNullOrEmpty(sigla)){
			return null;
		}
		
		for (ColumnsTypes typeInfo : values()) {
			if(typeInfo.getSigla().equals(sigla)){
				return typeInfo;
			}
		}
		
		return null;
	}
	
	//XXX GETTERS
	public Short getId() {
		return new Short(id);
	}
	
	public String getSigla() {
		return sigla;
	}
	
	public String getName() {
		return name;
	}
	
}
