package com.jrts.agents;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import com.jrts.behaviours.UpdateUnitTable;
import com.jrts.common.AgentStatus;
import com.jrts.common.GameConfig;
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

//		 unitFactory.trainUnit(Worker.class);
//		// unitFactory.trainUnit(Worker.class);
//		//
//		// // order someone to cut wood
//		 addBehaviour(new WakerBehaviour(this, 5000) {
//		 @Override
//			protected void handleElapsedTimeout() {
//				assignWoodcutter();
////				assignFoodCollector();
//			}
//		});

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

		addBehaviour(new UpdateUnitTable(this, Worker.class));

		// trains and gives order to workers
		addBehaviour(new CyclicBehaviour() {

			@Override
			public void action() {
				int numWorkers = 3 - workersCounter; 
				for (int i = 0; i < numWorkers; i++) {
					if (resourcesContainer.isThereEnoughFood(GameConfig.WORKER_FOOD_COST) && 
							resourcesContainer.isThereEnoughWood(GameConfig.WORKER_WOOD_COST) ) {
						
						resourcesContainer.removeFood(GameConfig.WORKER_FOOD_COST);
						resourcesContainer.removeWood(GameConfig.WORKER_WOOD_COST);
						unitFactory.trainUnit(Worker.class);
						workersCounter++;
					}
				}
				AID freeWorker = unitTable.getFreeUnits();
				if (freeWorker != null) {
					/*
					String newStatus = (resourcesContainer.getFood() > resourcesContainer.getWood() ? AgentStatus.WOOD_CUTTING
							: AgentStatus.FOOD_COLLECTING);
					changeAgentStatus(freeWorker, newStatus);
					*/
					Order order = (resourcesContainer.getFood() > resourcesContainer.getWood() ? new Order(AgentStatus.WOOD_CUTTING)
							: new Order(AgentStatus.FOOD_COLLECTING));
					changeAgentStatus(freeWorker, order);
					unitTable.put(freeWorker, order.getOrder());
				} else {
					//logger.info("no free workers");
				}
			}
		});

	}

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
