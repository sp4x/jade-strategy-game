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
	
	private Level logLevel = Level.FINE;

	int workersCounter = 0;

	boolean unavailableFood = false, unavailableWood = false;

	public ResourceAI() {
		super();
		logLevel = Level.FINE;
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
		int neededWorkers = goalLevels.extimateResourceUnits();

		// too much busy workers
		if(orders == null){
			worker = unitTable.getAFreeUnit();
			order = new Order(AgentStatus.FREE);
			
		} else if (busyWorkers > neededWorkers) {
			String orderToCancel = orders[1];
			worker = unitTable.getAUnitWithStatus(orderToCancel);
			if (worker == null) {
				orderToCancel = orders[0];
				worker = unitTable.getAUnitWithStatus(orderToCancel);
			}
			order = new Order(AgentStatus.FREE);

			// free workers to employ
		} else if (freeWorkers > 0 && busyWorkers < neededWorkers) {
			worker = unitTable.getAFreeUnit();
			order = new Order(orders[0]);

			// reassign workers if needed
		} else {
			int food = resourcesContainer.getFood();
			int wood = resourcesContainer.getWood();
			// too much food and wood needed
			if (food > 2 * wood && orders[0].equals(AgentStatus.WOOD_CUTTING))
				worker = unitTable
						.getAUnitWithStatus(AgentStatus.FOOD_COLLECTING);
			// too much wood and food needed
			else if (wood > 2 * food
					&& orders[0].equals(AgentStatus.FOOD_COLLECTING))
				worker = unitTable.getAUnitWithStatus(AgentStatus.WOOD_CUTTING);
			order = new Order(orders[0]);
		}

		if (worker != null && order != null) {
			giveOrder(worker, order);
		}
	}

	

	protected void trainWorkers() {
		if(unitFactory.getQueueWorkerCount() > 1) return;
		
		// heuristic based on costs and population
		int population = getTeamDF().countUnits() - unitFactory.getQueuedUnitsCount();
		int neededUnits = goalLevels.extimateNeededUnits() - population;
		boolean train = neededUnits > 0
				&& population < GameConfig.POPULATION_LIMIT
				&& unitFactory.getQueueWorkerCount() == 0
				&& resourcesContainer.isThereEnoughFood(GameConfig.WORKER_FOOD_COST)
				&& resourcesContainer.isThereEnoughWood(GameConfig.WORKER_WOOD_COST);

		if (train) {
			resourcesContainer.removeFood(GameConfig.WORKER_FOOD_COST);
			resourcesContainer.removeWood(GameConfig.WORKER_WOOD_COST);
			unitFactory.trainUnit(Worker.class);
			workersCounter++;
		}
	}

	public String[] extimateOrderPriority() {
		if (unavailableFood && !unavailableWood)
			return new String[] { AgentStatus.WOOD_CUTTING,
					AgentStatus.FOOD_COLLECTING };
		if (!unavailableFood && unavailableWood)
			return new String[] { AgentStatus.FOOD_COLLECTING,
					AgentStatus.WOOD_CUTTING };
		if (unavailableFood && unavailableWood)
			return null;

		double foodRatio = 1, woodRatio = 1;
		if (goalLevels.getResources() == GoalPriority.HIGH)
			foodRatio = 3;
		

		int woodCutters = unitTable
				.getUnitsWithStatus(AgentStatus.WOOD_CUTTING).size();
		int foodCollectors = unitTable.getUnitsWithStatus(
				AgentStatus.FOOD_COLLECTING).size();

		//foodCollectors : woodCutters = foodRatio : woodRatio
		if (foodCollectors < foodRatio * (double) woodCutters / woodRatio)
			return new String[] { AgentStatus.FOOD_COLLECTING,
					AgentStatus.WOOD_CUTTING };
		return new String[] { AgentStatus.WOOD_CUTTING,
				AgentStatus.FOOD_COLLECTING };
	}

	public UnitTable getWorkersMap() {
		return unitTable;
	}

	@Override
	protected void updatePerception() {
		if (unavailableFood) {
			Position p = requestMap().findNearest(myCityCenter, CellType.FOOD);
			if (p != null)
				unavailableFood = false;
		}
		if (unavailableWood) {
			Position p = requestMap().findNearest(myCityCenter, CellType.WOOD);
			if (p != null)
				unavailableWood = false;
		}
		if (!unavailableFood && !unavailableWood)
			sendNotification(Notification.RESOURCES_FOUND, null, getMasterAID());
	}

	@Override
	protected void handleNotification(Notification n) {
		super.handleNotification(n);

		if (n.getSubject().equals(Notification.UNAVAILABLE_RESOURCE)) {
			CellType resourceType = (CellType) n.getContentObject();
			logger.log(logLevel, getAID().getName() + ": no more "
					+ resourceType + " in our known world..");
			sendNotification(Notification.UNAVAILABLE_RESOURCE, resourceType,
					getMasterAID());
			if (resourceType.equals(CellType.FOOD))
				unavailableFood = true;
			else
				unavailableWood = true;

		} else if (n.getSubject().equals(Notification.RESOURCES_UPDATE)) {
			AggiornaRisorse aggiornamento = (AggiornaRisorse) n
					.getContentObject();
			int collectedFood = aggiornamento.getFood();
			int collectedWood = aggiornamento.getWood();
			resourcesContainer.addFood(collectedFood);
			resourcesContainer.addWood(collectedWood);
		} else if (n.getSubject().equals(Notification.UNIT_DEATH)) {
			workersCounter--;
			sendNotification(Notification.UNIT_DEATH, n.getContentObject(), getMasterAID());
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
