package br.com.xavier.suricate.dbms.interfaces.transactions;

import java.io.Serializable;

import br.com.xavier.suricate.dbms.enums.TransactionOperationStatus;
import br.com.xavier.suricate.dbms.interfaces.transactions.operation.ITransactionOperation;

public interface IScheduleResult extends Serializable {

	ITransactionOperation getTransactionOperation();
	TransactionOperationStatus getStatus();

}
