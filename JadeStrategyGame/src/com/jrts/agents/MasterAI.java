package com.jrts.agents;

import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.PlatformController;

import com.jrts.common.GameConfig;
import com.jrts.environment.Floor;
import com.jrts.environment.Position;
import com.jrts.environment.World;

@SuppressWarnings("serial")
public class MasterAI extends JrtsAgent {	

	protected void setup(){
		
		World world = World.getInstance();
		team = getAID().getLocalName();
		world.addTeam(team);

		//create ResourceAI
		// get a container controller for creating new agents
		PlatformController container = getContainerController();
		AgentController resourceAI;
		try {
			String[] arg = new String[1];
			arg[0] = team;
			resourceAI = container.createNewAgent(team + "-resourceAI", "com.jrts.agents.ResourceAI", arg);
			resourceAI.start();
		} catch (ControllerException e) {
			e.printStackTrace();
		}
		System.out.println(team + ":MasterAI setup");
		
		addBehaviour(new TickerBehaviour(this, 1000) {

			@Override
			protected void onTick() {
				updatePerception();
			}
		});
	}
	
	@Override
	protected void updatePerception() {
		World world = World.getInstance();
		Position cityCentre = world.getBuilding(team);
		System.out.println(cityCentre);
		Floor centre = world.getPerception(cityCentre, GameConfig.CITY_CENTRE_SIGHT);
		updateLocalPerception(centre);
		if (perception.get(cityCentre).getEnergy() <= 0) {
			//TODO do something
		}
		
		ACLMessage msg = receive();
		if (msg != null && msg.getPerformative() == ACLMessage.INFORM) {
			try {
				Object info = msg.getContentObject();
				if (info instanceof Floor)
					perception.mergeWith((Floor) info);
			} catch (UnreadableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}