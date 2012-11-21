package com.jrts.O2Ainterfaces;

import com.jrts.environment.WorldMap;

public interface Team {
	
	public int getFood();
	public int getWood();
	public int getQueueWorkerCount();
	public int getQueueSoldierCount();
		
	public String getTeamName();
	
	public WorldMap getWorldMap();

	public int getEnergy();
	
	public void decease();
}
