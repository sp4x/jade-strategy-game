package com.jrts.agents;

import jade.core.AID;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.PlatformController;

import com.jrts.common.GameConfig;
import com.jrts.environment.Floor;
import com.jrts.environment.Position;
import com.jrts.environment.World;

@SuppressWarnings("serial")
public class MasterAI extends JrtsAgent {
	
	AID resourceAID;

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
	}
	
	@Override
	protected void updatePerception() {
		World world = World.getInstance();
		//the area near the building is always visible
		Position cityCentre = world.getBuilding(getTeam());
		Floor centre = world.getPerception(cityCentre, GameConfig.CITY_CENTRE_SIGHT);
		//TODO maybe do something if an enemy is detected
		updateLocalPerception(centre);
		if (perception.get(cityCentre).getResourceEnergy() <= 0) {
			//TODO do something if the main building has been destroyed
		}
		//wait from perception messages from other agents
		receivePerception();
		//update resourcesAI's perception
		sendPerception(perception, resourceAID);
	}
}