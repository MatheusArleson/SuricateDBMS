package br.com.xavier.suricate.dbms.interfaces.services;

import java.io.Serializable;
import java.util.Collection;

import br.com.xavier.suricate.dbms.interfaces.transactions.IScheduleResult;
import br.com.xavier.suricate.dbms.interfaces.transactions.operation.ITransactionOperation;

public interface ILockManager extends Serializable {
	
	Collection<IScheduleResult> process(ITransactionOperation txOp);

}
