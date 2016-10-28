package br.com.xavier.suricate.dbms.abstractions.transactions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

import br.com.xavier.suricate.dbms.enums.OperationTypes;
import br.com.xavier.suricate.dbms.impl.transactions.Transaction;
import br.com.xavier.suricate.dbms.impl.transactions.TransactionOperation;
import br.com.xavier.suricate.dbms.interfaces.transactions.IObjectId;
import br.com.xavier.suricate.dbms.interfaces.transactions.ITransaction;
import br.com.xavier.suricate.dbms.interfaces.transactions.ITransactionGenerator;
import br.com.xavier.suricate.dbms.interfaces.transactions.ITransactionOperation;

public abstract class AbstractTransactionGenerator 
		implements ITransactionGenerator {

	private static final long serialVersionUID = 1673904005292670940L;

	private Queue<ITransaction> transactionsQueue = new ConcurrentLinkedQueue<>();
	private Random random = new Random(System.nanoTime());
	
	@Override
	public List<ITransaction> generateTransactions(int numberOfTransactions, int maxNumberOfOperations, Collection<IObjectId> objectIds) {
		ArrayList<IObjectId> objectsIdsList = new ArrayList<>(objectIds);
		
		int transactionsCount = 1;
		while(transactionsCount != numberOfTransactions){
			int numberOfOperations = random.nextInt(maxNumberOfOperations);
			Long transactionId = new Long(transactionsCount);
			
			GeneratorThread runnable = new GeneratorThread(transactionId, numberOfOperations, objectsIdsList);
			Thread thread = new Thread(runnable);
			thread.start();
		}
		
		while(transactionsQueue.size() != numberOfTransactions){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		List<ITransaction> transactions = new ArrayList<>(transactionsQueue);
		return transactions;
	}
	
	class GeneratorThread implements Runnable {

		private Long transactionId;
		private Integer operationsNumber;
		private List<IObjectId> objectIds;
		
		public GeneratorThread(Long transactionId, Integer operationsNumber, List<IObjectId> objectIds) {
			this.objectIds = objectIds;
			this.transactionId = transactionId;
		}
		
		@Override
		public void run() {
			ITransaction transaction = new Transaction(transactionId);
			int opCount = operationsNumber;
			while(opCount > 0){
				IObjectId objectId = fetchRandomObjectId(); 
				OperationTypes operationType = fetchRandomOperationType();
				
				ITransactionOperation operation = new TransactionOperation(transaction, operationType , objectId);
				transaction.addOperation(operation);
			}
			
			OperationTypes finalOperationType = fetchRandomFinalOperation();
			ITransactionOperation finalTransactionOperation = new TransactionOperation(transaction, finalOperationType, null);
			
			transaction.addOperation(finalTransactionOperation);
			
			transactionsQueue.add(transaction);
		}

		private IObjectId fetchRandomObjectId() {
			int index = random.nextInt(objectIds.size());
	        IObjectId item = objectIds.get(index);
			return item;
		}

		private OperationTypes fetchRandomOperationType() {
			int index = random.nextInt();
			return index % 2 == 0 ? OperationTypes.READ : OperationTypes.WRITE;
		}
		
		private OperationTypes fetchRandomFinalOperation() {
			int index = random.nextInt();
			return index % 2 == 0 ? OperationTypes.COMMIT : OperationTypes.ABORT;
		}

	}

}
