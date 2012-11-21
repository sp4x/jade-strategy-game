package com.jrts.behaviours;

import com.jrts.agents.Soldier;
import com.jrts.environment.Cell;
import com.jrts.environment.Position;
import com.jrts.environment.WorldMap;
import com.jrts.messages.EnemySightingItem;

public class FightingBehaviour extends UnitBehaviour {
	
	private static final long serialVersionUID = 1L;
	
	Soldier soldier;
	String target;
	Position targetPosition;
	FollowPathBehaviour followPathBehaviour;
	boolean done = false;

	public FightingBehaviour(Soldier soldier, String target) {
		super(true, soldier);
		this.soldier = soldier;
		this.target = target;
	}
	
	public FightingBehaviour(Soldier soldier, Position targetPosition) {
		super(true, soldier);
		this.soldier = soldier;
		this.targetPosition = targetPosition;
	}
	
	private void hitOnBuilding() {
		WorldMap worldMap = soldier.requestMap();
		Cell targetCell = worldMap.get(targetPosition);
		if (targetCell.isCityCenter()) {
			if (soldier.getPosition().isNextTo(targetPosition))
				soldier.sendHit(soldier.getPosition().getDirectionTo(targetPosition));
			else
				reach(targetPosition);
		}
		else {
			done = true;
		}
	}

	@Override
	public void myAction() {
		if (target == null) {
			hitOnBuilding();
		} else {
			hitOnTargetEnemy();
		}
	}

	private void hitOnTargetEnemy() {
		EnemySightingItem enemy = soldier.getLastEnemySighting().getById(target);
		//if the enemy is not in sight or it's dead
		if (enemy == null) {
			done = true;
		} else {
			Position enemyCurrentPosition = enemy.getPosition();
			//if it's close attack
			if (soldier.getPosition().isNextTo(enemyCurrentPosition))
				soldier.sendHit(soldier.getPosition().getDirectionTo(enemyCurrentPosition));
			else //walk until it's in range
				reach(enemyCurrentPosition);
		}
	}

	private void reach(Position position) {
		if (targetPosition == null) {
			targetPosition = position;
		}
		
		if (!position.equals(targetPosition) || followPathBehaviour == null) {
			targetPosition = position;
			followPathBehaviour = new FollowPathBehaviour(soldier, targetPosition);
		}
		else {
			followPathBehaviour.myAction();
		}
	}

	@Override
	public boolean done() {
		return done;
	}
}
