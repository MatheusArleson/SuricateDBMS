package br.com.xavier.suricate.dbms.impl.services;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import br.com.xavier.suricate.dbms.abstractions.services.AbstractFileSystemManager;

public class FileSystemManager 
		extends AbstractFileSystemManager {

	private static final long serialVersionUID = 3920413139050733945L;
	
	public FileSystemManager(File workspaceFolder, FilenameFilter fileNameFilter) throws IOException {
		super(workspaceFolder, fileNameFilter);
	}

}
