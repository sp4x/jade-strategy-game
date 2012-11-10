package com.jrts.common;


import java.util.logging.Logger;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

public class UndirectedWeightedGraph extends SimpleWeightedGraph<String, DefaultWeightedEdge> {

	private static final long serialVersionUID = 1L;

	Logger logger = Logger.getLogger(UndirectedWeightedGraph.class.getName());
	
	public UndirectedWeightedGraph () {
		super(DefaultWeightedEdge.class);
	}
	
	public void addWeightedEdge (String v1, String v2, double weight) {
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
		for( String v : vertexSet())
			System.out.print(v + " ");
		logger.info(".");
		
		logger.info("Edges");
		for( DefaultWeightedEdge edge: edgeSet())
			logger.info(getEdgeSource(edge)+","+getEdgeTarget(edge)+"="+getEdgeWeight(edge));
		return super.toString();
	}
}
