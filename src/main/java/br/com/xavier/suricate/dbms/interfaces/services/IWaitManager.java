package br.com.xavier.suricate.dbms.interfaces.services;

import java.io.Serializable;

import br.com.xavier.suricate.dbms.interfaces.transactions.operation.ITransactionOperation;

public interface IWaitManager extends Serializable {
	
	boolean isWaiting(ITransactionOperation txOp);
	void putOnWait(ITransactionOperation txOp);
	void freeFromWait(ITransactionOperation txOp);
	
}
