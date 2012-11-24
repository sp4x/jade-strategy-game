package com.jrts.common;

import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.jrts.environment.Position;

@SuppressWarnings("serial")
public class UndirectedWeightedGraph extends SimpleWeightedGraph<Position, DefaultWeightedEdge> {
	
	Lock l = new ReentrantLock();
	
	Logger logger = Logger.getLogger(UndirectedWeightedGraph.class.getName());
	Level logLevel = Level.FINE;
	
	public UndirectedWeightedGraph () {
		super(DefaultWeightedEdge.class);
	}
	
	public void addWeightedEdge (Position v1, Position v2, double weight) {
		l.lock();
		if (!containsVertex(v1))
			addVertex(v1);
		if (!containsVertex(v2))
			addVertex(v2);
		Graphs.addEdge(this, v1, v2, weight); 
		Graphs.addEdge(this, v2, v1, weight); 
		l.unlock();
	}
	
	@Override
	public Set<Position> vertexSet(){
		l.lock();
		Set<Position> set = super.vertexSet();
		l.unlock();
		return set;
	}
	
	@Override
	public Set<DefaultWeightedEdge> edgeSet(){
		l.lock();
		Set<DefaultWeightedEdge> set = super.edgeSet();
		l.unlock();
		return set;
	}
	
	@Override
	public String toString() {
		logger.log(logLevel, "Nodes");
		for( Position v : vertexSet())
			System.out.print(v + " ");
		logger.log(logLevel, ".");
		
		logger.log(logLevel, "Edges");
		for( DefaultWeightedEdge edge: edgeSet())
			logger.log(logLevel, getEdgeSource(edge)+","+getEdgeTarget(edge)+"="+getEdgeWeight(edge));
		return super.toString();
	}
	
	@Override
	public synchronized UndirectedWeightedGraph clone() {
		l.lock();
		UndirectedWeightedGraph graph = new UndirectedWeightedGraph();
		for( Position v : vertexSet())
			graph.addVertex(v);
		for( DefaultWeightedEdge edge: edgeSet())
			graph.addEdge(getEdgeSource(edge), getEdgeTarget(edge));
		l.unlock();
		return graph;
	}
	
	@Override
	public synchronized boolean removeVertex(Position vertex) {
		l.lock();
		HashMap<String,DefaultWeightedEdge> list = new HashMap<String,DefaultWeightedEdge>();
		Set<DefaultWeightedEdge> edgeList = edgeSet();
		for( DefaultWeightedEdge edge: edgeList)
			if(getEdgeSource(edge).equals(vertex) || getEdgeTarget(edge).equals(vertex))
				list.put(edge.toString(), edge);
		for(DefaultWeightedEdge edge : list.values())
			removeEdge(edge);
		l.unlock();
		return true;
	}
}
