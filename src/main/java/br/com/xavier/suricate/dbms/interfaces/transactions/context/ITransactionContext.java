package br.com.xavier.suricate.dbms.interfaces.transactions.context;

import java.io.Serializable;

import br.com.xavier.suricate.dbms.interfaces.transactions.IScheduleResult;
import br.com.xavier.suricate.dbms.interfaces.transactions.ITransaction;
import br.com.xavier.suricate.dbms.interfaces.transactions.operation.ITransactionOperation;

public interface ITransactionContext extends Serializable {
	
	boolean hasNext();
	ITransactionOperation next();
	
	boolean isTransactionBlocked(ITransaction transaction);
	boolean isTransactionBlocked(Long transactionId);
	
	void process(IScheduleResult scheduleResult);

}
