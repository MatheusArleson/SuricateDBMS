package br.com.xavier.suricate.dbms.interfaces.transactions;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface ITransactionGenerator 
		extends Serializable {
	
	List<ITransaction> generateTransactions(int numberOfTransactions, int maxNumberOfOperations, Collection<IObjectId> objectIds);

}
