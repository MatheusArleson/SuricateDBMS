package br.com.xavier.suricate.dbms.abstractions.transactions;

import java.util.Objects;

import br.com.xavier.suricate.dbms.enums.TransactionOperationStatus;
import br.com.xavier.suricate.dbms.interfaces.transactions.IScheduleResult;
import br.com.xavier.suricate.dbms.interfaces.transactions.operation.ITransactionOperation;

public abstract class AbstractScheduleResult implements IScheduleResult {
	
	private static final long serialVersionUID = -3433332235683565152L;
	
	//XXX PROPERTIES
	private final ITransactionOperation transactionOperation;
	private final TransactionOperationStatus status;
	
	//XXX CONSTRUCTOR
	public AbstractScheduleResult(ITransactionOperation txOp, TransactionOperationStatus status) {
		this.transactionOperation = Objects.requireNonNull(txOp);
		this.status = Objects.requireNonNull(status);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((transactionOperation == null) ? 0 : transactionOperation.hashCode());
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
		AbstractScheduleResult other = (AbstractScheduleResult) obj;
		if (status != other.status)
			return false;
		if (transactionOperation == null) {
			if (other.transactionOperation != null)
				return false;
		} else if (!transactionOperation.equals(other.transactionOperation))
			return false;
		return true;
	}
	

	@Override
	public String toString() {
		return "AbstractScheduleResult [transactionOperation=" + transactionOperation + ", status=" + status + "]";
	}

	//XXX OVERRIDE METHODS
	@Override
	public ITransactionOperation getTransactionOperation() {
		return transactionOperation;
	}
	
	@Override
	public TransactionOperationStatus getStatus() {
		return status;
	}
	
}
