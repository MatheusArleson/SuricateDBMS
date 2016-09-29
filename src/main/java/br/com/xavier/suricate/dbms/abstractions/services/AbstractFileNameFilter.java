package br.com.xavier.suricate.dbms.abstractions.services;

import java.io.File;
import java.io.Serializable;

import br.com.xavier.suricate.dbms.interfaces.services.IFileNameFilter;

public abstract class AbstractFileNameFilter 
		implements IFileNameFilter, Serializable {
	
	private static final long serialVersionUID = -5202676092186769122L;
	
	//XXX PROPERTIES
	private String extension;
	
	//XXX CONSTRUCTOR
	public AbstractFileNameFilter(String extension) {
		this.extension = validate(extension);
	}

	private String validate(String sufix) {
		if(sufix == null || sufix.trim().isEmpty()){
			throw new IllegalArgumentException("Extension must not be null or empty : " + sufix);
		}
		
		return sufix;
	}
	
	//XXX OVERRIDE METHODS
	@Override
	public boolean accept(File file, String name) {
		if(file == null || name == null || name.trim().isEmpty()){
			return false;
		}
		
		return name.endsWith(extension);
	}
	
	@Override
	public String getExtension() {
		return extension;
	}

}
