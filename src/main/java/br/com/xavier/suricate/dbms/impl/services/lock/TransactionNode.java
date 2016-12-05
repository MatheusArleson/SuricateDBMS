package br.com.xavier.suricate.dbms.impl.services.lock;

import java.util.Objects;

import br.com.xavier.graphs.abstractions.nodes.AbstractNode;
import br.com.xavier.suricate.dbms.interfaces.transactions.ITransaction;

public class TransactionNode extends AbstractNode implements Comparable<TransactionNode> {

	private ITransaction tx;
	
	//XXX CONSTRUCTOR
	public TransactionNode(ITransaction tx) {
		this.tx = Objects.requireNonNull(tx);
	}
	
	//XXX OVERRIDE METHODS
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tx == null) ? 0 : tx.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TransactionNode other = (TransactionNode) obj;
		if (tx == null) {
			if (other.tx != null)
				return false;
		} else if (!tx.equals(other.tx))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "[transactionId=" + tx.getId() + "]";
	}
	
	@Override
	public int compareTo(TransactionNode other) {
		if(other == null){
			return -1;
		}
		
		Long thisTxId = tx.getId();
		Long otherTxId = other.tx.getId();
		if(thisTxId == otherTxId){
			return 0;
		} else {
			return thisTxId < otherTxId ? -1 : 1 ;
		}
	}
	
	//XXX GETTERS
	public ITransaction getTransaction() {
		return tx;
	}
}
