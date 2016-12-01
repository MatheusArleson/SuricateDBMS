package br.com.xavier.suricate.dbms.abstractions.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import br.com.xavier.suricate.dbms.enums.TransactionOperationStatus;
import br.com.xavier.suricate.dbms.impl.transactions.ScheduleResult;
import br.com.xavier.suricate.dbms.interfaces.services.IDeadLockManager;
import br.com.xavier.suricate.dbms.interfaces.services.ILockManager;
import br.com.xavier.suricate.dbms.interfaces.services.ITransactionManager;
import br.com.xavier.suricate.dbms.interfaces.services.IWaitManager;
import br.com.xavier.suricate.dbms.interfaces.transactions.IDeadLockResult;
import br.com.xavier.suricate.dbms.interfaces.transactions.IObjectId;
import br.com.xavier.suricate.dbms.interfaces.transactions.IScheduleResult;
import br.com.xavier.suricate.dbms.interfaces.transactions.ITransaction;
import br.com.xavier.suricate.dbms.interfaces.transactions.operation.ITransactionOperation;

public abstract class AbstractTransactionManager implements ITransactionManager {

	private static final long serialVersionUID = 4158707909856020194L;
	
	//XXX DEPENDENCIES
	private final ILockManager lockManager;
	private final IWaitManager waitManager;
	private final IDeadLockManager deadLockManager;
	
	//XXX CONSTRUCTOR
	public AbstractTransactionManager(ILockManager lockManager, IWaitManager waitManager, IDeadLockManager deadLockManager) {	
		this.lockManager = Objects.requireNonNull( lockManager );
		this.waitManager = Objects.requireNonNull( waitManager );
		this.deadLockManager = Objects.requireNonNull( deadLockManager );
	}
	
	//XXX OVERRIDE METHODS
	@Override
	public Collection<IScheduleResult> schedule(ITransactionOperation txOp) {
		IObjectId objectId = txOp.getObjectId();
		boolean isLocked = lockManager.isLocked(objectId);
		if(isLocked){
			return processLockedObject(waitManager, txOp);
		} else {
			return processNotLockedObject(deadLockManager, txOp);
		}
	}
	
	//XXX LOCKED OBJECT METHODS
	private Collection<IScheduleResult> processLockedObject(IWaitManager waitManager, ITransactionOperation txOp) {
		putOnWait(waitManager, txOp);
		return generateWaitResult(txOp);
	}

	private void putOnWait(IWaitManager waitManager, ITransactionOperation txOp) {
		waitManager.putOnWait(txOp);
	}
	
	private Collection<IScheduleResult> generateWaitResult(ITransactionOperation txOp) {
		IScheduleResult rst = new ScheduleResult(txOp, TransactionOperationStatus.WAITING);
		
		Collection<IScheduleResult>results = new ArrayList<>(1);
		results.add(rst);
		
		return results;
	}

	//XXX NOT LOCKED OBJECT METHODS
	private Collection<IScheduleResult> processNotLockedObject(IDeadLockManager deadLockManager, ITransactionOperation txOp) {
		IDeadLockResult dlResult = detectDeadLock(deadLockManager, txOp);
		if(dlResult.hasDeadLock()){
			return processDeadLock(deadLockManager, txOp, dlResult);
		} else {
			return processNotDeadLock(txOp);
		}
	}

	private IDeadLockResult detectDeadLock(IDeadLockManager deadLockManager, ITransactionOperation txOp) {
		return deadLockManager.detectDeadLock(txOp);
	}
	
	private Collection<IScheduleResult> processDeadLock(IDeadLockManager deadLockManager, ITransactionOperation txOp, IDeadLockResult dlResult) {
		Collection<ITransaction> abortedTransactions = deadLockManager.resolveDeadLock(dlResult);
		
		return null;
	}

	private Collection<IScheduleResult> processNotDeadLock(ITransactionOperation txOp) {
		// TODO Auto-generated method stub
		return null;
	}
}
