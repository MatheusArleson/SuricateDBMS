package br.com.xavier.suricate.dbms;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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
import br.com.xavier.suricate.dbms.interfaces.dbms.IDbms;
import br.com.xavier.suricate.dbms.interfaces.low.IThreeByteValue;
import br.com.xavier.suricate.dbms.interfaces.services.ITextSeparators;
import br.com.xavier.suricate.dbms.interfaces.table.ITable;
import br.com.xavier.suricate.dbms.interfaces.table.access.IRowId;

public final class Main {

	//XXX CONSTRUCTOR
	private Main() {}
	
	//XXX MAIN METHOD
	public static void main(String[] args) throws Exception {
		System.out.println("#> SURICATE DB > INITIALIZING PARAMETERS... > " + new Date());
		String columnsSeparator = new String("|");
		String nameMetadataSeparator = new String("%");
		String typeSizeSeparator = new String("#");
		String endLineSeparator = new String("\n");
		ITextSeparators separators = new TextSeparators(columnsSeparator, nameMetadataSeparator, typeSizeSeparator, endLineSeparator);
		
		String[] filePaths = new String[] {/*"file_samples/file_sample.txt", "file_samples/file_sample2.txt", "file_samples/file_sample3.txt",*/ "file_samples/forn-tpch.txt"/* , "file_samples/cli-tpch.txt"*/};
		Charset charset = StandardCharsets.UTF_8;
		IThreeByteValue blockSize = new BigEndianThreeBytesValue(4096);
		
		File workspaceFolder = new File("db_workspace");
		int bufferDataBlockSlots = 50;
		
		File outputFile = new File(workspaceFolder, "output.txt");
		System.out.println("#> SURICATE DB > PARAMETERS INITIALIZED > " + new Date());
		
		System.out.println("#> SURICATE DB > INITIALIZING WORKSPACE > " + new Date());
		IDbms dbms = new SuricateDbms(workspaceFolder, bufferDataBlockSlots);
		System.out.println("#> SURICATE DB > WORKSPACE INITIALIZED > " + new Date());
		
		System.out.println("#> SURICATE DB > IMPORTING TABLES > " + new Date());
		for (String filePath : filePaths) {
			System.out.println("##> IMPORTING FILE > " + filePath);
			File textFile = new File(filePath);
			ITable table = dbms.importTableFile(textFile, charset, separators, blockSize);
			
			System.out.println("##> FILE IMPORTED > " + filePath);
			System.out.println("###> Table Name > " + FilenameUtils.getBaseName( table.getFile().getName() ));
			System.out.println("###> Table Id > " + table.getHeaderBlock().getHeaderContent().getTableId());
			System.out.println("###> Table Next Free Block Id > " + table.getHeaderBlock().getHeaderContent().getNextFreeBlockId());
			System.out.println("###> Table Header Size > " + table.getHeaderBlock().getHeaderContent().getHeaderSize());
		}
		
		System.out.println("#> SURICATE DB > TABLES IMPORTED > " + new Date());
		
		System.out.println("#> SURICATE DB > SHUTDOWN > " + new Date());
		dbms.shutdown();
		dbms = null;
		System.out.println("#> SURICATE DB > INSTANCE DOWN > " + new Date());
		
		System.out.println("#> SURICATE DB > INITIALIZING NEW INSTANCE > " + new Date());
		dbms = new SuricateDbms(workspaceFolder, bufferDataBlockSlots);
		System.out.println("#> SURICATE DB > NEW INSTANCE INITIALIZED > " + new Date());
		
		System.out.println("#> SURICATE DB > OUTPUT TABLES DATA > " + new Date());
		String dataStr = dbms.dumpAllTablesData(separators);
		FileUtils.writeStringToFile(outputFile, dataStr, charset);
		System.out.println("#> SURICATE DB > END OF OUTPUT TABLES DATA > " + new Date());
		
		printBufferStatistics(dbms);
		
		System.out.println("#> SURICATE DB > FETCHING ALL ROW IDS > " + new Date());
		Collection<ITable> allTables = dbms.getAllTables();
		List<IRowId> allRowIds = new LinkedList<>();
		for (ITable table : allTables) {
			Collection<IRowId> tableRowIds = dbms.getRowIds(table);
			allRowIds.addAll(tableRowIds);
		}
		System.out.println("#> SURICATE DB > FINISHED FETCH ALL ROW IDS > " + new Date());
		
		System.out.println("#> SURICATE DB > SHUFFLING ALL ROW IDS > " + new Date());
		shuffle(allRowIds);
		System.out.println("#> SURICATE DB > FINISHED SHUFFLE ALL ROW IDS > " + new Date());
		
		System.out.println("#> SURICATE DB > ACCESSING SHUFFLED ROW IDS > " + new Date());
		for (IRowId rowId : allRowIds) {
			System.out.println("###> ACCESSING ROW ID > " + rowId.getTableId() + "." + rowId.getBlockId().getValue() + "." + rowId.getByteOffset());
			dbms.getDataBlock(rowId);
		}
		System.out.println("#> SURICATE DB > END OF ACCESS OF SHUFFLED ROW IDS > " + new Date());
		
		printBufferStatistics(dbms);
		
	}

	private static void shuffle(List<IRowId> allRowIds) {
		long seed = System.nanoTime();
		Random rng = new Random(seed);
		Collections.shuffle(allRowIds, rng);
	}

	private static void printBufferStatistics(IDbms dbms) {
		System.out.println("#> SURICATE DB > BUFFER STATISTICS > " + new Date());
		System.out.println( dbms.getBufferStatistics() );
		System.out.println("#> SURICATE DB > END OF BUFFER STATISTICS > " + new Date());
	}
	
}
