package com.jrts.agents;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import com.jrts.behaviours.UpdateUnitTable;
import com.jrts.common.AgentStatus;
import com.jrts.common.GameConfig;
import com.jrts.common.GoalPriority;
import com.jrts.common.Order;
import com.jrts.common.UnitTable;
import com.jrts.messages.AggiornaRisorse;

@SuppressWarnings("serial")
public class ResourceAI extends GoalBasedAI {

	int workersCounter = 0;

	public ResourceAI() {
		super();
	}

	protected void setup() {
		super.setup();

		// unitFactory.trainUnit(Worker.class);
		// // unitFactory.trainUnit(Worker.class);
		// //
		// // // order someone to cut wood
		// addBehaviour(new WakerBehaviour(this, 5000) {
		// @Override
		// protected void handleElapsedTimeout() {
		// assignWoodcutter();
		// // assignFoodCollector();
		// }
		// });

		// listen for resources update by the workers
		addBehaviour(new CyclicBehaviour(this) {
			@Override
			public void action() {
				ACLMessage msg = receive(MessageTemplate
						.MatchConversationId(AggiornaRisorse.class
								.getSimpleName()));
				if (msg != null) {
					try {
						AggiornaRisorse aggiornamento = (AggiornaRisorse) msg
								.getContentObject();
						int collectedFood = aggiornamento.getFood();
						int collectedWood = aggiornamento.getWood();
						resourcesContainer.addFood(collectedFood);
						resourcesContainer.addWood(collectedWood);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					// if no message is arrived, block the behaviour
					block();
				}
			}
		});

		addBehaviour(new UpdateUnitTable(this, Worker.class));

		// trains and gives order to workers
		addBehaviour(new CyclicBehaviour() {

			@Override
			public void action() {
				if (goalLevels != null) {
					trainWorkers();
					employWorker();
				}
			}
		});

	}

	protected void employWorker() {
		AID freeWorker = unitTable.getFreeUnits();
		if (freeWorker != null) {
			Order order = new Order(extimateResourceToCollect());
			changeAgentStatus(freeWorker, order);
			unitTable.put(freeWorker, order.getOrder());
		} else {
			// logger.info("no free workers");
		}
	}

	protected void trainWorkers() {
		int numWorkers = extimateNumWorkers() - workersCounter;
		for (int i = 0; i < numWorkers; i++) {
			if (resourcesContainer
					.isThereEnoughFood(GameConfig.WORKER_FOOD_COST)
					&& resourcesContainer
							.isThereEnoughWood(GameConfig.WORKER_WOOD_COST)) {

				resourcesContainer
						.removeFood(GameConfig.WORKER_FOOD_COST);
				resourcesContainer
						.removeWood(GameConfig.WORKER_WOOD_COST);
				unitFactory.trainUnit(Worker.class);
				workersCounter++;
			}
		}
	}

	public String extimateResourceToCollect() {
		double foodRatio;
		if (goalLevels.getResources() == GoalPriority.HIGH) {
			foodRatio = 2;
		} else {
			foodRatio = 1;
		}
		double food = (double) resourcesContainer.getFood();
		double wood = (double) resourcesContainer.getWood();
		return foodRatio * food < wood ? AgentStatus.FOOD_COLLECTING
				: AgentStatus.WOOD_CUTTING;
	}

	public int extimateNumWorkers() {
		switch (goalLevels.getResources()) {
		case HIGH:
			return 6;
		case MEDIUM:
			return 4;
		case LOW:
			return 2;
		default:
			return 0;
		}
	}

	@Deprecated
	public boolean assignWoodcutter() {
		AID worker = unitTable.getFreeUnits();
		if (worker != null) {
			Order order = new Order(AgentStatus.WOOD_CUTTING);
			changeAgentStatus(worker, order);
			unitTable.put(worker, order.getOrder());
			return true;
		}
		return false;
	}

	@Deprecated
	public boolean assignFoodCollector() {
		AID worker = unitTable.getFreeUnits();
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
	}

	@Override
	public void onGoalsChanged() {

	}
}
