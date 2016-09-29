package br.com.xavier.suricate.dbms.impl.services;

import java.io.File;
import java.io.IOException;

import br.com.xavier.suricate.dbms.abstractions.services.AbstractFileSystemManager;
import br.com.xavier.suricate.dbms.interfaces.services.IFileNameFilter;

public class FileSystemManager 
		extends AbstractFileSystemManager {

	private static final long serialVersionUID = 3920413139050733945L;
	
	public FileSystemManager(File workspaceFolder, IFileNameFilter fileNameFilter) throws IOException {
		super(workspaceFolder, fileNameFilter);
	}

}
