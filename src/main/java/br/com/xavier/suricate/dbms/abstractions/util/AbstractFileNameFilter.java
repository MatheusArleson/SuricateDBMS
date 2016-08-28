package br.com.xavier.suricate.dbms.abstractions.util;

import java.io.File;
import java.io.FilenameFilter;
import java.io.Serializable;

public abstract class AbstractFileNameFilter 
		implements FilenameFilter, Serializable {
	
	private static final long serialVersionUID = -5202676092186769122L;
	
	//XXX PROPERTIES
	private String sufix;
	
	//XXX CONSTRUCTOR
	public AbstractFileNameFilter(String sufix) {
		this.sufix = validate(sufix);
	}

	private String validate(String sufix) {
		if(sufix == null || sufix.trim().isEmpty()){
			throw new IllegalArgumentException("Suffix must not be null or empty : " + sufix);
		}
		
		return sufix;
	}
	
	//XXX OVERRIDE METHODS
	@Override
	public boolean accept(File file, String name) {
		if(file == null || name == null || name.trim().isEmpty()){
			return false;
		}
		
		return name.endsWith(sufix);
	}

}
