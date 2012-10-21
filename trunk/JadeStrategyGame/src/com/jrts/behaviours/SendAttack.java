package com.jrts.behaviours;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

import com.jrts.agents.GoalBasedAI;
import com.jrts.agents.Unit;
import com.jrts.agents.Worker;
import com.jrts.environment.Direction;
import com.jrts.messages.GoalLevels;

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
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		unit.sendHit(Direction.random());
	}
}
