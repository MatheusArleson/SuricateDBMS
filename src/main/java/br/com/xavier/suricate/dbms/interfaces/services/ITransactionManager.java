package br.com.xavier.suricate.dbms.interfaces.services;

import java.io.Serializable;

import br.com.xavier.suricate.dbms.interfaces.transactions.IScheduleResult;
import br.com.xavier.suricate.dbms.interfaces.transactions.operation.ITransactionOperation;

public interface ITransactionManager extends Serializable {

	IScheduleResult schedule(ITransactionOperation txOp);

}
