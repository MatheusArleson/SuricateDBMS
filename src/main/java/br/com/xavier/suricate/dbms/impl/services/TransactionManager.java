package br.com.xavier.suricate.dbms.impl.services;

import br.com.xavier.suricate.dbms.abstractions.services.AbstractTransactionManager;
import br.com.xavier.suricate.dbms.interfaces.services.IDeadLockManager;
import br.com.xavier.suricate.dbms.interfaces.services.ILockManager;
import br.com.xavier.suricate.dbms.interfaces.services.IWaitManager;

public final class TransactionManager extends AbstractTransactionManager {

	private static final long serialVersionUID = -1492045244444853216L;
	
	public TransactionManager(ILockManager lockManager, IWaitManager waitManager,  IDeadLockManager deadLockManager) {
		super(lockManager, waitManager, deadLockManager);
	}

}
