package br.com.xavier.suricate.dbms.abstractions.services;

import java.io.File;
import java.util.Objects;

import br.com.xavier.suricate.dbms.interfaces.services.ILockManager;
import br.com.xavier.suricate.dbms.interfaces.transactions.IObjectId;

public abstract class AbstractLockManager implements ILockManager {

	private static final long serialVersionUID = -6920168270904481451L;
	
	//XXX PROPERTIES
	private File workspaceFolder;
	
	//XXX CONSTRUCTOR
	public AbstractLockManager(File workspaceFolder) {
		this.workspaceFolder = Objects.requireNonNull(workspaceFolder);
	}
	
	//XXX OVERRIDE METHODS
	@Override
	public boolean isLocked(IObjectId objectId) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void lock(IObjectId objectId) {
		// TODO Auto-generated method stub
		
	}
}
