package br.com.xavier.suricate.dbms.impl.transactions;

import br.com.xavier.suricate.dbms.abstractions.transactions.AbstractScheduleResult;
import br.com.xavier.suricate.dbms.enums.TransactionOperationStatus;
import br.com.xavier.suricate.dbms.interfaces.transactions.operation.ITransactionOperation;

public final class ScheduleResult extends AbstractScheduleResult {
	
	private static final long serialVersionUID = -7665785835792106844L;
	
	//XXX CONSTRUCTOR
	public ScheduleResult(ITransactionOperation txOp, TransactionOperationStatus status) {
		super(txOp, status);
	}

}
