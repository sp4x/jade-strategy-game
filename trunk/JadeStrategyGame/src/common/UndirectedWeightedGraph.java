package common;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

public class UndirectedWeightedGraph extends SimpleWeightedGraph<String, DefaultWeightedEdge> {

	private static final long serialVersionUID = 1L;

	public UndirectedWeightedGraph () {
		super(DefaultWeightedEdge.class);
	}
	
	public void addWeightedEdge (String v1, String v2, double weight) {
		if (!containsVertex(v1))
			addVertex(v1);
		if (!containsVertex(v2))
			addVertex(v2);
		DefaultWeightedEdge e = new DefaultWeightedEdge();
		addEdge(v1, v2, e);
		setEdgeWeight(e, weight);
	}
	
	@Override
	public String toString() {
		System.out.println("Nodes");
		for( String v : vertexSet())
			System.out.print(v + " ");
		System.out.println(".");
		
		System.out.println("Edges");
		for( DefaultWeightedEdge edge: edgeSet())
			System.out.println(getEdgeSource(edge)+","+getEdgeTarget(edge)+"="+getEdgeWeight(edge));
		return super.toString();
	}
}
