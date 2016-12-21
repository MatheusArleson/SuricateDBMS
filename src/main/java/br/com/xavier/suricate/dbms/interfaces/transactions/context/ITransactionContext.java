package br.com.xavier.suricate.dbms.interfaces.transactions.context;

import java.io.Serializable;
import java.util.Collection;

import br.com.xavier.suricate.dbms.interfaces.transactions.IScheduleResult;
import br.com.xavier.suricate.dbms.interfaces.transactions.ITransaction;
import br.com.xavier.suricate.dbms.interfaces.transactions.operation.ITransactionOperation;

public interface ITransactionContext extends Serializable {
	
	boolean hasNext();
	ITransactionOperation next();
	
	boolean isTransactionBlocked(ITransaction transaction);
	boolean isTransactionBlocked(Long transactionId);
	
	void process(Collection<IScheduleResult> scheduleResults);
	void process(IScheduleResult scheduleResult);
	
	String getFinalScheduleAsString();

}
