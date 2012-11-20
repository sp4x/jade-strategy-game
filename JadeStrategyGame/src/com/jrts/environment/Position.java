package com.jrts.environment;

import jade.util.leap.Serializable;

import java.util.Collection;
import java.util.List;
import java.util.TreeMap;

public class Position implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	int row;
	int col;
	
	public Position(int row, int col) {
		super();
		this.row = row;
		this.col = col;
	}

	public Position step(Direction d) {

//		Position p = new Position(row + d.rowVar(), col + d.colVar());
//		if(p.getCol() < 0) p.setCol(0);
//		else if(p.getCol() >= GameConfig.WORLD_COLS) p.setCol(GameConfig.WORLD_COLS - 1);
//		
//		if(p.getRow() < 0) p.setRow(0);
//		else if(p.getRow() >= GameConfig.WORLD_ROWS) p.setRow(GameConfig.WORLD_ROWS - 1);
//		
//		return p;

		return new Position(row + d.rowVar(), col + d.colVar());
	}
	
	public Position followPath(List<Direction> path) {
		Position p = this;
		for (Direction direction : path) {
			p = p.step(direction);
		}
		
		return p;
	}
	
	
//	public Position bigStep(Direction d, int numSteps) {
//		Position newPosition = this;
//		for (int i = 0; i < numSteps; i++)
//			newPosition = newPosition.step(d);	
//		
//		return newPosition;
//	}
	
	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}
	
	public void setRow(int row) {
		this.row = row;
	}

	public void setCol(int col) {
		this.col = col;
	}
	
	public double distance(Position other) {
		if(other == null){
			return Double.MAX_VALUE;
		}
		float x = Math.abs(this.col - other.col);
		float y = Math.abs(this.row - other.row);
		return Math.hypot(x, y);
	}
	
	public boolean isNextTo(Position other) {
		return distance(other) < 2;
	}
	
	@Override
	public Position clone(){
		return new Position(row, col);
	}

	@Override
	public String toString() {
		return "(" + Integer.toString(row) + "," + Integer.toString(col) + ")";
	}
	
	@Override
	public int hashCode() {
		return row*1000 + col;
	}

	@Override
	public boolean equals(Object pos){
		Position p = (Position) pos;
		return getRow() == p.getRow() && getCol() == p.getCol();
	}
	
	public Position nearest(Collection<Position> candidates) {
		TreeMap<Double, Position> sortedMap = new TreeMap<Double, Position>();
		for (Position position : candidates) {
			sortedMap.put(distance(position), position);
		}
		return sortedMap.firstEntry().getValue();
	}
}
