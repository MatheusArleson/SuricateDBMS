package br.com.xavier.suricate.dbms.abstractions.transactions;

import java.util.Objects;

import br.com.xavier.suricate.dbms.enums.TransactionOperationStatus;
import br.com.xavier.suricate.dbms.interfaces.transactions.IScheduleResult;
import br.com.xavier.suricate.dbms.interfaces.transactions.operation.ITransactionOperation;

public abstract class AbstractScheduleResult implements IScheduleResult {
	
	private static final long serialVersionUID = -3433332235683565152L;
	
	//XXX PROPERTIES
	private final ITransactionOperation transactionOperation;
	private final TransactionOperationStatus status;
	
	//XXX CONSTRUCTOR
	public AbstractScheduleResult(ITransactionOperation txOp, TransactionOperationStatus status) {
		this.transactionOperation = Objects.requireNonNull(txOp);
		this.status = Objects.requireNonNull(status);
	}
	
	//XXX OVERRIDE METHODS
	@Override
	public ITransactionOperation getTransactionOperation() {
		return transactionOperation;
	}
	
	@Override
	public TransactionOperationStatus getStatus() {
		return status;
	}
	
}
