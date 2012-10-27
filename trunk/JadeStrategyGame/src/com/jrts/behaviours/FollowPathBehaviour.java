package com.jrts.behaviours;

import jade.core.behaviours.Behaviour;

import java.util.ArrayList;

import com.jrts.agents.Unit;
import com.jrts.common.GameConfig;
import com.jrts.common.Utils;
import com.jrts.environment.Direction;
import com.jrts.environment.Floor;
import com.jrts.environment.Position;
import com.jrts.environment.World;

@SuppressWarnings("serial")
public class FollowPathBehaviour extends Behaviour {

	ArrayList<Direction> list;
	Unit unit;
	private int goalRow, goalCol;
	int remainingAttempts;
	
	Floor worldCachedCopy = null;
		
	public FollowPathBehaviour(Unit unit, int goalRow, int goalCol, int remainingAttempts) {
		this.unit = unit;
		this.goalRow = goalRow;
		this.goalCol = goalCol;
		this.remainingAttempts = remainingAttempts;
		this.list = new ArrayList<Direction>();
		
		//se prima invocazione del behaviour
		if(remainingAttempts == GameConfig.UNIT_MOVING_ATTEMPTS || worldCachedCopy == null){
			setWorldCachedCopy(World.getInstance().getFloor().getCopy());
		}
		
		if(remainingAttempts > 0)
			this.list = Utils.calculatePath(getWorldCachedCopy(), unit.getPosition(), new Position(goalRow, goalCol));
	}

	@Override
	public void action() {
		System.out.println("Follow Path (" + unit.getId() + "," + unit.getStatus() + ")");
		unit.spendTime();
		
		//se non riesco a spostarmi ricalcolo il path
		if (!list.isEmpty() && !unit.move(list.remove(0))) {
			System.out.println(unit.getLocalName() + ":Need path recalculation");
			list.clear();
			unit.addBehaviour(new FollowPathBehaviour(unit, goalRow, goalCol, remainingAttempts-1));
		}
		else
			remainingAttempts = GameConfig.UNIT_MOVING_ATTEMPTS;
	}

	@Override
	public boolean done() {
		//Se il path è stato eseguito correttamente ma la posizione raggiunta non è quella giusta
		//significa che è stata fatta un'approssimazione della posizione obiettivo
		if(list.isEmpty() && !unit.getPosition().equals(new Position(goalRow, goalCol)))
			unit.goThere(new Position(goalRow, goalCol));
		return list.isEmpty();
	}

	public int getGoalRow() {
		return goalRow;
	}

	public void setGoalRow(int goalRow) {
		this.goalRow = goalRow;
	}

	public int getGoalCol() {
		return goalCol;
	}

	public void setGoalCol(int goalCol) {
		this.goalCol = goalCol;
	}

	public Floor getWorldCachedCopy() {
		return worldCachedCopy;
	}

	public void setWorldCachedCopy(Floor cachedCopy) {
		this.worldCachedCopy = cachedCopy;
	}
}
