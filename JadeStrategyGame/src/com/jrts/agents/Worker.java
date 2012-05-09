package com.jrts.agents;

import com.jrts.common.AgentStatus;
import com.jrts.common.GameConfig;
import com.jrts.environment.Direction;
import com.jrts.environment.Position;
import com.jrts.gui.AttacksManager;

@SuppressWarnings("serial")
public class Worker extends Unit {

	public Worker() {
		super();
	}

	public Worker(Position position) {
		super(position);
	}

	@Override
	protected void setup(){
		super.setup();
		setLife(GameConfig.WORKER_LIFE);
		setSpeed(GameConfig.WORKER_SPEED);
		setForceOfAttack(GameConfig.WORKER_DAMAGES);
		setSight(GameConfig.WORKER_SIGHT);
		
		setStatus(AgentStatus.FREE);
		
	}
	
	private void sendHit(Direction direction) {
		AttacksManager.addHit(getPosition().clone(), direction, GameConfig.WORKER_DAMAGES);
	}
}
;