package com.jrts.behaviours;

import com.jrts.agents.Soldier;
import com.jrts.environment.Direction;
import com.jrts.environment.Position;
import com.jrts.environment.World;

public class PatrolBehaviour extends UnitBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int distance = 20;
	
	public static final int DISTANCE_LITTLE = 5;
	public static final int DISTANCE_MEDIUM = 8;
	public static final int DISTANCE_BIG = 10;

	Soldier soldier;
	Position cityCenter;
	Position p1, p2;
	boolean go1 = true;
	/**
	 * 
	 * @param soldier
	 * @param direction: TOP | RIGHT | DOWN | LEFT 
	 */
	public PatrolBehaviour(Soldier soldier, Direction direction, int distance) {
		super(false);
		
		this.soldier = soldier;
		this.cityCenter = World.getInstance().getCityCenter(soldier.getTeamName());
		
		this.distance = distance;
		
		if(direction == Direction.LEFT)
		{
			p1 = cityCenter.bigStep(Direction.LEFT_UP, this.distance);
			p2 = cityCenter.bigStep(Direction.LEFT_DOWN, this.distance);
		} else if(direction == Direction.UP)
		{
			p1 = cityCenter.bigStep(Direction.LEFT_UP, this.distance);
			p2 = cityCenter.bigStep(Direction.RIGHT_UP, this.distance);
		} else if(direction == Direction.RIGHT)
		{
			p1 = cityCenter.bigStep(Direction.RIGHT_UP, this.distance);
			p2 = cityCenter.bigStep(Direction.RIGHT_DOWN, this.distance);
		} else if(direction == Direction.DOWN)
		{
			p1 = cityCenter.bigStep(Direction.LEFT_DOWN, this.distance);
			p2 = cityCenter.bigStep(Direction.RIGHT_DOWN, this.distance);
		}
		
	}

	@Override
	public void action() {

		if(go1) this.soldier.goThere(p2);
		else 	this.soldier.goThere(p1);

		go1 = !go1;
	}

	@Override
	public boolean done() {
		return false;
	}
}
