package com.jrts.behaviours;

import java.util.LinkedList;
import java.util.Queue;

import com.jrts.agents.Unit;

public class BehaviourScheduler extends UnitBehaviour {
	
	private static final long serialVersionUID = 1L;
	
	Queue<UnitBehaviour> scheduledBehaviours = new LinkedList<UnitBehaviour>();
	UnitBehaviour current = null;
	Unit unit;

	public BehaviourScheduler(String agentStatus, Unit u) {
		super(agentStatus, false, u);
		this.unit = u;
	}

	@Override
	public void myAction() {
		if (current == null && !scheduledBehaviours.isEmpty()) {
			current = scheduledBehaviours.poll();
		} else if (current.done()) {
			current = null;
		} else {
			current.myAction();
		}
	}

	@Override
	public boolean done() {
		return current == null && scheduledBehaviours.isEmpty();
	}
	
	public void queueBehaviour(UnitBehaviour b) {
		scheduledBehaviours.add(b);
	}

}
