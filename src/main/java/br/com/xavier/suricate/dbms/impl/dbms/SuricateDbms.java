package br.com.xavier.suricate.dbms.impl.dbms;

import java.io.File;
import java.io.IOException;

import br.com.xavier.suricate.dbms.abstractions.dbms.AbstractDbms;
import br.com.xavier.suricate.dbms.impl.util.FileNameFilter;

public class SuricateDbms 
		extends AbstractDbms {

	private static final long serialVersionUID = 8305216838403911711L;

	private static final String SURICATE_FILE_SUFFIX = ".suricate";
	
	public SuricateDbms(File workspaceFolder) throws IOException {
		super(new FileNameFilter(SURICATE_FILE_SUFFIX), workspaceFolder);
	}

}
