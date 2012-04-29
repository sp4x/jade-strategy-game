package com.jrts.agents;

import jade.core.AID;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.PlatformController;

import java.util.ArrayList;

import com.jrts.environment.Position;
import com.jrts.environment.World;

@SuppressWarnings("serial")
public class ResourceAI extends JrtsAgent {
	
	ArrayList<AID> workersList = new ArrayList<AID>();
	
	public ResourceAI() {}

	protected void setup(){
		String[] args = (String[]) getArguments();
		if (args != null) {
			setTeam(args[0]);
		}
		else{
			System.out.println("Needs team's name");
			System.exit(1);
		}
		
		for (int i = 0; i < 10; i++)
			createWorker();
	}
	
	public boolean createWorker(){
		World world = World.getInstance();
		String workerName = getTeam() + "-worker"+workersList.size();
		Position workerPosition = world.addUnit(workerName, world.getBuilding(getTeam()));
		if(workerPosition != null){
			//Instantiate the worker
			// get a container controller for creating new agents
			PlatformController container = getContainerController();
			AgentController worker;
			try {
				Object[] args = {workerPosition, getTeam()};
				worker = container.createNewAgent(workerName, "com.jrts.agents.Worker", args);
				worker.start();
				// keep the worker's ID on a local list
				workersList.add( new AID(workerName, AID.ISLOCALNAME) );
			} catch (ControllerException e) {
				e.printStackTrace();
			}
		}
		else{
			System.out.println(getTeam() + ":Cannot instantiate the worker");
		}
		return false;
	}

	@Override
	protected void updatePerception() {
		// TODO Auto-generated method stub
	}
}
