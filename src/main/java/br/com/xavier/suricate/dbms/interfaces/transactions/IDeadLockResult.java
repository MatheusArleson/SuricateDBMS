package br.com.xavier.suricate.dbms.interfaces.transactions;

import java.io.Serializable;
import java.util.Collection;

public interface IDeadLockResult extends Serializable {
	
	boolean hasDeadLock();
	Collection<ITransaction> getTransactions();
	
}
