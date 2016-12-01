package br.com.xavier.suricate.dbms.interfaces.services;

import java.io.Serializable;
import java.util.Collection;

import br.com.xavier.suricate.dbms.interfaces.transactions.IDeadLockResult;
import br.com.xavier.suricate.dbms.interfaces.transactions.ITransaction;
import br.com.xavier.suricate.dbms.interfaces.transactions.operation.ITransactionOperation;

public interface IDeadLockManager extends Serializable {

	IDeadLockResult detectDeadLock(ITransactionOperation txOp);
	Collection<ITransaction> resolveDeadLock(IDeadLockResult deadLockResult);
	
}
