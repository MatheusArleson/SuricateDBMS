package br.com.xavier.suricate.dbms.interfaces.services;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import br.com.xavier.suricate.dbms.interfaces.low.IThreeByteValue;
import br.com.xavier.suricate.dbms.interfaces.table.ITable;

public interface IFileParser {
	
	ITable parse(File f, Charset charset, ITextSeparators separators, Byte tableId, IThreeByteValue blockSize) throws IOException;
	
}
