package com.jrts.behaviours;

import jade.core.behaviours.Behaviour;

import java.util.LinkedList;
import java.util.Queue;

import com.jrts.agents.Unit;

public class BehaviourScheduler extends UnitBehaviour {
	
	Queue<Behaviour> scheduledBehaviours = new LinkedList<Behaviour>();
	Behaviour current = null;
	Unit unit;

	public BehaviourScheduler(String agentStatus, Unit u) {
		super(agentStatus, false, u);
		this.unit = u;
	}

	@Override
	public void myAction() {
		if (current == null && !scheduledBehaviours.isEmpty()) {
			current = scheduledBehaviours.poll();
			unit.addBehaviour(current);
		} else if (current.done()) {
			current = null;
		}
	}

	@Override
	public boolean done() {
		return current == null && scheduledBehaviours.isEmpty();
	}
	
	public void queueBehaviour(Behaviour b) {
		scheduledBehaviours.add(b);
	}

}
