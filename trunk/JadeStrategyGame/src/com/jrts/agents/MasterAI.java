package com.jrts.agents;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.PlatformController;

import com.jrts.environment.Cell;
import com.jrts.environment.Floor;
import com.jrts.environment.World;

public class MasterAI extends Agent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Floor floorImage;

	protected void setup(){
		
		World world = World.getInstance();
		String teamName = getAID().getLocalName();
		world.addTeam(teamName);

		//create ResourceAI
		// get a container controller for creating new agents
		PlatformController container = getContainerController();
		AgentController resourceAI;
		try {
			String[] arg = new String[1];
			arg[0] = teamName;
			resourceAI = container.createNewAgent(teamName + "-resourceAI", "agents.ResourceAI", arg);
			resourceAI.start();
		} catch (ControllerException e) {
			e.printStackTrace();
		}
		System.out.println(teamName + ":MasterAI setup");
		
		floorImage = world.getFloor().getCopy();
		floorImage.setAll(Cell.UNKNOWN);
		
		//receive perceptions from units
		addBehaviour(new CyclicBehaviour() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void action() {
				ACLMessage msg = receive();
				if (msg != null && msg.getPerformative() == ACLMessage.INFORM) {
					try {
						Object info = msg.getContentObject();
						if (info instanceof Floor)
							floorImage.mergeWith((Floor) info);
					} catch (UnreadableException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
		});
	}
}