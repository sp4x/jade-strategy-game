package common;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.traverse.DepthFirstIterator;

import logic.Cell;
import logic.Floor;

public class Utils {
	public static List<DefaultWeightedEdge> calculatePath(Floor floor, int x1, int y1, int x2, int y2){
		//create walkable graph
		UndirectedWeightedGraph walkableGraph = new UndirectedWeightedGraph();
		for (int i = 0; i < floor.getCols(); i++)
			for (int j = 0; j < floor.getRows(); j++){
				if(floor.get(i, j) == Cell.FREE){
					if( i-1 >= 0 && floor.get(i-1, j) == Cell.FREE){
						String v1 = i + "," + j;
						String v2 = (i-1) + "," + j;
						walkableGraph.addWeightedEdge(v1, v2, 1);
					}
					if( j-1 >= 0 && floor.get(i, j-1) == Cell.FREE){
						String v1 = i + "," + j;
						String v2 = i + "," + (j-1);
						walkableGraph.addWeightedEdge(v1, v2, 1);
					}
				}
				//TODO se la cella di destinazione non è libera sceglierne un'altra vicina come dest
			}

		//calculate path on the graph
		String sourceNode = x1 + "," + y1;
		String destNode = x2 + "," + y2;
		List<DefaultWeightedEdge> path = new DijkstraShortestPath<String, DefaultWeightedEdge>
											(walkableGraph, sourceNode, destNode).getPathEdgeList();
		return path;
	}

//	public static ArrayList<String> edgeListToCellList(String src, List<DefaultWeightedEdge> list) {
//		ArrayList<String> cellList = new ArrayList<String>();
//		cellList.add(src);
//		String curr = src;
//		for (int i = 1; i < list.size(); i++) {
//			String source = DefaultWeightedEdge.class.getField("source").get(list.get(i)).toString();
//			String target = DefaultWeightedEdge.class.getField("dest").get(list.get(i)).toString();
//			if(source.equals(curr)){
//				curr = target;
//				cellList.add(target);
//			}
//			else{
//				curr = source;
//				cellList.add(source);
//			}
//		}
//		return cellList;
//	}
}
