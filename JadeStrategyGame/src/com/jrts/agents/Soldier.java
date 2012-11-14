package com.jrts.agents;

import com.jrts.behaviours.ExploreBehaviour;
import com.jrts.behaviours.PatrolBehaviour;
import com.jrts.common.AgentStatus;
import com.jrts.common.GameConfig;
import com.jrts.environment.CellType;
import com.jrts.environment.Direction;
import com.jrts.environment.Position;
import com.jrts.logic.AttacksManager;
import com.jrts.messages.EnemySighting;
import com.jrts.messages.Notification;
import com.jrts.messages.Order;

@SuppressWarnings("serial")
public class Soldier extends Unit {
	
	int knapsack = 0;
	CellType resourceCarried;

	public Soldier() {
		this(null, null);
	}

	public Soldier(String id, Position position) {
		super(id, position);
		setLife(GameConfig.SOLDIER_LIFE);
		setSpeed(GameConfig.SOLDIER_SPEED);
		setForceOfAttack(GameConfig.SOLDIER_DAMAGES);
		setSight(GameConfig.SOLDIER_SIGHT);
	}

	@Override
	protected void setup(){
		super.setup();
		
		switchStatus(AgentStatus.FREE);
		
		//explore();
		//patrol(Direction.UP, PatrolBehaviour.DISTANCE_LITTLE);
		
//		addBehaviour(new SendAttack(this));
	}
	
	public void sendHit(Direction direction) {
		AttacksManager.addHit(getPosition().clone(), direction, GameConfig.SOLDIER_DAMAGES, GameConfig.HIT_RANGE);
	}

	/**
	 * 
	 * @param direction must be one between TOP, RIGHT, DOWN, LEFT
	 */
	public void patrol(Direction direction, int distance) {
		/*
		Direction direction = Direction.UP;
		int distance = PatrolBehaviour.DISTANCE_LITTLE;
		*/
		addBehaviour(new PatrolBehaviour(this, direction, distance));
		switchStatus(AgentStatus.PATROLING);
	}

	public void explore() {
		addBehaviour(new ExploreBehaviour(this));
		switchStatus(AgentStatus.EXPLORING);
	}
	
	@Override
	public CellType getType() {
		return CellType.SOLDIER;
	}

	@Override
	public void onEnemySighted(EnemySighting list) {
		
	}
	
	@Override
	protected void handleNotification(Notification n) {
		if (n.getSubject().equals(Notification.ORDER)) {
			Order order = (Order) n.getContentObject();
			if (order.getOrder().equals(AgentStatus.PATROLING)) {
				Direction dir = order.getPatrolDirection();
				int distance = order.getPatrolDistance();
				patrol(dir, distance);
				
			} else if (order.getOrder().equals(AgentStatus.EXPLORING)) {
				explore();
			}
		}
	}
}
