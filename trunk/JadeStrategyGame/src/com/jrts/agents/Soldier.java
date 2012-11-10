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
		
		switchStatus(AgentStatus.FREE_SOLDIER);
		
		//explore();
		//patrol(Direction.UP, PatrolBehaviour.DISTANCE_LITTLE);
		
//		addBehaviour(new SendAttack(this));
	}
	
	public void sendHit(Direction direction) {
		AttacksManager.addHit(getPosition().clone(), direction, GameConfig.SOLDIER_DAMAGES);
	}

	/**
	 * 
	 * @param direction must be one between TOP, RIGHT, DOWN, LEFT
	 */
	public void patrol(Direction direction, int distance) {
		/*
		Direction direction = Direction.UP;
		int distance = PatrolBehaviour.DISTANCE_LITTLE;
		*/
		addBehaviour(new PatrolBehaviour(this, direction, distance));
		switchStatus(AgentStatus.PATROLING);
	}

	public void explore() {
		logger.warning("STARTING EXPLORING");
		addBehaviour(new ExploreBehaviour(this));
		switchStatus(AgentStatus.EXPLORING);
	}
	
	@Override
	public CellType getType() {
		return CellType.SOLDIER;
	}
}
