package br.com.xavier.suricate.dbms.interfaces.transactions;

import java.io.Serializable;

import br.com.xavier.suricate.dbms.enums.OperationTypes;

public interface ITransactionOperation 
		extends Serializable {
	
	OperationTypes getOperationType();
	IObjectId getObjectId();
	Long getTransactionId();
	
	
}
