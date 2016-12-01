package br.com.xavier.suricate.dbms.abstractions.services;

import br.com.xavier.graphs.impl.edges.DefaultUnweightedEdge;
import br.com.xavier.graphs.impl.nodes.NumberedNode;
import br.com.xavier.graphs.impl.simple.directed.DefaultSDUGraph;
import br.com.xavier.graphs.interfaces.Graph;
import br.com.xavier.suricate.dbms.interfaces.services.IDeadLockManager;
import br.com.xavier.suricate.dbms.interfaces.transactions.IDeadLockResult;
import br.com.xavier.suricate.dbms.interfaces.transactions.operation.ITransactionOperation;

public abstract class AbstractDeadLockManager implements IDeadLockManager {

	private static final long serialVersionUID = 8953790804589801830L;
	
	//XXX DEPENDENCIES
	private Graph<NumberedNode, DefaultUnweightedEdge<NumberedNode>> waitGraph;
	
	//XXX CONSTRUCTOR
	public AbstractDeadLockManager() {
		this.waitGraph = new DefaultSDUGraph<>();
	}
	
	//XXX OVERRIDE METHODS
	@Override
	public IDeadLockResult checkDeadLock(ITransactionOperation txOp) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
