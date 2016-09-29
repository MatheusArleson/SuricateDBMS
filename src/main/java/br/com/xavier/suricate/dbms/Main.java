package br.com.xavier.suricate.dbms;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

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
	
	public static void main(String[] args) throws IOException {
		String columnsSeparator = new String("|");
		String nameMetadataSeparator = new String("%");
		String typeSizeSeparator = new String("#");
		String endLineSeparator = new String("\n");
		ITextSeparators separators = new TextSeparators(columnsSeparator, nameMetadataSeparator, typeSizeSeparator, endLineSeparator);
		
		String fileName = "file_sample.txt";
		File textFile = new File(fileName);
		Charset charset = StandardCharsets.UTF_8;
		IThreeByteValue blockSize = new BigEndianThreeBytesValue(256);
		
		File workspaceFolder = new File("C:/matheus/eclipse/workspace/db_workspace");
		int bufferDataBlockSlots = 10;
		
		IDbms dbms = new SuricateDbms(workspaceFolder, bufferDataBlockSlots);
		ITable table = dbms.importTableFile(textFile, charset, separators, blockSize);
		
		String dataStr = dbms.printData(table, separators);
		System.out.println(dataStr);
		
		//dbms.importTableFile(textFile, charset, separators, blockSize);
	}
	
}
