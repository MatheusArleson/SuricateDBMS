package br.com.xavier.suricate.dbms.impl.services.lock;

import br.com.xavier.suricate.dbms.abstractions.services.lock.AbstractLock;
import br.com.xavier.suricate.dbms.enums.LockType;
import br.com.xavier.suricate.dbms.interfaces.transactions.operation.ITransactionOperation;

public final class Lock extends AbstractLock {

	private static final long serialVersionUID = 3140907152942526104L;

	public Lock(ITransactionOperation txOp, LockType lockType) {
		super(txOp, lockType);
	}

}
