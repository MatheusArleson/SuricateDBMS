package br.com.xavier.suricate.dbms.abstractions.services;

import java.util.Collection;
import java.util.Objects;

import org.apache.log4j.Logger;

import br.com.xavier.suricate.dbms.interfaces.services.ILockManager;
import br.com.xavier.suricate.dbms.interfaces.services.ITransactionManager;
import br.com.xavier.suricate.dbms.interfaces.transactions.IScheduleResult;
import br.com.xavier.suricate.dbms.interfaces.transactions.operation.ITransactionOperation;

public abstract class AbstractTransactionManager implements ITransactionManager {

	private static final long serialVersionUID = 4158707909856020194L;
	
	private static final Logger LOGGER = Logger.getLogger(AbstractTransactionManager.class);
	
	//XXX DEPENDENCIES
	private final ILockManager lockManager;
	
	//XXX CONSTRUCTOR
	public AbstractTransactionManager(ILockManager lockManager) {	
		this.lockManager = Objects.requireNonNull( lockManager );
	}
	
	//XXX OVERRIDE METHODS
	@Override
	public Collection<IScheduleResult> schedule(ITransactionOperation txOp) {
		LOGGER.debug("##> TX_MGR > SCHEDULE > " + txOp.toString());
		return lockManager.process(txOp);
	}
}
