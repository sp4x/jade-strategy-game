package com.jrts.pathfinding;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import com.jrts.common.GameConfig;
import com.jrts.common.UndirectedWeightedGraph;
import com.jrts.environment.CellType;
import com.jrts.environment.Direction;
import com.jrts.environment.Floor;
import com.jrts.environment.Position;

public class DijkstraPathfinder implements Pathfinder {
	
	public static final boolean TOLERANCE = true;

	UndirectedWeightedGraph mainWalkableGraph;

	@Override
	public ArrayList<Direction> calculatePath(Floor floor,
			Position startPosition, Position endPosition) {
		if (TOLERANCE) {
			Position correctedEndPosition = approximateEndPosition(floor,
					startPosition, endPosition);
			// System.out.println("Corrected EndPos:" + correctedEndPosition);
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
		
		//VisualGraph.show(walkableGraph, 30);

		if (!walkableGraph.containsVertex(endPosition)) {
			// System.out.println("Dest Node not reachable");
			return new ArrayList<Direction>();
		}
		List<DefaultWeightedEdge> list = new DijkstraShortestPath<Position, DefaultWeightedEdge>(
				walkableGraph, startPosition, endPosition).getPathEdgeList();
		// System.out.println("List Edges:" + list);
		ArrayList<Direction> directions = edgeListToDirectionList(walkableGraph, startPosition, list);
		// System.out.println("Cells List:" + cellList);
		// System.out.println("Path's StartPos:" + startPosition);
		// System.out.println("Path's EndPos:" + endPosition);
		// System.out.println("Path's direction:" + directions);
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
			Position endPos) {
		CellType targetType = floor.get(endPos).getType();
		if (targetType == CellType.FREE || targetType == CellType.UNKNOWN)
			return endPos;
		return floor.nextTo(startPos, endPos, CellType.FREE,
				GameConfig.PATH_TOLERANCE);
	}

	private UndirectedWeightedGraph createWalkableGraph(Floor floor,
			Position startPos, Position endPos, boolean includeCellWithUnit) {
		Integer startRow = startPos.getRow();
		Integer startCol = startPos.getCol();
		// create walkable graph
		UndirectedWeightedGraph walkableGraph = new UndirectedWeightedGraph();
		// avoid exceptions
		walkableGraph.addVertex(startPos);
		// walkableGraph.addVertex(endRow + "," + endCol);
		for (int i = 0; i < floor.getRows(); i++)
			for (int j = 0; j < floor.getCols(); j++) {
				CellType cellType = floor.get(i, j).getType();
				// NB La cella di partenza anche se occupata dall'unita' fa
				// parte del grafo
				if (cellType == CellType.FREE
						|| cellType == CellType.UNKNOWN
						|| (includeCellWithUnit && (cellType == CellType.WORKER || cellType == CellType.SOLDIER))) {
					// A
					// |
					if (i - 1 >= 0
							&& (cellType == CellType.FREE
									|| cellType == CellType.UNKNOWN
									|| (includeCellWithUnit && (cellType == CellType.WORKER || cellType == CellType.SOLDIER)) || (i - 1 == startRow && j == startCol))) {
						walkableGraph.addWeightedEdge(new Position(i, j),
								new Position(i - 1, j), 1);
					}
					// |
					// v
					if (i + 1 < floor.getRows()
							&& (cellType == CellType.FREE
									|| cellType == CellType.UNKNOWN
									|| (includeCellWithUnit && (cellType == CellType.WORKER || cellType == CellType.SOLDIER)) || (i + 1 == startRow && j == startCol))) {
						walkableGraph.addWeightedEdge(new Position(i, j),
								new Position(i + 1, j), 1);
					}
					// <-
					if (j - 1 >= 0
							&& (cellType == CellType.FREE
									|| cellType == CellType.UNKNOWN
									|| (includeCellWithUnit && (cellType == CellType.WORKER || cellType == CellType.SOLDIER)) || (i == startRow && j - 1 == startCol))) {
						walkableGraph.addWeightedEdge(new Position(i, j),
								new Position(i, j - 1), 1);
					}
					// ->
					if (j + 1 < floor.getCols()
							&& (cellType == CellType.FREE
									|| cellType == CellType.UNKNOWN
									|| (includeCellWithUnit && (cellType == CellType.WORKER || cellType == CellType.SOLDIER)) || (i == startRow && j + 1 == startCol))) {
						walkableGraph.addWeightedEdge(new Position(i, j),
								new Position(i, j + 1), 1);
					}
					// A
					// \
					if (j - 1 >= 0
							&& i - 1 >= 0
							&& (cellType == CellType.FREE
									|| cellType == CellType.UNKNOWN
									|| (includeCellWithUnit && (cellType == CellType.WORKER || cellType == CellType.SOLDIER)) || (i - 1 == startRow && j - 1 == startCol))) {
						walkableGraph.addWeightedEdge(new Position(i, j),
								new Position(i - 1, j - 1), 1.5);
					}

					// A
					// /
					if (j + 1 < floor.getCols()
							&& i - 1 >= 0
							&& (cellType == CellType.FREE
									|| cellType == CellType.UNKNOWN
									|| (includeCellWithUnit && (cellType == CellType.WORKER || cellType == CellType.SOLDIER)) || (i - 1 == startRow && j + 1 == startCol))) {
						walkableGraph.addWeightedEdge(new Position(i, j),
								new Position(i - 1, j + 1), 1.5);
					}
					// /
					// v
					if (j - 1 >= 0
							&& i + 1 < floor.getRows()
							&& (cellType == CellType.FREE
									|| cellType == CellType.UNKNOWN
									|| (includeCellWithUnit && (cellType == CellType.WORKER || cellType == CellType.SOLDIER)) || (i + 1 == startRow && j - 1 == startCol))) {
						walkableGraph.addWeightedEdge(new Position(i, j),
								new Position(i + 1, j - 1), 1.5);
					}
					// \
					// v
					if (j + 1 < floor.getCols()
							&& i + 1 < floor.getRows()
							&& (cellType == CellType.FREE
									|| cellType == CellType.UNKNOWN
									|| (includeCellWithUnit && (cellType == CellType.WORKER || cellType == CellType.SOLDIER)) || (i + 1 == startRow && j + 1 == startCol))) {
						walkableGraph.addWeightedEdge(new Position(i, j),
								new Position(i + 1, j + 1), 1.5);
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
}
