package agents;

import gui.MainFrame;
import jade.core.Agent;

import com.jrts.environment.World;

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
		else{
			System.out.println("Need World's size");
			System.exit(1);
		}
		World world = World.getInstance();
		String name = getAID().getName();
		world.addTeam(name);
		
		//testing
		world.addUnit("worker", world.getBuilding(name));
		new MainFrame(world.getFloor());
	}
}