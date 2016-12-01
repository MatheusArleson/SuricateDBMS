package br.com.xavier.suricate.dbms.abstractions.services;

import br.com.xavier.suricate.dbms.interfaces.services.IWaitManager;
import br.com.xavier.suricate.dbms.interfaces.transactions.operation.ITransactionOperation;

public abstract class AbstractWaitManager implements IWaitManager {

	private static final long serialVersionUID = 5865471113265824222L;
	
	//XXX CONSTRUCTOR
	public AbstractWaitManager() {	}
	
	//XXX OVERRIDE METHODS
	@Override
	public boolean isWaiting(ITransactionOperation txOp) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void putOnWait(ITransactionOperation txOp) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void freeFromWait(ITransactionOperation txOp) {
		// TODO Auto-generated method stub
		
	}
	
}
