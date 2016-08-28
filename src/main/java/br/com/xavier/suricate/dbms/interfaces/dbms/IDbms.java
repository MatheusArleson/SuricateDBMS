package br.com.xavier.suricate.dbms.interfaces.dbms;

import java.io.File;
import java.io.Serializable;

public interface IDbms
		extends Serializable, ITableManager, IRowManager {
	
	File getWorkspace();
	void setWorkspace(File workspaceFolder);

}
