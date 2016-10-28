package br.com.xavier.suricate.dbms.interfaces.transactions;

import java.io.Serializable;
import java.util.Collection;

public interface ITransactionRandomizer 
		extends Serializable {
	
	ITransactionOperation fetchRandomTransactionOperation(Collection<ITransaction> transactions);

}
