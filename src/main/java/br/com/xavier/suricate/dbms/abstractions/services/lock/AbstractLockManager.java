package br.com.xavier.suricate.dbms.abstractions.services.lock;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Queue;

import br.com.xavier.graphs.impl.algorithms.NodeInfo;
import br.com.xavier.graphs.impl.edges.DefaultUnweightedEdge;
import br.com.xavier.graphs.impl.simple.directed.DefaultSDUGraph;
import br.com.xavier.graphs.interfaces.Graph;
import br.com.xavier.suricate.dbms.abstractions.util.GraphCycleDetector;
import br.com.xavier.suricate.dbms.enums.LockType;
import br.com.xavier.suricate.dbms.enums.ObjectIdType;
import br.com.xavier.suricate.dbms.enums.OperationTypes;
import br.com.xavier.suricate.dbms.enums.TransactionOperationStatus;
import br.com.xavier.suricate.dbms.impl.services.lock.Lock;
import br.com.xavier.suricate.dbms.impl.services.lock.TransactionNode;
import br.com.xavier.suricate.dbms.impl.transactions.ScheduleResult;
import br.com.xavier.suricate.dbms.impl.transactions.operation.TransactionOperation;
import br.com.xavier.suricate.dbms.interfaces.services.ILockManager;
import br.com.xavier.suricate.dbms.interfaces.services.lock.ILock;
import br.com.xavier.suricate.dbms.interfaces.transactions.IObjectId;
import br.com.xavier.suricate.dbms.interfaces.transactions.IScheduleResult;
import br.com.xavier.suricate.dbms.interfaces.transactions.ITransaction;
import br.com.xavier.suricate.dbms.interfaces.transactions.operation.ITransactionOperation;

public abstract class AbstractLockManager implements ILockManager {

	private static final long serialVersionUID = -6920168270904481451L;
	
	//XXX PROPERTIES
	private Map<String, List<ILock>> tablesLocksMap;
	private Map<String, List<ILock>> blocksLocksMap;
	private Map<String, List<ILock>> rowsLocksMap;
	
	private Map<ITransaction, Queue<ITransactionOperation>> waitMap;
	
	private GraphCycleDetector graphCycleDetector;
	private List<NodeInfo> cycleNodesInfo;
	private Graph<TransactionNode, DefaultUnweightedEdge<TransactionNode>> waitForGraph;
	
	//XXX CONSTRUCTOR
	public AbstractLockManager() {
		generateMaps();
		generateGraphProperties();
	}
	
	//XXX ABSTRACT METHODS
	public abstract ILock generateLockInstance(ITransactionOperation txOp, LockType lockType);
	
	public abstract Collection<IScheduleResult> handleIncompatibleLocks(ILock lock, ILock otherLock);
	//public abstract IDeadLockResult detectDeadLock(ITransactionOperation txOp); 	
	//public abstract  Collection<ITransaction> resolveDeadLock(IDeadLockResult deadLockResult);

	//XXX IS LOCKED METHODS
	@Override
	public Collection<IScheduleResult> process(ITransactionOperation txOp) {
		OperationTypes operationType = txOp.getOperationType();
		if( operationType.equals(OperationTypes.COMMIT) || operationType.equals(OperationTypes.ABORT) ){
			return processFinalOperation(txOp);
		}
		
		return processOtherOperation(txOp);
	}
	
	private Collection<IScheduleResult> processFinalOperation(ITransactionOperation txOp) {
		removeNodeFromGraph(txOp);
		removeLocks(txOp.getTransaction());
		
		//XXX TODO FIXME free tx from wait and re-process them
		
		Collection<IScheduleResult> results = new LinkedList<>();
		results.add(new ScheduleResult(txOp, TransactionOperationStatus.SCHEDULED));
		return results;
	}

	private Collection<IScheduleResult> processOtherOperation(ITransactionOperation txOp){
		addNodeToGraph(txOp);
		List<ILock> relatedLocks = fetchRelatedLocks(txOp);
		
		if(relatedLocks != null && !relatedLocks.isEmpty()){
			ILock lock = generateLockInstance(txOp, LockType.NORMAL);
			return handleRelatedLocks(lock, relatedLocks);
		} else {
			addLocks( txOp );
			
			Collection<IScheduleResult> results = new LinkedList<>();
			results.add(new ScheduleResult(txOp, TransactionOperationStatus.SCHEDULED));
			return results;
		}
	}
	
	private Collection<IScheduleResult> handleRelatedLocks(ILock lock, List<ILock> relatedLocks) {
		ITransactionOperation lockTxOp = lock.getTransactionOperation();
		ITransaction lockTx = lockTxOp.getTransaction();
		
		Collection<IScheduleResult> schedulesResults = new LinkedList<>();
		Collection<ITransaction> abortedTransactions = new LinkedList<>();
		
		Iterator<ILock> locksIterator = relatedLocks.iterator();
		while(locksIterator.hasNext()){
			ILock relatedLock = locksIterator.next();
			ITransaction relatedTx = relatedLock.getTransactionOperation().getTransaction();
			
			boolean isOperationTxAborted = abortedTransactions.stream().anyMatch(s -> s.getId().equals( lockTx.getId() ) );
			if(isOperationTxAborted){
				break;
			}
			
			boolean isOperationTxWaiting = schedulesResults.stream().anyMatch(s -> 
				s.getTransactionOperation().getTransaction().getId().equals( lockTx.getId() )
				&& s.getStatus().equals(TransactionOperationStatus.WAITING)
			);
			if(isOperationTxWaiting){
				break;
			}
			
			boolean isRelatedLockTxAborted = abortedTransactions.stream().anyMatch(s -> s.getId().equals( relatedTx.getId() ) );
			if(isRelatedLockTxAborted){
				continue;
			}
			
			boolean isCompatible = ILock.isCompatible(lock, relatedLock);
			if( !isCompatible ){
				Collection<IScheduleResult> conflicResult = handleIncompatibleLocks(lock, relatedLock);
				schedulesResults.addAll(conflicResult);
				
				//syncronize conflict result with cenario
				for (IScheduleResult result : conflicResult) {
					switch ( result.getStatus() ) {
					case ABORT_TRANSACTION:
						processAbortResult(result, abortedTransactions);
						break;
						
					case WAITING:
						processWaitResult(lockTxOp, relatedTx);
						handleDeadLock(lockTx, schedulesResults, abortedTransactions); 
						break;

					default:
						throw new IllegalArgumentException("Unknow result type.");
					}
				}
			}
		}
		
		for (ITransaction abortedTx : abortedTransactions) {
			Collection<IScheduleResult>reprocessResults = reprocessWaitingTransactions( abortedTx );
			schedulesResults.addAll(reprocessResults);
		}
		
		if(schedulesResults.isEmpty()){
			schedulesResults.add(new ScheduleResult(lockTxOp, TransactionOperationStatus.SCHEDULED));
		}
		
		return schedulesResults;
	}

	private List<ILock> fetchRelatedLocks(ITransactionOperation txOp) {
		IObjectId objectId = txOp.getObjectId();
		ObjectIdType type = objectId .getType();
		
		switch ( type ) {
		case TABLE:
			return fetchTableReatedLocks(objectId);
			
		case BLOCK:
			return fetchBlockRelatedLocks(objectId);
			
		case ROW:
			return fetchRowRelatedLocks(objectId);
			
		default:
			handleUnknowObjectIdType();
			return null;
		}
	}
	
	private List<ILock> fetchTableReatedLocks(IObjectId objectId) {
		List<ILock> relatedLocks = new LinkedList<>();
		
		relatedLocks.addAll( fetchTableLocks(objectId) );
		
		return relatedLocks;
	}

	private List<ILock> fetchBlockRelatedLocks(IObjectId objectId) {
		List<ILock> relatedLocks = new LinkedList<>();
		
		relatedLocks.addAll( fetchTableLocks(objectId) );
		relatedLocks.addAll( fetchBlockLocks(objectId) );
		
		return relatedLocks;
	}

	private List<ILock> fetchRowRelatedLocks(IObjectId objectId) {
		List<ILock> relatedLocks = new LinkedList<>();
		
		relatedLocks.addAll( fetchTableLocks(objectId) );
		relatedLocks.addAll( fetchBlockLocks(objectId) );
		relatedLocks.addAll( fetchRowLocks(objectId) );
		
		return relatedLocks;
	}

	private List<ILock> fetchTableLocks(IObjectId objectId) {
		String mapKey = deriveTablesLocksMapKey(objectId);
		return fetchFromLockMap(mapKey, tablesLocksMap);
	}

	private List<ILock> fetchBlockLocks(IObjectId objectId) {
		String mapKey = deriveBlocksLocksMapKey(objectId);
		return fetchFromLockMap(mapKey, blocksLocksMap);
	}
	
	private List<ILock> fetchRowLocks(IObjectId objectId) {
		String mapKey = deriveRowsLocksMapKey(objectId);
		return fetchFromLockMap(mapKey, rowsLocksMap);
	}
	
	private void handleUnknowObjectIdType() {
		throw new UnsupportedOperationException("Unknow objectId type");
	}
	
	private void addLocks(ITransactionOperation txOp){
		switch (txOp.getObjectId().getType()) {
		case TABLE:
			addTableLock(txOp, LockType.NORMAL);
			return;
			
		case BLOCK:
			addBlockLock(txOp, LockType.NORMAL);
			addTableLock(txOp, LockType.INTENTIONAL);
			return;
		
		case ROW:
			addRowLock(txOp, LockType.NORMAL);
			addBlockLock(txOp, LockType.INTENTIONAL);
			addTableLock(txOp, LockType.INTENTIONAL);
			return;

		default:
			throw new UnsupportedOperationException("Unknow operation type.");
		}
	}
	
	private void addTableLock(ITransactionOperation txOp, LockType lockType) {
		ILock lock = new Lock(txOp, lockType);
		String mapKey = deriveTablesLocksMapKey(txOp.getObjectId());
		putInLockMap(mapKey, lock, tablesLocksMap);
	}

	private void addBlockLock(ITransactionOperation txOp, LockType lockType) {
		ILock lock = new Lock(txOp, lockType);
		String mapKey = deriveBlocksLocksMapKey(txOp.getObjectId());
		putInLockMap(mapKey, lock, blocksLocksMap);
	}

	private void addRowLock(ITransactionOperation txOp, LockType lockType) {
		ILock lock = new Lock(txOp, lockType);
		String mapKey = deriveRowsLocksMapKey(txOp.getObjectId());
		putInLockMap(mapKey, lock, rowsLocksMap);
	}
	
	private void removeLocks(ITransaction tx){
		removeRowLocks(tx);
		removeBlockLocks(tx);
		removeTableLocks(tx);
	}
	
	private void removeTableLocks(ITransaction tx) {
		removeFromLockMap(tx, blocksLocksMap);
	}

	private void removeBlockLocks(ITransaction tx) {
		removeFromLockMap(tx, blocksLocksMap);
	}

	private void removeRowLocks(ITransaction tx) {
		removeFromLockMap(tx, rowsLocksMap);
	}
	
	private void processAbortResult(IScheduleResult result, Collection<ITransaction> abortedTransactionBuffer){
		ITransactionOperation resultTxOp = result.getTransactionOperation();
		ITransaction resultTx = resultTxOp.getTransaction();
		
		abortedTransactionBuffer.add(resultTx);
		removeNodeFromGraph(resultTxOp);
		removeLocks(resultTx);
	}
	
	private void processWaitResult(ITransactionOperation txOp, ITransaction otherTx){
		ITransaction tx = txOp.getTransaction();
		addEdge(tx, otherTx);
		addToWaitMap(otherTx, txOp);
	}
	
	private void handleDeadLock(ITransaction lockTx, Collection<IScheduleResult> schedulesResults, Collection<ITransaction> abortedTransactions) {
		boolean hasDeadLock = detectGraphCycle( lockTx );
		if( hasDeadLock ){
			IScheduleResult deadLockResult = solveDeadLock(cycleNodesInfo);
			schedulesResults.add(deadLockResult);
			processAbortResult(deadLockResult, abortedTransactions);
		}
	}

	private IScheduleResult solveDeadLock(Collection<NodeInfo> cycleNodesInfo) {
		Collection<TransactionNode> visitedTxNodes = fetchVisitedNodes(cycleNodesInfo);
		ITransaction abortedTx = fetchMostRecentTransaction(visitedTxNodes);
		
		ITransactionOperation txOp = new TransactionOperation(abortedTx , OperationTypes.ABORT, null);
		IScheduleResult result = new ScheduleResult(txOp, TransactionOperationStatus.ABORT_TRANSACTION);
		
		return result;
	}
	
	private Collection<IScheduleResult> reprocessWaitingTransactions(ITransaction abortedTx){
		LinkedList<IScheduleResult> schedulesResults = new LinkedList<>();
		
		Queue<ITransactionOperation> waitingOperations = getWaitingOperations( abortedTx );
		for (ITransactionOperation waitingTxOp : waitingOperations) {
			Collection<IScheduleResult> results = process(waitingTxOp);
			schedulesResults.addAll(results);
		}
		
		purgeFromWaitMap(abortedTx);
		return schedulesResults;
	}
	
	//XXX LOCK MAPS METHODS
	private void generateMaps() {
		this.tablesLocksMap = new LinkedHashMap<>();
		this.blocksLocksMap = new LinkedHashMap<>();
		this.rowsLocksMap = new LinkedHashMap<>();
		this.waitMap = new LinkedHashMap<>();
	}
	
	private String deriveTablesLocksMapKey(IObjectId objectId) {
		return String.valueOf( objectId.getTableId() );
	}
	
	private String deriveBlocksLocksMapKey(IObjectId objectId) {
		return deriveTablesLocksMapKey(objectId) + "." + String.valueOf( objectId.getBlockId().getValue() );
	}
	
	private String deriveRowsLocksMapKey(IObjectId objectId) {
		return deriveBlocksLocksMapKey(objectId) + "." + String.valueOf( objectId.getByteOffset() );
	}

	private List<ILock> fetchFromLockMap(String mapKey, Map<String, List<ILock>> map) {
		List<ILock> mapCollection = map.get(mapKey);
		if(mapCollection == null){
			return Collections.emptyList();
		}
		
		List<ILock> clonedCollection = new LinkedList<>( mapCollection );
		return clonedCollection;
	}
	
	private void putInLockMap(String mapKey, ILock lock, Map<String, List<ILock>> map) {
		List<ILock> locks = map.get(mapKey);
		if( locks == null ){
			locks = new LinkedList<>();
		}
		
		locks.add(lock);
		map.put(mapKey, locks);
	}
	
	private void removeFromLockMap(ITransaction tx, Map<String, List<ILock>> map) {
		for (String mapKey : map.keySet()) {
			List<ILock> locks = map.get(mapKey);
			ListIterator<ILock> iterator = locks.listIterator();
			while(iterator.hasNext()){
				ILock lock = iterator.next();
				Long lockTxId = lock.getTransactionOperation().getTransaction().getId();
				if( lockTxId.equals(tx.getId()) ){
					iterator.remove();
				}
			}
		}
	}
	
	//XXX WAIT MAP METHODS
	private void addToWaitMap(ITransaction tx, ITransactionOperation txOp){
		Queue<ITransactionOperation> waitingTxOps = waitMap.get(tx);
		if(waitingTxOps == null){
			waitingTxOps = new LinkedList<>();
		}
		
		waitingTxOps.add(txOp);
		waitMap.put(tx, waitingTxOps);
	}
	
	private Queue<ITransactionOperation> getWaitingOperations(ITransaction tx){
		Queue<ITransactionOperation> waitingQueue = waitMap.get(tx);
		if(waitingQueue == null){
			return new LinkedList<>();
		}
		
		return waitingQueue;
	}
	
	private void purgeFromWaitMap(ITransaction tx){
		Iterator<Map.Entry<ITransaction, Queue<ITransactionOperation>>> mapKeysIterator = waitMap.entrySet().iterator();
		while (mapKeysIterator.hasNext()) {
			Queue<ITransactionOperation> waitingTxOps = waitMap.get(mapKeysIterator.next());
			
			Iterator<ITransactionOperation> iterator = waitingTxOps.iterator();
			while(iterator.hasNext()){
				ITransactionOperation txOp = iterator.next();
				if(txOp.getTransaction().getId().equals(tx.getId())){
					iterator.remove();
				}
			}
			
			if(waitingTxOps.isEmpty()){
				mapKeysIterator.remove();
			}
		}
	}
	
	//XXX GRAPH METHODS
	private void generateGraphProperties() {
		this.waitForGraph = new DefaultSDUGraph<>();
		this.graphCycleDetector = new GraphCycleDetector();
	}
	
	private void addNodeToGraph(ITransactionOperation txOp) {
		TransactionNode node = new TransactionNode( txOp.getTransaction() );
		if( !waitForGraph.containsNode(node) ){
			waitForGraph.addNode(node);
		}
		//write snapshot here
	}
	
	private void removeNodeFromGraph(ITransactionOperation txOp) {
		TransactionNode node = new TransactionNode( txOp.getTransaction() );
		waitForGraph.removeNode(node);
		//write snapshot here
	}
	
	private void addEdge(ITransaction sourceTx, ITransaction targetTx){
		TransactionNode source = new TransactionNode(sourceTx);
		TransactionNode target = new TransactionNode(targetTx);
		DefaultUnweightedEdge<TransactionNode> edge = new DefaultUnweightedEdge<TransactionNode>(source, target);
		waitForGraph.addEdge(edge);
		//write snapshot here
	}
	
	private boolean detectGraphCycle(ITransaction tx){
		this.cycleNodesInfo = graphCycleDetector.doAlgorithm( waitForGraph, new TransactionNode(tx) );
		return graphCycleDetector.hasCycle();
	}
	
	private Collection<TransactionNode> fetchVisitedNodes(Collection<NodeInfo> nodesInfo){
		Collection<TransactionNode> visitedNodes = new LinkedList<>();
		for (NodeInfo nodeInfo : nodesInfo) {
			if( nodeInfo.isVisited() ){
				visitedNodes.add((TransactionNode) nodeInfo.getNode());
			}
		}
		
		return visitedNodes;
	}
	
	private ITransaction fetchMostRecentTransaction(Collection<TransactionNode> visitedTxNodes){
		ITransaction mostRecentTx = null;
		for (TransactionNode node : visitedTxNodes) {
			ITransaction visitedTx = node.getTransaction();
			if(mostRecentTx == null){
				mostRecentTx = visitedTx;
			} else {
				boolean isAfter = visitedTx.getTimeStamp().isAfter(mostRecentTx.getTimeStamp());
				if( isAfter  ){
					mostRecentTx = visitedTx;
				}
			}
		}
		
		return mostRecentTx;
	}
	
	//XXX GETTERS/SETTERS
	protected Graph<TransactionNode, DefaultUnweightedEdge<TransactionNode>> getWaitForGraph() {
		return waitForGraph;
	}
}
