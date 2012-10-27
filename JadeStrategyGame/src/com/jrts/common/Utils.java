package com.jrts.common;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import com.jrts.environment.Cell;
import com.jrts.environment.CellType;
import com.jrts.environment.Direction;
import com.jrts.environment.Floor;
import com.jrts.environment.Position;

public class Utils {
	
	public static ArrayList<Direction> calculatePath(Floor floor, Position startPosition, Position endPosition){
		int startRow = startPosition.getRow();
		int startCol = startPosition.getCol();
		int endRow = endPosition.getRow();
		int endCol = endPosition.getCol();
		
		UndirectedWeightedGraph walkableGraph = createWalkableGraph(floor, startRow, startCol, endRow, endCol);
		System.out.println("Initial EndPos:" + endPosition);
		
		Position correctedEndPosition = approximateEndPosition(walkableGraph, floor, startRow, startCol, endRow, endCol);
		System.out.println("Corrected EndPos:" + correctedEndPosition);

		endRow = correctedEndPosition.getRow();
		endCol = correctedEndPosition.getCol();
		walkableGraph = createWalkableGraph(floor, startRow, startCol, endRow, endCol);
		
		//calculate path on the graph
		String sourceNode = startRow + "," + startCol;
		String destNode = endRow + "," + endCol;
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
		System.out.println("Path's StartPos:" + startPosition);
		System.out.println("Path's EndPos:" + endPosition);
		System.out.println("Path's direction:" + directions);
		return directions;
	}

	/**
	 * Avoid path null when end position is unreachable 
	 * 
	 * @param walkableGraph
	 * @param floor
	 * @param startRow
	 * @param startCol
	 * @param endRow
	 * @param endCol
	 * @return correctedEndPosition
	 */
	private static Position approximateEndPosition(UndirectedWeightedGraph walkableGraph, Floor floor, int startRow, int startCol, int endRow, int endCol) {
		floor = floor.getCopy();
		Position endPos = new Position(endRow, endCol);
		if(walkableGraph.containsVertex(endPos.getRow() + "," + endPos.getCol()))
			return endPos;
		else{
			int counter = 0;
			Position correctedEndPosition;
			do {
				correctedEndPosition = floor.nextTo(endPos, CellType.FREE, GameConfig.PATH_TOLERANCE);
				if(correctedEndPosition == null || 
						!walkableGraph.containsVertex(correctedEndPosition.getRow() + "," + correctedEndPosition.getCol())){
					counter++;
					floor.set(correctedEndPosition.getRow(), correctedEndPosition.getCol(), new Cell(CellType.WOOD));
				}
				else
					return correctedEndPosition;
			}while(counter <= GameConfig.PATH_TOLERANCE*GameConfig.PATH_TOLERANCE);//covered area
			return null;
		}
	}

	private static UndirectedWeightedGraph createWalkableGraph(Floor floor, int startRow, int startCol, int endRow, int endCol) {
		//create walkable graph
		UndirectedWeightedGraph walkableGraph = new UndirectedWeightedGraph();
		//avoid exceptions
		walkableGraph.addVertex(startRow + "," + startCol);
		walkableGraph.addVertex(endRow + "," + endCol);
		for (int i = 0; i < floor.getRows(); i++)
			for (int j = 0; j < floor.getCols(); j++){
				//NB La cella di partenza anche se occupata dall'unita' fa parte del grafo
				if(floor.get(i, j).getType() == CellType.FREE || floor.get(i, j).getType() == CellType.UNKNOWN){
					// A
					// |
					if( i-1 >= 0 && (floor.get(i-1, j).getType() == CellType.FREE || floor.get(i-1, j).getType() == CellType.UNKNOWN || (i-1==startRow && j==startCol)) ){
						String v1 = i + "," + j;
						String v2 = (i-1) + "," + j;
						walkableGraph.addWeightedEdge(v1, v2, 1);
					}
					// |
					// v
					if( i+1 < floor.getRows() && (floor.get(i+1, j).getType() == CellType.FREE || floor.get(i+1, j).getType() == CellType.UNKNOWN || (i+1==startRow && j==startCol)) ){
						String v1 = i + "," + j;
						String v2 = (i+1) + "," + j;
						walkableGraph.addWeightedEdge(v1, v2, 1);
					}
					// <-
					if( j-1 >= 0 && (floor.get(i, j-1).getType() == CellType.FREE || floor.get(i, j-1).getType() == CellType.UNKNOWN || (i==startRow && j-1==startCol)) ){
						String v1 = i + "," + j;
						String v2 = i + "," + (j-1);
						walkableGraph.addWeightedEdge(v1, v2, 1);
					}
					// ->
					if( j+1 <= floor.getCols() && (floor.get(i, j+1).getType() == CellType.FREE || floor.get(i, j+1).getType() == CellType.UNKNOWN || (i==startRow && j+1==startCol)) ){
						String v1 = i + "," + j;
						String v2 = i + "," + (j+1);
						walkableGraph.addWeightedEdge(v1, v2, 1);
					}
					// A
					//  \
					if( j-1 >= 0 && i-1 >= 0 && (floor.get(i-1, j-1).getType() == CellType.FREE || floor.get(i-1, j-1).getType() == CellType.UNKNOWN || (i-1==startRow && j-1==startCol)) ){
						String v1 = i + "," + j;
						String v2 = (i-1) + "," + (j-1);
						walkableGraph.addWeightedEdge(v1, v2, 1);
					}
					
					//   A
					//  /
					if( j+1 < floor.getCols() && i-1 >= 0 && (floor.get(i-1, j+1).getType() == CellType.FREE || floor.get(i-1, j+1).getType() == CellType.UNKNOWN || (i-1==startRow && j+1==startCol)) ){
						String v1 = i + "," + j;
						String v2 = (i-1) + "," + (j+1);
						walkableGraph.addWeightedEdge(v1, v2, 1);
					}
					//  /
					// v
					if( j-1 >= 0 && i+1 < floor.getRows() && (floor.get(i+1, j-1).getType() == CellType.FREE || floor.get(i+1, j-1).getType() == CellType.UNKNOWN || (i+1==startRow && j-1==startCol)) ){
						String v1 = i + "," + j;
						String v2 = (i+1) + "," + (j-1);
						walkableGraph.addWeightedEdge(v1, v2, 1);
					}
					//  \
					//   v
					if( j+1 < floor.getCols() && i+1 < floor.getRows() && (floor.get(i+1, j+1).getType() == CellType.FREE || floor.get(i+1, j+1).getType() == CellType.UNKNOWN || (i+1==startRow && j+1==startCol)) ){
						String v1 = i + "," + j;
						String v2 = (i+1) + "," + (j+1);
						walkableGraph.addWeightedEdge(v1, v2, 1);
					}
				}
			}
		return walkableGraph;
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
