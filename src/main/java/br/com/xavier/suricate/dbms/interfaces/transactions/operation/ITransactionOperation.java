package br.com.xavier.suricate.dbms.interfaces.transactions.operation;

import java.io.Serializable;

import br.com.xavier.suricate.dbms.enums.OperationTypes;
import br.com.xavier.suricate.dbms.interfaces.transactions.IObjectId;

public interface ITransactionOperation 
		extends Serializable {
	
	OperationTypes getOperationType();
	Long getTransactionId();
	IObjectId getObjectId();
	
}
