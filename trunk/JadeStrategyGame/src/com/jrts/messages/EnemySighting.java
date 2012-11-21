package com.jrts.messages;

import java.io.Serializable;
import java.util.ArrayList;

import com.jrts.environment.CellType;
import com.jrts.environment.Position;

public class EnemySighting implements Serializable {
	private static final long serialVersionUID = 6794563996661095367L;
	
	Position sightingPosition;
	ArrayList<EnemySightingItem> enemies;
	int soldierNum = 0, workerNum = 0;
	
	public EnemySighting(Position position) {
		enemies = new ArrayList<EnemySightingItem>();
		this.sightingPosition = position;
	}
	
	public void addEnemy(EnemySightingItem enemy) {
		this.enemies.add(enemy);
		if (enemy.getType() == CellType.SOLDIER)
			soldierNum++;
		else
			workerNum++;
	}
	
	public ArrayList<EnemySightingItem> getEnemies() {
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
	
	public Position getSightingPosition() {
		return sightingPosition;
	}
	
	public Position getAWorker() {
		for (EnemySightingItem item : getEnemies()) {
			if (item.isWorker())
				return item.getPosition();
		}
		return null;
	}
	
	public Position getASoldier() {
		for (EnemySightingItem item : getEnemies()) {
			if (item.isSoldier())
				return item.getPosition();
		}
		return null;
	}
	
	@Override
	public String toString() {
		String s = "";
		for (EnemySightingItem e : enemies) {
			s += "\t" + e.getId() + " in " + e.getPosition() + "\n"; 
		}
		
		return s;
	}
}
