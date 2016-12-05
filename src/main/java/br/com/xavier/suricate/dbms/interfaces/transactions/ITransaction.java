package br.com.xavier.suricate.dbms.interfaces.transactions;

import java.io.Serializable;
import java.time.LocalDateTime;

import br.com.xavier.suricate.dbms.interfaces.transactions.operation.ITransactionOperation;

public interface ITransaction 
		extends Serializable {
	
	Long getId();
	LocalDateTime getTimeStamp();
	
	void addOperation(ITransactionOperation operation);
	void removeOperation(ITransactionOperation operation);
	
	boolean hasNextOperation();
	ITransactionOperation getNextOperation();
	
}
