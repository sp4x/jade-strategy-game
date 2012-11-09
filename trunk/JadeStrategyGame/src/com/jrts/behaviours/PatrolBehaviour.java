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
	
	private static final int RAY = 20;

	Soldier soldier;
	Position cityCenter;
	Position p1, p2;
	boolean go1 = true;
	/**
	 * 
	 * @param soldier
	 * @param direction: TOP | RIGHT | DOWN | LEFT 
	 */
	public PatrolBehaviour(Soldier soldier, Direction direction) {
		super(false);
		
		this.soldier = soldier;
		this.cityCenter = World.getInstance().getCityCenter(soldier.getTeamName());
		
		if(direction == Direction.LEFT)
		{
			p1 = cityCenter.bigStep(Direction.LEFT_UP, PatrolBehaviour.RAY);
			p2 = cityCenter.bigStep(Direction.LEFT_DOWN, PatrolBehaviour.RAY);
		} else if(direction == Direction.UP)
		{
			p1 = cityCenter.bigStep(Direction.LEFT_UP, PatrolBehaviour.RAY);
			p2 = cityCenter.bigStep(Direction.RIGHT_UP, PatrolBehaviour.RAY);
		} else if(direction == Direction.RIGHT)
		{
			p1 = cityCenter.bigStep(Direction.RIGHT_UP, PatrolBehaviour.RAY);
			p2 = cityCenter.bigStep(Direction.RIGHT_DOWN, PatrolBehaviour.RAY);
		} else if(direction == Direction.DOWN)
		{
			p1 = cityCenter.bigStep(Direction.LEFT_DOWN, PatrolBehaviour.RAY);
			p2 = cityCenter.bigStep(Direction.RIGHT_DOWN, PatrolBehaviour.RAY);
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
