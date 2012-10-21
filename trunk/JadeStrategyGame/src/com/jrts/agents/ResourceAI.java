package com.jrts.agents;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

import java.io.IOException;
import java.util.ArrayList;

import com.jrts.behaviours.UpdateWorkersMap;
import com.jrts.common.AgentStatus;
import com.jrts.common.WorkersMap;
import com.jrts.messages.AggiornaRisorse;

@SuppressWarnings("serial")
public class ResourceAI extends GoalBasedAI {

	int collectedWood;
	int collectedFood;

	WorkersMap workersMap = new WorkersMap();

	public ResourceAI() {
		super();
	}

	protected void setup() {
		super.setup();

		unitFactory.trainUnit(Worker.class);
		unitFactory.trainUnit(Worker.class);

		// order someone to cut wood
		addBehaviour(new WakerBehaviour(this, 15000) {
			@Override
			protected void handleElapsedTimeout() {
				assignWoodcutter();
				assignFoodCollector();
			}
		});

		// inform the masterAI about resources
		addBehaviour(new CyclicBehaviour(this) {
			@Override
			public void action() {
				doWait(1000);
				// send a food/wood update message to the masterAi
				ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
				msg.setConversationId(AggiornaRisorse.class.getSimpleName());
				try {
					msg.setContentObject(new AggiornaRisorse(collectedWood,
							collectedFood));
				} catch (IOException e) {
					e.printStackTrace();
				}

				msg.addReceiver(new AID(getTeamName(), AID.ISLOCALNAME));

				send(msg);
			}
		});

		// listen for resources update by the workers
		addBehaviour(new CyclicBehaviour(this) {
			@Override
			public void action() {
				// listen if a food/wood update message arrives from the
				// resourceAi
				ACLMessage msg = receive(MessageTemplate
						.MatchConversationId(AggiornaRisorse.class
								.getSimpleName()));
				if (msg != null) {
					try {
						AggiornaRisorse aggiornamento = (AggiornaRisorse) msg
								.getContentObject();
						collectedFood += aggiornamento.getFood();
						collectedWood += aggiornamento.getWood();
					} catch (UnreadableException e) {
						e.printStackTrace();
					}
				} else {
					// if no message is arrived, block the behaviour
					block();
				}
			}
		});
		
		addBehaviour(new UpdateWorkersMap(this));

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
}
