package br.com.xavier.suricate.dbms.impl.dbms;

import java.io.File;
import java.io.IOException;

import br.com.xavier.suricate.dbms.abstractions.dbms.AbstractDbms;
import br.com.xavier.suricate.dbms.impl.services.FileNameFilter;
import br.com.xavier.suricate.dbms.interfaces.services.ILockManager;

public class SuricateDbms 
		extends AbstractDbms {

	private static final long serialVersionUID = 8305216838403911711L;

	private static final String SURICATE_FILE_SUFFIX = ".suricata";
	
	public SuricateDbms(File workspaceFolder, int bufferDataBlockSlots, ILockManager lockManager) throws IOException {
		super(workspaceFolder, new FileNameFilter(SURICATE_FILE_SUFFIX), bufferDataBlockSlots, lockManager);
	}

}
