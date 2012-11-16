package com.jrts.environment;


public class WorldMap extends Floor {

	private static final long serialVersionUID = 1L;

	public WorldMap(int rows, int cols) {
		super(rows, cols, CellType.UNKNOWN);
	}

	public void update(Perception perception) {
		for (int i = 0; i < perception.rows; i++) {
			for (int j = 0; j < perception.cols; j++) {
				Cell cell = new Cell(perception.getRelative(i, j));
				CellType curr = cell.getType();
				if (curr != CellType.UNKNOWN) {
					if (curr == CellType.WORKER || curr == CellType.SOLDIER) 
						cell.setType(CellType.FREE);
					set(perception.getAbsoluteRow(i), perception.getAbsoluteCol(j), cell);
				}
			}
		}
	}

	// public Position findNearest(Position center, CellType type) {
	// double distance = Double.MAX_VALUE;
	// Position nearestPosition = null;
	// for (int i = 0; i < rows; i++) {
	// for (int j = 0; j < cols; j++) {
	// Position p = new Position(i, j);
	// if (get(p).getType() == type) {
	// double currentDistance = center.distance(p);
	// if (currentDistance < distance) {
	// nearestPosition = p;
	// distance = currentDistance;
	// }
	// }
	// }
	// }
	// return nearestPosition;
	// }

	public Position findNearest(Position center, CellType type) {
//		return nextTo(center, type, (rows + cols) / 2);

		Position nearest = null;
		double distance = Double.MAX_VALUE;
		for (int i=0; i < getRows(); i++) {
			for (int j=0; j < getCols(); j++) {
				if (get(i, j).getType() == type) {
					Position currPos = new Position(i, j);
					double currDistance = center.distance(currPos);
					if (currDistance < distance) {
						nearest = currPos;
						distance = currDistance;
					}
				}
			}
		}
		
		return nearest;
	}

}
