package com.jrts.agents;

import jade.core.AID;
import jade.lang.acl.ACLMessage;

import com.jrts.behaviours.ExploreBehaviour;
import com.jrts.behaviours.FightingBehaviour;
import com.jrts.behaviours.PatrolBehaviour;
import com.jrts.common.AgentStatus;
import com.jrts.common.GameConfig;
import com.jrts.environment.CellType;
import com.jrts.environment.Direction;
import com.jrts.environment.Position;
import com.jrts.logic.AttacksManager;
import com.jrts.messages.EnemySighting;
import com.jrts.messages.MessageSubject;
import com.jrts.messages.Notification;
import com.jrts.messages.Order;

@SuppressWarnings("serial")
public class Soldier extends Unit {
	
	int knapsack = 0;
	CellType resourceCarried;

	Order lastOrderReceived;
	EnemySighting lastEnemySighting;
	
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
		
		sendNotification(Notification.NEW_SOLDIER, null, getMilitaryAID());
	}
	
	public void sendHit(Direction direction) {
		AttacksManager.addHit(getPosition().clone(), direction, GameConfig.SOLDIER_DAMAGES, GameConfig.HIT_RANGE);
	}

	/**
	 * 
	 * @param direction must be one between TOP, RIGHT, DOWN, LEFT
	 */
	public void patrol(Direction direction, int distance) {
		addBehaviour(new PatrolBehaviour(this, direction, distance, requestMap()));
		switchStatus(AgentStatus.PATROLING);
	}

	public void explore() {
		addBehaviour(new ExploreBehaviour(this));
		switchStatus(AgentStatus.EXPLORING);
	}
	
	public void goToAttack(Position pos)
	{
		switchStatus(AgentStatus.GO_FIGHTING);
		goThere(pos);
	}
	
	@Override
	public CellType getType() {
		return CellType.SOLDIER;
	}

	@Override
	public void onEnemySighted(EnemySighting enemies) {
		lastEnemySighting = enemies;
		sendNotification(Notification.ENEMY_SIGHTED, enemies, getMilitaryAID());
		
		if(getStatus().equals(AgentStatus.GO_FIGHTING))
		{
			String target = enemies.getEnemies().iterator().next().getId();
			attack(target);
		}
	}
	
	public void attack(String target) {
		ACLMessage proposal = new ACLMessage(ACLMessage.PROPOSE);
		proposal.setConversationId(MessageSubject.FIGHT);
		proposal.addReceiver(new AID(target, AID.ISLOCALNAME));
		send(proposal);
	}
	
	@Override
	public void engageFight(String target, Position targetPosition) {
		switchStatus(AgentStatus.FIGHTING);
		addBehaviour(new FightingBehaviour(this, target, targetPosition));
	}
	
	@Override
	protected void handleNotification(Notification n) {
		super.handleNotification(n);
		if (n.getSubject().equals(Notification.ORDER)) {
			Order order = (Order) n.getContentObject();
			this.lastOrderReceived = order;
			
			if (order.getOrder().equals(AgentStatus.PATROLING)) {
				goThere(getCityCenter());
				switchStatus(AgentStatus.FREE);
			}else if (order.getOrder().equals(AgentStatus.PATROLING)) {
				Direction dir = order.getDirection();
				int distance = order.getDistance();
				patrol(dir, distance);
				
			} else if (order.getOrder().equals(AgentStatus.EXPLORING)) {
				explore();
			} else if (order.getOrder().equals(AgentStatus.WAIT_TO_FIGHT)) {
				goThere(order.getPosition());
				switchStatus(AgentStatus.WAIT_TO_FIGHT);
			} else if (order.getOrder().equals(AgentStatus.GO_FIGHTING)) {
				goToAttack(order.getPosition());
			}
		}
	}

	@Override
	protected void die() {
		sendNotification(Notification.UNIT_DEATH, getPosition(), getMilitaryAID());
		terminate();
	}

	@Override
	public boolean onAttackProposal(String attacker) {
		engageFight(attacker, null);
		return true;
	}

	public EnemySighting getLastEnemySighting() {
		return lastEnemySighting;
	}
	
}
