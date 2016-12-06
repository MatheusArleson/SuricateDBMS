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
		Collection<IObjectId> objectIds = generateObjectIds(numberOfTransactions, new Random(System.nanoTime()));

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

	//TODO FIXME generate object ids for table
	//TODO FIXME generate object ids for row
	private static Collection<IObjectId> generateObjectIds(int numberOfTransactions, Random random) {
		List<IObjectId> objectIds = new ArrayList<>();
		
		int objectIdCount = 0;
		while(objectIdCount < numberOfTransactions){
			Integer randomNumber = random.nextInt(20);
			Byte tableId = randomNumber.byteValue();
			
			IThreeByteValue blockId = new BigEndianThreeBytesValue(1);
			Long byteOffset = -1L;
			
			IObjectId objId = new ObjectId(tableId, blockId, byteOffset );
			objectIds.add(objId);
			
			objectIdCount++;
		}
		
		return objectIds;
	}
	
}
