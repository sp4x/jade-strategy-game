package com.jrts.environment;

import jade.util.leap.Serializable;

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
		return new Position(row + d.rowVar(), col + d.colVar());
	}
	
	public Position bigStep(Direction d, int numSteps) {
		Position newPosition = this;
		for (int i = 0; i < numSteps; i++)
			newPosition = newPosition.step(d);	
		
		return newPosition;
	}
	
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
		float x = Math.abs(this.col - other.col);
		float y = Math.abs(this.row - other.row);
		return Math.hypot(x, y);
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
	public boolean equals(Object pos){
		Position p = (Position) pos;
		return getRow() == p.getRow() && getCol() == p.getCol();
	}
}
