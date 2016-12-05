package br.com.xavier.suricate.dbms.abstractions.services;

import java.util.Collection;
import java.util.Objects;

import br.com.xavier.suricate.dbms.enums.OperationTypes;
import br.com.xavier.suricate.dbms.interfaces.services.ILockManager;
import br.com.xavier.suricate.dbms.interfaces.services.ITransactionManager;
import br.com.xavier.suricate.dbms.interfaces.transactions.IScheduleResult;
import br.com.xavier.suricate.dbms.interfaces.transactions.operation.ITransactionOperation;

public abstract class AbstractTransactionManager implements ITransactionManager {

	private static final long serialVersionUID = 4158707909856020194L;
	
	//XXX DEPENDENCIES
	private final ILockManager lockManager;
	
	//XXX CONSTRUCTOR
	public AbstractTransactionManager(ILockManager lockManager) {	
		this.lockManager = Objects.requireNonNull( lockManager );
	}
	
	//XXX OVERRIDE METHODS
	@Override
	public Collection<IScheduleResult> schedule(ITransactionOperation txOp) {
		OperationTypes operationType = txOp.getOperationType();
		if( operationType.equals(OperationTypes.COMMIT) || operationType.equals(OperationTypes.ABORT) ){
			return processFinalOperation(txOp);
		} else {
			return processOtherOperation(txOp);
		}
	}
	
	private Collection<IScheduleResult> processFinalOperation(ITransactionOperation txOp) {
		Collection<IScheduleResult> results = lockManager.process(txOp);
		//waitManager -> free transactions from wait and reprocess them
		return null;
	}
	
	private Collection<IScheduleResult> processOtherOperation(ITransactionOperation txOp) {
		Collection<IScheduleResult> lockResults = lockManager.process(txOp);
		return null;
	}
}
