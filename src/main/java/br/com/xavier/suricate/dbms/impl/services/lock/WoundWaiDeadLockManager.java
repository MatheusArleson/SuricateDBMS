package br.com.xavier.suricate.dbms.impl.services.lock;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.LinkedList;

import br.com.xavier.suricate.dbms.abstractions.services.lock.AbstractLockManager;
import br.com.xavier.suricate.dbms.enums.LockType;
import br.com.xavier.suricate.dbms.enums.TransactionOperationStatus;
import br.com.xavier.suricate.dbms.impl.transactions.ScheduleResult;
import br.com.xavier.suricate.dbms.interfaces.services.lock.ILock;
import br.com.xavier.suricate.dbms.interfaces.transactions.IScheduleResult;
import br.com.xavier.suricate.dbms.interfaces.transactions.ITransaction;
import br.com.xavier.suricate.dbms.interfaces.transactions.operation.ITransactionOperation;

public final class WoundWaiDeadLockManager extends AbstractLockManager {

	private static final long serialVersionUID = -8936681674258166214L;

	//XXX CONSTRUCTOR
	public WoundWaiDeadLockManager() {
		super();
	}
	
	//XXX OVERRIDE METHODS
	@Override
	public ILock generateLockInstance(ITransactionOperation txOp, LockType lockType) {
		return new Lock(txOp, lockType);
	}

	@Override
	public Collection<IScheduleResult> handleIncompatibleLocks(ILock lock, ILock otherLock) {
		ITransactionOperation txOp = lock.getTransactionOperation();
		ITransaction lockTx = txOp.getTransaction();
		LocalDateTime lockTxTimestamp = lockTx.getTimeStamp();
		
		ITransactionOperation otherTxOp = otherLock.getTransactionOperation();
		ITransaction otherTx = otherTxOp.getTransaction();
		LocalDateTime otherTxTimestamp = otherTx.getTimeStamp();
		
		Collection<IScheduleResult> results = new LinkedList<>();
		if( lockTxTimestamp.isBefore(otherTxTimestamp) ){
			results.add( new ScheduleResult(otherTxOp, TransactionOperationStatus.ABORT_TRANSACTION) );
		} else {
			results.add( new ScheduleResult(txOp, TransactionOperationStatus.WAITING) );
		}
		
		return results;
	}
	
}
