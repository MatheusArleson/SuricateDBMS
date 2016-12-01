package br.com.xavier.suricate.dbms.interfaces.services;

import java.io.Serializable;

import br.com.xavier.suricate.dbms.interfaces.transactions.IDeadLockResult;
import br.com.xavier.suricate.dbms.interfaces.transactions.operation.ITransactionOperation;

public interface IDeadLockManager extends Serializable {

	IDeadLockResult checkDeadLock(ITransactionOperation txOp);
	
}
