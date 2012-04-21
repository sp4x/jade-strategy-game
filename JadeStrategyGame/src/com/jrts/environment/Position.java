package com.jrts.environment;

public class Position {
	
	int row;
	int col;
	
	public Position(int row, int col) {
		super();
		this.row = row;
		this.col = col;
	}

	Position step(Direction d) {
		return new Position(row + d.rowVar(), col + d.colVar());
	}
	
	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
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
