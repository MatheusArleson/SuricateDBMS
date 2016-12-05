package br.com.xavier.suricate.dbms.abstractions.util;

import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.com.xavier.graphs.abstractions.algorithms.transversal.AbstractTransversalAlgorithm;
import br.com.xavier.graphs.impl.algorithms.NodeInfo;
import br.com.xavier.graphs.interfaces.Graph;
import br.com.xavier.graphs.interfaces.edges.Edge;
import br.com.xavier.graphs.interfaces.nodes.Node;
import br.com.xavier.graphs.util.Util;

public class GraphCycleDetector extends AbstractTransversalAlgorithm {
	
	//XXX PROPERTIES
	private boolean hasCycle;
	
	//XXX CONSTRUCTOR
	public GraphCycleDetector() {}

	//XXX OVERRIDE METHODS
	@Override
	public <N extends Node, E extends Edge<N>> List<NodeInfo> doAlgorithm(Graph<N, E> graph, N root) {
		hasCycle = false;
		Util.checkIllegalNode(graph, root);
		
		Map<N, NodeInfo> nodesInfoMap = generateNodesInfoMap(graph);
		
		Deque<N> stack = new LinkedList<>();
		stack.push(root);
		
		NodeInfo rootNodeInfo = nodesInfoMap.get(root);
		rootNodeInfo.setVisited(true);
		
		List<NodeInfo> resultList = new LinkedList<>();
		resultList.add(rootNodeInfo);
		
		while(!stack.isEmpty()){
			N currentNode = stack.pop();
			
			Set<E> distinctEdges = graph.getDistinctEdges(currentNode);
			for (E edge : distinctEdges) {
				N adjacentNode = edge.getTarget();
				NodeInfo adjacentNodeInfo = nodesInfoMap.get(adjacentNode);
				
				if(!adjacentNodeInfo.isVisited()){
					adjacentNodeInfo.setVisited(true);
					
					stack.push(adjacentNode);
					resultList.add(adjacentNodeInfo);
				
				} else if( stack.contains(adjacentNode) ){
					this.hasCycle = true;
					return resultList;
				}
			}
		}
		
		return Collections.unmodifiableList(resultList);
	}

	//XXX GETTERS/SETTERS
	public boolean hasCycle() {
		return hasCycle;
	}

}
