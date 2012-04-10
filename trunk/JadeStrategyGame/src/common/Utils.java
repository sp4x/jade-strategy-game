package common;

import java.util.ArrayList;
import java.util.List;

import logic.Cell;
import logic.Direction;
import logic.Floor;

import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;

public class Utils {
	public static ArrayList<Direction> calculatePath(Floor floor, int x1, int y1, int x2, int y2){
		//create walkable graph
		UndirectedWeightedGraph walkableGraph = new UndirectedWeightedGraph();
		for (int i = 0; i < floor.getRows(); i++)
			for (int j = 0; j < floor.getCols(); j++){
				//NB La cella di partenza anche se occupata dall'unita' fa parte del grafo
				if(floor.get(i, j) == Cell.FREE){
					// A
					// |
					if( i-1 >= 0 && (floor.get(i-1, j) == Cell.FREE || (i-1==x1 && j==y1)) ){
						String v1 = i + "," + j;
						String v2 = (i-1) + "," + j;
						walkableGraph.addWeightedEdge(v1, v2, 1);
					}
					// |
					// v
					if( i+1 < floor.getRows() && (floor.get(i+1, j) == Cell.FREE || (i+1==x1 && j==y1)) ){
						String v1 = i + "," + j;
						String v2 = (i+1) + "," + j;
						walkableGraph.addWeightedEdge(v1, v2, 1);
					}
					// <-
					if( j-1 >= 0 && (floor.get(i, j-1) == Cell.FREE || (i==x1 && j-1==y1)) ){
						String v1 = i + "," + j;
						String v2 = i + "," + (j-1);
						walkableGraph.addWeightedEdge(v1, v2, 1);
					}
					// ->
					if( j+1 <= floor.getCols() && (floor.get(i, j+1) == Cell.FREE || (i==x1 && j+1==y1)) ){
						String v1 = i + "," + j;
						String v2 = i + "," + (j+1);
						walkableGraph.addWeightedEdge(v1, v2, 1);
					}
					// A
					//  \
					if( j-1 >= 0 && i-1 >= 0 && (floor.get(i-1, j-1) == Cell.FREE || (i-1==x1 && j-1==y1)) ){
						String v1 = i + "," + j;
						String v2 = (i-1) + "," + (j-1);
						walkableGraph.addWeightedEdge(v1, v2, 1);
					}
					
					//   A
					//  /
					if( j+1 < floor.getCols() && i-1 >= 0 && (floor.get(i-1, j+1) == Cell.FREE || (i-1==x1 && j+1==y1)) ){
						String v1 = i + "," + j;
						String v2 = (i-1) + "," + (j+1);
						walkableGraph.addWeightedEdge(v1, v2, 1);
					}
					//  /
					// v
					if( j-1 >= 0 && i+1 < floor.getRows() && (floor.get(i+1, j-1) == Cell.FREE || (i+1==x1 && j-1==y1)) ){
						String v1 = i + "," + j;
						String v2 = (i+1) + "," + (j-1);
						walkableGraph.addWeightedEdge(v1, v2, 1);
					}
					//  \
					//   v
					if( j+1 < floor.getCols() && i+1 < floor.getRows() && (floor.get(i+1, j+1) == Cell.FREE || (i+1==x1 && j+1==y1)) ){
						String v1 = i + "," + j;
						String v2 = (i+1) + "," + (j+1);
						walkableGraph.addWeightedEdge(v1, v2, 1);
					}
				}
				//TODO se la cella di destinazione non è libera sceglierne un'altra vicina come dest
			}

		//calculate path on the graph
		String sourceNode = x1 + "," + y1;
		String destNode = x2 + "," + y2;
		if(!walkableGraph.containsVertex(destNode)){
			System.out.println("Dest Node not reachable");
			return new ArrayList<Direction>();
		}
		List<DefaultWeightedEdge> list = new DijkstraShortestPath<String, DefaultWeightedEdge>
											(walkableGraph, sourceNode, destNode).getPathEdgeList();
//		System.out.println("List Edges:" + list);
		ArrayList<String> cellList = edgeListToCellList(walkableGraph, sourceNode, list);
//		System.out.println("Cells List:" + cellList);
		ArrayList<Direction> directions = cellListToDirections(cellList);
		return directions;
	}

	public static ArrayList<String> edgeListToCellList(UndirectedWeightedGraph walkableGraph, String src, List<DefaultWeightedEdge> list) {
		ArrayList<String> cellList = new ArrayList<String>();
		cellList.add(src);
		String curr = src;
		if(list == null)
			return cellList;
		for (int i = 0; i < list.size(); i++) {
			String source = walkableGraph.getEdgeSource(list.get(i));
			String target = walkableGraph.getEdgeTarget(list.get(i));
			if(source.equals(curr)){
				curr = target;
				cellList.add(target);
			}
			else{
				curr = source;
				cellList.add(source);
			}
		}
		return cellList;
	}
	
	public static ArrayList<Direction> cellListToDirections(ArrayList<String> cellList) {
		ArrayList<Direction> directionList = new ArrayList<Direction>();
		for (int i = 0; i < cellList.size()-1; i++) {
//			System.out.println("Current edge " + cellList.get(i) + "-" + cellList.get(i+1));
			int i1 = Integer.parseInt(cellList.get(i).split(",")[0]);
			int j1 = Integer.parseInt(cellList.get(i).split(",")[1]);
			int i2 = Integer.parseInt(cellList.get(i+1).split(",")[0]);
			int j2 = Integer.parseInt(cellList.get(i+1).split(",")[1]);

			if(i1!=i2){//dall'alto in basso o viceversa
				if(i1<i2){//scende
					if(j1==j2)//stessa colonna
						directionList.add(Direction.DOWN);
					else if(j1<j2)//scende verso destra
						directionList.add(Direction.RIGHT_DOWN);
					else if(j1>j2)//scende verso sinistra
						directionList.add(Direction.LEFT_DOWN);
				}
				else{//sale
					if(j1==j2)//stessa colonna
						directionList.add(Direction.UP);
					else if(j1<j2)//sale verso destra
						directionList.add(Direction.RIGHT_UP);
					else if(j1>j2)//sale verso sinistra
						directionList.add(Direction.LEFT_UP);
				}
			}
			else//stessa riga
			if(j1!=j2){
				if(j1<j2)
					directionList.add(Direction.RIGHT);
				else
					directionList.add(Direction.LEFT);
			}
//			System.out.println("Direction choosed " + directionList.get(directionList.size()-1));
		}
		return directionList;
	}
}
