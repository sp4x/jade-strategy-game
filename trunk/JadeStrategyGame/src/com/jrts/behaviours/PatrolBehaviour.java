package com.jrts.behaviours;

import com.jrts.agents.Soldier;
import com.jrts.environment.Direction;
import com.jrts.environment.Position;
import com.jrts.environment.WorldMap;

@SuppressWarnings("serial")
public class PatrolBehaviour extends UnitBehaviour {
	
	private int distance = 20;
	
	public static final int DISTANCE_LITTLE = 5;
	public static final int DISTANCE_MEDIUM = 8;
	public static final int DISTANCE_BIG = 10;

	Soldier soldier;
	Position cityCenter;
	Position p1, p2;
	boolean go1 = true;
	/**
	/**
	 * 
	 * @param soldier
	 * @param direction: TOP | RIGHT | DOWN | LEFT 
	 */
	public PatrolBehaviour(Soldier soldier, Direction direction, int distance, WorldMap worldMap) {
		super(false);
		
		this.soldier = soldier;
		this.cityCenter = soldier.requestCityCenterPosition();
		
		this.distance = distance;
		
		if(direction == Direction.LEFT)
		{
			p1 = worldMap.bigStep(cityCenter, Direction.LEFT_UP, this.distance);
			p2 = worldMap.bigStep(cityCenter, Direction.LEFT_DOWN, this.distance);
		} else if(direction == Direction.UP)
		{
			p1 = worldMap.bigStep(cityCenter, Direction.LEFT_UP, this.distance);
			p2 = worldMap.bigStep(cityCenter, Direction.RIGHT_UP, this.distance);
		} else if(direction == Direction.RIGHT)
		{
			p1 = worldMap.bigStep(cityCenter, Direction.RIGHT_UP, this.distance);
			p2 = worldMap.bigStep(cityCenter, Direction.RIGHT_DOWN, this.distance);
		} else if(direction == Direction.DOWN)
		{
			p1 = worldMap.bigStep(cityCenter, Direction.LEFT_DOWN, this.distance);
			p2 = worldMap.bigStep(cityCenter, Direction.RIGHT_DOWN, this.distance);
		}
	}

	@Override
	public void myAction() {
		if(go1)
			this.soldier.goThere(p2);
		else
			this.soldier.goThere(p1);
		go1 = !go1;
	}

	@Override
	public boolean done() {
		return false;
	}
}
