package com.jrts.agents;

import java.util.logging.Level;

import com.jrts.behaviours.CollectResources;
import com.jrts.behaviours.GoUpgradingBehaviour;
import com.jrts.common.AgentStatus;
import com.jrts.common.GameConfig;
import com.jrts.common.Utils;
import com.jrts.environment.Cell;
import com.jrts.environment.CellType;
import com.jrts.environment.Direction;
import com.jrts.environment.Position;
import com.jrts.environment.World;
import com.jrts.environment.WorldMap;
import com.jrts.logic.AttacksManager;
import com.jrts.messages.AggiornaRisorse;
import com.jrts.messages.Death;
import com.jrts.messages.EnemySighting;
import com.jrts.messages.Notification;
import com.jrts.messages.Order;

@SuppressWarnings("serial")
public class Worker extends Unit {
	
	private Level logLevel = Level.FINE;
	
	int knapsack = 0;
	CellType resourceCarried;

	public Worker() {
		this(null, null);
	}

	public Worker(String id, Position position) {
		super(id, position);
		
		setLife(GameConfig.WORKER_LIFE);
		setSpeed(GameConfig.WORKER_SPEED);
		setForceOfAttack(GameConfig.WORKER_DAMAGES);
		setSight(GameConfig.WORKER_SIGHT);
	}


	@Override
	protected void setup(){
		super.setup();
		
		switchStatus(AgentStatus.FREE);
		
		sendNotification(Notification.NEW_WORKER, null, getResourceAID());
		
//		addBehaviour(new SendAttack(this));
	}
	
	public void sendHit(Direction direction) {
		AttacksManager.addHit(getPosition().clone(), direction, GameConfig.WORKER_DAMAGES, GameConfig.HIT_RANGE);
	}
	
	public void takeResources(Position resourcePosition) {
		if (getPerception() == null) 
			return;
		Cell cell = getPerception().get(resourcePosition);
		CellType resource = cell.getType();
		boolean validResource = cell.isResource();
		if (validResource)
			World.getInstance().takeEnergy(resourcePosition, 1);
		if (knapsack == 0 && validResource) {
			resourceCarried = resource;
			knapsack++;
		}
		//can only carry one kind of resource
		else if (resourceCarried == resource)
			knapsack++;
		else if (validResource) { 
			knapsack = 1;
			resourceCarried = resource;
		}
	}
	
	public boolean knapsackIsFull() {
		return knapsack == GameConfig.WORKER_KNAPSACK_CAPACITY;
	}

	public void dropResources() {
		//send the resource message to the resourceAI
		logger.log(logLevel, getId() + ": drop resources");
		int collectedWood = getStatus().equals(AgentStatus.WOOD_CUTTING) ? knapsack : 0;
		int collectedFood = getStatus().equals(AgentStatus.FOOD_COLLECTING) ? knapsack : 0;
		
		sendNotification(Notification.RESOURCES_UPDATE, new AggiornaRisorse(collectedWood, collectedFood), getResourceAID());
		
		knapsack = 0;
	}

	public void goUpgrading() {
		switchStatus(AgentStatus.GO_UPGRADING);
		addBehaviour(new GoUpgradingBehaviour(this));
	}
	
	public void collectWood() {
		switchStatus(AgentStatus.WOOD_CUTTING);
		addBehaviour(new CollectResources(this, CellType.WOOD));
	}

	public void collectFood() {
		switchStatus(AgentStatus.FOOD_COLLECTING);
		addBehaviour(new CollectResources(this, CellType.FOOD));
	}
	
	@Override
	public CellType getType() {
		return CellType.WORKER;
	}
	
	public int getKnapsack() {
		return knapsack;
	}

	@Override
	public void onEnemySighted(EnemySighting enemies) {
		sendNotification(Notification.ENEMY_SIGHTED, enemies, getMilitaryAID());
	}
	
	@Override
	protected void handleNotification(Notification n) {
		super.handleNotification(n);
	}
	
	@Override
	public void takeOrder(Order order) {
		super.takeOrder(order);
		if (order.getNextStatus().equals(AgentStatus.GO_UPGRADING)) {
			this.goUpgrading();
		} else if (order.getNextStatus().equals(AgentStatus.WOOD_CUTTING)) {
			collectWood();
		} else if (order.getNextStatus().equals(AgentStatus.FOOD_COLLECTING)) {
			collectFood();
		}
	}

	@Override
	protected void die() {
		sendNotification(Notification.UNIT_DEATH, new Death(Death.WORKER), getResourceAID());
		terminate();
	}

	@Override
	public boolean onAttacNotification(String attacker) {
		WorldMap worldMap = requestMap();
		int i = Utils.random.nextInt(Direction.ALL.length);
		Position p = worldMap.bigStep(getPosition(), Direction.ALL[i], GameConfig.RUNAWAY_DISTANCE);
		goThere(p);
		return false;
	}

}
