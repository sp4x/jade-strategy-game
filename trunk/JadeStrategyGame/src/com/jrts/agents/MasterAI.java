package com.jrts.agents;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.PlatformController;

import com.jrts.O2Ainterfaces.Team;
import com.jrts.common.GameConfig;
import com.jrts.environment.Floor;
import com.jrts.environment.Position;
import com.jrts.environment.World;


@SuppressWarnings("serial")
public class MasterAI extends JrtsAgent implements Team {
	
	AID resourceAID;
	
	int food, wood;
	
	public MasterAI() {
		registerO2AInterface(Team.class, this);
	}

	protected void setup(){
		super.setup();
		World world = World.getInstance();
		masterAID = getAID();
		world.addTeam(getTeam());
		
		resourceAID = new AID(getTeam() + "-resourceAI", AID.ISLOCALNAME);

		//create ResourceAI
		// get a container controller for creating new agents
		PlatformController container = getContainerController();
		AgentController resourceAI, df;
		try {
			df = container.createNewAgent(getTeamDF().getLocalName(), "jade.domain.df", null);
			df.start();
			String[] arg = new String[1];
			arg[0] = getTeam();
			resourceAI = container.createNewAgent(resourceAID.getLocalName(), "com.jrts.agents.ResourceAI", arg);
			resourceAI.start();
		} catch (ControllerException e) {
			e.printStackTrace();
		}
		
		addBehaviour(new CyclicBehaviour() {
			@Override
			public void action() {
				// listen if a food/wood update message arrives from the resourceAi
				ACLMessage msg = receive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
				if (msg != null) {
					String mess = msg.getContent();
					if (mess.matches("food: \\d+ wood: \\d+")) {
						String[] array = mess.split("\\s");
						food = Integer.parseInt(array[1]);
						wood = Integer.parseInt(array[3]);
					}
				} else {
					// if no message is arrived, block the behaviour
					block();
				}
			}
		});
	}
	
	@Override
	protected void updatePerception() {
		World world = World.getInstance();
		//the area near the building is always visible
		Position citycenter = world.getBuilding(getTeam());
		Floor center = world.getPerception(citycenter, GameConfig.CITY_CENTER_SIGHT);
		//TODO maybe do something if an enemy is detected
		updateLocalPerception(center);
		if (perception.get(citycenter).getResourceEnergy() <= 0) {
			//TODO do something if the main building has been destroyed
		}
		//wait from perception messages from other agents
		receivePerception();
		//update resourcesAI's perception
		sendPerception(perception, resourceAID);
	}

	/** O2A methods (for use in non-agent java objects) */
	
	@Override
	public int getFood() {
		return 102;
	}

	@Override
	public int getWood() {
		return 203;
	}

	@Override
	public String getTeamName() {
		return getLocalName();
	}
}