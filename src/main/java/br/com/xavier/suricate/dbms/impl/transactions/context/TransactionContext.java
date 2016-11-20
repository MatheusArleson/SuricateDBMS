package br.com.xavier.suricate.dbms.impl.transactions.context;

import java.util.List;

import br.com.xavier.suricate.dbms.abstractions.transactions.context.AbstractTransactionContext;
import br.com.xavier.suricate.dbms.interfaces.transactions.ITransaction;

public final class TransactionContext 
			extends AbstractTransactionContext {

	private static final long serialVersionUID = 5498361915286419313L;

	public TransactionContext(List<ITransaction> transactions) {
		super(transactions);
	}

}
