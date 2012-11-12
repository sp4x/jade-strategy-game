package com.jrts.messages;

import java.io.Serializable;
import java.util.ArrayList;

import com.jrts.O2Ainterfaces.IUnit;
import com.jrts.environment.CellType;
import com.jrts.environment.Position;

public class EnemySighting implements Serializable {
	private static final long serialVersionUID = 6794563996661095367L;
	
	Position unitPosition;
	ArrayList<IUnit> enemies;
	int soldierNum = 0, workerNum = 0;
	
	public EnemySighting(Position unitPosition) {
		enemies = new ArrayList<IUnit>();
		this.unitPosition = unitPosition;
	}
	
	public void addEnemy(IUnit enemy) {
		this.enemies.add(enemy);
		if (enemy.getType() == CellType.SOLDIER)
			soldierNum++;
		else
			workerNum++;
	}
	
	public ArrayList<IUnit> getEnemies() {
		return enemies;
	}
	
	public boolean isEmpty() {
		return enemies.isEmpty();
	}
	
	public int getSoldierNumber() {
		return soldierNum;
	}
	
	public int getWorkerNumber() {
		return workerNum;
	}
	
	public Position getUnitPosition() {
		return unitPosition;
	}
}
