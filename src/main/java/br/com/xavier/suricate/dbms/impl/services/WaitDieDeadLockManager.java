package br.com.xavier.suricate.dbms.impl.services;

import java.util.Collection;

import br.com.xavier.suricate.dbms.abstractions.services.AbstractDeadLockManager;
import br.com.xavier.suricate.dbms.interfaces.transactions.IDeadLockResult;
import br.com.xavier.suricate.dbms.interfaces.transactions.ITransaction;

public final class WaitDieDeadLockManager extends AbstractDeadLockManager {

	private static final long serialVersionUID = -4994871185146870155L;
	
	//XXX CONSTRUCTOR
	public WaitDieDeadLockManager() {
		super();
	}
	
	//XXX OVERRIDE METHODS
	@Override
	public Collection<ITransaction> resolveDeadLock(IDeadLockResult deadLockResult) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
