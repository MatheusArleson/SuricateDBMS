package br.com.xavier.suricate.dbms.interfaces.services;

import java.io.Serializable;

import br.com.xavier.suricate.dbms.interfaces.transactions.IObjectId;

public interface ILockManager extends Serializable {
	
	boolean isLocked(IObjectId objectId);
	void lock(IObjectId objectId);

}
