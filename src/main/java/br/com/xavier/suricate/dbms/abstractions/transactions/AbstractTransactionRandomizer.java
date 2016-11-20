package br.com.xavier.suricate.dbms.abstractions.transactions;

import java.util.Collection;

import br.com.xavier.suricate.dbms.interfaces.transactions.ITransaction;
import br.com.xavier.suricate.dbms.interfaces.transactions.ITransactionRandomizer;
import br.com.xavier.suricate.dbms.interfaces.transactions.operation.ITransactionOperation;

public abstract class AbstractTransactionRandomizer
			implements ITransactionRandomizer {

	private static final long serialVersionUID = -8349138566444964813L;

	@Override
	public ITransactionOperation fetchRandomTransactionOperation(Collection<ITransaction> transactions) {
		// TODO Auto-generated method stub
		return null;
	}

}
