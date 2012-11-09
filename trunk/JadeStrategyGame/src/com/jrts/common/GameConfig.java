package com.jrts.common;

public class GameConfig {
	public static final int WORLD_ROWS = 40;
	public static final int WORLD_COLS = 40;
	public static final int WORLD_VISIBILITY_MAP_FACTOR = 3;
	
	public static final int WORKER_LIFE = 10;
	public static final int SOLDIER_LIFE = 20;
	
	public static final int WORKER_DAMAGES = 1;
	public static final int SOLDIER_DAMAGES = 3;
	
	public static final int WORKER_SPEED = 15;
	public static final int SOLDIER_SPEED = 7;
	
	public static final int WORKER_FOOD_COST = 30;
	public static final int WORKER_WOOD_COST = 10;

	public static final int SOLDIER_FOOD_COST = 40;
	public static final int SOLDIER_WOOD_COST = 30;
	
	public static final int SOLDIER_NUMBER_LIMIT = 10;
	public static final int WORKER_NUMBER_LIMIT = 10;
	
	public static final int WORKER_SIGHT = 1; //TODO change
	public static final int SOLDIER_SIGHT = 1;
	public static final int CITY_CENTER_SIGHT = 5;
	
	public static final int WORKER_KNAPSACK_CAPACITY = 10;
	
	public static final int UNIT_MOVING_ATTEMPTS = 20;
	
	public static int REFRESH_TIME = 5000;
	public static final int MIN_REFRESH_TIME = 1000;
	public static final int MAX_REFRESH_TIME = 8000;
	
	public static final float VERTICAL_OVERLAP = 1f;
	public static final float HORIZONTAL_OVERLAP = 1.00f;
	
	public static final int ATTACKS_REFRESH = 50;
	public static final int PERCEPTION_REFRESH = 500;
	
	public static final int UNIT_CREATION_TIME = 1;
	public static final long WORKERS_MAP_REFRESH_TIME = 100;
	
	public static final int STARTUP_WOOD = 50;
	public static final int STARTUP_FOOD = 50;
	
	public static final int PATH_TOLERANCE = 5;
}
