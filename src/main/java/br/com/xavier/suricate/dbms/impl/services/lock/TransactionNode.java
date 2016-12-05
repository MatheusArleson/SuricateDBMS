package br.com.xavier.suricate.dbms.impl.services.lock;

import java.util.Objects;

import br.com.xavier.graphs.abstractions.nodes.AbstractNode;
import br.com.xavier.suricate.dbms.interfaces.transactions.ITransaction;

public class TransactionNode extends AbstractNode implements Comparable<TransactionNode> {

	private Long transactionId;
	
	//XXX CONSTRUCTOR
	public TransactionNode(ITransaction tx) {
		this.transactionId = Objects.requireNonNull(tx.getId());
	}
	
	//XXX OVERRIDE METHODS
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((transactionId == null) ? 0 : transactionId.hashCode());
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
		if (transactionId == null) {
			if (other.transactionId != null)
				return false;
		} else if (!transactionId.equals(other.transactionId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "[transactionId=" + transactionId + "]";
	}
	
	@Override
	public int compareTo(TransactionNode other) {
		if(other == null){
			return -1;
		}
		
		if(transactionId == other.transactionId){
			return 0;
		} else {
			return transactionId < other.transactionId ? -1 : 1 ;
		}
	}
	
}
