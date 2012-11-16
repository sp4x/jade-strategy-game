package com.jrts.behaviours;

import com.jrts.agents.Unit;
import com.jrts.common.GameStatistics;

@SuppressWarnings("serial")
public abstract class UnitBehaviour extends JrtsBehaviour{
	
	boolean idle = false;
	final boolean highPriority;

	public UnitBehaviour(boolean highPriority, Unit u) {
		super(u);
		this.highPriority = highPriority;
	}

	protected UnitBehaviour(boolean highPriority) {
		super();
		this.highPriority = highPriority;
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
