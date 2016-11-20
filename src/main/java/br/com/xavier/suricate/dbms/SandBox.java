package br.com.xavier.suricate.dbms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import br.com.xavier.suricate.dbms.impl.low.BigEndianThreeBytesValue;
import br.com.xavier.suricate.dbms.impl.transactions.ObjectId;
import br.com.xavier.suricate.dbms.impl.transactions.context.TransactionContextGenerator;
import br.com.xavier.suricate.dbms.interfaces.low.IThreeByteValue;
import br.com.xavier.suricate.dbms.interfaces.transactions.IObjectId;
import br.com.xavier.suricate.dbms.interfaces.transactions.context.ITransactionContext;

public class SandBox {
	
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		
		int numberOfTransactions = 1;
		int maxNumberOfOperations = 5;
		Collection<IObjectId> objectIds =generateObjectIds(numberOfTransactions, new Random(System.nanoTime()));

		TransactionContextGenerator tg = new TransactionContextGenerator();
		ITransactionContext ctx = tg.generateTransactions(numberOfTransactions, maxNumberOfOperations, objectIds);
		
		System.out.println("done");
		
	}

	private static Collection<IObjectId> generateObjectIds(int numberOfTransactions, Random random) {
		List<IObjectId> objectIds = new ArrayList<>();
		
		int objectIdCount = 0;
		while(objectIdCount < numberOfTransactions){
			Integer randomNumber = random.nextInt(20);
			Byte tableId = randomNumber.byteValue();;
			
			IThreeByteValue blockId = new BigEndianThreeBytesValue(1);
			Long byteOffset = -1L;
			
			IObjectId objId = new ObjectId(tableId, blockId, byteOffset );
			objectIds.add(objId);
			
			objectIdCount++;
		}
		
		return objectIds;
	}
	
}
