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
			System.out.println("Creo grafo");
			mainWalkableGraph = createWalkableGraph(floor, new Position(0, 0),
					new Position(GameConfig.WORLD_ROWS - 1,
							GameConfig.WORLD_COLS - 1), true);
		}

		UndirectedWeightedGraph walkableGraph = mainWalkableGraph.clone();

		// adapt current walkableGraph from global
		for (int row = 0; row < floor.getRows(); row++)
			for (int col = 0; col < floor.getCols(); col++) {
				Position p = new Position(row, col);
				if (!p.equals(startPosition)
						&& floor.get(p).getType() != CellType.FREE
						&& floor.get(p).getType() != CellType.UNKNOWN)
					walkableGraph.removeVertex(p);
			}

		if (!walkableGraph.containsVertex(endPosition)) {
			// System.out.println("Dest Node not reachable");
			return new ArrayList<Direction>();
		}
		List<DefaultWeightedEdge> list = new DijkstraShortestPath<Position, DefaultWeightedEdge>(
				walkableGraph, startPosition, endPosition).getPathEdgeList();
		// System.out.println("List Edges:" + list);
		ArrayList<Position> cellList = edgeListToCellList(walkableGraph,
				startPosition, list);
		// System.out.println("Cells List:" + cellList);
		ArrayList<Direction> directions = cellListToDirections(cellList);
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
					if (j + 1 <= floor.getCols()
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

	public ArrayList<Position> edgeListToCellList(
			UndirectedWeightedGraph walkableGraph, Position sourceNode,
			List<DefaultWeightedEdge> list) {
		ArrayList<Position> cellList = new ArrayList<Position>();
		cellList.add(sourceNode);
		if (list == null)
			return cellList;
		Position curr = sourceNode;
		for (int i = 0; i < list.size(); i++) {
			Position source = walkableGraph.getEdgeSource(list.get(i));
			Position target = walkableGraph.getEdgeTarget(list.get(i));
			if (source.equals(curr))
				curr = target;
			else
				curr = source;
			cellList.add(curr);
		}
		return cellList;
	}

	public ArrayList<Direction> cellListToDirections(
			ArrayList<Position> cellList) {
		ArrayList<Direction> directionList = new ArrayList<Direction>();
		for (int i = 0; i < cellList.size() - 1; i++) {
			// System.out.println("Current edge " + cellList.get(i) + "-" +
			// cellList.get(i+1));
			int i1 = cellList.get(i).getRow();
			int j1 = cellList.get(i).getCol();
			int i2 = cellList.get(i + 1).getRow();
			int j2 = cellList.get(i + 1).getCol();

			if (i1 != i2) {// dall'alto in basso o viceversa
				if (i1 < i2) {// scende
					if (j1 == j2)// stessa colonna
						directionList.add(Direction.DOWN);
					else if (j1 < j2)// scende verso destra
						directionList.add(Direction.RIGHT_DOWN);
					else if (j1 > j2)// scende verso sinistra
						directionList.add(Direction.LEFT_DOWN);
				} else {// sale
					if (j1 == j2)// stessa colonna
						directionList.add(Direction.UP);
					else if (j1 < j2)// sale verso destra
						directionList.add(Direction.RIGHT_UP);
					else if (j1 > j2)// sale verso sinistra
						directionList.add(Direction.LEFT_UP);
				}
			} else // stessa riga
			if (j1 != j2) {
				if (j1 < j2)
					directionList.add(Direction.RIGHT);
				else
					directionList.add(Direction.LEFT);
			}
			// System.out.println("Direction choosed " +
			// directionList.get(directionList.size()-1));
		}
		return directionList;
	}
}
