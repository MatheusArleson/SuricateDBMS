package br.com.xavier.suricate.dbms.interfaces.transactions;

import java.io.Serializable;

public interface ITransaction 
		extends Serializable {
	
	Long getId();
	
	void addOperation(ITransactionOperation operation);
	void removeOperation(ITransactionOperation operation);
	ITransactionOperation getNextOperation();
	
}
