package br.com.xavier.suricate.dbms.interfaces.dbms;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

public interface IDbms
		extends Serializable, ITableManager, IRowManager {
	
	File getWorkspace() throws IOException;
	void setWorkspace(File workspaceFolder) throws IOException;

}
