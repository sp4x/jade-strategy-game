package com.jrts.agents;

import java.io.IOException;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.PlatformController;

import com.jrts.O2Ainterfaces.Team;
import com.jrts.common.GameConfig;
import com.jrts.environment.Perception;
import com.jrts.environment.Position;
import com.jrts.environment.World;
import com.jrts.environment.WorldMap;
import com.jrts.messages.AggiornaRisorse;


@SuppressWarnings("serial")
public class MasterAI extends JrtsAgent implements Team {
	
	AID resourceAID;
	
	WorldMap worldMap;
	
	int food, wood;
	
	public MasterAI() {
		registerO2AInterface(Team.class, this);
	}

	protected void setup(){
		super.setup();
		World world = World.getInstance();
		worldMap = new WorldMap(world.getRows(), world.getCols());
		setTeamName(getAID().getLocalName());
		world.addTeam(getTeamName());
		
		resourceAID = new AID(getTeamName() + "-resourceAI", AID.ISLOCALNAME);

		//create ResourceAI
		// get a container controller for creating new agents
		PlatformController container = getContainerController();
		AgentController resourceAI, df;
		try {
			df = container.createNewAgent(getTeamDF().getLocalName(), "jade.domain.df", null);
			df.start();
			String[] arg = new String[1];
			arg[0] = getTeamName();
			resourceAI = container.createNewAgent(resourceAID.getLocalName(), ResourceAI.class.getName(), arg);
			resourceAI.start();
		} catch (ControllerException e) {
			e.printStackTrace();
		}
		
		// listen if a food/wood update message arrives from the resourceAi
		addBehaviour(new CyclicBehaviour() {
			@Override
			public void action() {
				ACLMessage msg = receive(MessageTemplate.MatchConversationId(AggiornaRisorse.class.getSimpleName()));
				if (msg != null) {
					try {
						AggiornaRisorse aggiornamento = (AggiornaRisorse) msg.getContentObject();
						food = aggiornamento.getFood();
						wood = aggiornamento.getWood();
					} catch (UnreadableException e) {
						e.printStackTrace();
					}
				} else {
					block();
				}
			}
		});
		
		// listen for world map request and answer
		addBehaviour(new CyclicBehaviour() {
			@Override
			public void action() {
				MessageTemplate mt = MessageTemplate.MatchConversationId(WorldMap.class.getSimpleName());
				ACLMessage msg = receive(mt);
				if (msg != null) {
					ACLMessage reply = msg.createReply();
					try {
						reply.setContentObject(worldMap);
					} catch (IOException e) {
						e.printStackTrace();
					}
//					System.out.println("replying");
					send(reply);
				} else {
					block();
				}
			}
		});
	}
	
	@Override
	protected void updatePerception() {
		World world = World.getInstance();
		//the area near the building is always visible
		Position citycenter = world.getBuilding(getTeamName());
		Perception center = world.getPerception(citycenter, GameConfig.CITY_CENTER_SIGHT);
		//TODO maybe do something if an enemy is detected
		//TODO check if the main building has been destroyed
		worldMap.update(center);
		//wait from perception messages from other agents
		receivePerceptions();
	}
	
	public void receivePerceptions() {
		MessageTemplate mt = MessageTemplate.MatchConversationId(Perception.class.getSimpleName());
		ACLMessage msg = receive(mt);
		while (msg != null) {
			try {
				Perception info = (Perception) msg.getContentObject();
				worldMap.update(info);
			} catch (UnreadableException e) {
				e.printStackTrace();
			}
			msg = receive(mt);
		}
	}

	/** O2A methods (for use in non-agent java objects) */
	
	@Override
	public int getFood() {
		return food;
	}

	@Override
	public int getWood() {
		return wood;
	}

	@Override
	public String getTeamName() {
		return getLocalName();
	}
}