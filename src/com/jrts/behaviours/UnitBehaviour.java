package com.jrts.behaviours;

import jade.core.behaviours.Behaviour;

import com.jrts.agents.Unit;

@SuppressWarnings("serial")
public abstract class UnitBehaviour extends Behaviour {
	
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
}
