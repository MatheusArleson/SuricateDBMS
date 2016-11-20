package br.com.xavier.suricate.dbms.abstractions.transactions.operation;

import br.com.xavier.suricate.dbms.enums.OperationTypes;
import br.com.xavier.suricate.dbms.interfaces.transactions.IObjectId;
import br.com.xavier.suricate.dbms.interfaces.transactions.ITransaction;
import br.com.xavier.suricate.dbms.interfaces.transactions.operation.ITransactionOperation;

public abstract class AbstractTransactionOperation 
			implements ITransactionOperation {

	private static final long serialVersionUID = 5858340741174982063L;
	
	//XXX PROPERTIES
	private OperationTypes operationType;
	private Long transactionId;
	private IObjectId objectId;
	
	//XXX CONSTRUCTOR
	public AbstractTransactionOperation(ITransaction transaction, OperationTypes operationType, IObjectId objectId) {
		super();
		setTransactionId(transaction);
		setOperationType(operationType);
		setObjectId(objectId);
	}

	//XXX OVERRIDE METHODS
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((objectId == null) ? 0 : objectId.hashCode());
		result = prime * result + ((operationType == null) ? 0 : operationType.hashCode());
		result = prime * result + ((transactionId == null) ? 0 : transactionId.hashCode());
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
		AbstractTransactionOperation other = (AbstractTransactionOperation) obj;
		if (transactionId == null) {
			if (other.transactionId != null)
				return false;
		} else if (!transactionId.equals(other.transactionId))
			return false;
		if (operationType != other.operationType)
			return false;
		if (objectId == null) {
			if (other.objectId != null)
				return false;
		} else if (!objectId.equals(other.objectId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AbstractTransactionOperation [" 
			+ "operationType=" + operationType 
			+ ", transactionId=" + transactionId
			+ ", objectId=" + objectId 
		+ "]";
	}

	//XXX PRIVATE METHODS
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
		if(objectId != null){
			IObjectId.validate(objectId);
			this.objectId = objectId;
			
		} else {
		
			if(operationType == null){
				throw new IllegalArgumentException("ObjectId cannot be null.");
			} else {
				switch (operationType) {
				case ABORT:
				case COMMIT:
					break;

				default:
					IObjectId.validate(objectId);
					break;
				}
				
				this.objectId = objectId;
			
			}
		}
	}
	
	//XXX GETTERS/SETTERS
	@Override
	public OperationTypes getOperationType() {
		return operationType;
	}

	@Override
	public Long getTransactionId() {
		return new Long(transactionId);
	}

	@Override
	public IObjectId getObjectId() {
		return objectId;
	}
	
}
