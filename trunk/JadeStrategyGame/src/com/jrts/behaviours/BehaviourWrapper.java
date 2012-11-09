package com.jrts.behaviours;

import jade.core.behaviours.CyclicBehaviour;


public class BehaviourWrapper extends CyclicBehaviour {
	
	UnitBehaviour highPriority;
	UnitBehaviour backgrond;
	
	public void wrap(UnitBehaviour b) {
		if (b.isHighPriority())
			highPriority = b;
		else
			backgrond = b;
	}

	@Override
	public void action() {
		if (highPriority != null) {
			highPriority.action();
			if (highPriority.done())
				highPriority = null;
		} else if (backgrond != null) {
			backgrond.action();
			if (backgrond.done())
				backgrond = null;
		}
	}

}
