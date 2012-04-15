package agents;

import java.util.ArrayList;

import jade.core.AID;
import jade.core.Agent;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.PlatformController;

import com.jrts.environment.Position;
import com.jrts.environment.World;

public class ResourceAI extends Agent {
	
	String teamName;
	
	ArrayList<AID> workersList = new ArrayList<AID>();
	
	public ResourceAI() {}

	protected void setup(){
		String[] args = (String[]) getArguments();
		if (args != null) {
			teamName = args[0];
		}
		else{
			System.out.println("Needs team's name");
			System.exit(1);
		}
		System.out.println(teamName + ":ResourceAI setup");
		
		createWorker();
		createWorker();
		createWorker();
		createWorker();
		createWorker();
		createWorker();
		createWorker();
	}
	
	public boolean createWorker(){
		World world = World.getInstance();
		Position workerPosition = world.addUnit("worker", world.getBuilding(teamName));
		if(workerPosition != null){
			//Instantiate the worker
			// get a container controller for creating new agents
			PlatformController container = getContainerController();
			AgentController worker;
			try {
				Position[] arg = new Position[1];
				arg[0] = workerPosition;
				worker = container.createNewAgent("worker"+workersList.size(), "agents.Worker", arg);
				worker.start();
				// keep the worker's ID on a local list
				workersList.add( new AID("worker"+workersList.size(), AID.ISLOCALNAME) );
			} catch (ControllerException e) {
				e.printStackTrace();
			}
			System.out.println(teamName + ":Created worker " + workersList.get(workersList.size()-1));
		}
		else{
			System.out.println(teamName + ":Cannot instantiate the worker");
		}
		return false;
	}
}
