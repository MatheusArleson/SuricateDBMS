package br.com.xavier.suricate.dbms;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

import br.com.xavier.suricate.dbms.impl.dbms.SuricateDbms;
import br.com.xavier.suricate.dbms.impl.low.BigEndianThreeBytesValue;
import br.com.xavier.suricate.dbms.impl.transactions.ObjectId;
import br.com.xavier.suricate.dbms.impl.transactions.context.TransactionContextGenerator;
import br.com.xavier.suricate.dbms.interfaces.dbms.IDbms;
import br.com.xavier.suricate.dbms.interfaces.low.IThreeByteValue;
import br.com.xavier.suricate.dbms.interfaces.transactions.IObjectId;
import br.com.xavier.suricate.dbms.interfaces.transactions.IScheduleResult;
import br.com.xavier.suricate.dbms.interfaces.transactions.context.ITransactionContext;
import br.com.xavier.suricate.dbms.interfaces.transactions.operation.ITransactionOperation;

public class SandBox {
	
	private static final Logger LOGGER = Logger.getLogger(SandBox.class);
	
	private static File workspaceFolder = new File("db_workspace");
	private static Integer bufferDataBlockSlots = 3000;
//	private static IThreeByteValue blockSize = new BigEndianThreeBytesValue(4096);
	
//	private static File outputDataFile = new File(workspaceFolder, "outputData.txt");
//	private static File outputLogFile = new File(workspaceFolder, "outputLog.log");
//	
//	private static String columnsSeparator = new String("|");
//	private static String nameMetadataSeparator = new String("%");
//	private static String typeSizeSeparator = new String("#");
//	private static String endLineSeparator = new String("\n");
//	
//	private static String[] filesPaths = new String[] {"file_samples/forn-tpch.txt" , "file_samples/cli-tpch.txt"/*, "file_samples/file_sample.txt", "file_samples/file_sample2.txt", "file_samples/file_sample3.txt"*/};
//	private static Charset filesCharset = StandardCharsets.UTF_8;
	
//	private static String timeStampPattern = "dd/MM/yyyy HH:mm:ss.SSS";
//	private static SimpleDateFormat dateFormatter = new SimpleDateFormat(timeStampPattern);
	
	public static void main(String[] args) throws Exception {
		
		IDbms database = new SuricateDbms(workspaceFolder, bufferDataBlockSlots);
		
		int numberOfTransactions = 5;
		int maxNumberOfOperations = 5;
		
		int numberOfObjectsIds = 10;
		int maxTableId = 20; 
		int maxBlockId = 20; 
		int maxRowOffset = 20;
		
		Random random = new Random(System.nanoTime());
		Collection<IObjectId> objectIds = generateObjectIds(numberOfObjectsIds, random, maxTableId, maxBlockId, maxRowOffset);

		TransactionContextGenerator tg = new TransactionContextGenerator();
		
		LOGGER.debug("#> GENERATING OBJECT IDS");
		ITransactionContext ctx = tg.generateTransactions(numberOfTransactions, maxNumberOfOperations, objectIds);
		
		LOGGER.debug("#> GENERATING OBJECT IDS");
		while(ctx.hasNext()){
			ITransactionOperation txOp = ctx.next();
			Collection<IScheduleResult> scheduleResults = database.schedule(txOp);
			ctx.process(scheduleResults);
		}
		
		LOGGER.debug("#> FINISHED PROCESS CTX ");
	}

	private static Collection<IObjectId> generateObjectIds(int numberOfObjectsIds, Random random, Integer maxTableId, Integer maxBlockId, Integer maxRowOffset) {
		List<IObjectId> objectIds = new ArrayList<>();
		
		int objectIdCount = 0;
		while(objectIdCount < numberOfObjectsIds){
			Byte tableId = Byte.MIN_VALUE;
			while(tableId < 1){
				tableId = ((Integer) random.nextInt(maxTableId)).byteValue();
			}
			
			Integer blockId = Integer.MIN_VALUE;
			while(blockId < 1){
				blockId = random.nextInt(maxBlockId);
			}
			IThreeByteValue blockIdTbv = new BigEndianThreeBytesValue(blockId);
			
			Long byteOffset = Long.MIN_VALUE;
			while(byteOffset < -1){
				byteOffset = random.nextLong();
			}
			
			IObjectId objId = new ObjectId(tableId, blockIdTbv, byteOffset);
			IObjectId.validate(objId);
			objectIds.add(objId);
			
			objectIdCount++;
		}
		
		return objectIds;
	}
	
}
