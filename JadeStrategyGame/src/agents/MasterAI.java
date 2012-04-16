package agents;

import jade.core.Agent;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.PlatformController;

import com.jrts.environment.World;

public class MasterAI extends Agent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
	}
}