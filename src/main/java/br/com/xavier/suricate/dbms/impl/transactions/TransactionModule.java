package br.com.xavier.suricate.dbms.impl.transactions;

import java.util.Collection;
import java.util.Objects;

import br.com.xavier.suricate.dbms.interfaces.transactions.IObjectId;
import br.com.xavier.suricate.dbms.interfaces.transactions.ITransaction;
import br.com.xavier.suricate.dbms.interfaces.transactions.ITransactionGenerator;
import br.com.xavier.suricate.dbms.interfaces.transactions.ITransactionRandomizer;
import br.com.xavier.suricate.dbms.interfaces.transactions.operation.ITransactionOperation;

public final class TransactionModule {

	//XXX PROPERTIES
	private final ITransactionGenerator generator;
	private final ITransactionRandomizer randomizer;
	
	private Collection<ITransaction> transactions;

	//XXX CONSTRUCTOR
	public TransactionModule() {
		this(new TransactionGenerator(), new TransactionRandomizer());
	}
	
	public TransactionModule(ITransactionGenerator generator, ITransactionRandomizer randomizer) {
		super();
		this.generator = Objects.requireNonNull(generator);
		this.randomizer = Objects.requireNonNull(randomizer);
	}
	
	//XXX DELEGATE METHODS
	public void generateTransactions(int numberOfTransactions, int maxNumberOfOperations, Collection<IObjectId> objectIds) {
		transactions = generator.generateTransactions(numberOfTransactions, maxNumberOfOperations, objectIds);
	}

	public ITransactionOperation fetchRandomTransactionOperation() {
		return randomizer.fetchRandomTransactionOperation(transactions);
	}

}
