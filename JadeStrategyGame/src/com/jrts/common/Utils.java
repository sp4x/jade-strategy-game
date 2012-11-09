package com.jrts.common;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import com.jrts.environment.CellType;
import com.jrts.environment.Direction;
import com.jrts.environment.Floor;
import com.jrts.environment.Position;
import com.jrts.environment.WorldMap;

public class Utils {
	
	public static ArrayList<Direction> calculatePath(Floor floor, Position startPosition, Position endPosition, boolean tolerance){
		int startRow = startPosition.getRow();
		int startCol = startPosition.getCol();
		int endRow = endPosition.getRow();
		int endCol = endPosition.getCol();
		
//		UndirectedWeightedGraph walkableGraph = createWalkableGraph(floor, startRow, startCol, endRow, endCol);
//		System.out.println("Initial EndPos:" + endPosition);
		
		if(tolerance){
			Position correctedEndPosition = approximateEndPosition(/*walkableGraph,*/ floor, startPosition, endPosition);
//			System.out.println("Corrected EndPos:" + correctedEndPosition);
			if (correctedEndPosition == null)
				return new ArrayList<Direction>();
	
			endRow = correctedEndPosition.getRow();
			endCol = correctedEndPosition.getCol();
		}
		
		UndirectedWeightedGraph walkableGraph = createWalkableGraph(floor, startRow, startCol, endRow, endCol);
		
		//calculate path on the graph
		String sourceNode = startRow + "," + startCol;
		String destNode = endRow + "," + endCol;
		if(!walkableGraph.containsVertex(destNode)){
//			System.out.println("Dest Node not reachable");
			return new ArrayList<Direction>();
		}
		List<DefaultWeightedEdge> list = new DijkstraShortestPath<String, DefaultWeightedEdge>
											(walkableGraph, sourceNode, destNode).getPathEdgeList();
//		System.out.println("List Edges:" + list);
		ArrayList<String> cellList = edgeListToCellList(walkableGraph, sourceNode, list);
//		System.out.println("Cells List:" + cellList);
		ArrayList<Direction> directions = cellListToDirections(cellList);
//		System.out.println("Path's StartPos:" + startPosition);
//		System.out.println("Path's EndPos:" + endPosition);
//		System.out.println("Path's direction:" + directions);
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
	private static Position approximateEndPosition(/*UndirectedWeightedGraph walkableGraph,*/ Floor floor, Position startPos, Position endPos) {
//		floor = floor.getCopy();
//		if(walkableGraph.containsVertex(endPos.getRow() + "," + endPos.getCol()))
//			return endPos;
		CellType targetType = floor.get(endPos).getType();
		if ( targetType == CellType.FREE || targetType == CellType.UNKNOWN )
			return endPos;
		return floor.nextTo(startPos, endPos, CellType.FREE, GameConfig.PATH_TOLERANCE);
//		else{
//			int counter = 0;
//			Position correctedEndPosition;
//			do {
//				if(correctedEndPosition == null || 
//						!walkableGraph.containsVertex(correctedEndPosition.getRow() + "," + correctedEndPosition.getCol())){
//					counter++;
//					floor.set(correctedEndPosition.getRow(), correctedEndPosition.getCol(), new Cell(CellType.WOOD));
//				}
//				else
//					return correctedEndPosition;
//			}while(counter <= GameConfig.PATH_TOLERANCE*GameConfig.PATH_TOLERANCE);//covered area
//			return null;
//		}
	}

	private static UndirectedWeightedGraph createWalkableGraph(Floor floor, int startRow, int startCol, int endRow, int endCol) {
		//create walkable graph
		UndirectedWeightedGraph walkableGraph = new UndirectedWeightedGraph();
		//avoid exceptions
		walkableGraph.addVertex(startRow + "," + startCol);
//		walkableGraph.addVertex(endRow + "," + endCol);
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
						walkableGraph.addWeightedEdge(v1, v2, 1.5);
					}
					
					//   A
					//  /
					if( j+1 < floor.getCols() && i-1 >= 0 && (floor.get(i-1, j+1).getType() == CellType.FREE || floor.get(i-1, j+1).getType() == CellType.UNKNOWN || (i-1==startRow && j+1==startCol)) ){
						String v1 = i + "," + j;
						String v2 = (i-1) + "," + (j+1);
						walkableGraph.addWeightedEdge(v1, v2, 1.5);
					}
					//  /
					// v
					if( j-1 >= 0 && i+1 < floor.getRows() && (floor.get(i+1, j-1).getType() == CellType.FREE || floor.get(i+1, j-1).getType() == CellType.UNKNOWN || (i+1==startRow && j-1==startCol)) ){
						String v1 = i + "," + j;
						String v2 = (i+1) + "," + (j-1);
						walkableGraph.addWeightedEdge(v1, v2, 1.5);
					}
					//  \
					//   v
					if( j+1 < floor.getCols() && i+1 < floor.getRows() && (floor.get(i+1, j+1).getType() == CellType.FREE || floor.get(i+1, j+1).getType() == CellType.UNKNOWN || (i+1==startRow && j+1==startCol)) ){
						String v1 = i + "," + j;
						String v2 = (i+1) + "," + (j+1);
						walkableGraph.addWeightedEdge(v1, v2, 1.5);
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
	
	/**
	 * This function let you know in what angle of the map the unit is located
	 * @param The position of the unit
	 * @return a Direction between LEFT_UP, RIGHT_UP, LEFT_DOWN, RIGHT_DOWN
	 */
	public static Direction getMapAnglePosition(Position p)
	{
		if(p.getRow() <= GameConfig.WORLD_ROWS/2){
			if(p.getCol() <= GameConfig.WORLD_COLS/2)
				return Direction.LEFT_UP;
			else return Direction.RIGHT_UP;
		} else {
			if(p.getCol() <= GameConfig.WORLD_COLS/2)
				return Direction.LEFT_DOWN;
			else return Direction.RIGHT_DOWN;			
		}
	}
	
	/**
	 * Get the position of a random unknown cell located in one of the angles of the map
	 * @param a worldmap of a team
	 * @param a direction in witch looking for a unknown cell, must be between LEFT_UP, RIGHT_UP, LEFT_DOWN, RIGHT_DOWN
	 * @return the position of an unknown cell or NULL if not found
	 */
	public static Position getRandomUnknownCellPosition(WorldMap map, Direction d)
	{
		Random r = new Random(GregorianCalendar.getInstance().getTimeInMillis());
		int w = 0;
		int h = 0;
		
		if(d.equals(Direction.LEFT_UP))
		{
			w = 0;
			h = 0;
		} else if(d.equals(Direction.LEFT_DOWN))
		{
			w = 0;
			h = GameConfig.WORLD_ROWS/2;
		} else if(d.equals(Direction.RIGHT_UP)){
			w = GameConfig.WORLD_COLS/2;
			h = 0;
		} else if(d.equals(Direction.RIGHT_DOWN)){
			w = GameConfig.WORLD_COLS/2;
			h = GameConfig.WORLD_ROWS/2;
		}
		
		for (int i = 0; i < 10; i++) {

			int x = w + r.nextInt(GameConfig.WORLD_COLS/2);
			int y = h + r.nextInt(GameConfig.WORLD_ROWS/2);
			Position pos = new Position(x, y);
			if(map.get(pos).getType().equals(CellType.UNKNOWN))
				return pos;
		}
		
		return null;
	}
}
