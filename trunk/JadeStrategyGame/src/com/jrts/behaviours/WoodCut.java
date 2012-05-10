package com.jrts.behaviours;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;

import com.jrts.agents.Worker;
import com.jrts.environment.CellType;
import com.jrts.environment.Position;

public class WoodCut extends OneShotBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public WoodCut(Agent a) {
		super(a);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void action() {
		Worker worker = (Worker) myAgent;
		Position wood = worker.findNearest(CellType.WOOD);
		if (wood != null)
			worker.goThere(wood);
	}

}
