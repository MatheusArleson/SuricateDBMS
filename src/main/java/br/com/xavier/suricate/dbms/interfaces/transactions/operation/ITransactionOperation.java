package br.com.xavier.suricate.dbms.interfaces.transactions.operation;

import java.io.Serializable;

import br.com.xavier.suricate.dbms.enums.OperationTypes;
import br.com.xavier.suricate.dbms.interfaces.transactions.IObjectId;
import br.com.xavier.suricate.dbms.interfaces.transactions.ITransaction;

public interface ITransactionOperation 
		extends Serializable {
	
	OperationTypes getOperationType();
	IObjectId getObjectId();
	ITransaction getTransaction();
	
}
