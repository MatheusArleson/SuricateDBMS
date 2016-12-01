package br.com.xavier.suricate.dbms.impl.services;

import java.util.Collection;

import br.com.xavier.suricate.dbms.abstractions.services.AbstractDeadLockManager;
import br.com.xavier.suricate.dbms.interfaces.transactions.IDeadLockResult;
import br.com.xavier.suricate.dbms.interfaces.transactions.ITransaction;

public final class WoundWaiDeadLockManager extends AbstractDeadLockManager {

	private static final long serialVersionUID = -8936681674258166214L;

	//XXX CONSTRUCTOR
	public WoundWaiDeadLockManager() {
		super();
	}
	
	//XXX OVERRIDE METHODS
	@Override
	public Collection<ITransaction> resolveDeadLock(IDeadLockResult deadLockResult) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
