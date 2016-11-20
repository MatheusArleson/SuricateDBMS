package br.com.xavier.suricate.dbms.abstractions.transactions.context;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import br.com.xavier.suricate.dbms.interfaces.transactions.ITransaction;
import br.com.xavier.suricate.dbms.interfaces.transactions.context.ITransactionContext;
import br.com.xavier.suricate.dbms.interfaces.transactions.operation.ITransactionOperation;

public abstract class AbstractTransactionContext 
				implements ITransactionContext {

	private static final long serialVersionUID = 199034666949334055L;
	
	//XXX PROPERTIES
	private Map<Long, ITransaction> transactionsMap;
	private Map<Long, ITransaction> blockedTransactionsMap;
	
	private Long minTxId;
	private Long maxTxId;
	
	//XXX CONSTRUCTOR
	public AbstractTransactionContext(List<ITransaction> transactions) {
		setTransactions(transactions);
		blockedTransactionsMap = new LinkedHashMap<>();
		minTxId = Long.MAX_VALUE;
		maxTxId = Long.MIN_VALUE;
	}
	
	//XXX OVERRIDE METHODS
	@Override
	public boolean hasNext() {
		
		
		
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public ITransactionOperation next(){
		ITransaction tx = fetchRandomTransaction();
		ITransactionOperation txOp = tx.getNextOperation(); 
		return txOp;
	}
	
	@Override
	public boolean isTransactionBlocked(ITransaction transaction) {
		Long txId = transaction.getId();
		return isTransactionBlocked(txId);
	}
	
	@Override
	public boolean isTransactionBlocked(Long transactionId) {
		if(blockedTransactionsMap.get(transactionId) != null){
			return true;
		}
		
		return false;
	}
	
	@Override
	public void blockTransaction(ITransaction transaction) {
		if( isTransactionBlocked(transaction) ){
			return;
		}
		
		addTransactionToBlocked(transaction);
	}
	
	@Override
	public void unblockTransaction(ITransaction transaction) {
		if( !isTransactionBlocked(transaction) ){
			return;
		}
		
		removeTransactionFromBlocked(transaction);
	}
	
	//XXX PRIVATE METHODS
	private void setTransactions(List<ITransaction> transactions){
		if(transactions == null || transactions.isEmpty()){
			throw new IllegalArgumentException("Transactions must not be null or empry.");
		}
		
		this.transactionsMap = new LinkedHashMap<>();
		transactions.forEach(t -> {
			Long txId = t.getId();
			setMinTransactionId(txId);
			setMaxTransactionId(txId);
			transactionsMap.put(txId, t);
		});
	}

	private void setMaxTransactionId(Long txId) {
		this.maxTxId = Math.max(txId, this.maxTxId);
	}

	private void setMinTransactionId(Long txId) {
		this.minTxId = Math.min(txId, this.minTxId);
	}
	
	private void addTransactionToBlocked(ITransaction transaction) {
		Long id = transaction.getId();
		blockedTransactionsMap.put(id, transaction);
	}
	
	private void removeTransactionFromBlocked(ITransaction transaction) {
		blockedTransactionsMap.remove(transaction.getId());
	}
	
	private ITransaction fetchRandomTransaction() {
		ITransaction tx = null;
		
		while(tx == null){
			Long randomTxId = ThreadLocalRandom.current().nextLong(minTxId, maxTxId + 1);
			if( !isTransactionBlocked(randomTxId) ){
				tx = transactionsMap.get(randomTxId);
			}
		}
		
		return tx;
	}
	
}
