package com.jrts.environment;

import java.util.List;

import common.Utils;

import logic.Direction;
import agents.Unit;

public class World {
	
	private static World instance = null;
	
	private Floor floor;
	
	public static void create(int rows, int cols, float woodPercentage) {
		instance = new World(rows, cols);
		int numWood = (int) (((float) (rows*cols)) * woodPercentage);
		instance.floor.generateObject(numWood, Cell.WOOD);
	}
	
	public static World getInstance() {
		return instance;
	}
	
	private World(int rows, int cols) {
		this.floor = new Floor(rows, cols);
	}
	
	public synchronized boolean move(Unit unit, Direction d) {
		int destRow = unit.getRow() + d.rowVar();
		int destCol = unit.getCol() + d.colVar();
		if (this.floor.get(destRow, destCol) == Cell.FREE) {
			this.floor.set(unit.getRow(), unit.getCol(), Cell.FREE);
			this.floor.set(destRow, destCol, Cell.UNIT);
			unit.setPosition(destRow, destCol);
			return true;
		}
		return false;
	}
	
	public synchronized List<Direction> getPath(Unit unit, int destRow, int destCol) {
		return Utils.calculatePath(floor, unit.getRow(), unit.getCol(), destRow, destCol);
	}
	
	public synchronized boolean addObject(Cell objectType, int row, int col) {
		if (this.floor.get(row, row) == Cell.FREE) {
			this.floor.set(row, col, objectType);
			return true;
		}
		return false;
	}

}
