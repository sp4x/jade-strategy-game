package com.jrts.behaviours;

import com.jrts.agents.Unit;
import com.jrts.common.GameStatistics;

@SuppressWarnings("serial")
public abstract class UnitBehaviour extends JrtsBehaviour{
	
	final String agentStatus;
	final boolean highPriority;

	public UnitBehaviour(String agentStatus, boolean highPriority, Unit u) {
		super(u);
		this.agentStatus = agentStatus;
		this.highPriority = highPriority;
	}

	protected UnitBehaviour(String agentStatus, boolean highPriority) {
		this(agentStatus, highPriority, null);
	}

	public boolean isHighPriority() {
		return highPriority;
	}
	
	@Override
	public void unitAction(){
		GameStatistics.increaseCounter();
		myAction();
	}
	
	public abstract void myAction();
}
