package com.jrts.behaviours;

import com.jrts.agents.Unit;
import com.jrts.environment.Position;
import com.jrts.gui.AttacksManager;

import jade.core.behaviours.CyclicBehaviour;

@SuppressWarnings("serial")
public class CheckReceivedAttacks extends CyclicBehaviour {

	public CheckReceivedAttacks(Unit unit) {
		super(unit);
	}

	@SuppressWarnings("unused")
	@Override
	public void action() {
		Unit agent = (Unit) myAgent;
		Position p = agent.getPosition();
		int damage = AttacksManager.getDamagesFor(agent.getLocalName());
		agent.decreaseLife(damage);
//		System.out.println("Agent " + agent.getLocalName() + " colpito. Life: " + agent.getLife());
	}

}
