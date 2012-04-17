package com.jrts.agents;

import java.util.ArrayList;

import jade.core.AID;
import jade.core.Agent;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.PlatformController;

import com.jrts.environment.Position;
import com.jrts.environment.World;

public class ResourceAI extends Agent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
				Object[] args = {workerPosition, teamName};
				String workerName = teamName + "-worker"+workersList.size();
				worker = container.createNewAgent(workerName, "com.jrts.agents.Worker", args);
				worker.start();
				// keep the worker's ID on a local list
				workersList.add( new AID(workerName, AID.ISLOCALNAME) );
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
