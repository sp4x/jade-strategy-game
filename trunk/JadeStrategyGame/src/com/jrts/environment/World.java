package com.jrts.environment;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class World {
	
	private static int WOOD_ENERGY = 100;
	private static int FOOD_ENERGY = 10000;
	private static int BUILDING_ENERGY = 1000;
	private static int FOOD_MIN_DISTANCE = 3;
	private static int FOOD_MAX_DISTANCE = 6;
	
	private static World instance = null;
	
	private Floor floor;
	
	private Map<String, Position> teams = new HashMap<String, Position>();
	
	public static void create(int rows, int cols, float woodPercentage) {
		instance = new World(rows, cols);
		int numWood = (int) (((float) (rows*cols)) * woodPercentage);
		Cell wood = Cell.WOOD;
		wood.energy = WOOD_ENERGY;
		instance.floor.generateObject(numWood, wood);
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
	
	boolean addObject(Cell objectType, Position p) {
		if (isAvailable(p)) {
			floor.set(p.row, p.col, objectType);
			return true;
		}
		return false;
	}

	
	Position nextTo(Position p, int maxDistance) {
		if (maxDistance == 0)
			return p;
		for (int minDistance = 1; minDistance <= maxDistance; minDistance++) {
			for (Direction d : Direction.ALL) {
				Position candidate = nextTo(p.step(d), minDistance-1);
				if (candidate != null && isAvailable(candidate))
					return candidate;
			}
		}
		return null;
	}
	
	Position near(Position p, int minDistance, int maxDistance) {
		if (p.row+maxDistance > floor.rows || p.col+maxDistance > floor.cols)
			return null;
		int maxIterations = 10;
		int len = maxDistance - minDistance + 1;
		Random r = new Random();
		Position candidate = null;
		do {
			int rowOffset = r.nextInt(len) + minDistance;
			int colOffset = r.nextInt(len) + minDistance;
			int rowMultiplier = ( r.nextInt(2) == 0 ? -1 : 1);
			int colMultiplier = ( r.nextInt(2) == 0 ? -1 : 1);
			int row = p.row+rowOffset*rowMultiplier;
			int col = p.col+colOffset*colMultiplier;
			candidate = new Position(row, col);
		} while (!isAvailable(candidate) && --maxIterations>0);
		return candidate;
	}
	
	boolean isAvailable(Position p) {
		return p != null && floor.get(p.row, p.col) == Cell.FREE;
	}
	
	
	/**
	 * adds the city centre in a random position for a new team with the specified name
	 * @param name the name of the team, has to be unique
	 */
	public synchronized void addTeam(String name) {
		Random r = new Random();
		Cell base = Cell.BUILDING;
		base.id = name;
		base.energy = BUILDING_ENERGY;
		Position p;
		do {
			p = new Position(r.nextInt(floor.rows), r.nextInt(floor.cols));
		} while (!addObject(base, p));
		teams.put(name, p);
		System.out.println("adding team "+name+" in "+p.toString());
		Position foodPosition = near(p, FOOD_MIN_DISTANCE, FOOD_MAX_DISTANCE);
		Cell food = Cell.FOOD;
		food.energy = FOOD_ENERGY;
		addObject(food, foodPosition);
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
			p = nextTo(p,2);
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
		System.out.println("asking for "+teamName);
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
				boolean inRange = Math.abs(centre.row-row) <= sight && Math.abs(centre.col-col) <= sight;
				Cell perceived = ( inRange ? floor.get(row, col) : Cell.UNKNOWN);
				perception.set(row, col, perceived);
			}
		}
		floor.get(centre).damage = 0; //set damage to 0 after it has been perceived
		return perception;
	}
	
	public synchronized void applyDamage(Position target, int damage) {
		Cell targetCell = floor.get(target);
		if (targetCell == Cell.UNIT) {
			targetCell.damage = damage;
		}
		else if (targetCell == Cell.BUILDING) {
			targetCell.energy -= damage;
		}
	}
	
	public synchronized int takeEnergy(Position target, int amount) {
		Cell targetCell = floor.get(target);
		if (targetCell.energy >= amount) {
			targetCell.energy -= amount;
			return amount;
		} else {
			int taken = targetCell.energy;
			targetCell.energy = 0;
			return taken;
		}
	}
	
	public synchronized void clear(Position p) {
		floor.set(p.row, p.col, Cell.FREE);
	}

}
