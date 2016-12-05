package br.com.xavier.suricate.dbms.abstractions.transactions;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Queue;

import br.com.xavier.suricate.dbms.interfaces.transactions.ITransaction;
import br.com.xavier.suricate.dbms.interfaces.transactions.operation.ITransactionOperation;

public abstract class AbstractTransaction 
		implements ITransaction {
	
	private static final long serialVersionUID = 1988573226912796648L;
	
	//XXX PROPERTIES
	private Long id;
	private LocalDateTime timeStamp;
	private Queue<ITransactionOperation> operations;
	
	//XXX CONSTRUCTORS
	public AbstractTransaction(Long id) {
		this(id, new LinkedList<>());
	}
	
	public AbstractTransaction(Long id, Queue<ITransactionOperation> operations) {
		this.timeStamp = LocalDateTime.now();
		setId(id);
		setOperations(operations);
	}

	//XXX OVERRIDE METHODS
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractTransaction other = (AbstractTransaction) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "AbstractTransaction [id=" + id + "]";
	}

	@Override
	public void addOperation(ITransactionOperation operation) {
		this.operations.add(operation);
	}

	@Override
	public void removeOperation(ITransactionOperation operation) {
		this.operations.remove(operation);
	}
	
	@Override
	public boolean hasNextOperation(){
		return !this.operations.isEmpty();
	}
	
	@Override
	public ITransactionOperation getNextOperation() {
		return operations.poll();
	}
	
	@Override
	public Long getId() {
		return new Long(id);
	}
	
	@Override
	public LocalDateTime getTimeStamp() {
		return timeStamp;
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
