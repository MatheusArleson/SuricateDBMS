package br.com.xavier.suricate.dbms.interfaces.services.lock;

import java.io.Serializable;

import br.com.xavier.suricate.dbms.enums.LockType;
import br.com.xavier.suricate.dbms.enums.OperationTypes;
import br.com.xavier.suricate.dbms.interfaces.transactions.IObjectId;
import br.com.xavier.suricate.dbms.interfaces.transactions.operation.ITransactionOperation;

public interface ILock extends Serializable {
	
	ITransactionOperation getTransactionOperation();
	LockType getLockType();
	
	static boolean isCompatible(ILock lock, ILock otherLock){
		Long otherTxId = otherLock.getTransactionOperation().getTransaction().getId();
		Long thisTxId = lock.getTransactionOperation().getTransaction().getId();
		if(thisTxId.equals(otherTxId)){
			return true;
		}
		
		IObjectId otherObjectId = otherLock.getTransactionOperation().getObjectId();
		IObjectId thisObjectId = lock.getTransactionOperation().getObjectId();
		if( !thisObjectId.equals(otherObjectId) ){
			return true;
		}
		
		OperationTypes thisLockOperationType = lock.getTransactionOperation().getOperationType();
		OperationTypes otherLockOperationType = otherLock.getTransactionOperation().getOperationType();
		
		if(thisLockOperationType.equals(OperationTypes.READ) && otherLockOperationType.equals(OperationTypes.READ)){
			return true;
		}
		
		LockType thisLockType = lock.getLockType();
		LockType otherLockType = otherLock.getLockType();
		
		if(thisLockType.equals(LockType.INTENTIONAL) && otherLockType.equals(LockType.INTENTIONAL)){
			return true;
		}
		
		return false;
	}
	
}
