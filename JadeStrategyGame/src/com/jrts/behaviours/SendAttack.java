package com.jrts.behaviours;

import jade.core.behaviours.CyclicBehaviour;

import com.jrts.agents.Worker;
import com.jrts.common.GameConfig;
import com.jrts.environment.Direction;

public class SendAttack extends CyclicBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8832334971270701493L;
	private Worker unit;
	
	public SendAttack(Worker unit) {
		super();
		this.unit = unit;
	}

	@Override
	public void action() {
		try {
			Thread.sleep(GameConfig.ATTACKS_SLEEP_TIME);
		} catch (InterruptedException e) {
		}
		unit.sendHit(Direction.random());
	}
}
