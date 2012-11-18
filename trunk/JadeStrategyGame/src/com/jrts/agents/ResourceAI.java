package com.jrts.agents;

import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;

import com.jrts.behaviours.UpdateUnitTable;
import com.jrts.common.AgentStatus;
import com.jrts.common.GameConfig;
import com.jrts.common.GoalPriority;
import com.jrts.common.UnitTable;
import com.jrts.environment.CellType;
import com.jrts.environment.Position;
import com.jrts.messages.AggiornaRisorse;
import com.jrts.messages.Notification;
import com.jrts.messages.Order;

@SuppressWarnings("serial")
public class ResourceAI extends GoalBasedAI {

	int workersCounter = 0;

	boolean noMoreFood = false, noMoreWood = false;
	
	public ResourceAI() {
		super();
	}

	protected void setup() {
		super.setup();

		addBehaviour(new UpdateUnitTable(this, Worker.class));

		// trains and gives order to workers
		addBehaviour(new TickerBehaviour(this, 1000) {
			@Override
			protected void onTick() {
				if (goalLevels != null) {
					trainWorkers();
					employWorker();
				}
			}
		});
	}

	protected void employWorker() {
		AID freeWorker = unitTable.getAFreeUnit();
		if (freeWorker != null) {
			String orderString = extimateResourceToCollect();
			if (orderString != null ) {
				Order order = new Order(orderString);
				giveOrder(freeWorker, order);
				unitTable.put(freeWorker, order.getOrder());
			}
		} else {
			// logger.info("no free workers");
		}
	}

	protected void trainWorkers() {
		int numWorkers = extimateNumWorkers() - workersCounter;
		for (int i = 0; i < numWorkers; i++) {
			if (resourcesContainer.isThereEnoughFood(GameConfig.WORKER_FOOD_COST) && 
					resourcesContainer.isThereEnoughWood(GameConfig.WORKER_WOOD_COST)) {

				resourcesContainer.removeFood(GameConfig.WORKER_FOOD_COST);
				resourcesContainer.removeWood(GameConfig.WORKER_WOOD_COST);
				unitFactory.trainUnit(Worker.class);
				workersCounter++;
			}
		}
	}

	public String extimateResourceToCollect() {
		if (noMoreFood && !noMoreWood) 
			return AgentStatus.WOOD_CUTTING;
		if (!noMoreFood && noMoreWood) 
			return AgentStatus.FOOD_COLLECTING;
		if (noMoreFood && noMoreWood) 
			return null;
		
		double foodRatio;
		if (goalLevels.getResources() == GoalPriority.HIGH) {
			foodRatio = 2;
		} else {
			foodRatio = 1;
		}
		double food = (double) resourcesContainer.getFood();
		double wood = (double) resourcesContainer.getWood();

		return foodRatio * food < wood ? AgentStatus.FOOD_COLLECTING : AgentStatus.WOOD_CUTTING;
	}

	public int extimateNumWorkers() {
		switch (goalLevels.getResources()) {
		case HIGH:
			return 4;
		case MEDIUM:
			return 3;
		case LOW:
			return 2;
		default:
			return 0;
		}
	}

	@Deprecated
	public boolean assignWoodcutter() {
		AID worker = unitTable.getAFreeUnit();
		if (worker != null) {
			Order order = new Order(AgentStatus.WOOD_CUTTING);
			giveOrder(worker, order);
			unitTable.put(worker, order.getOrder());
			return true;
		}
		return false;
	}

	@Deprecated
	public boolean assignFoodCollector() {
		AID worker = unitTable.getAFreeUnit();
		if (worker != null) {
			Order order = new Order(AgentStatus.FOOD_COLLECTING);
			unitTable.put(worker, order.getOrder());
			return true;
		}
		return false;
	}

	public UnitTable getWorkersMap() {
		return unitTable;
	}

	@Override
	protected void updatePerception() {
		if (noMoreFood) {
			Position p = requestMap().findNearest(cityCenter, CellType.FOOD);
			if (p != null) noMoreFood = false;
		}
		if (noMoreWood) {
			Position p = requestMap().findNearest(cityCenter, CellType.WOOD);
			if (p != null) noMoreWood = false;
		}
	}

	@Override
	public void onGoalsChanged() {

	}

	@Override
	protected void handleNotification(Notification n) {
		super.handleNotification(n);
		
		if (n.getSubject().equals(Notification.NO_MORE_RESOURCE)) {
			CellType resourceType = (CellType) n.getContentObject();
			logger.info(getAID().getName() + ": no more " + resourceType + " in our known world..");
			sendNotification(Notification.NO_MORE_RESOURCE, resourceType, getMasterAID());
			if (resourceType.equals(CellType.FOOD))
				noMoreFood = true;
			else
				noMoreWood = true;
			
		} else if (n.getSubject().equals(Notification.RESOURCES_UPDATE)) {
			AggiornaRisorse aggiornamento = (AggiornaRisorse) n.getContentObject();
			int collectedFood = aggiornamento.getFood();
			int collectedWood = aggiornamento.getWood();
			resourcesContainer.addFood(collectedFood);
			resourcesContainer.addWood(collectedWood);
		}
	}

	@Override
	protected Object handleRequest(String requestSubject) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
