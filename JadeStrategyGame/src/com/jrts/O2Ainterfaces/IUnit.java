package com.jrts.O2Ainterfaces;

import com.jrts.environment.CellType;
import com.jrts.environment.Position;

public interface IUnit {
	
	public int getLife();
	public void decreaseLife(int damage);
	public String getStatus();
	public String getId();
	public Position getPosition();
	public String getTeamName();
	public CellType getType();
	public int getKnapsack();
}
