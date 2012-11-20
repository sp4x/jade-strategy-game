package com.jrts.environment;

import java.util.Collection;
import java.util.LinkedList;


public class WorldMap extends Floor {

	private static final long serialVersionUID = 1L;

	public WorldMap(int rows, int cols) {
		super(rows, cols, CellType.UNKNOWN);
	}

	public void update(Perception perception) {
		for (int i = 0; i < perception.rows; i++) {
			for (int j = 0; j < perception.cols; j++) {
				Cell cell = new Cell(perception.getRelative(i, j));
				if (!cell.isUnknown()) {
					if (cell.isUnit()) 
						cell.setType(CellType.FREE);
					set(perception.getAbsoluteRow(i), perception.getAbsoluteCol(j), cell);
				}
			}
		}
	}

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
	
	
	public double exploredPercentage() {
		double totalCells = rows*cols, unknown = 0;
		for (int i=0; i < getRows(); i++) {
			for (int j=0; j < getCols(); j++) {
				if (get(i, j).type == CellType.UNKNOWN)
					unknown += 1;
			}
		}
		return unknown*100.0/totalCells;
	}
	
	
	public Collection<Position> getKnownCityCenters() {
		Collection<Position> positions = new LinkedList<Position>();
		for (int i=0; i < getRows(); i++) {
			for (int j=0; j < getCols(); j++) {
				if (get(i, j).type == CellType.CITY_CENTER)
					positions.add(new Position(i, j));
			}
		}
		return positions;
	}

	public Position bigStep(Position start, Direction d, int numSteps) {
		Position newPosition = start;
		for (int i = 0; i < numSteps; i++) {
			Position p = newPosition.step(d);
			if(p.getCol() < 0) p.setCol(0);
			else if(p.getCol() >= cols) p.setCol(cols - 1 );
			
			if(p.getRow() < 0) p.setRow(0);
			else if(p.getRow() >= rows) p.setRow(rows - 1);
		}
		
		return newPosition;
	}
	
	
	

}
