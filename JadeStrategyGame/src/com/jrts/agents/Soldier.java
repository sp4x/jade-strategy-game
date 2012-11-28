package com.jrts.agents;

import jade.core.AID;

import java.util.logging.Level;

import com.jrts.agents.MasterAI.Nature;
import com.jrts.behaviours.BehaviourScheduler;
import com.jrts.behaviours.ExploreBehaviour;
import com.jrts.behaviours.FightingBehaviour;
import com.jrts.behaviours.FollowPathBehaviour;
import com.jrts.behaviours.PatrolBehaviour;
import com.jrts.common.AgentStatus;
import com.jrts.common.GameConfig;
import com.jrts.common.Utils;
import com.jrts.environment.CellType;
import com.jrts.environment.Direction;
import com.jrts.environment.Position;
import com.jrts.logic.AttacksManager;
import com.jrts.messages.EnemySighting;
import com.jrts.messages.Notification;
import com.jrts.messages.Order;

@SuppressWarnings("serial")
public class Soldier extends Unit {

	private Level logLevel = Level.FINE;

	int knapsack = 0;
	CellType resourceCarried;

	Order lastOrderReceived;

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
	protected void setup() {
		super.setup();

		switchStatus(AgentStatus.FREE);

		// explore();
		// patrol(Direction.UP, PatrolBehaviour.DISTANCE_LITTLE);

		// addBehaviour(new SendAttack(this));

		sendNotification(Notification.NEW_SOLDIER, null, getMilitaryAID());
	}

	public void sendHit(Direction direction) {
		spendTime();
		AttacksManager.addHit(getPosition().clone(), direction,
				GameConfig.SOLDIER_DAMAGES, GameConfig.HIT_RANGE);
	}

	/**
	 * 
	 * @param direction
	 *            must be one between TOP, RIGHT, DOWN, LEFT
	 */
	public void patrol(Direction direction, int distance) {
		addBehaviour(new PatrolBehaviour(this, direction, distance,
				requestMap()));
	}

	public void explore() {
		addBehaviour(new ExploreBehaviour(this));
	}

	public void goToAttack(Position pos) {
		BehaviourScheduler b = new BehaviourScheduler(AgentStatus.GO_FIGHTING,
				this);
		b.queueBehaviour(new FollowPathBehaviour(this, pos));
		b.queueBehaviour(new FightingBehaviour(this, pos));
		addBehaviour(new FightingBehaviour(this, pos));
	}

	@Override
	public CellType getType() {
		return CellType.SOLDIER;
	}

	@Override
	public void onEnemySighted(EnemySighting enemies) {
		sendNotification(Notification.ENEMY_SIGHTED, enemies, getMilitaryAID());
		
		boolean natureCase = nature == Nature.AGGRESSIVE
				|| (nature == Nature.AVERAGE && Utils.random.nextBoolean());
		
		//cannot fight with another if it's already fighting
		boolean forbidden = getStatus().equals(AgentStatus.FIGHTING);
		
		boolean nearCityCenter = getPosition().distance(getCityCenter()) < 10;
		
		boolean aggressiveStatus = getStatus().equals(AgentStatus.PATROLING)
				|| getStatus().equals(AgentStatus.GO_FIGHTING);

		
		if (!forbidden && (natureCase || nearCityCenter || aggressiveStatus)) {
			String target = enemies.getEnemies().iterator().next().getId();
			attack(target);
		}
	}

	public void attack(String target) {
		AID targetAid = new AID(target, AID.ISLOCALNAME);
		sendNotification(Notification.ATTACK, id, targetAid);
		engageFight(target);
	}

	public void engageFight(String target) {
		addBehaviour(new FightingBehaviour(this, target));
	}

	@Override
	protected void handleNotification(Notification n) {
		super.handleNotification(n);
	}

	@Override
	public void takeOrder(Order order) {
		super.takeOrder(order);
		this.lastOrderReceived = order;

		if (order.getNextStatus().equals(AgentStatus.PATROLING)) {
			Direction dir = order.getDirection();
			int distance = order.getDistance();
			patrol(dir, distance);

		} else if (order.getNextStatus().equals(AgentStatus.EXPLORING)) {
			explore();
			
		} else if (order.getNextStatus().equals(AgentStatus.GO_FIGHTING)) {
			goToAttack(order.getPosition());
		}
	}

	@Override
	protected void die() {
		this.setStatus(AgentStatus.DYING);
		sendNotification(Notification.UNIT_DEATH, this.getClass().getCanonicalName(), getResourceAID());
		logger.log(logLevel, getAID().getLocalName() + ":dying");
		terminate();
	}

	@Override
	public void onAttacNotification(String attacker) {
		// reject fight if the attacker has not been spotted yet
		// or if the soldier is already fighting
		if (lastEnemySighting.getById(attacker) != null
				&& !getStatus().equals(AgentStatus.FIGHTING))
			engageFight(attacker);
	}

}
