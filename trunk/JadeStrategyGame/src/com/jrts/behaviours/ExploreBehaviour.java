package com.jrts.behaviours;

import java.util.ArrayList;

import com.jrts.agents.Soldier;
import com.jrts.common.Utils;
import com.jrts.environment.Direction;
import com.jrts.environment.Position;

public class ExploreBehaviour extends UnitBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Soldier soldier;
	Position cityCenter;
	boolean exploring;
	FollowPathBehaviour followPathBehaviour;
	ArrayList<Direction> directions;

	/**
	 * 
	 * @param soldier
	 */
	public ExploreBehaviour(Soldier soldier) {
		super(false);
		this.soldier = soldier;
		this.cityCenter = soldier.requestCityCenterPosition();
		
		directions = new ArrayList<Direction>();
		
		directions.add(Direction.LEFT_UP);
		directions.add(Direction.LEFT_DOWN);
		directions.add(Direction.RIGHT_UP);
		directions.add(Direction.RIGHT_DOWN);

		directions.remove(Utils.getMapAnglePosition(this.soldier.getPosition()));
		
		explore();
	}
	
	public void explore(){
		Direction dir = directions.get(Utils.random.nextInt(3));
		Position posToGo = Utils.getRandomUnknownCellPosition(this.soldier.requestMap(), dir);

		if (posToGo != null) {
			followPathBehaviour = new FollowPathBehaviour(soldier, posToGo, 1);
			exploring = true;
		}
	}

	@Override
	public void myAction() {
		followPathBehaviour.myAction();
		if(followPathBehaviour.done()){
			explore();
		}
	}

	@Override
	public boolean done() {
		return false;
	}
}
