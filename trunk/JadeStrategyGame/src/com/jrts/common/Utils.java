package com.jrts.common;

import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

import com.jrts.environment.CellType;
import com.jrts.environment.Direction;
import com.jrts.environment.Floor;
import com.jrts.environment.Position;
import com.jrts.environment.WorldMap;
import com.jrts.pathfinding.AStarPathfinder;
import com.jrts.pathfinding.DijkstraPathfinder;
import com.jrts.pathfinding.Pathfinder;

public class Utils {
	
	public static Random random = new Random(GregorianCalendar.getInstance().getTimeInMillis());
	
	public static Pathfinder pathfinder = new DijkstraPathfinder();
	
	
	/**
	 * This function let you know in what angle of the map the unit is located
	 * @param The position of the unit
	 * @return a Direction between LEFT_UP, RIGHT_UP, LEFT_DOWN, RIGHT_DOWN
	 */
	public static Direction getMapAnglePosition(Position p)
	{
		if(p.getRow() <= GameConfig.WORLD_ROWS/2){
			if(p.getCol() <= GameConfig.WORLD_COLS/2)
				return Direction.LEFT_UP;
			else return Direction.RIGHT_UP;
		} else {
			if(p.getCol() <= GameConfig.WORLD_COLS/2)
				return Direction.LEFT_DOWN;
			else return Direction.RIGHT_DOWN;			
		}
	}
	
	/**
	 * Get the position of a random unknown cell located in one of the angles of the map
	 * @param a worldmap of a team
	 * @param a direction in witch looking for a unknown cell, must be between LEFT_UP, RIGHT_UP, LEFT_DOWN, RIGHT_DOWN
	 * @return the position of an unknown cell or NULL if not found
	 */
	public static Position getRandomUnknownCellPosition(WorldMap map, Direction d)
	{
		int w = 0;
		int h = 0;
		
		if(d.equals(Direction.LEFT_UP))
		{
			w = 0;
			h = 0;
		} else if(d.equals(Direction.LEFT_DOWN))
		{
			w = 0;
			h = GameConfig.WORLD_ROWS/2;
		} else if(d.equals(Direction.RIGHT_UP)){
			w = GameConfig.WORLD_COLS/2;
			h = 0;
		} else if(d.equals(Direction.RIGHT_DOWN)){
			w = GameConfig.WORLD_COLS/2;
			h = GameConfig.WORLD_ROWS/2;
		}
		
		for (int i = 0; i < 10; i++) {

			int x = w + getRandom().nextInt(GameConfig.WORLD_COLS/2);
			int y = h + getRandom().nextInt(GameConfig.WORLD_ROWS/2);
			Position pos = new Position(x, y);
			if(map.get(pos).getType().equals(CellType.UNKNOWN))
				return pos;
		}
		
		return null;
	}

	public static Random getRandom() {
		return random;
	}

	public static void setRandom(Random random) {
		Utils.random = random;
	}

	public static List<Direction> calculatePath(Floor worldCachedCopy,
			Position start, Position goal) {
		return pathfinder.calculatePath(worldCachedCopy, start, goal);
	}
}
