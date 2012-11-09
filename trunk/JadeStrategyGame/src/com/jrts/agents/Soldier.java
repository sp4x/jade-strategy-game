package com.jrts.agents;

import com.jrts.behaviours.ExploreBehaviour;
import com.jrts.behaviours.PatrolBehaviour;
import com.jrts.common.AgentStatus;
import com.jrts.common.GameConfig;
import com.jrts.environment.CellType;
import com.jrts.environment.Direction;
import com.jrts.environment.Position;
import com.jrts.logic.AttacksManager;

@SuppressWarnings("serial")
public class Soldier extends Unit {
	
	int knapsack = 0;
	CellType resourceCarried;

	public Soldier() {
		this(null, null);
	}


	public Soldier(String id, Position position) {
		super(id, position);
		setLife(GameConfig.SOLDIER_LIFE);
		setSpeed(GameConfig.SOLDIER_SPEED);
		setForceOfAttack(GameConfig.SOLDIER_DAMAGES);
		setSight(GameConfig.SOLDIER_SIGHT);
	}


	@Override
	protected void setup(){
		super.setup();
		
		switchStatus(AgentStatus.FREE);
		
		explore();
		
//		addBehaviour(new SendAttack(this));
	}
	
	public void sendHit(Direction direction) {
		AttacksManager.addHit(getPosition().clone(), direction, GameConfig.SOLDIER_DAMAGES);
	}

	/**
	 * 
	 * @param d must be one between TOP, RIGHT, DOWN, LEFT
	 */
	public void patrol(Direction d) {
		//TODO: switchStatus(AgentStatus.);
		addBehaviour(new PatrolBehaviour(this, d));
	}

	public void explore() {
		logger.warning("STARTING EXPLORING");
		addBehaviour(new ExploreBehaviour(this));
	}
	
	@Override
	public CellType getType() {
		return CellType.SOLDIER;
	}
}
