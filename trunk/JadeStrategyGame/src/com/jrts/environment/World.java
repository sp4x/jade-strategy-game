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
		Direction[] neigh = {Direction.DOWN, Direction.LEFT, Direction.LEFT_DOWN,
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
	
	
	/**
	 * adds the city centre in a random position for a new team with the specified name
	 * @param name the name of the team, has to be unique
	 */
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
	
	
	/**
	 * adds a unit in the specified position or in the neighborhood if it is not free
	 * @param id an identification string for the unit, should be unique for the world
	 * @param p the position to use. Can be the position of a factory
	 * @return the position where the unit has been created
	 */
	public synchronized Position addUnit(String id, Position p) {
		Cell unit = Cell.UNIT;
		unit.id = id;
		if (!isAvailable(p)) {
			p = nextTo(p);
			if (p == null)
				return null;
		}
		floor.set(p.row, p.col, unit);
		return p;
	}
	
	/**
	 * gets the position of the city centre of the team with the specified name
	 * @param teamName the name of the team
	 * @return the position of the main building
	 */
	public synchronized Position getBuilding(String teamName) {
		return teams.get(teamName);
	}

	public Floor getFloor() {
		return floor;
	}
	
	/**
	 * Returns the perceived floor in a certain position with the specified range of sight
	 * @param centre where the observer is located
	 * @param sight the range of sight of the observer
	 * @return a floor object where the perceived cells are the same of the world's floor
	 * and the others are set to UNKNOWN
	 */
	public synchronized Floor getPerception(Position centre, int sight) {
		Floor perception = new Floor(floor.rows, floor.cols);
		for (int row = 0; row < floor.rows; row++) {
			for (int col = 0; col < floor.cols; col++) {
				int sumOfDiff = Math.abs(centre.row-row) + Math.abs(centre.col-col);
				Cell perceived = (sumOfDiff <= sight ? floor.get(row, col) : Cell.UNKNOWN);
				perception.set(row, col, perceived);
			}
		}
		return perception;
	}

}
