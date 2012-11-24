package com.jrts.behaviours;

import java.util.logging.Logger;

import com.jrts.agents.Worker;
import com.jrts.common.AgentStatus;
import com.jrts.environment.Cell;
import com.jrts.environment.CellType;
import com.jrts.environment.Position;
import com.jrts.environment.WorldMap;
import com.jrts.messages.Notification;

public class CollectResources extends UnitBehaviour {

	private static final long serialVersionUID = 1L;
	
	Logger logger = Logger.getLogger(CollectResources.class.getName());
	
	Worker worker;
	CellType resourceToCollect;

	Position resourcePosition;
	Position cityCenter;

	public CollectResources(Worker worker, CellType resource) {
		super(resource == CellType.FOOD ? AgentStatus.FOOD_COLLECTING : AgentStatus.WOOD_CUTTING, false);
		this.worker = worker;
		this.resourceToCollect = resource;
		this.cityCenter = worker.requestCityCenterPosition();
	}

	@Override
	public void myAction() {
		WorldMap map = worker.requestMap();
		if (map == null) 
			return;
		
		// check if there is still some resource to collect in resourcePosition
		if (resourcePosition == null || map.get(resourcePosition).getType() != resourceToCollect) {
//			logger.log(logLevel, "No more " + resourceToCollect + " in " + resourcePosition + "; searching for " + resourceToCollect + " in our worldmap"); 
			// if not try to find an equal resource nearby 
			resourcePosition = worker.findNearest(resourceToCollect);
			// if there isn't an equal resource, then inform the resourceAi and set the status to free 
			if (resourcePosition == null) {
//				logger.log(logLevel, "No " + resourceToCollect + " our worlmap; swith my status to free");
				worker.switchStatus(AgentStatus.FREE);
				// inform the resourceAi that there is no resource of that type in the known world
				worker.sendNotification(Notification.NO_MORE_RESOURCE, resourceToCollect, worker.getResourceAID());
			} else {
//				logger.log(logLevel, "Found " + resourceToCollect + " in " + resourcePosition + "; I'm going there");
				worker.goThere(resourcePosition);
			}
		} else { // there is still resourceToCollect in resourcePosition
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
				// segno questa cella come non raggiungibile e cerco un'altra risorsa uguale
				map.set(resourcePosition, new Cell(CellType.FREE));
				resourcePosition = worker.findNearest(resourceToCollect);
				if (resourcePosition == null) {
					worker.switchStatus(AgentStatus.FREE);
					// inform the resourceAi that there is no resource of that type in the known world
					worker.sendNotification(Notification.NO_MORE_RESOURCE, resourceToCollect, worker.getResourceAID());
				} else {
					worker.goThere(resourcePosition);
				}
			}
		}
	}
	
	
	@Override
	public boolean done() {
		String agentStatus = worker.getStatus();
		if (!agentStatus.equals(AgentStatus.FOOD_COLLECTING) && resourceToCollect == CellType.FOOD)
			return true;
		if (!agentStatus.equals(AgentStatus.WOOD_CUTTING) && resourceToCollect == CellType.WOOD)
			return true;
		return false;
	}

}
