package com.jrts.agents;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import com.jrts.behaviours.UpdateWorkersMap;
import com.jrts.common.AgentStatus;
import com.jrts.common.GameConfig;
import com.jrts.common.WorkersMap;
import com.jrts.messages.AggiornaRisorse;

@SuppressWarnings("serial")
public class ResourceAI extends GoalBasedAI {

	WorkersMap workersMap = new WorkersMap();
	int workersCounter = 0;

	public ResourceAI() {
		super();
	}

	protected void setup() {
		super.setup();

		// unitFactory.trainUnit(Worker.class);
		// unitFactory.trainUnit(Worker.class);
		//
		// // order someone to cut wood
		// addBehaviour(new WakerBehaviour(this, 15000) {
		// @Override
		// protected void handleElapsedTimeout() {
		// assignWoodcutter();
		// assignFoodCollector();
		// }
		// });

		// listen for resources update by the workers
		addBehaviour(new CyclicBehaviour(this) {
			@Override
			public void action() {
				ACLMessage msg = receive(MessageTemplate.MatchConversationId(AggiornaRisorse.class.getSimpleName()));
				if (msg != null) {
					try {
						AggiornaRisorse aggiornamento = (AggiornaRisorse) msg.getContentObject();
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

		addBehaviour(new UpdateWorkersMap(this));

		// trains and gives order to workers
		addBehaviour(new CyclicBehaviour() {

			@Override
			public void action() {
				int numWorkers = (resourcesContainer.getFood() + resourcesContainer.getWood()) / 50 - workersCounter + 1;
				for (int i = 0; i < numWorkers; i++) {
					if (resourcesContainer.isThereEnoughFood(GameConfig.WORKER_FOOD_COST) && 
							resourcesContainer.isThereEnoughWood(GameConfig.WORKER_WOOD_COST) ) {
						
						resourcesContainer.removeFood(GameConfig.WORKER_FOOD_COST);
						resourcesContainer.removeWood(GameConfig.WORKER_WOOD_COST);
						unitFactory.trainUnit(Worker.class);
						workersCounter++;
					}
				}
				AID freeWorker = workersMap.getFreeWorker();
				if (freeWorker != null) {
					String newStatus = (resourcesContainer.getFood() > resourcesContainer.getWood() ? AgentStatus.WOOD_CUTTING
							: AgentStatus.FOOD_COLLECTING);
					changeAgentStatus(freeWorker, newStatus);
					workersMap.put(freeWorker, newStatus);
				} else {
					//logger.info("no free workers");
				}
			}
		});

	}

	public boolean assignWoodcutter() {
		AID worker = workersMap.getFreeWorker();
		if (worker != null) {
			changeAgentStatus(worker, AgentStatus.WOOD_CUTTING);
			workersMap.put(worker, AgentStatus.WOOD_CUTTING);
			return true;
		}
		return false;
	}

	public boolean assignFoodCollector() {
		AID worker = workersMap.getFreeWorker();
		if (worker != null) {
			changeAgentStatus(worker, AgentStatus.FOOD_COLLECTING);
			workersMap.put(worker, AgentStatus.FOOD_COLLECTING);
			return true;
		}
		return false;
	}

	public WorkersMap getWorkersMap() {
		return workersMap;
	}

	@Override
	protected void updatePerception() {
	}

	@Override
	public void onGoalsChanged() {

	}
}
