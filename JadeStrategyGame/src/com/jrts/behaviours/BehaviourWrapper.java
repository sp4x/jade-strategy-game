package com.jrts.behaviours;

import jade.core.behaviours.CyclicBehaviour;


public class BehaviourWrapper extends CyclicBehaviour {
	
	private static final long serialVersionUID = 1L;
	
	UnitBehaviour highPriority;
	UnitBehaviour backgrond;
	
	public void wrap(UnitBehaviour b) {
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
					highPriority = null;
			} else {
				backgrond.action();
				if (backgrond.done())
					backgrond = null;
			}
		}
	}

}
