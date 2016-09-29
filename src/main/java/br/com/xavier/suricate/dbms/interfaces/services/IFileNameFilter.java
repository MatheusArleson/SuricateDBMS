package br.com.xavier.suricate.dbms.interfaces.services;

import java.io.FilenameFilter;

public interface IFileNameFilter
		extends FilenameFilter {
	
	String getExtension();

}
