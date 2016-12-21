package br.com.xavier.suricate.dbms.abstractions.services.lock;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Queue;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import br.com.xavier.graphs.abstractions.AbstractGraph;
import br.com.xavier.graphs.impl.algorithms.NodeInfo;
import br.com.xavier.graphs.impl.edges.DefaultUnweightedEdge;
import br.com.xavier.graphs.impl.parser.CytoscapeUnweightedParser;
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
	
	private static final Logger LOGGER = Logger.getLogger(AbstractLockManager.class);
	private static final String TEMPLATE_REPLACE_TOKEN = "#JS#";
	
	//XXX PROPERTIES
	private final File workspaceFolder;
	private final boolean generateSnapshots;
	
	private Map<String, List<ILock>> tablesLocksMap;
	private Map<String, List<ILock>> blocksLocksMap;
	private Map<String, List<ILock>> rowsLocksMap;
	
	private Map<ITransaction, Queue<ITransactionOperation>> waitMap;
	
	private GraphCycleDetector graphCycleDetector;
	private CytoscapeUnweightedParser<TransactionNode, DefaultUnweightedEdge<TransactionNode>> graphJavaScriptParser;
	private List<NodeInfo> cycleNodesInfo;
	
	private Long snapShotCounter;
	private Graph<TransactionNode, DefaultUnweightedEdge<TransactionNode>> waitForGraph;
	private Queue<String> graphTraffic;
	
	//XXX CONSTRUCTOR
	public AbstractLockManager(File workspaceFolder, boolean generateSnapshots) {
		this.workspaceFolder = workspaceFolder;
		this.generateSnapshots = generateSnapshots;
		generateMaps();
		generateGraphProperties();
	}
	
	//XXX ABSTRACT METHODS
	public abstract ILock generateLockInstance(ITransactionOperation txOp, LockType lockType);
	public abstract Collection<IScheduleResult> handleIncompatibleLocks(ILock lock, ILock otherLock);

	//XXX IS LOCKED METHODS
	@Override
	public Collection<IScheduleResult> process(ITransactionOperation txOp) {
		LOGGER.debug("###> LCK_MGR > PROCESS > " + txOp.toString());
		
		OperationTypes operationType = txOp.getOperationType();
		if( operationType.equals(OperationTypes.COMMIT) || operationType.equals(OperationTypes.ABORT) ){
			LOGGER.debug("#> LCK_MGR > PROCESS FINAL OPERATION");
			return processFinalOperation(txOp);
		}
		
		LOGGER.debug("#> LCK_MGR > PROCESS OTHER OPERATION");
		return processOtherOperation(txOp);
	}
	
	private Collection<IScheduleResult> processFinalOperation(ITransactionOperation txOp) {
		LOGGER.debug("##> LCK_MGR > REMOVE TX NODE FROM GRAPH");
		removeNodeFromGraph(txOp);
		
		ITransaction tx = txOp.getTransaction();
		
		LOGGER.debug("##> LCK_MGR > PURGE TX RELATED LOCKS");
		removeLocks(tx);
		
		LOGGER.debug("##> LCK_MGR > GENERATE SCHEDULED RESULT");
		Collection<IScheduleResult> results = new LinkedList<>();
		results.add(new ScheduleResult(txOp, TransactionOperationStatus.SCHEDULED));
		
		LOGGER.debug("##> LCK_MGR > RE-PROCESS WAITING TX");
		Collection<IScheduleResult> reprocessResults = reprocessWaitingTransactions(tx);
		results.addAll(reprocessResults);
		
		LOGGER.debug("##> LCK_MGR > RETURN RESULTS");
		return results;
	}

	private Collection<IScheduleResult> processOtherOperation(ITransactionOperation txOp){
		LOGGER.debug("##> LCK_MGR > ADD TX NODE TO GRAPH");
		addNodeToGraph(txOp);
		
		LOGGER.debug("##> LCK_MGR > FETCH RELATED LOCKS");
		List<ILock> relatedLocks = fetchRelatedLocks(txOp);
		
		if(relatedLocks != null && !relatedLocks.isEmpty()){
			LOGGER.debug("##> LCK_MGR > RELATED LOCKS FOUND > CHECK COMPATIBILITY");
			ILock lock = generateLockInstance(txOp, LockType.NORMAL);
			return handleRelatedLocks(lock, relatedLocks);
		} else {
			LOGGER.debug("##> LCK_MGR > NO RELATED LOCKS FOUND");
			
			LOGGER.debug("##> LCK_MGR > ADD RELATED LOCKS");
			addLocks( txOp );
			
			LOGGER.debug("##> LCK_MGR > GENERATE SCHEDULED RESULT");
			Collection<IScheduleResult> results = new LinkedList<>();
			results.add(new ScheduleResult(txOp, TransactionOperationStatus.SCHEDULED));
			
			LOGGER.debug("##> LCK_MGR > RETURN RESULTS");
			return results;
		}
	}
	
	private Collection<IScheduleResult> handleRelatedLocks(ILock lock, List<ILock> relatedLocks) {
		LOGGER.debug("###> LCK_MGR > LOCKS > HANDLING RELATED LOCKS");
		
		ITransactionOperation lockTxOp = lock.getTransactionOperation();
		ITransaction lockTx = lockTxOp.getTransaction();
		
		Collection<IScheduleResult> schedulesResults = new LinkedList<>();
		Collection<ITransaction> abortedTransactions = new LinkedList<>();
		
		Iterator<ILock> locksIterator = relatedLocks.iterator();
		while(locksIterator.hasNext()){
			ILock relatedLock = locksIterator.next();
			LOGGER.debug("###> LCK_MGR > REALTED LOCKS > CHECKING LOCK > " + relatedLock.toString());
			
			ITransaction relatedTx = relatedLock.getTransactionOperation().getTransaction();
			
			boolean isOperationTxAborted = abortedTransactions.stream().anyMatch(s -> s.getId().equals( lockTx.getId() ) );
			if(isOperationTxAborted){
				LOGGER.debug("###> LCK_MGR > RELATED LOCKS > TX FROM TX_OP WAS ABORTED.");
				break;
			}
			
			boolean isOperationTxWaiting = schedulesResults.stream().anyMatch(s -> 
				s.getTransactionOperation().getTransaction().getId().equals( lockTx.getId() )
				&& s.getStatus().equals(TransactionOperationStatus.WAITING)
			);
			if(isOperationTxWaiting){
				LOGGER.debug("###> LCK_MGR > RELATED LOCKS > TX FROM TX_OP IS WAITING.");
				break;
			}
			
			boolean isRelatedLockTxAborted = abortedTransactions.stream().anyMatch(s -> s.getId().equals( relatedTx.getId() ) );
			if(isRelatedLockTxAborted){
				LOGGER.debug("###> LCK_MGR > RELATED LOCKS > RELATED TX WAS ABORTED > SKIPPING");
				continue;
			}
			
			boolean isCompatible = ILock.isCompatible(lock, relatedLock);
			LOGGER.debug("###> LCK_MGR > RELATED LOCKS > COMPATIBLE > " + isCompatible);
			
			if( !isCompatible ){
				LOGGER.debug("###> LCK_MGR > RELATED LOCKS > DELEGATING HANDLE OF IMCOMPATIBLE LOCKS");
				Collection<IScheduleResult> conflicResult = handleIncompatibleLocks(lock, relatedLock);
				schedulesResults.addAll(conflicResult);
				
				LOGGER.debug("###> LCK_MGR > RELATED LOCKS > SYNC RESULT");
				for (IScheduleResult result : conflicResult) {
					switch ( result.getStatus() ) {
					case ABORT_TRANSACTION:
						
						LOGGER.debug("###> LCK_MGR > RELATED LOCKS > PROCESS ABORT RESULT");
						processAbortResult(result, abortedTransactions);
						break;
						
					case WAITING:
						LOGGER.debug("###> LCK_MGR > RELATED LOCKS > PROCESS WAIT RESULT");
						processWaitResult(lockTxOp, relatedTx);
						
						LOGGER.debug("###> LCK_MGR > RELATED LOCKS > CHECK DEAD LOCK");
						handleDeadLock(lockTx, schedulesResults, abortedTransactions); 
						break;

					default:
						throw new IllegalArgumentException("Unknow result type.");
					}
				}
			}
		}
		
		LOGGER.debug("###> LCK_MGR > RELATED LOCKS > RE-PROCESS ABORTED TXS WAITING TX-OPS");
		for (ITransaction abortedTx : abortedTransactions) {
			Collection<IScheduleResult>reprocessResults = reprocessWaitingTransactions( abortedTx );
			schedulesResults.addAll(reprocessResults);
		}
		
		if(schedulesResults.isEmpty()){
			LOGGER.debug("###> LCK_MGR > RELATED LOCKS > NO CONFLICT FOUND");
			schedulesResults.add(new ScheduleResult(lockTxOp, TransactionOperationStatus.SCHEDULED));
		}
		
		return schedulesResults;
	}

	private List<ILock> fetchRelatedLocks(ITransactionOperation txOp) {
		LOGGER.debug("###> LCK_MGR > RELATED LOCKS > FETCHING RELATED LOCKS");
		
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
			handleUnknowObjectIdType(type);
			return null;
		}
	}
	
	private List<ILock> fetchTableReatedLocks(IObjectId objectId) {
		LOGGER.debug("###> LCK_MGR > LOCKS > RELATED TABLE LOCKS");
		
		List<ILock> relatedLocks = new LinkedList<>();
		relatedLocks.addAll( fetchTableLocks(objectId) );
		return relatedLocks;
	}

	private List<ILock> fetchBlockRelatedLocks(IObjectId objectId) {
		LOGGER.debug("###> LCK_MGR > LOCKS > RELATED BLOCK LOCKS");
		
		List<ILock> relatedLocks = new LinkedList<>();
		
		relatedLocks.addAll( fetchTableLocks(objectId) );
		relatedLocks.addAll( fetchBlockLocks(objectId) );
		
		return relatedLocks;
	}

	private List<ILock> fetchRowRelatedLocks(IObjectId objectId) {
		LOGGER.debug("###> LCK_MGR > LOCKS > RELATED ROW LOCKS");
		
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
	
	private void handleUnknowObjectIdType(ObjectIdType type) {
		String message = "Unknow objectId type : " + type.toString();
		LOGGER.error(message);
		throw new UnsupportedOperationException(message);
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
		LOGGER.debug("###> LCK_MGR > LOCKS > ADD TABLE LOCK > " + lock.toString());
		
		String mapKey = deriveTablesLocksMapKey(txOp.getObjectId());
		putInLockMap(mapKey, lock, tablesLocksMap);
	}

	private void addBlockLock(ITransactionOperation txOp, LockType lockType) {
		ILock lock = new Lock(txOp, lockType);
		LOGGER.debug("###> LCK_MGR > LOCKS > ADD BLOCK LOCK > " + lock.toString());
		
		String mapKey = deriveBlocksLocksMapKey(txOp.getObjectId());
		putInLockMap(mapKey, lock, blocksLocksMap);
	}

	private void addRowLock(ITransactionOperation txOp, LockType lockType) {
		ILock lock = new Lock(txOp, lockType);
		LOGGER.debug("###> LCK_MGR > LOCKS > ADD ROW LOCK > " + lock.toString());
		
		String mapKey = deriveRowsLocksMapKey(txOp.getObjectId());
		putInLockMap(mapKey, lock, rowsLocksMap);
	}
	
	private void removeLocks(ITransaction tx){
		removeRowLocks(tx);
		removeBlockLocks(tx);
		removeTableLocks(tx);
	}
	
	private void removeTableLocks(ITransaction tx) {
		LOGGER.debug("###> LCK_MGR > LOCKS > RMV TABLE LOCKS");
		removeFromLockMap(tx, blocksLocksMap);
	}

	private void removeBlockLocks(ITransaction tx) {
		LOGGER.debug("###> LCK_MGR > LOCKS > RMV BLOCK LOCKS");
		removeFromLockMap(tx, blocksLocksMap);
	}

	private void removeRowLocks(ITransaction tx) {
		LOGGER.debug("###> LCK_MGR > LOCKS > RMV ROW LOCKS");
		removeFromLockMap(tx, rowsLocksMap);
	}
	
	private void processAbortResult(IScheduleResult result, Collection<ITransaction> abortedTransactionBuffer){
		LOGGER.debug("####> LCK_MGR > ABORT RESULT > PROCESSING ABORT RESULT > " + result.toString());
		
		ITransactionOperation resultTxOp = result.getTransactionOperation();
		ITransaction resultTx = resultTxOp.getTransaction();
		
		abortedTransactionBuffer.add(resultTx);
		
		LOGGER.debug("####> LCK_MGR > ABORT RESULT > RMV TX NODE FROM GRAPH" + resultTxOp.toString());
		removeNodeFromGraph(resultTxOp);
		
		LOGGER.debug("####> LCK_MGR > ABORT RESULT > RMV LOCKS > " + resultTx.toString());
		removeLocks(resultTx);
	}
	
	private void processWaitResult(ITransactionOperation txOp, ITransaction otherTx){
		ITransaction tx = txOp.getTransaction();
		
		LOGGER.debug("####> LCK_MGR > WAIT RESULT > ADD TX EDGE TO GRAPH > SOURCE : " + tx.getId() + " > TARGET : " + otherTx.getId());
		addEdge(tx, otherTx);
		
		LOGGER.debug("####> LCK_MGR > WAIT RESULT > ADD TO WAIT MAP");
		addToWaitMap(otherTx, txOp);
	}
	
	private void handleDeadLock(ITransaction lockTx, Collection<IScheduleResult> schedulesResults, Collection<ITransaction> abortedTransactions) {
		LOGGER.debug("####> LCK_MGR > DEAD LOCKS > DETECTING DEAD LOCK");
		
		boolean hasDeadLock = detectGraphCycle( lockTx );
		LOGGER.debug("####> LCK_MGR > DEAD LOCKS > HAS DEAD LOCK > " + hasDeadLock);
		
		if( hasDeadLock ){
			LOGGER.debug("####> LCK_MGR > DEAD LOCKS > SOLVING DEAD LOCK");
			IScheduleResult deadLockResult = solveDeadLock(cycleNodesInfo);
			
			LOGGER.debug("####> LCK_MGR > DEAD LOCKS > PROCESS DEAD LOCK RESULT");
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
		LOGGER.debug("###> LCK_MGR > RE-PROCESS > ABORTED TX > " + abortedTx.toString());
		
		LinkedList<IScheduleResult> schedulesResults = new LinkedList<>();
		
		Queue<ITransactionOperation> waitingOperations = getWaitingOperations( abortedTx );
		for (ITransactionOperation waitingTxOp : waitingOperations) {
			LOGGER.debug("###> LCK_MGR > RE-PROCESS > WAITING TX OP > " + waitingTxOp);
			Collection<IScheduleResult> results = process(waitingTxOp);
			schedulesResults.addAll(results);
		}
		
		LOGGER.debug("###> LCK_MGR > WAIT > PURGE TX");
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
		this.graphJavaScriptParser = new CytoscapeUnweightedParser<>();
		this.snapShotCounter = new Long(0L);
		this.graphTraffic = new LinkedList<>();
	}
	
	private void addNodeToGraph(ITransactionOperation txOp) {
		TransactionNode node = new TransactionNode( txOp.getTransaction() );
		if( !waitForGraph.containsNode(node) ){
			waitForGraph.addNode(node);
			graphTraffic.add("ADD NODE TX " + node.getTransaction().getId());
		}
		
		generateGraphSnapshot();
	}
	
	private void removeNodeFromGraph(ITransactionOperation txOp) {
		TransactionNode node = new TransactionNode( txOp.getTransaction() );
		waitForGraph.removeNode(node);
		graphTraffic.add("REMOVE NODE TX " + node.getTransaction().getId());
		generateGraphSnapshot();
	}
	
	private void addEdge(ITransaction sourceTx, ITransaction targetTx){
		TransactionNode source = new TransactionNode(sourceTx);
		TransactionNode target = new TransactionNode(targetTx);
		DefaultUnweightedEdge<TransactionNode> edge = new DefaultUnweightedEdge<TransactionNode>(source, target);
		waitForGraph.addEdge(edge);
		graphTraffic.add("ADD EDGE SOURCE TX > " + source.getTransaction().getId() + " | TARGET TX > " + target.getTransaction().getId());
		generateGraphSnapshot();
	}
	
	@Override
	public String getGraphTrafficAsString() {
		StringBuffer sb = new StringBuffer();
		for (String movement : graphTraffic) {
			sb.append(movement);
			sb.append("\n");
		}
		
		return sb.toString();
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
	
	private void generateGraphSnapshot() {
		if( !generateSnapshots ){
			return;
		}
		
		try {
			AbstractGraph<TransactionNode, DefaultUnweightedEdge<TransactionNode>> graph = (AbstractGraph<TransactionNode, DefaultUnweightedEdge<TransactionNode>>) waitForGraph;
			String javaScript = graphJavaScriptParser.parse(graph, "cy", "cy");
			
			ClassLoader classLoader = getClass().getClassLoader();
			File templateFile = new File(classLoader.getResource("snapTemplate.html").getFile());
			FileInputStream fis = new FileInputStream(templateFile );
			
			String templateFileStr = IOUtils.toString(fis, StandardCharsets.UTF_8);
			String snapshot = templateFileStr.replaceAll(TEMPLATE_REPLACE_TOKEN, javaScript);
			
			File outputFile = File.createTempFile("suricateDBMS_lockManager_snap_" + snapShotCounter + "_", ".html", workspaceFolder);
			FileUtils.writeStringToFile(outputFile, snapshot, StandardCharsets.UTF_8);
			
			snapShotCounter++;
			
			LOGGER.debug("####> LCK_MNG > GRAPH SNAPSHOT > FILE GENERATED > " + outputFile.getName());
			
		} catch(Exception e) {
			System.out.println("ERROR WHILE WRITING SNAPSHOT");
			e.printStackTrace();
		}
	}
	
	//XXX GETTERS/SETTERS
	protected Graph<TransactionNode, DefaultUnweightedEdge<TransactionNode>> getWaitForGraph() {
		return waitForGraph;
	}
}
