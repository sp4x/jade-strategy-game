package com.jrts.O2Ainterfaces;

import com.jrts.agents.MasterAI.Nature;
import com.jrts.environment.WorldMap;
import com.jrts.messages.GoalLevels;

public interface Team {
	
	public int getFood();
	public int getWood();
	public int getQueueWorkerCount();
	public int getQueueSoldierCount();
		
	public String getTeamName();
	public Nature getTeamNature();
	public GoalLevels getGoalLevels();
	
	public int getNumDeadWorkers();
	public int getNumDeadSoldiers();
	
	public WorldMap getWorldMap();

	public int getEnergy();
	
	public void decease();
	
	public String getProgressTrainingWorker();
	public String getProgressTrainingSoldier();
}
