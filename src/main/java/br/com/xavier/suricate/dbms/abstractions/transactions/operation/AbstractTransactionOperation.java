package br.com.xavier.suricate.dbms.abstractions.transactions;

import br.com.xavier.suricate.dbms.enums.OperationTypes;
import br.com.xavier.suricate.dbms.interfaces.transactions.IObjectId;
import br.com.xavier.suricate.dbms.interfaces.transactions.ITransaction;
import br.com.xavier.suricate.dbms.interfaces.transactions.ITransactionOperation;

public abstract class AbstractTransactionOperation 
			implements ITransactionOperation {

	private static final long serialVersionUID = 5858340741174982063L;
	
	//XXX PROPERTIES
	private OperationTypes operationType;
	private IObjectId objectId;
	private Long transactionId;
	
	//XXX CONSTRUCTOR
	public AbstractTransactionOperation(ITransaction transaction, OperationTypes operationType, IObjectId objectId) {
		super();
		setTransactionId(transaction);
		setOperationType(operationType);
		setObjectId(objectId);
	}

	private void setTransactionId(ITransaction transaction) {
		if(transaction == null){
			throw new IllegalArgumentException("Transaction cannot be null.");
		}
		
		Long transactionId = transaction.getId();
		if(transactionId == null){
			throw new IllegalArgumentException("Transaction id cannot be null.");
		}
		
		this.transactionId = transactionId;
	}

	private void setOperationType(OperationTypes operationType) {
		if(operationType == null){
			throw new IllegalArgumentException("OperationType cannot be null.");
		}
		
		this.operationType = operationType;
	}

	private void setObjectId(IObjectId objectId) {
		if(objectId == null){
			throw new IllegalArgumentException("ObjectId cannot be null.");
		}
		
		IObjectId.validate(objectId);
		
		this.objectId = objectId;
	}

	//XXX GETTERS/SETTERS
	@Override
	public OperationTypes getOperationType() {
		return operationType;
	}

	@Override
	public IObjectId getObjectId() {
		return objectId;
	}

	@Override
	public Long getTransactionId() {
		return new Long(transactionId);
	}

}
