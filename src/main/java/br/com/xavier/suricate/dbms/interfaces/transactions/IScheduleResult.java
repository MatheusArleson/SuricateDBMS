package br.com.xavier.suricate.dbms.interfaces.transactions;

import br.com.xavier.suricate.dbms.enums.TransactionOperationStatus;

public interface IScheduleResult {

	TransactionOperationStatus getStatus();

}
