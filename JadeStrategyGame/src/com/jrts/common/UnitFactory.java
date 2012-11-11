package com.jrts.common;

import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.PlatformController;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

import com.jrts.O2Ainterfaces.IUnit;
import com.jrts.agents.Unit;
import com.jrts.environment.Position;
import com.jrts.environment.World;

public class UnitFactory extends Thread {

	Logger logger = Logger.getLogger(UnitFactory.class.getName());
	
	String team;
	PlatformController controller;
	Position cityCenter;
	long unitCounter = 0;
	
	LinkedBlockingQueue<Class<? extends Unit>> queue;
	
	public UnitFactory(String team, PlatformController controller, Position cityCenter) {
		this.team = team;
		this.controller = controller;
		this.queue = new LinkedBlockingQueue<Class<? extends Unit>>();
		this.cityCenter = cityCenter;
	}
	

	@Override
	public void run() {
		logger.info(team + " fa partire la unit factory");
		while (true) {
			try {
				logger.info(team + " unitfactory: try to take from the queue");
				Class<? extends Unit> claz = queue.take();
				logger.info(team + " unitfactory: takes a " + claz.getSimpleName());
				createUnit(claz);
			} catch (InterruptedException e) {
			}
		}
	}
	
	public void trainUnit(final Class<? extends Unit> claz)  {
		logger.info(team + " request to train a " + claz.getSimpleName());
		try {
			queue.put(claz);
		} catch (InterruptedException e) {
		}
	}
	
	public synchronized long counter() {
		return unitCounter++;
	}
	
	private void createUnit(final Class<? extends Unit> claz) {
		try {
			Thread.sleep(GameConfig.UNIT_CREATION_TIME*1000);
		} catch (InterruptedException e1) {
		}
		logger.info(team + " unit factory is creating a " + claz.getSimpleName());
		World world = World.getInstance();
		String unitName = team + "-" + claz.getSimpleName() + counter();
		Position unitPosition = world.neighPosition(cityCenter);
		if(unitPosition != null){
			//Instantiate the unit
			AgentController agentController;
			try {
				Object[] args = {unitPosition, team};
				synchronized (this) {
					agentController = controller.createNewAgent(unitName, claz.getName(), args);
					agentController.start();
				}
				IUnit o2a = agentController.getO2AInterface(IUnit.class);
				World.getInstance().addUnit(unitPosition, unitName, o2a);
				
			} catch (ControllerException e) {
				e.printStackTrace();
			}
		}
		else{
			logger.severe(team + ":Cannot instantiate the unit");
		}
	}
}
