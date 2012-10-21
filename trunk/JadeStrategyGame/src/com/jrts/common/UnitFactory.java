package com.jrts.common;

import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.PlatformController;

import java.util.concurrent.LinkedBlockingQueue;

import com.jrts.O2Ainterfaces.IUnit;
import com.jrts.O2Ainterfaces.Team;
import com.jrts.agents.Unit;
import com.jrts.environment.Position;
import com.jrts.environment.World;

public class UnitFactory extends Thread {

	String team;
	PlatformController controller;
	
	long unitCounter = 0;
	
	LinkedBlockingQueue<Class<? extends Unit>> queue = new LinkedBlockingQueue<Class<? extends Unit>>();
	
	public UnitFactory(String team, PlatformController controller) {
		this.team = team;
		this.controller = controller;
	}
	

	@Override
	public void run() {
		while (!World.getInstance().isGameFinished()) {
			try {
				Class<? extends Unit> claz = queue.take();
				createUnit(claz);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void trainUnit(final Class<? extends Unit> claz)  {
		queue.add(claz);
	}
	
	public synchronized long counter() {
		return unitCounter++;
	}
	
	private void createUnit(final Class<? extends Unit> claz) {
		try {
			Thread.sleep(GameConfig.UNIT_CREATION_TIME*1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
			System.exit(1);
		}
		World world = World.getInstance();
		String unitName = team + "-" + claz.getSimpleName() + counter();
		Position unitPosition = world.neighPosition(world.getCityCenter(team));
		if(unitPosition != null){
			//Instantiate the unit
			AgentController agentController;
			try {
				Object[] args = {unitPosition, team};
				agentController = controller.createNewAgent(unitName, claz.getName(), args);
				agentController.start();
				IUnit o2a = agentController.getO2AInterface(IUnit.class);
				World.getInstance().addUnit(unitPosition, unitName, o2a);
				
			} catch (ControllerException e) {
				e.printStackTrace();
			}
		}
		else{
			System.out.println(team + ":Cannot instantiate the unit");
		}
	}
}
