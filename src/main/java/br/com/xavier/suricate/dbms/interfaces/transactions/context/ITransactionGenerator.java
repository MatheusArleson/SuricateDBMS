package br.com.xavier.suricate.dbms.interfaces.transactions.context;

import java.io.Serializable;
import java.util.Collection;

import br.com.xavier.suricate.dbms.interfaces.transactions.IObjectId;

public interface ITransactionGenerator 
		extends Serializable {
	
	ITransactionContext generateTransactions(int numberOfTransactions, int maxNumberOfOperations, Collection<IObjectId> objectIds);

}
