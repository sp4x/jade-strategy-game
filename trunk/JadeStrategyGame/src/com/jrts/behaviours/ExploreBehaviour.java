package com.jrts.behaviours;

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
	FollowPathBehaviour exploreBehaviour;

	/**
	 * 
	 * @param soldier
	 */
	public ExploreBehaviour(Soldier soldier) {
		super(false);
		this.soldier = soldier;
		this.cityCenter = soldier.requestCityCenterPosition();
		
		explore();
	}
	
	public void explore(){
		exploring = false;
		Position posToGo = Utils.getRandomUnknownCellPosition(this.soldier.requestMap(), Direction.random());

		if (posToGo != null) {
			exploreBehaviour = new FollowPathBehaviour(soldier, posToGo, 1);
			exploring = true;
		}
	}

	@Override
	public void myAction() {
		if(exploring){
			exploreBehaviour.myAction();
			if(exploreBehaviour.done()){
				explore();
			}
		}
	}

	@Override
	public boolean done() {
		return false;
	}
}
