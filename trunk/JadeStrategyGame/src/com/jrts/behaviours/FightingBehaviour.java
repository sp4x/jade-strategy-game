package com.jrts.behaviours;

import com.jrts.agents.Soldier;
import com.jrts.common.AgentStatus;
import com.jrts.common.GameConfig;
import com.jrts.environment.Cell;
import com.jrts.environment.Direction;
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
		super(AgentStatus.FIGHTING, true, soldier);
		this.soldier = soldier;
		this.target = target;
	}
	
	public FightingBehaviour(Soldier soldier, Position targetPosition) {
		super(AgentStatus.FIGHTING, true, soldier);
		this.soldier = soldier;
		this.targetPosition = targetPosition;
	}
	
	private void hitOnBuilding() {
		WorldMap worldMap = soldier.requestMap();
		Cell targetCell = worldMap.get(targetPosition);
		if (targetCell.isCityCenter()) {
			Position soldierPosition = soldier.getPosition().clone();
			//if it's close attack
			if (soldierPosition.isNextTo(targetPosition)) {
				Direction dir = soldier.getPosition().getDirectionTo(targetPosition);
				if (dir != null)
					soldier.sendHit(dir);
			} else {
				//walk until it's in range
				reach(targetPosition);
			}
		}
		else {
			done = true;
		}
	}

	@Override
	public void myAction() {
		try {
			Thread.sleep(GameConfig.ATTACKS_SLEEP_TIME);
		} catch (InterruptedException e) {}
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
			Position soldierPosition = soldier.getPosition();
			//if it's close attack
			if (soldierPosition.isNextTo(enemyCurrentPosition)) {
				Direction dir = soldier.getPosition().getDirectionTo(enemyCurrentPosition);
				if (dir != null)
					soldier.sendHit(dir);
			} else {
				//walk until it's in range
				reach(enemyCurrentPosition);
			}
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
