package com.jrts.environment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import common.Utils;

public class World {
	
	private static World instance = null;
	
	private Floor floor;
	
	private Map<String, Position> teams = new HashMap<String, Position>();
	
	public static void create(int rows, int cols, float woodPercentage) {
		instance = new World(rows, cols);
		int numWood = (int) (((float) (rows*cols)) * woodPercentage);
		instance.floor.generateObject(numWood, Cell.WOOD);
	}
	
	public static World getInstance() {
		return instance;
	}
	
	private World(int rows, int cols) {
		floor = new Floor(rows, cols);
	}
	
	
	/**
	 * changes the position of an object using the specified direction
	 * 
	 * @param source the position of the object
	 * @param d the direction
	 * @return true if the movement has been performed
	 */
	public synchronized boolean move(Position source, Direction d) {
		Position destination = source.step(d);
		Cell srcCell = floor.get(source.row, source.col);
		if (isAvailable(destination)) {
			floor.set(source.row, source.col, Cell.FREE);
			floor.set(destination.row, destination.col, srcCell);
			source.row = destination.row;
			source.col = destination.col;
			return true;
		}
		return false;
	}
	
	public synchronized List<Direction> getPath(Position source, int destRow, int destCol) {
		return Utils.calculatePath(floor, source.getRow(), source.getCol(), destRow, destCol);
	}
	
	boolean addObject(Cell objectType, Position p) {
		if (isAvailable(p)) {
			floor.set(p.row, p.col, objectType);
			return true;
		}
		return false;
	}
	
	public Position nextTo(Position p) {
		Direction []neigh = {Direction.DOWN, Direction.LEFT, Direction.LEFT_DOWN,
				Direction.LEFT_UP, Direction.RIGHT, Direction.RIGHT_DOWN,
				Direction.RIGHT_UP, Direction.UP};
		for (int i = 0; i < neigh.length; i++) {
			Position candidate = p.step(neigh[i]);
			if (isAvailable(candidate))
				return candidate;
		}
		return null;
	}
	
	boolean isAvailable(Position p) {
		return floor.get(p.row, p.col) == Cell.FREE;
	}
	
	public synchronized void addTeam(String name) {
		Random r = new Random();
		Cell base = Cell.BUILDING;
		base.id = name;
		Position p;
		do {
			p = new Position(r.nextInt(floor.rows), r.nextInt(floor.cols));
		} while (!addObject(base, p));
		teams.put(name, p);
	}
	
	public synchronized boolean addUnit(String id, Position p) {
		Cell unit = Cell.UNIT;
		unit.id = id;
		if (!isAvailable(p)) {
			p = nextTo(p);
			if (p == null) {
				System.out.println("cannot add unit");
				return false;
			}
		}
		floor.set(p.row, p.col, unit);
		return true;
	}
	
	public synchronized Position getBuilding(String teamName) {
		return teams.get(teamName);
	}

	public Floor getFloor() {
		return floor;
	}
	
	

}
