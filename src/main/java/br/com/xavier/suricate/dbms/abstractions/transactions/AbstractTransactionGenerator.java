package br.com.xavier.suricate.dbms.abstractions.transactions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import br.com.xavier.suricate.dbms.enums.OperationTypes;
import br.com.xavier.suricate.dbms.impl.transactions.Transaction;
import br.com.xavier.suricate.dbms.impl.transactions.operation.TransactionOperation;
import br.com.xavier.suricate.dbms.interfaces.transactions.IObjectId;
import br.com.xavier.suricate.dbms.interfaces.transactions.ITransaction;
import br.com.xavier.suricate.dbms.interfaces.transactions.ITransactionGenerator;
import br.com.xavier.suricate.dbms.interfaces.transactions.operation.ITransactionOperation;

public abstract class AbstractTransactionGenerator 
		implements ITransactionGenerator {

	private static final long serialVersionUID = 1673904005292670940L;

	//XXX PROPERTIES
	private List<ITransaction> transactionsQueue;
	private Random random;
	
	//XXX CONSTRUCTOR
	public AbstractTransactionGenerator() {
		transactionsQueue = new CopyOnWriteArrayList<>();
		random = new Random(System.nanoTime());
	}
	
	//XXX OVERRIDE METHODS
	@Override
	public List<ITransaction> generateTransactions(int numberOfTransactions, int maxNumberOfOperations, Collection<IObjectId> objectIds) {
		ArrayList<IObjectId> objectsIdsList = new ArrayList<>(objectIds);
		
		int transactionsCount = 0;
		while(transactionsCount < numberOfTransactions){
			int numberOfOperations = genreateNumberOfTransactions(maxNumberOfOperations);
			Long transactionId = new Long(transactionsCount);
			
			GeneratorThread runnable = new GeneratorThread(transactionId, numberOfOperations, objectsIdsList);
			Thread thread = new Thread(runnable);
			thread.start();
			
			transactionsCount++;
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

	private int genreateNumberOfTransactions(int maxNumberOfOperations) {
		int nextInt = 0;
		while(nextInt == 0){
			nextInt = random.nextInt(maxNumberOfOperations);
		}
		return nextInt;
	}
	
	//XXX INNER CLASSES
	class GeneratorThread implements Runnable {

		private Long transactionId;
		private Integer operationsNumber;
		private List<IObjectId> objectIds;
		
		public GeneratorThread(Long transactionId, Integer operationsNumber, List<IObjectId> objectIds) {
			this.objectIds = objectIds;
			this.transactionId = transactionId;
			this.operationsNumber = operationsNumber;
		}
		
		@Override
		public void run() {
			ITransaction transaction = new Transaction(transactionId);
			int opCount = 0;
			while(opCount < this.operationsNumber){
				IObjectId objectId = fetchRandomObjectId(); 
				OperationTypes operationType = fetchRandomOperationType();
				
				ITransactionOperation operation = new TransactionOperation(transaction, operationType , objectId);
				transaction.addOperation(operation);
				
				opCount++;
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
