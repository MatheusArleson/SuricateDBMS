package br.com.xavier.suricate.dbms.impl.services.lock;

import java.util.Collection;
import java.util.Collections;

import br.com.xavier.suricate.dbms.abstractions.services.lock.AbstractLockManager;
import br.com.xavier.suricate.dbms.enums.LockType;
import br.com.xavier.suricate.dbms.interfaces.services.lock.ILock;
import br.com.xavier.suricate.dbms.interfaces.transactions.IScheduleResult;
import br.com.xavier.suricate.dbms.interfaces.transactions.operation.ITransactionOperation;

public class NoPreventionLockManager extends AbstractLockManager {

	private static final long serialVersionUID = 3993734155682972876L;
	
	//XXX CONSTRUCTOR
	public NoPreventionLockManager() {
		super();
	}
	
	//XXX OVERRIDE METHODS
	@Override
	public ILock generateLockInstance(ITransactionOperation txOp, LockType lockType) {
		return new Lock(txOp, lockType);
	}

	@Override
	public Collection<IScheduleResult> handleIncompatibleLocks(ILock lock, ILock otherLock) {
		return Collections.emptyList();
	}

}