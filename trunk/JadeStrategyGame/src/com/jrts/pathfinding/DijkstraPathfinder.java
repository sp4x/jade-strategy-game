package com.jrts.pathfinding;

import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Transformer;

import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import com.jrts.common.GameConfig;
import com.jrts.common.UndirectedWeightedGraph;
import com.jrts.common.VisualGraph;
import com.jrts.environment.Cell;
import com.jrts.environment.CellType;
import com.jrts.environment.Direction;
import com.jrts.environment.Floor;
import com.jrts.environment.Position;

public class DijkstraPathfinder implements Pathfinder {
	
	public static final boolean TOLERANCE = true;
	public static Floor floor = null;
	public static ArrayList<Direction> EMPTY_PATH = new ArrayList<Direction>();

	UndirectedWeightedGraph mainWalkableGraph;

	@Override
	public ArrayList<Direction> calculatePath(Floor floor, Position startPosition, Position endPosition, int tolerance) {
		
		System.out.println("Distance requested: " + startPosition.distance(endPosition));
		if(startPosition.equals(endPosition))
			return EMPTY_PATH;
		
		DijkstraPathfinder.floor = floor;
		
		if (TOLERANCE) {
			Position correctedEndPosition = approximateEndPosition(floor,
					startPosition, endPosition, tolerance);
			// logger.log(logLevel, "Corrected EndPos:" + correctedEndPosition);
			if (correctedEndPosition == null)
				return new ArrayList<Direction>();

			endPosition = correctedEndPosition;
		}

		if (mainWalkableGraph == null) {
			mainWalkableGraph = createWalkableGraph(floor, new Position(0, 0),
					new Position(GameConfig.WORLD_ROWS - 1, GameConfig.WORLD_COLS - 1), true);
		}

		UndirectedWeightedGraph walkableGraph = mainWalkableGraph.clone();

		// adapt current walkableGraph from global
		for (int index = 0; index < floor.getBusyCells().size(); index++)
			walkableGraph.removeVertex(floor.getBusyCells().get(index));
		
		if((startPosition.getRow() == endPosition.getRow() || startPosition.getCol() == endPosition.getCol()) &&
				startPosition.distance(endPosition) > 10)
			VisualGraph.show(walkableGraph, 30);

		if (!walkableGraph.containsVertex(endPosition)) {
			// logger.log(logLevel, "Dest Node not reachable");
			return EMPTY_PATH;
		}

//		List<DefaultWeightedEdge> list = new DijkstraShortestPath<Position, DefaultWeightedEdge>(
//				walkableGraph, startPosition, endPosition).getPath().getEdgeList();
	    List<DefaultWeightedEdge> list = DijkstraShortestPath.findPathBetween(
					walkableGraph, startPosition, endPosition);
		// logger.log(logLevel, "List Edges:" + list);
		ArrayList<Direction> directions = edgeListToDirectionList(walkableGraph, startPosition, list);
		// logger.log(logLevel, "Cells List:" + cellList);
		// logger.log(logLevel, "Path's StartPos:" + startPosition);
		// logger.log(logLevel, "Path's EndPos:" + endPosition);
		// logger.log(logLevel, "Path's direction:" + directions);
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
	private Position approximateEndPosition(Floor floor, Position startPos,
			Position endPos, int tolerance) {

		Cell target = floor.get(endPos);
		if (target.isWalkable())
			return endPos;
		return floor.nextTo(startPos, endPos, CellType.FREE, tolerance);
	}

	private UndirectedWeightedGraph createWalkableGraph(Floor floor,
			Position startPos, Position endPos, boolean includeCellWithUnit) {
		// create walkable graph
		UndirectedWeightedGraph walkableGraph = new UndirectedWeightedGraph();
		// avoid exceptions
		walkableGraph.addVertex(startPos);
		// walkableGraph.addVertex(endRow + "," + endCol);
		for (int i = 0; i < floor.getRows(); i++)
			for (int j = 0; j < floor.getCols(); j++) {
				Cell cell = floor.get(i, j);
				// NB La cella di partenza anche se occupata dall'unita' fa parte del grafo
				boolean includeCell = cell.isWalkable() || (includeCellWithUnit && cell.isUnit()) || cell.equals(startPos);
				if (includeCell) {
					// A
					// |
					if (isBounded(i-1, j) || startPos.equals(i-1, j)) {
						walkableGraph.addWeightedEdge(new Position(i, j), new Position(i - 1, j), 1);
					}
					// |
					// v
					if (isBounded(i+1, j) || startPos.equals(i+1, j)) {
						walkableGraph.addWeightedEdge(new Position(i, j), new Position(i + 1, j), 1);
					}
					// <-
					if (isBounded(i, j-1) || startPos.equals(i, j-1)) {
						walkableGraph.addWeightedEdge(new Position(i, j), new Position(i, j - 1), 1);
					}
					// ->
					if (isBounded(i, j+1) || startPos.equals(i, j+1)) {
						walkableGraph.addWeightedEdge(new Position(i, j), new Position(i, j + 1), 1);
					}
					// A
					// \
					if (isBounded(i-1, j-1) || startPos.equals(i-1, j-1)) {
						walkableGraph.addWeightedEdge(new Position(i, j), new Position(i - 1, j - 1), 1.5);
					}
					// A
					// /
					if (isBounded(i-1, j+1) || startPos.equals(i-1, j+1)) {
						walkableGraph.addWeightedEdge(new Position(i, j), new Position(i - 1, j + 1), 1.5);
					}
					// /
					// v
					if (isBounded(i+1, j-1) || startPos.equals(i+1, j-1)) {
						walkableGraph.addWeightedEdge(new Position(i, j), new Position(i + 1, j - 1), 1.5);
					}
					// \
					// v
					if (isBounded(i+1, j+1) || startPos.equals(i+1, j+1)) {
						walkableGraph.addWeightedEdge(new Position(i, j), new Position(i + 1, j + 1), 1.5);
					}
				}
			}
		return walkableGraph;
	}

	public ArrayList<Direction> edgeListToDirectionList(
			UndirectedWeightedGraph walkableGraph, Position sourceNode,
			List<DefaultWeightedEdge> list) {
		ArrayList<Position> cellList = new ArrayList<Position>();
		ArrayList<Direction> directionList = new ArrayList<Direction>();
		cellList.add(sourceNode);
		if (list == null)
			return directionList;
		Position curr = sourceNode;
		for (int i = 0; i < list.size(); i++) {
			Position source = walkableGraph.getEdgeSource(list.get(i));
			Position target = walkableGraph.getEdgeTarget(list.get(i));
			if (source.equals(curr))
				curr = target;
			else
				curr = source;
			cellList.add(curr);
			if(cellList.size()>1){
				Position previous = cellList.get(cellList.size()-2);
				Position last = cellList.get(cellList.size()-1);
				directionList.add(getDirection(previous, last));
			}
		}
		return directionList;
	}
	
	public Direction getDirection(Position start, Position end) {
		int i1 = start.getRow();
		int j1 = start.getCol();
		int i2 = end.getRow();
		int j2 = end.getCol();

		if (i1 != i2) {// dall'alto in basso o viceversa
			if (i1 < i2) {// scende
				if (j1 == j2)// stessa colonna
					return Direction.DOWN;
				else if (j1 < j2)// scende verso destra
					return Direction.RIGHT_DOWN;
				else if (j1 > j2)// scende verso sinistra
					return Direction.LEFT_DOWN;
			} else {// sale
				if (j1 == j2)// stessa colonna
					return Direction.UP;
				else if (j1 < j2)// sale verso destra
					return Direction.RIGHT_UP;
				else if (j1 > j2)// sale verso sinistra
					return Direction.LEFT_UP;
			}
		} else // stessa riga
		if (j1 != j2) {
			if (j1 < j2)
				return Direction.RIGHT;
			else
				return Direction.LEFT;
		}
		return Direction.DOWN;
	}
	
	private boolean isBounded(int row, int col){
		boolean rowBounded = row >= 0 && row < floor.getRows();
		boolean colBounded = col >= 0 && col < floor.getCols();
		return rowBounded && colBounded;
	}
}
