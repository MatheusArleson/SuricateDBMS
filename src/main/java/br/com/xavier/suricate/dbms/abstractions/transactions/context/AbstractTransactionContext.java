package br.com.xavier.suricate.dbms.abstractions.transactions.context;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import br.com.xavier.suricate.dbms.enums.TransactionOperationStatus;
import br.com.xavier.suricate.dbms.interfaces.transactions.IScheduleResult;
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
		blockedTransactionsMap = new LinkedHashMap<>();
		minTxId = Long.MAX_VALUE;
		maxTxId = Long.MIN_VALUE;
		setTransactions(transactions);
	}
	
	//XXX OVERRIDE METHODS
	@Override
	public boolean hasNext() {
		return !transactionsMap.isEmpty();
	}
	
	@Override
	public ITransactionOperation next(){
		ITransaction tx = fetchRandomTransaction();
		ITransactionOperation txOp = tx.getNextOperation();
		
		if( !tx.hasNextOperation() ){
			handleEmptyTransaction( tx );
		}
		
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
	public void process(IScheduleResult result) {
		if( result == null ){
			throw new IllegalArgumentException("Null schedule result");
		}
		
		TransactionOperationStatus status = result.getStatus();
		switch (status) {
		case SCHEDULED:
			break;
			
		case WAITING:
			break;
			
		case ABORT_TRANSACTION:
			break;

		default:
			break;
		}
		
	}
	
	//XXX PRIVATE METHODS
	private void blockTransaction(ITransaction transaction) {
		if( isTransactionBlocked(transaction) ){
			return;
		}
		
		addTransactionToBlocked(transaction);
	}
	
	private void unblockTransaction(ITransaction transaction) {
		if( !isTransactionBlocked(transaction) ){
			return;
		}
		
		removeTransactionFromBlocked(transaction);
	}
	
	private void setTransactions(List<ITransaction> transactions){
		if(transactions == null || transactions.isEmpty()){
			throw new IllegalArgumentException("Transactions must not be null or empry.");
		}
		
		this.transactionsMap = new LinkedHashMap<>();
		transactions.forEach(t -> {
			Long txId = t.getId();
			setMinTransactionId(txId);
			setMaxTransactionId(txId);
			mapTransaction(t, txId);
		});
	}
	
	private void setMaxTransactionId(Long txId) {
		this.maxTxId = Math.max(txId, this.maxTxId);
	}

	private void setMinTransactionId(Long txId) {
		this.minTxId = Math.min(txId, this.minTxId);
	}
	
	private void mapTransaction(ITransaction t, Long txId) {
		transactionsMap.put(txId, t);
	}

	private void ummapTransaction(ITransaction emptyTransation) {
		transactionsMap.remove( emptyTransation.getId() );
	}
	
	private void handleEmptyTransaction( ITransaction emptyTransation ){
		ummapTransaction(emptyTransation);
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
