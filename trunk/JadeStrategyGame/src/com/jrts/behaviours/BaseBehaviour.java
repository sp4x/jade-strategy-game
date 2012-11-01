package com.jrts.behaviours;

import com.jrts.agents.Unit;

import jade.core.behaviours.Behaviour;

public abstract class BaseBehaviour extends Behaviour {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	Unit unit;
	
	BaseBehaviour(Unit unit) {
		super(unit);
		this.unit = unit;
	}

	public abstract void baseAction();

	@Override
	public void action() {
		if (unit.getHighPriorityBehaviour() == null)
			baseAction();
	}


}
