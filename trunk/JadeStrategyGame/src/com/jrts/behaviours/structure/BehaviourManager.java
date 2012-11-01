package com.jrts.behaviours.structure;

import com.jrts.agents.Unit;

public class BehaviourManager {
	
	Unit unit;
	BaseBehaviour lowPriority = null;
	BaseBehaviour highPriority = null;
	
	public BehaviourManager(Unit unit) {
		this.unit = unit;
	}
	
	public void refresh(){
		if(lowPriority != null && lowPriority.done())
			lowPriority = null;
		if(highPriority != null && highPriority.done()){
			highPriority = null;
			if(lowPriority != null)
				lowPriority.setIdle(false);
		}
	}
	
	public void addBehaviour(BaseBehaviour newBehaviour){
		if (newBehaviour.isHighPriority()) {
			if (highPriority != null)
				unit.removeBehaviour(getHighPriorityBehaviour());
			highPriority = (BaseBehaviour) newBehaviour;
		} else {
			lowPriority = newBehaviour;
			if (getHighPriorityBehaviour() != null)
				lowPriority.setIdle(true);
		}
	}

	public BaseBehaviour getLowPriorityBehaviour() {
		return lowPriority;
	}
	
	public BaseBehaviour getHighPriorityBehaviour() {
		return highPriority;
	}
}
