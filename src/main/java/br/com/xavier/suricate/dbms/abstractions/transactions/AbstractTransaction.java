package br.com.xavier.suricate.dbms.abstractions.transactions;

import java.util.LinkedList;
import java.util.Queue;

import br.com.xavier.suricate.dbms.interfaces.transactions.ITransactionOperation;
import br.com.xavier.suricate.dbms.interfaces.transactions.ITransaction;

public abstract class AbstractTransaction 
		implements ITransaction {
	
	private static final long serialVersionUID = 1988573226912796648L;
	
	//XXX PROPERTIES
	private Long id;
	private Queue<ITransactionOperation> operations;
	
	//XXX CONSTRUCTORS
	public AbstractTransaction(Long id) {
		this(id, new LinkedList<>());
	}
	
	public AbstractTransaction(Long id, Queue<ITransactionOperation> operations) {
		setId(id);
		setOperations(operations);
	}

	//XXX OVERRIDE METHODS
	@Override
	public void addOperation(ITransactionOperation operation) {
		this.operations.add(operation);
	}
	
	@Override
	public void removeOperation(ITransactionOperation operation) {
		this.operations.remove(operation);
	}
	
	@Override
	public ITransactionOperation getNextOperation() {
		return operations.poll();
	}
	
	@Override
	public Long getId() {
		return new Long(id);
	}
	
	//XXX PRIVATE METHODS
	private void setId(Long id){
		if(id == null){
			throw new IllegalArgumentException("Transaction id cannot be null.");
		}
		
		if(id < 0L){
			throw new IllegalArgumentException("Transaction id must be greather than zero.");
		}
		
		this.id = id;
	}
	
	private void setOperations(Queue<ITransactionOperation> operations) {
		if(operations == null){
			throw new IllegalArgumentException("Operations cannot be null.");
		}
		
		this.operations = operations;
	}
	
}
