package com.jrts.behaviours;

import com.jrts.agents.Soldier;
import com.jrts.environment.Direction;
import com.jrts.environment.Position;
import com.jrts.messages.EnemySightingItem;

public class FightingBehaviour extends UnitBehaviour {
	
	private static final long serialVersionUID = 1L;
	
	Soldier soldier;
	String target;
	Position targetPosition;
	Direction targetDirection;
	FollowPathBehaviour followPathBehaviour;

	//in case the soldier has been attacked
	public FightingBehaviour(Soldier soldier, String attacker) {
		this(soldier, attacker, null);
	}

	//in case a soldier decided to attack a target placed in enemyPosition
	public FightingBehaviour(Soldier soldier, String target,
			Position enemyPosition) {
		super(true, soldier);
		this.soldier = soldier;
		this.target = target;
		this.targetPosition = enemyPosition;
	}

	@Override
	public void myAction() {
		//if I know the target position I need to be next to it
		if (targetPosition != null && !soldier.getPosition().isNextTo(targetPosition)) {
			this.followPathBehaviour = new FollowPathBehaviour(soldier, targetPosition, 1);
		} else if (followPathBehaviour != null) {
			followPathBehaviour.myAction();
			if (followPathBehaviour.done())
				followPathBehaviour = null;
		} else if (targetDirection == null) {
			findDirection();
		} else {
			soldier.sendHit(targetDirection);
		}
	}

	private void findDirection() {
		EnemySightingItem item = soldier.getLastEnemySighting().getById(target);
		if (item != null) {
			Position p = item.getPosition();
			if (soldier.getPosition().isNextTo(p)) {
				targetPosition = p;
				targetDirection = soldier.getPosition().getDirectionTo(p);
			}
		}
	}

	@Override
	public boolean done() {
		return soldier.getLastEnemySighting().getById(target) == null;
	}
}
