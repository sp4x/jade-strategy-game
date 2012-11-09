package com.jrts.environment;

import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

import com.jrts.O2Ainterfaces.IUnit;
import com.jrts.common.ThreadMonitor;

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

	Random r = new Random(GregorianCalendar.getInstance().getTimeInMillis());

	/**
	 * I 4 booleani rappresentano i 4 angoli della mappa, ogni qual volta la base di una squadra 
	 * occupa uno di essi, allora la relativa cella dell'array diventa true.
	 */
	private boolean[] occupiedAngles = {false, false, false, false};
	private Logger logger = Logger.getLogger(World.class.getName());
	
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

	
	public synchronized boolean doMovement(Position source, Direction d) {
		Position destination = source.step(d);
		Cell srcCell = floor.get(source);
		if (isAvailable(destination)) {
			clear(source);
			floor.set(destination, srcCell);
			return true;
		} 
		return false;
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
	public boolean move(Position source, Direction d) {
		ThreadMonitor.getInstance().doWait();
		return doMovement(source, d);
	}

	boolean addObject(Cell objectType, Position p) {
		if (isAvailable(p)) {
			floor.set(p, objectType);
			return true;
		}
		return false;
	}


	public synchronized Cell getCell(Position p) {
		return floor.getCopy(p);
	}


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
		return floor.isValid(p) && floor.get(p).type == CellType.FREE;
	}
	
	void clear(Position p) {
		floor.set(p, new Cell(CellType.FREE));
	}

	/**
	 * adds the city center in a random position for a new team with the
	 * specified name
	 * 
	 * @param name
	 *            the name of the team, has to be unique
	 */
	public void addTeam(String name) {
		// Prendo un angolo a caso tra 0 e 3
		int angle = r.nextInt(4);

		// se l'angolo non e' occupato da un'altra squadra 
		// lo utilizzo per mettere la base della squadra corrente,
		// altrimenti ne scelgo un altro

		while(this.occupiedAngles[angle])
			angle = (angle + 1) % 4;

		this.occupiedAngles[angle] = true;
		
		System.out.println("TEAM " + name + " in angolo " + angle);
		
		Cell base = new Cell(CellType.CITY_CENTER, name);
		base.resourceEnergy = BUILDING_ENERGY;

		//Inizializzo la var con una posizione inesistente
		Position startP = new Position(-1, -1);
		
		// A seconda dell'angolo della mappa scelto e del valore della var n
		// scelgo la posizione della mappa ove posizionare la base
		int n = r.nextInt(10) + 1;
		do {
			switch (angle) {
			case 0:
				startP = new Position(n, n);
				break;
			case 1:
				startP = new Position((this.rows - n), n);
				break;
			case 2:
				startP = new Position((this.rows - n), (this.cols - n));
				break;
			case 3:
				startP = new Position(n, (this.cols - n));
				break;
			}
			
			// Genero un numero casuale che sarà utilizzato per distanziare la base
			// dalla cella di riferimento dellangolo della mappa scelto
			//startP.col += (r.nextInt(2) == 0)? r.nextInt(10) : - r.nextInt(10);
		
		} while (!addObject(base, startP));
		
		teams.put(name, startP);
		Position foodPosition = near(startP, FOOD_MIN_DISTANCE, FOOD_MAX_DISTANCE);

		Cell food = new Cell(CellType.FOOD);
		food.resourceEnergy = FOOD_ENERGY;
		addObject(food, foodPosition);

		logger .info("TEAM " + name + " added in " + startP.toString());
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

	public synchronized void addUnit(Position p, String unitId, IUnit unit) {
		Cell unitCell = new Cell(unitId, unit, unit.getType());
		floor.set(p, unitCell);
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
			clear(target);
			if (targetCell.type == CellType.CITY_CENTER)
				teams.remove(targetCell.id);
			return taken;
		}
	}

	public int getRows() {
		return rows;
	}

	public int getCols() {
		return cols;
	}

	public synchronized void killUnit(IUnit u) {
		Cell target = floor.get(u.getPosition());
		if (u.getId().equals(target.id)) {
			clear(u.getPosition());
		}
	}

	public synchronized boolean isGameFinished() {
		return teams.size() <= 1;
	}
}