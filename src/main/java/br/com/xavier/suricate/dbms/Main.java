package br.com.xavier.suricate.dbms;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;

import org.apache.commons.io.FilenameUtils;

import br.com.xavier.suricate.dbms.impl.dbms.SuricateDbms;
import br.com.xavier.suricate.dbms.impl.low.BigEndianThreeBytesValue;
import br.com.xavier.suricate.dbms.impl.services.TextSeparators;
import br.com.xavier.suricate.dbms.interfaces.dbms.IDbms;
import br.com.xavier.suricate.dbms.interfaces.low.IThreeByteValue;
import br.com.xavier.suricate.dbms.interfaces.services.ITextSeparators;
import br.com.xavier.suricate.dbms.interfaces.table.ITable;

public final class Main {

	//XXX CONSTRUCTOR
	private Main() {}
	
	public static void main(String[] args) throws Exception {
		System.out.println("#> SURICATE DB > INITIALIZING PARAMETERS... > " + new Date());
		String columnsSeparator = new String("|");
		String nameMetadataSeparator = new String("%");
		String typeSizeSeparator = new String("#");
		String endLineSeparator = new String("\n");
		ITextSeparators separators = new TextSeparators(columnsSeparator, nameMetadataSeparator, typeSizeSeparator, endLineSeparator);
		
		String[] filePaths = new String[] {"file_sample.txt", "file_sample2.txt", "file_sample3.txt", };
		Charset charset = StandardCharsets.UTF_8;
		IThreeByteValue blockSize = new BigEndianThreeBytesValue(256);
		
		File workspaceFolder = new File("C:/Users/matheus.xavier/Documents/GitHub/SuricateDBMS/db_workspace");
		int bufferDataBlockSlots = 10;
		System.out.println("#> SURICATE DB > PARAMETERS INITIALIZED > " + new Date());
		
		System.out.println("#> SURICATE DB > INITIALIZING WORKSPACE > " + new Date());
		IDbms dbms = new SuricateDbms(workspaceFolder, bufferDataBlockSlots);
		System.out.println("#> SURICATE DB > WORKSPACE INITIALIZED > " + new Date());
		
		System.out.println("#> SURICATE DB > IMPORTING TABLES > " + new Date());
		for (String filePath : filePaths) {
			System.out.println("##> IMPORTING FILE > " + filePath);
			File textFile = new File(filePath);
			dbms.importTableFile(textFile, charset, separators, blockSize);
			System.out.println("##> FILE IMPORTED > " + filePath);
		}
		
		System.out.println("#> SURICATE DB > TABLES IMPORTED > " + new Date());
		
		System.out.println("#> SURICATE DB > SHUTDOWN > " + new Date());
		dbms.shutdown();
		dbms = null;
		System.out.println("#> SURICATE DB > INSTANCE DOWN > " + new Date());
		
		System.out.println("#> SURICATE DB > INITIALIZING NEW INSTANCE > " + new Date());
		dbms = new SuricateDbms(workspaceFolder, bufferDataBlockSlots);
		System.out.println("#> SURICATE DB > NEW INSTANCE INITIALIZED > " + new Date());
		
		System.out.println("#> SURICATE DB > PRINT TABLES DATA > " + new Date());
		Collection<ITable> tables = dbms.getAllTables();
		for (ITable table : tables) {
			String tableName = FilenameUtils.getBaseName(table.getFile().getName());
			System.out.println("##> TABLE > " + tableName);
			String dataStr = dbms.printData(table, separators);
			System.out.println(dataStr);
		}
		System.out.println("#> SURICATE DB > END OF PRINT TABLES DATA > " + new Date());
	}
	
}
