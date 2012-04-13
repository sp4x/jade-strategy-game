package agents;

import gui.MainFrame;

import com.jrts.environment.World;

import jade.core.Agent;

public class MasterAi extends Agent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void setup(){
		String[] args = (String[]) getArguments();
		if (args != null) {
			World.create(Integer.parseInt(args[0]), Integer.parseInt(args[1]), 0.1f);
		}
		World world = World.getInstance();
		String name = getAID().getName();
		world.addTeam(name);
		
		//testing
		world.addUnit("worker", world.getBuilding(name));
		new MainFrame(world.getFloor());
	}
}