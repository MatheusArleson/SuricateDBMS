package br.com.xavier.suricate.dbms.impl.transactions;

import java.util.Queue;

import br.com.xavier.suricate.dbms.abstractions.transactions.AbstractTransaction;
import br.com.xavier.suricate.dbms.interfaces.transactions.operation.ITransactionOperation;

public class Transaction
		extends AbstractTransaction {

	private static final long serialVersionUID = 335583085860739496L;

	public Transaction(Long id) {
		super(id);
	}
	
	public Transaction(Long id, Queue<ITransactionOperation> operations) {
		super(id, operations);
	}

}
