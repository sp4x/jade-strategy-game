package com.jrts.behaviours.structure;

import jade.core.behaviours.Behaviour;

@SuppressWarnings("serial")
public abstract class BaseBehaviour extends Behaviour {
	
	boolean idle = false;
	final boolean highPriority;
	
	protected BaseBehaviour(boolean highPriority) {
		this.highPriority = highPriority;
	}

	public abstract void baseAction();

	@Override
	public final void action() {
		if (!idle)
			baseAction();
	}

	public boolean isIdle() {
		return idle;
	}

	public void setIdle(boolean idle) {
		this.idle = idle;
	}

	public boolean isHighPriority() {
		return highPriority;
	}
}
