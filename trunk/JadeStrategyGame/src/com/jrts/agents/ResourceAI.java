package com.jrts.agents;

import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;

import java.util.logging.Level;

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
		logLevel = Level.INFO;
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
		AID worker = null;
		Order order = null;
		String[] orders = extimateOrderPriority();
		int freeWorkers = unitTable.getFreeUnits().size();
		int busyWorkers = unitTable.getBusyUnits().size();
		int neededWorkers = extimateNumWorkers();
		
		//too much busy workers
		if (busyWorkers > neededWorkers) {
			String orderToCancel = orders[1];
			worker = unitTable.getAUnitWithStatus(orderToCancel);
			if (worker == null) {
				orderToCancel = orders[0];
				worker = unitTable.getAUnitWithStatus(orderToCancel);
			}
			order = new Order(AgentStatus.FREE);
		
		//free workers to employ	
		} else if (freeWorkers > 0 && busyWorkers < neededWorkers) {
			worker = unitTable.getAFreeUnit();
			order = new Order(orders[0]);
		
		//reassign workers if needed
		} else {
			int food = resourcesContainer.getFood();
			int wood = resourcesContainer.getWood();
			//too much food and wood needed
			if (food > 2*wood && orders[0].equals(AgentStatus.WOOD_CUTTING))
				worker = unitTable.getAUnitWithStatus(AgentStatus.FOOD_COLLECTING);
			//too much wood and food needed
			else if (wood > 2*food && orders[0].equals(AgentStatus.FOOD_COLLECTING))
				worker = unitTable.getAUnitWithStatus(AgentStatus.WOOD_CUTTING);
			order = new Order(orders[0]);
		}
		
		if (worker != null && order != null) {
			giveOrder(worker, order);
			unitTable.put(worker, order.getOrder());
		}
	}

	public boolean populationLimitReached() {
		int soldiersCounter = getTeamDF().searchByUnitType(Soldier.class).length;
		return workersCounter + soldiersCounter >= GameConfig.POPULATION_LIMIT;
	}

	protected void trainWorkers() {
		//heuristic based on costs and population
		int minFood = GameConfig.WORKER_FOOD_COST*(workersCounter + 1);
		int minWood = GameConfig.WORKER_WOOD_COST*(workersCounter + 1);
		if (resourcesContainer.getFood() >= minFood && resourcesContainer.getWood() >= minWood) {
			if (resourcesContainer
					.isThereEnoughFood(GameConfig.WORKER_FOOD_COST)
					&& resourcesContainer
							.isThereEnoughWood(GameConfig.WORKER_WOOD_COST)
					&& !populationLimitReached()) {

				resourcesContainer.removeFood(GameConfig.WORKER_FOOD_COST);
				resourcesContainer.removeWood(GameConfig.WORKER_WOOD_COST);
				unitFactory.trainUnit(Worker.class);
				workersCounter++;
			}
		}
	}

	public String[] extimateOrderPriority() {
		if (noMoreFood && !noMoreWood)
			return new String[]{AgentStatus.WOOD_CUTTING, AgentStatus.FOOD_COLLECTING};
		if (!noMoreFood && noMoreWood)
			return new String[]{AgentStatus.FOOD_COLLECTING, AgentStatus.WOOD_CUTTING};
		if (noMoreFood && noMoreWood)
			return null;

		double foodRatio;
		if (goalLevels.getResources() == GoalPriority.HIGH) {
			foodRatio = 2;
		} else {
			foodRatio = 1;
		}
		
		int woodCutters = unitTable.getUnitsWithStatus(AgentStatus.WOOD_CUTTING).size();
		int foodCollectors = unitTable.getUnitsWithStatus(AgentStatus.FOOD_COLLECTING).size();

		if (foodRatio * foodCollectors <= woodCutters)
			return new String[]{AgentStatus.FOOD_COLLECTING, AgentStatus.WOOD_CUTTING};
		return new String[]{AgentStatus.WOOD_CUTTING, AgentStatus.FOOD_COLLECTING};
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
			if (p != null)
				noMoreFood = false;
		}
		if (noMoreWood) {
			Position p = requestMap().findNearest(cityCenter, CellType.WOOD);
			if (p != null)
				noMoreWood = false;
		}
		if (!noMoreFood && !noMoreWood)
			sendNotification(Notification.RESOURCES_FOUND, null, getMasterAID());
	}

	@Override
	public void onGoalsChanged() {
	}

	@Override
	protected void handleNotification(Notification n) {
		super.handleNotification(n);

		if (n.getSubject().equals(Notification.NO_MORE_RESOURCE)) {
			CellType resourceType = (CellType) n.getContentObject();
			logger.log(logLevel, getAID().getName() + ": no more " + resourceType
					+ " in our known world..");
			sendNotification(Notification.NO_MORE_RESOURCE, resourceType,
					getMasterAID());
			if (resourceType.equals(CellType.FOOD))
				noMoreFood = true;
			else
				noMoreWood = true;

		} else if (n.getSubject().equals(Notification.RESOURCES_UPDATE)) {
			AggiornaRisorse aggiornamento = (AggiornaRisorse) n
					.getContentObject();
			int collectedFood = aggiornamento.getFood();
			int collectedWood = aggiornamento.getWood();
			resourcesContainer.addFood(collectedFood);
			resourcesContainer.addWood(collectedWood);
		} else if (n.getSubject().equals(Notification.UNIT_DEATH)) {
			workersCounter--;
			sendNotification(Notification.UNIT_DEATH, n.getContentObject(),
					getMasterAID());
		} else if (n.getSubject().equals(Notification.READY_TO_BE_UPGRADED)) {
			workersCounter--;
		}
	}

	@Override
	protected Object handleRequest(String requestSubject) {
		// TODO Auto-generated method stub
		return null;
	}

}
