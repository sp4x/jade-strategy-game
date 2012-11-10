package com.jrts.behaviours;

import com.jrts.agents.Unit;
import com.jrts.agents.Worker;
import com.jrts.common.AgentStatus;
import com.jrts.environment.CellType;
import com.jrts.environment.Position;
import com.jrts.environment.World;

public class CollectResources extends UnitBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Worker worker;
	CellType resourceToCollect;

	Position resourcePosition;
	Position cityCenter;

	public CollectResources(Worker worker, CellType resource) {
		super(false, worker);
		this.worker = worker;
		this.resourceToCollect = resource;
		this.cityCenter = World.getInstance().getCityCenter(worker.getTeamName());
		this.resourcePosition = worker.findNearest(resourceToCollect);
	}

	public void action() {
		boolean nearResource = worker.getPosition().isNextTo(resourcePosition);
		boolean nearCityCenter = worker.getPosition().isNextTo(cityCenter);
		if (nearResource && !worker.knapsackIsFull()) {
			worker.spendTime();
			worker.takeResources(resourcePosition);
		} else if (nearCityCenter && worker.knapsackIsFull()) {
			worker.dropResources();
		} else if (worker.knapsackIsFull()) {
			worker.goThere(cityCenter);
		} else if (!worker.knapsackIsFull()) {
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
