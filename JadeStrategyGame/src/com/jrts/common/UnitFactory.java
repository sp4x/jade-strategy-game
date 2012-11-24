package com.jrts.common;

import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.PlatformController;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

import com.jrts.O2Ainterfaces.IUnit;
import com.jrts.agents.MasterAI.Nature;
import com.jrts.agents.Unit;
import com.jrts.environment.Position;
import com.jrts.environment.World;

public class UnitFactory extends Thread {

	Logger logger = Logger.getLogger(UnitFactory.class.getName());
	
	Nature nature;
	String team;
	PlatformController controller;
	Position cityCenter;
	Position meetingPoint;
	long unitCounter = 0;
	
	LinkedBlockingQueue<Class<? extends Unit>> queue;

	int workerQueueCount = 0;
	int soldierQueueCount = 0;
	
	
	
	public UnitFactory(String team, PlatformController controller, Position cityCenter, Position meetingPoint, Nature nature) {
		this.team = team;
		this.controller = controller;
		this.queue = new LinkedBlockingQueue<Class<? extends Unit>>();
		this.cityCenter = cityCenter;
		this.meetingPoint = meetingPoint;
		this.nature = nature;
	}
	

	@Override
	public void run() {
		while (true) {
			try {
				Class<? extends Unit> claz = queue.take();
				createUnit(claz);
			} catch (InterruptedException e) {
			}
		}
	}
	
	public void trainUnit(final Class<? extends Unit> clazz)  {
//		logger.log(logLevel, team + " request to train a " + claz.getSimpleName());
		try {
			queue.put(clazz);
			
			incrementCounters(clazz);
			
		} catch (InterruptedException e) {
		}
	}
	
	public synchronized void incrementCounters(Class<?> clazz) {
		if(clazz.getName().contains("Worker")) workerQueueCount++;
		else if(clazz.getName().contains("Soldier")) soldierQueueCount++;
	}
	
	public synchronized long counter() {
		return unitCounter++;
	}
	
	private void createUnit(final Class<? extends Unit> claz) {
		try {
			Thread.sleep(GameConfig.UNIT_CREATION_TIME*1000);
		} catch (InterruptedException e1) {
		}
		World world = World.getInstance();
		String unitName = team + "-" + claz.getSimpleName() + counter();
		Position unitPosition = world.neighPosition(cityCenter);
//		Position unitMeetingPoint = world.randomMeetingPoint(meetingPoint);
		if(unitPosition != null){
			//Instantiate the unit
			AgentController agentController;
			try {
				Object[] args = {cityCenter, unitPosition, meetingPoint, team, nature};
				synchronized (this) {
					agentController = controller.createNewAgent(unitName, claz.getName(), args);
					agentController.start();

					if(claz.getName().contains("Worker")) workerQueueCount--;
					else if(claz.getName().contains("Soldier")) soldierQueueCount--;
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

	public int getQueueWorkerCount() {
		return workerQueueCount;
	}

	public int getQueueSoldierCount() {
		return soldierQueueCount;
	}
	
	public boolean queueIsEmpty() {
		return queue.isEmpty();
	}
	
	public int getQueuedUnitsCount() {
		return queue.size();
	}
	
	
}
