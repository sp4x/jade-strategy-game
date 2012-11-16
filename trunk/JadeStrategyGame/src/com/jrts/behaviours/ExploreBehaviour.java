package com.jrts.behaviours;

import java.util.LinkedList;

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
	boolean going;
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
		if(!going){
			Position posToGo = Utils.getRandomUnknownCellPosition(this.soldier.requestMap(), Direction.random());

			if (posToGo != null) {
				exploreBehaviour = new FollowPathBehaviour(soldier, posToGo, 1);
				going = true;
			}
		}
	}

	@Override
	public void myAction() {
		if(going){
			exploreBehaviour.myAction();
			if(exploreBehaviour.done()){
				going = false;
				explore();
			}
		}
	}

	@Override
	public boolean done() {
		return false;
	}
}
