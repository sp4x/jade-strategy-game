package com.jrts.behaviours;

import com.jrts.agents.Unit;
import com.jrts.agents.Worker;
import com.jrts.behaviours.structure.BaseBehaviour;
import com.jrts.common.AgentStatus;
import com.jrts.environment.CellType;
import com.jrts.environment.Position;
import com.jrts.environment.World;

public class CollectResources extends BaseBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Worker worker;
	CellType resourceToCollect;

	Position resourcePosition;
	Position cityCenter;

	public CollectResources(Worker worker, CellType resource) {
		super(false);//low priority
		this.worker = worker;
		this.resourceToCollect = resource;
		this.cityCenter = World.getInstance().getCityCenter(worker.getTeamName());
		this.resourcePosition = worker.findNearest(resourceToCollect);
	}

	public void baseAction() {
		if (worker.getPosition().distance(resourcePosition) == 1) {
			worker.spendTime();
			worker.takeResources(resourcePosition);
		} else if (worker.getPosition().distance(cityCenter) == 1 && worker.knapsackIsFull()) {
			worker.dropResources();
		} else if (worker.knapsackIsFull()) {
			worker.goThere(cityCenter);
		} else {
			resourcePosition = worker.findNearest(resourceToCollect);
			worker.goThere(resourcePosition);
		}
	}
	
	
	@Override
	public boolean done() {
		String agentStatus = ((Unit) myAgent).getStatus();
		if (!agentStatus.equals(AgentStatus.FOOD_COLLECTING) && resourceToCollect == CellType.FOOD)
			return true;
		if (!agentStatus.equals(AgentStatus.WOOD_CUTTING) && resourceToCollect == CellType.WOOD)
			return true;
		return false;
	}

}
