package com.jrts.agents;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.PlatformController;

import java.io.IOException;
import java.util.ArrayList;

import com.jrts.O2Ainterfaces.IUnit;
import com.jrts.common.AgentStatus;
import com.jrts.environment.Position;
import com.jrts.environment.World;
import com.jrts.messages.AggiornaRisorse;

@SuppressWarnings("serial")
public class ResourceAI extends JrtsAgent {
	
	int collectedWood;
	int collectedFood;
	
	ArrayList<AID> workersList = new ArrayList<AID>();
	
	public ResourceAI() {
		super();
	}

	protected void setup(){
		super.setup();
		
		String[] args = (String[]) getArguments();
		if (args != null) {
			setTeamName(args[0]);
		}
		else{
			System.out.println("Needs team's name");
			System.exit(1);
		}
		
		createWorker();
		
		// order someone to cut wood
		addBehaviour(new WakerBehaviour(this, 5000) {
			@Override
			protected void handleElapsedTimeout() {
				assignWoodcutter();
			}
		});
		
		// inform the masterAI about resources 
		addBehaviour(new CyclicBehaviour(this){
			@Override
			public void action() {
				doWait(1000);
				// send a food/wood update message to the masterAi
				ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
				msg.setConversationId(AggiornaRisorse.class.getSimpleName());
				try {
					msg.setContentObject(new AggiornaRisorse(collectedWood, collectedFood));
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
				// listen if a food/wood update message arrives from the resourceAi
				ACLMessage msg = receive(MessageTemplate.MatchConversationId(AggiornaRisorse.class.getSimpleName()));
				if (msg != null) {
					try {
						AggiornaRisorse aggiornamento = (AggiornaRisorse) msg.getContentObject();
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
	}
	
	public AID createWorker(){
		World world = World.getInstance();
		String workerName = getTeamName() + "-worker"+workersList.size();
		Position workerPosition = world.neighPosition(world.getBuilding(getTeamName()));
		if(workerPosition != null){
			//Instantiate the worker
			// get a container controller for creating new agents
			PlatformController container = getContainerController();
			AgentController agentController;
			try {
				Object[] args = {workerPosition, getTeamName(), this};
				agentController = container.createNewAgent(workerName, Worker.class.getName(), args);
				agentController.start();
				IUnit o2a = agentController.getO2AInterface(IUnit.class);
				World.getInstance().addUnit(workerPosition, workerName, o2a);
				
				// keep the worker's ID on a local list
				
				AID workerAID = new AID(workerName, AID.ISLOCALNAME);
				workersList.add( workerAID );
				return workerAID;
			} catch (ControllerException e) {
				e.printStackTrace();
			}
		}
		else{
			System.out.println(getTeamName() + ":Cannot instantiate the worker");
		}
		return null;
	}
	
	public void assignWoodcutter() {
		ServiceDescription sd = new ServiceDescription();
		sd.setType(AgentStatus.FREE);
		DFAgentDescription[] free = search(sd);
		AID worker = (free.length > 0 ? free[0].getName() : createWorker());
		if (worker != null) {
			ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
			msg.setConversationId("order");
			msg.addReceiver(worker);
			msg.setContent(AgentStatus.WOOD_CUTTING);
			send(msg);
		}
	}

	@Override
	protected void updatePerception() {
	}

}
