package br.com.xavier.suricate.dbms.abstractions.services;

import java.util.Objects;

import br.com.xavier.suricate.dbms.interfaces.services.IDeadLockManager;
import br.com.xavier.suricate.dbms.interfaces.services.ILockManager;
import br.com.xavier.suricate.dbms.interfaces.services.ITransactionManager;
import br.com.xavier.suricate.dbms.interfaces.transactions.IDeadLockResult;
import br.com.xavier.suricate.dbms.interfaces.transactions.IObjectId;
import br.com.xavier.suricate.dbms.interfaces.transactions.IScheduleResult;
import br.com.xavier.suricate.dbms.interfaces.transactions.operation.ITransactionOperation;

public abstract class AbstractTransactionManager implements ITransactionManager {

	private static final long serialVersionUID = 4158707909856020194L;
	
	//XXX DEPENDENCIES
	private final ILockManager lockManager;
	private final IDeadLockManager deadLockManager;
	
	//XXX CONSTRUCTOR
	public AbstractTransactionManager(ILockManager lockManager, IDeadLockManager deadLockManager) {	
		this.lockManager = Objects.requireNonNull( lockManager );
		this.deadLockManager = Objects.requireNonNull( deadLockManager );
	}
	
	//XXX OVERRIDE METHODS
	@Override
	public IScheduleResult schedule(ITransactionOperation txOp) {
		
		IObjectId objectId = txOp.getObjectId();
		boolean isLocked = lockManager.isLocked(objectId);
		if(isLocked){
			return processLockedObject(txOp);
		} else {
			return processNotLockedObject(txOp);
		}
	}
	
	//XXX LOCKED OBJECT METHODS
	private IScheduleResult processLockedObject(ITransactionOperation txOp) {
		IDeadLockResult dlResult = deadLockManager.checkDeadLock(txOp);
		if(dlResult.hasDeadLock()){
			return processDeadLock(txOp, dlResult);
		} else {
			return processNotDeadLock(txOp);
		}
	}
	
	private IScheduleResult processDeadLock(ITransactionOperation txOp, IDeadLockResult dlResult) {
		// TODO Auto-generated method stub
		return null;
	}

	private IScheduleResult processNotDeadLock(ITransactionOperation txOp) {
		// TODO Auto-generated method stub
		return null;
	}

	//XXX NOT LOCKED OBJECT METHODS
	private IScheduleResult processNotLockedObject(ITransactionOperation txOp) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
