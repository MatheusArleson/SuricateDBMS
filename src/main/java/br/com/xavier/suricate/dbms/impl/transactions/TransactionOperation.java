package br.com.xavier.suricate.dbms.impl.transactions;

import br.com.xavier.suricate.dbms.abstractions.transactions.AbstractTransactionOperation;
import br.com.xavier.suricate.dbms.enums.OperationTypes;
import br.com.xavier.suricate.dbms.interfaces.transactions.IObjectId;
import br.com.xavier.suricate.dbms.interfaces.transactions.ITransaction;

public class TransactionOperation 
		extends AbstractTransactionOperation {

	private static final long serialVersionUID = -9143234372498137675L;

	public TransactionOperation(ITransaction transaction, OperationTypes operationType, IObjectId objectId) {
		super(transaction, operationType, objectId);
	}

}
