package agents;

import gui.MainFrame;
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
		String[] args = (String[]) getArguments();
		if (args != null) {
			World.create(Integer.parseInt(args[0]), Integer.parseInt(args[1]), 0.1f);
		}
		else{
			System.out.println("Need World's size");
			System.exit(1);
		}
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
			resourceAI = container.createNewAgent("resourceAI", "agents.ResourceAI", arg);
			resourceAI.start();
		} catch (ControllerException e) {
			e.printStackTrace();
		}
		System.out.println(teamName + ":MasterAI setup");
		new MainFrame(world.getFloor());
	}
}