package br.com.xavier.suricate.dbms.impl.services.lock;

import java.io.File;
import java.util.Collection;
import java.util.LinkedList;

import br.com.xavier.suricate.dbms.abstractions.services.lock.AbstractLockManager;
import br.com.xavier.suricate.dbms.enums.LockType;
import br.com.xavier.suricate.dbms.enums.TransactionOperationStatus;
import br.com.xavier.suricate.dbms.impl.transactions.ScheduleResult;
import br.com.xavier.suricate.dbms.interfaces.services.lock.ILock;
import br.com.xavier.suricate.dbms.interfaces.transactions.IScheduleResult;
import br.com.xavier.suricate.dbms.interfaces.transactions.operation.ITransactionOperation;

public class NoPreventionLockManager extends AbstractLockManager {

	private static final long serialVersionUID = 3993734155682972876L;
	
	//XXX CONSTRUCTOR
	public NoPreventionLockManager(File workspaceFolder, boolean generateSnapshots) {
		super(workspaceFolder, generateSnapshots);
	}
	
	//XXX OVERRIDE METHODS
	@Override
	public ILock generateLockInstance(ITransactionOperation txOp, LockType lockType) {
		return new Lock(txOp, lockType);
	}

	@Override
	public Collection<IScheduleResult> handleIncompatibleLocks(ILock lock, ILock otherLock) {
		ITransactionOperation txOp = lock.getTransactionOperation();
		Collection<IScheduleResult> results = new LinkedList<>();
		results.add( new ScheduleResult(txOp, TransactionOperationStatus.WAITING) );
		return results;
	}

}
