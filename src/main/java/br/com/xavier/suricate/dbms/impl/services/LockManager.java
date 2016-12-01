package br.com.xavier.suricate.dbms.impl.services;

import java.io.File;

import br.com.xavier.suricate.dbms.abstractions.services.AbstractLockManager;

public final class LockManager extends AbstractLockManager {

	private static final long serialVersionUID = 4488618041195325784L;

	public LockManager(File workspaceFolder) {
		super(workspaceFolder);
	}

}
