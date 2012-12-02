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
	
	public static final int WORKER_SIGHT = 1;
	public static final int SOLDIER_SIGHT = 2;
	public static final int CITY_CENTER_SIGHT = 8;
	
	public static final int WORKER_KNAPSACK_CAPACITY = 10;
	
	public static final int UNIT_MOVING_ATTEMPTS = 20;
	
	public static final int NOTIFICATION_REFRESH = 1;
	
	public static int UNIT_WAIT_TIME = 4000;
	
	public static int REFRESH_TIME = 800;
	
	public static final int ATTACKS_REFRESH = 500;
	public static final int PERCEPTION_REFRESH = 500;
	
	public static final int UNIT_CREATION_TIME = 5;
	public static final long UNIT_TABLE_REFRESH_TIME = 100;
	public static final long SOLDIERS_TABLE_REFRESH_TIME = 100;
	
	public static final int STARTUP_WOOD = WORKER_WOOD_COST;
	public static final int STARTUP_FOOD = WORKER_FOOD_COST;

	public static final int PATH_TOLERANCE = 5;
	public static final int HIT_RANGE = 1;
	public static final int ICON_SIZE = 15;
	
	public static final int TREE_ENERGY = 500;
	public static final int FARM_ENERGY = 500;
	
	public static final boolean STATISTICS = true;
	public static final long GOALS_UPDATE = 1000;
	public static final int POPULATION_LIMIT = 2500;
	public static final int RUNAWAY_DISTANCE = 10;
	
	public static final int ATTACKS_SLEEP_TIME = 600;
	public static final int ENEMY_SIGHTING_BOUND = 10;
	public static final int BATTALION_SIZE = 3;
	
	public static final int LOW_PRIORITY_RESOURCES_UNITS = 2;
	public static final int MEDIUM_PRIORITY_RESOURCES_UNITS = 4;
	public static final int HIGH_PRIORITY_RESOURCES_UNITS = 6;
	
	public static final int LOW_PRIORITY_ATTACKS_UNITS = 0;
	public static final int MEDIUM_PRIORITY_ATTACKS_UNITS = 5;
	public static final int HIGH_PRIORITY_ATTACKS_UNITS = 10;
	
	public static final int LOW_PRIORITY_PATROLING_UNITS = 1;
	public static final int MEDIUM_PRIORITY_PATROLING_UNITS = 2;
	public static final int HIGH_PRIORITY_PATROLING_UNITS = 4;
	
	public static final int LOW_PRIORITY_EXPLORATION_UNITS = 0;
	public static final int MEDIUM_PRIORITY_EXPLORATION_UNITS = 1;
	public static final int HIGH_PRIORITY_EXPLORATION_UNITS = 2;
	
	public static final int MEETING_POINT_MIN_DISTANCE = 3;
	public static final int MEETING_POINT_MAX_DISTANCE = 4;
	
	public static final int DEFENCE_NATURE_DEFENSIVE_BOUND = 15;
	public static final int DEFENCE_NATURE_AVERAGE_BOUND = 10;
	public static final int DEFENCE_NATURE_AGGRESSIVE_BOUND = 5;
	
	public static int BUILDING_ENERGY = 100; //1000;

	public static int FOOD_MIN_DISTANCE = 3;
	public static int FOOD_MAX_DISTANCE = 5;
	public static int WOOD_MIN_DISTANCE = 3;
	public static int WOOD_MAX_DISTANCE = 5;
}
