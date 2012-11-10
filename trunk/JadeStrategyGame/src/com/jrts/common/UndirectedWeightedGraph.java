package com.jrts.common;


import java.util.logging.Logger;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.jrts.environment.Position;

@SuppressWarnings("serial")
public class UndirectedWeightedGraph extends SimpleWeightedGraph<Position, DefaultWeightedEdge> {
	
	Logger logger = Logger.getLogger(UndirectedWeightedGraph.class.getName());
	
	public UndirectedWeightedGraph () {
		super(DefaultWeightedEdge.class);
	}
	
	public void addWeightedEdge (Position v1, Position v2, double weight) {
		if (!containsVertex(v1))
			addVertex(v1);
		if (!containsVertex(v2))
			addVertex(v2);
		DefaultWeightedEdge e1 = new DefaultWeightedEdge();
		DefaultWeightedEdge e2 = new DefaultWeightedEdge();
		addEdge(v1, v2, e1);
		addEdge(v2, v1, e2);
		setEdgeWeight(e1, weight);
		setEdgeWeight(e2, weight);
	}
	
	@Override
	public String toString() {
		logger.info("Nodes");
		for( Position v : vertexSet())
			System.out.print(v + " ");
		logger.info(".");
		
		logger.info("Edges");
		for( DefaultWeightedEdge edge: edgeSet())
			logger.info(getEdgeSource(edge)+","+getEdgeTarget(edge)+"="+getEdgeWeight(edge));
		return super.toString();
	}
	
	@Override
	public synchronized UndirectedWeightedGraph clone() {
		UndirectedWeightedGraph graph = new UndirectedWeightedGraph();
		for( Position v : vertexSet())
			graph.addVertex(v);
		for( DefaultWeightedEdge edge: edgeSet())
			graph.addEdge(getEdgeSource(edge), getEdgeTarget(edge));
		return graph;
	}
	
	@Override
	public synchronized boolean removeVertex(Position vertex) {
//		for( DefaultWeightedEdge edge: edgeSet())
//			if(getEdgeSource(edge).equals(vertex) || getEdgeTarget(edge).equals(vertex))
//				removeEdge(edge);
		removeVertex(vertex);
		return true;
	}
}
