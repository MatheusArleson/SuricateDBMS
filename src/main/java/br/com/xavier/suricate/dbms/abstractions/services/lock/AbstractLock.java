package br.com.xavier.suricate.dbms.abstractions.services.lock;

import java.util.Objects;

import br.com.xavier.suricate.dbms.enums.LockType;
import br.com.xavier.suricate.dbms.interfaces.services.lock.ILock;
import br.com.xavier.suricate.dbms.interfaces.transactions.operation.ITransactionOperation;

public abstract class AbstractLock implements ILock {
	
	private static final long serialVersionUID = 6660489445715268166L;
	
	//XXX PROPERTIES
	private ITransactionOperation txOp;
	private LockType lockType;
	
	//XXX CONSTRUCTOR
	public AbstractLock(ITransactionOperation txOp, LockType lockType) {
		super();
		this.txOp = Objects.requireNonNull( txOp );
		this.lockType = Objects.requireNonNull(lockType);
	}
	
	//XXX OVERRIDE METHODS
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((txOp == null) ? 0 : txOp.hashCode());
		result = prime * result + ((lockType == null) ? 0 : lockType.hashCode());
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
		AbstractLock other = (AbstractLock) obj;
		if (txOp == null) {
			if (other.txOp != null)
				return false;
		} else if (!txOp.equals(other.txOp))
			return false;
		if (lockType != other.lockType)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "AbstractLock [" 
			+ "txOp=" + txOp 
			+ ", lockType=" + lockType 
		+ "]";
	}

	//XXX GETTERS/SETTERS
	@Override
	public ITransactionOperation getTransactionOperation() {
		return txOp;
	}
	
	@Override
	public LockType getLockType() {
		return lockType;
	}
	
}
