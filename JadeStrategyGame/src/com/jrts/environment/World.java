package com.jrts.environment;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.jrts.O2Ainterfaces.IUnit;

public class World {

	private static int WOOD_ENERGY = 100;
	private static int FOOD_ENERGY = 10000;
	private static int BUILDING_ENERGY = 1000;
	private static int FOOD_MIN_DISTANCE = 2;
	private static int FOOD_MAX_DISTANCE = 4;

	private static World instance = null;

	private final Floor floor;

	private final Map<String, Position> teams;

	private final int rows;
	private final int cols;

	public static void create(int rows, int cols, float woodPercentage) {
		instance = new World(rows, cols, woodPercentage);
	}

	public static World getInstance() {
		return instance;
	}

	private World(int rows, int cols, float woodPercentage) {
		this.rows = rows;
		this.cols = cols;
		floor = new Floor(rows, cols);

		int numWood = (int) (((float) (rows * cols)) * woodPercentage);
		Cell wood = new Cell(CellType.WOOD);
		wood.resourceEnergy = WOOD_ENERGY;
		floor.generateObject(numWood, wood);

		teams = new HashMap<String, Position>();
	}

	/**
	 * changes the position of an object using the specified direction
	 * 
	 * @param source
	 *            the position of the object
	 * @param d
	 *            the direction
	 * @return true if the movement has been performed
	 */
	public synchronized boolean move(Position source, Direction d) {
		floor.acquireLock();
		boolean result;
		Position destination = source.step(d);
		Cell srcCell = floor.get(source.row, source.col);
		if (isAvailable(destination)) {
			floor.set(source.row, source.col, new Cell(CellType.FREE));
			floor.set(destination.row, destination.col, srcCell);
			source.row = destination.row;
			source.col = destination.col;
			result = true;
		} else {
			result = false;
		}
		floor.releaseLock();
		return result;
	}

	boolean addObject(Cell objectType, Position p) {
		if (isAvailable(p)) {
			floor.set(p.row, p.col, objectType);
			return true;
		}
		return false;
	}

	/**
	 * returns the nearest free position from a source point next to a target.
	 * 
	 * @param src
	 *            the source position
	 * @param target
	 *            the target position
	 * @return the requested position
	 */
	public synchronized Position freePositionNextToTarget(Position src,
			Position target) {
		double distance = Double.MAX_VALUE;
		Position chosen = null;
		for (Direction d : Direction.VON_NEUMANN_NEIGH) {
			Position candidate = target.step(d);
			double candidateDistance = src.distance(candidate);
			if (isAvailable(candidate) && candidateDistance < distance) {
				chosen = candidate;
				distance = candidateDistance;
			}
		}
		return chosen;
	}

	public synchronized Cell getCell(Position p) {
		return floor.getCopy(p);
	}

	/*
	 * public Cell unSyncGetCell(Position p) { return floor.get(p); }
	 */

	/**
	 * returns a random free position near a center between the specified
	 * distance range
	 * 
	 * @param center
	 * @param minDistance
	 * @param maxDistance
	 * @return
	 */
	Position near(Position center, int minDistance, int maxDistance) {
		if (center.row + maxDistance > floor.rows
				|| center.col + maxDistance > floor.cols)
			return null;
		int maxIterations = 10;
		int len = maxDistance - minDistance + 1;
		Random r = new Random();
		Position candidate = null;
		do {
			int rowOffset = r.nextInt(len) + minDistance;
			int colOffset = r.nextInt(len) + minDistance;
			int rowMultiplier = (r.nextInt(2) == 0 ? -1 : 1);
			int colMultiplier = (r.nextInt(2) == 0 ? -1 : 1);
			int row = center.row + rowOffset * rowMultiplier;
			int col = center.col + colOffset * colMultiplier;
			candidate = new Position(row, col);
		} while (!isAvailable(candidate) && --maxIterations > 0);
		return candidate;
	}

	boolean isAvailable(Position p) {
		return p != null && floor.get(p.row, p.col).type == CellType.FREE;
	}

	/**
	 * adds the city center in a random position for a new team with the
	 * specified name
	 * 
	 * @param name
	 *            the name of the team, has to be unique
	 */
	public synchronized void addTeam(String name) {
		Random r = new Random();
		Cell base = new Cell(CellType.CITY_CENTER, name);
		base.resourceEnergy = BUILDING_ENERGY;
		Position p;
		do {
			p = new Position(r.nextInt(floor.rows), r.nextInt(floor.cols));
		} while (!addObject(base, p));
		teams.put(name, p);
		System.out.println("adding team " + name + " in " + p.toString());
		Position foodPosition = near(p, FOOD_MIN_DISTANCE, FOOD_MAX_DISTANCE);
		Cell food = new Cell(CellType.FOOD);
		food.resourceEnergy = FOOD_ENERGY;
		addObject(food, foodPosition);
	}

	/**
	 * gets a free position in the neighborhood of the parameter
	 * 
	 * @param p
	 *            the position to use as center of the neighborhood
	 * @return the position available
	 */
	public synchronized Position neighPosition(Position p) {
		if (!isAvailable(p)) {
			p = floor.nextTo(p, CellType.FREE, 2);
			if (p == null)
				return null;
		}
		return p;
	}

	public synchronized void addUnit(Position p, String id, IUnit unit) {
		Cell unitCell = new Cell(id, unit, unit.getType());
		floor.set(p.row, p.col, unitCell);
	}

	/**
	 * gets the position of the city center of the team with the specified name
	 * 
	 * @param teamName
	 *            the name of the team
	 * @return the position of the main building
	 */
	public synchronized Position getCityCenter(String teamName) {
		return teams.get(teamName);
	}

	public Floor getFloor() {
		return floor;
	}

	/**
	 * Returns the perceived floor in a certain position with the specified
	 * range of sight
	 * 
	 * @param center
	 *            where the observer is located
	 * @param sight
	 *            the range of sight of the observer
	 * @return a floor object where the perceived cells are the same of the
	 *         world's floor and the others are set to UNKNOWN
	 */

	public synchronized Perception getPerception(Position center, int sight) {
		return new Perception(floor, center, sight);
	}

	public synchronized int takeEnergy(Position target, int amount) {
		Cell targetCell = floor.get(target);
		if (targetCell.resourceEnergy >= amount) {
			targetCell.resourceEnergy -= amount;
			return amount;
		} else {
			int taken = targetCell.resourceEnergy;
			targetCell.resourceEnergy = 0;
			return taken;
		}
	}

	public synchronized void clear(Position p) {
		floor.acquireLock();
		floor.set(p.row, p.col, new Cell(CellType.FREE));
		floor.releaseLock();
	}

	public int getRows() {
		return rows;
	}

	public int getCols() {
		return cols;
	}

	public void agentDies(Position p) {
		floor.acquireLock();
		floor.set(p.getRow(), p.getCol(), new Cell(CellType.FREE));
		floor.releaseLock();
	}

	public Map<String, Position> getTeams() {
		return teams;
	}

	public synchronized boolean isGameFinished() {
		return teams.size() <= 1;
	}
}