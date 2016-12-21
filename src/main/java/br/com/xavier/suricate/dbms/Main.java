package br.com.xavier.suricate.dbms;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import br.com.xavier.suricate.dbms.impl.dbms.SuricateDbms;
import br.com.xavier.suricate.dbms.impl.low.BigEndianThreeBytesValue;
import br.com.xavier.suricate.dbms.impl.services.TextSeparators;
import br.com.xavier.suricate.dbms.impl.services.lock.WaitDieDeadLockManager;
import br.com.xavier.suricate.dbms.interfaces.dbms.IDbms;
import br.com.xavier.suricate.dbms.interfaces.low.IThreeByteValue;
import br.com.xavier.suricate.dbms.interfaces.services.ILockManager;
import br.com.xavier.suricate.dbms.interfaces.services.ITextSeparators;
import br.com.xavier.suricate.dbms.interfaces.table.ITable;
import br.com.xavier.suricate.dbms.interfaces.table.access.IRowId;

public final class Main {

	private static File workspaceFolder = new File("db_workspace");
	private static IThreeByteValue blockSize = new BigEndianThreeBytesValue(4096);
	private static Integer bufferDataBlockSlots = 3000;
	
	private static File outputDataFile = new File(workspaceFolder, "outputData.txt");
	private static File outputLogFile = new File(workspaceFolder, "outputLog.log");
	
	private static String columnsSeparator = new String("|");
	private static String nameMetadataSeparator = new String("%");
	private static String typeSizeSeparator = new String("#");
	private static String endLineSeparator = new String("\n");
	
	private static String[] filesPaths = new String[] {"file_samples/forn-tpch.txt" , "file_samples/cli-tpch.txt"/*, "file_samples/file_sample.txt", "file_samples/file_sample2.txt", "file_samples/file_sample3.txt"*/};
	private static Charset filesCharset = StandardCharsets.UTF_8;
	
	private static String timeStampPattern = "dd/MM/yyyy HH:mm:ss.SSS";
	private static SimpleDateFormat dateFormatter = new SimpleDateFormat(timeStampPattern);
	
	//XXX CONSTRUCTOR
	private Main() {}
	
	//XXX MAIN METHOD
	public static void main(String[] args) throws Exception {
		sysoAndLog("#> SURICATE DB > INITIALIZING PARAMETERS... > ");
		ITextSeparators separators = new TextSeparators(columnsSeparator, nameMetadataSeparator, typeSizeSeparator, endLineSeparator);
		sysoAndLog("#> SURICATE DB > PARAMETERS INITIALIZED > ");
		
		sysoAndLog("#> SURICATE DB > INITIALIZING WORKSPACE > ");
		ILockManager lockManager = new WaitDieDeadLockManager(workspaceFolder, false);
		IDbms dbms = new SuricateDbms(workspaceFolder, bufferDataBlockSlots, lockManager);
		sysoAndLog("#> SURICATE DB > WORKSPACE INITIALIZED > ");
		
		sysoAndLog("#> SURICATE DB > IMPORTING TABLES > ");
		for (String filePath : filesPaths) {
			sysoAndLog("##> IMPORTING FILE > " + filePath);
			File textFile = new File(filePath);
			ITable table = dbms.importTableFile(textFile, filesCharset, separators, blockSize);
			
			sysoAndLog("##> FILE IMPORTED > " + filePath);
			sysoAndLog("###> Table Name > " + FilenameUtils.getBaseName( table.getFile().getName() ));
			sysoAndLog("###> Table Id > " + table.getHeaderBlock().getHeaderContent().getTableId());
			sysoAndLog("###> Table Next Free Block Id > " + table.getHeaderBlock().getHeaderContent().getNextFreeBlockId());
			sysoAndLog("###> Table Header Size > " + table.getHeaderBlock().getHeaderContent().getHeaderSize());
		}
		
		sysoAndLog("#> SURICATE DB > TABLES IMPORTED > ");
		
		sysoAndLog("#> SURICATE DB > SHUTDOWN > ");
		dbms.shutdown();
		dbms = null;
		sysoAndLog("#> SURICATE DB > INSTANCE DOWN > ");
		
		sysoAndLog("#> SURICATE DB > INITIALIZING NEW INSTANCE > ");
		dbms = new SuricateDbms(workspaceFolder, bufferDataBlockSlots, lockManager);
		sysoAndLog("#> SURICATE DB > NEW INSTANCE INITIALIZED > ");
		
		sysoAndLog("#> SURICATE DB > OUTPUT TABLES DATA > ");
		String dataStr = dbms.dumpAllTablesData(separators);
		FileUtils.writeStringToFile(outputDataFile, dataStr, filesCharset);
		sysoAndLog("#> SURICATE DB > END OF OUTPUT TABLES DATA > ");
		
		sysoAndLog("#> SURICATE DB > BUFFER STATISTICS > ");
		sysoAndLog( dbms.getBufferStatistics() );
		sysoAndLog("#> SURICATE DB > END OF BUFFER STATISTICS > ");
		
		sysoAndLog("#> SURICATE DB > FETCHING ALL ROW IDS > ");
		Collection<ITable> allTables = dbms.getAllTables();
		List<IRowId> allRowIds = new LinkedList<>();
		for (ITable table : allTables) {
			Collection<IRowId> tableRowIds = dbms.getRowIds(table);
			allRowIds.addAll(tableRowIds);
		}
		sysoAndLog("#> SURICATE DB > FINISHED FETCH ALL ROW IDS > ");
		
		sysoAndLog("#> SURICATE DB > SHUFFLING ALL ROW IDS > ");
		shuffle(allRowIds);
		sysoAndLog("#> SURICATE DB > FINISHED SHUFFLE ALL ROW IDS > ");
		
		sysoAndLog("#> SURICATE DB > ACCESSING SHUFFLED ROW IDS > ");
		for (IRowId rowId : allRowIds) {
			sysoAndLog("###> ACCESSING ROW ID > " + rowId.getTableId() + "." + rowId.getBlockId().getValue() + "." + rowId.getByteOffset());
			dbms.getDataBlock(rowId);
		}
		sysoAndLog("#> SURICATE DB > END OF ACCESS OF SHUFFLED ROW IDS > ");
		
		sysoAndLog("#> SURICATE DB > BUFFER STATISTICS > ");
		sysoAndLog( dbms.getBufferStatistics() );
		sysoAndLog("#> SURICATE DB > END OF BUFFER STATISTICS > ");
		
		dbms.shutdown();
		dbms = null;
	}
	
	private static void sysoAndLog(String str) throws IOException {
		String message = dateFormatter.format(new Date()) + " : " + str  + "\n";
		System.out.print(message);
		
		FileUtils.writeStringToFile(outputLogFile, message, StandardCharsets.UTF_8, true);
	}
	
	private static void shuffle(List<IRowId> allRowIds) {
		long seed = System.nanoTime();
		Random rng = new Random(seed);
		Collections.shuffle(allRowIds, rng);
	}

}
