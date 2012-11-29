package com.jrts.common;

import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.PlatformController;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

import com.jrts.O2Ainterfaces.IUnit;
import com.jrts.agents.MasterAI.Nature;
import com.jrts.agents.Soldier;
import com.jrts.agents.Unit;
import com.jrts.agents.Worker;
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
	
	int workerTrainingProgress = 0;
	int soldierTrainingProgress = 0;

	private boolean finished = false;;
	
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
		while (!isFinished()) {
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
		long idleTime = GameConfig.UNIT_CREATION_TIME*1000/5;
		setProgress(claz.getCanonicalName(), 0);
		idle(idleTime);
		World world = World.getInstance();
		String unitId = team + "-" + claz.getSimpleName() + counter();
		Position unitPosition = world.neighPosition(cityCenter);
		if(unitPosition != null){
			setProgress(claz.getCanonicalName(), 25);
			idle(idleTime);
			//Instantiate the unit
			AgentController agentController;
			try {
				Object[] args = {cityCenter, unitPosition, meetingPoint, team, nature};
				setProgress(claz.getCanonicalName(), 50);
				idle(idleTime);
				if (!isFinished()) {
					synchronized (this) {
						agentController = controller.createNewAgent(unitId, claz.getName(), args);
						setProgress(claz.getCanonicalName(), 75);
						idle(idleTime);
						if(claz.getName().contains("Worker")) workerQueueCount--;
						else if(claz.getName().contains("Soldier")) soldierQueueCount--;
						setProgress(claz.getCanonicalName(), 90);
						idle(idleTime);
						IUnit o2a = agentController.getO2AInterface(IUnit.class);
						// wait until the unit is not added to the floor
						while (!World.getInstance().addUnit(unitPosition, unitId, o2a)) {
//							System.out.println("unitFactory : " + unitPosition + " not yet available, waiting..");
							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {}
						}
						agentController.start();
						setProgress(claz.getCanonicalName(), 100);
					}
				}
				
			} catch (ControllerException e) {
			}
		}
		else{
			logger.severe(team + ":Cannot instantiate the unit");
		}
	}
	
	private void idle(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e1) {
		}
	}


	public void setProgress(String className, int progress){
//		try {
//			Thread.sleep(GameConfig.UNIT_CREATION_TIME*100);
//		} catch (InterruptedException e1) {
//		}
		if(className.equals(Worker.class.getCanonicalName()))
			workerTrainingProgress = progress;
		else if(className.equals(Soldier.class.getCanonicalName()))
			soldierTrainingProgress = progress;

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


	public synchronized boolean isFinished() {
		return finished;
	}


	public synchronized void setFinished(boolean finished) {
		this.finished = finished;
	}


	public int getWorkerQueueCount() {
		return workerQueueCount;
	}


	public void setWorkerQueueCount(int workerQueueCount) {
		this.workerQueueCount = workerQueueCount;
	}


	public int getSoldierQueueCount() {
		return soldierQueueCount;
	}


	public void setSoldierQueueCount(int soldierQueueCount) {
		this.soldierQueueCount = soldierQueueCount;
	}


	public int getWorkerTrainingProgress() {
		return workerTrainingProgress;
	}


	public void setWorkerTrainingProgress(int workerTrainingProgress) {
		this.workerTrainingProgress = workerTrainingProgress;
	}


	public int getSoldierTrainingProgress() {
		return soldierTrainingProgress;
	}


	public void setSoldierTrainingProgress(int soldierTrainingProgress) {
		this.soldierTrainingProgress = soldierTrainingProgress;
	}
	
	
}
