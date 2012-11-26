package com.jrts.behaviours;

import jade.core.behaviours.CyclicBehaviour;

import com.jrts.agents.Unit;
import com.jrts.common.AgentStatus;


public class BehaviourWrapper extends CyclicBehaviour {
	
	private static final long serialVersionUID = 1L;
	
	Unit unit;
	UnitBehaviour highPriority;
	UnitBehaviour backgrond;
	
	
	
	public BehaviourWrapper(Unit unit) {
		super();
		this.unit = unit;
	}

	public void wrap(UnitBehaviour b) {
		String newStatus = b.agentStatus;
		if (newStatus != null)
			unit.switchStatus(newStatus);
		if (b.isHighPriority())
			highPriority = b;
		else
			backgrond = b;
		restart();
	}

	@Override
	public void action() {
		if (highPriority == null && backgrond == null) {
			block();
		} else {
			if (highPriority != null) {
				highPriority.action();
				if (highPriority.done())
					restoreBackground();
			} else {
				backgrond.action();
				if (backgrond.done())
					removeBackground();
			}
		}
	}

	private void removeBackground() {
		backgrond = null;
		unit.switchStatus(AgentStatus.FREE);
	}

	private void restoreBackground() {
		highPriority = null;
		if (backgrond != null && backgrond.agentStatus != null)
			unit.switchStatus(backgrond.agentStatus);
		else
			unit.switchStatus(AgentStatus.FREE);
	}

}
