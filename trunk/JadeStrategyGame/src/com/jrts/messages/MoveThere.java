package com.jrts.messages;

import java.io.Serializable;

import com.jrts.environment.Direction;


public class MoveThere implements Serializable {
	int sourceRow;
	int sourceCol;
	Direction dir;
	
	public MoveThere(int sourceRow, int sourceCol, Direction dir) {
		super();
		this.sourceRow = sourceRow;
		this.sourceCol = sourceCol;
		this.dir = dir;
	}

	public int getSourceRow() {
		return sourceRow;
	}

	public void setSourceRow(int sourceRow) {
		this.sourceRow = sourceRow;
	}

	public int getSourceCol() {
		return sourceCol;
	}

	public void setSourceCol(int sourceCol) {
		this.sourceCol = sourceCol;
	}

	public Direction getDir() {
		return dir;
	}

	public void setDir(Direction dir) {
		this.dir = dir;
	}
}
