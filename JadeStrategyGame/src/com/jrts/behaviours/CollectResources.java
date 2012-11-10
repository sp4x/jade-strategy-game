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
		if (worker.getPerception() == null) {
			return;
		} 
		boolean nearResource = worker.getPosition().isNextTo(resourcePosition);
		boolean nearCityCenter = worker.getPosition().isNextTo(cityCenter);
		if (nearResource && !worker.knapsackIsFull()) {
			System.out.println("nearResource && !worker.knapsackIsFull()");
			// check if there is still some resource to collect in resourcePosition
			if (worker.getPerception().get(resourcePosition).getType() != resourceToCollect) {
				System.out.println("no resource !!!");
				// if not try to find an equal resource nearby 
				resourcePosition = worker.findNearest(resourceToCollect);
				// if there isn't an equal resource, then inform the resourceAi and set the status to free 
				if (resourcePosition == null) {
					System.out.println("NIENTE QUI VICINO");
					worker.switchStatus(AgentStatus.FREE);
					// TODO inform the resourceAi that there is no resource of that type in the known world
				} else {
					System.out.println("TROVATA RISORSA IN " + resourcePosition);
					worker.goThere(resourcePosition);
				}
			} else {
				System.out.println("RESOURCE HERE");
				worker.spendTime();
				worker.takeResources(resourcePosition);
			}
		} else if (nearCityCenter && worker.knapsackIsFull()) {
			worker.dropResources();
		} else if (worker.knapsackIsFull()) {
			worker.goThere(cityCenter);
		} else if (!worker.knapsackIsFull()) {
			resourcePosition = worker.findNearest(resourceToCollect);
			if (resourcePosition == null) {
				worker.switchStatus(AgentStatus.FREE);
				// TODO inform the resourceAi that there is no resource of that type in the known world
			} else {
				worker.goThere(resourcePosition);
			}
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
