package com.jrts.agents;

import jade.lang.acl.ACLMessage;

import java.io.IOException;

import com.jrts.behaviours.CollectResources;
import com.jrts.common.AgentStatus;
import com.jrts.common.GameConfig;
import com.jrts.environment.CellType;
import com.jrts.environment.Direction;
import com.jrts.environment.Position;
import com.jrts.environment.World;
import com.jrts.logic.AttacksManager;
import com.jrts.messages.AggiornaRisorse;

@SuppressWarnings("serial")
public class Worker extends Unit {
	
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
		
//		addBehaviour(new SendAttack(this));
	}
	
	public void sendHit(Direction direction) {
		AttacksManager.addHit(getPosition().clone(), direction, GameConfig.WORKER_DAMAGES, GameConfig.HIT_RANGE);
	}
	
	public void takeResources(Position resourcePosition) {
		logger.info("taking resource at " + resourcePosition);
		CellType resource = getPerception().get(resourcePosition).getType();
		boolean validResource = (resource == CellType.WOOD || resource == CellType.FOOD);
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
		logger.info("drop resources");
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.addReceiver(getResourceAID());
		msg.setConversationId(AggiornaRisorse.class.getSimpleName());
		try {
			int collectedWood = getStatus().equals(AgentStatus.WOOD_CUTTING) ? knapsack : 0;
			int collectedFood = getStatus().equals(AgentStatus.FOOD_COLLECTING) ? knapsack : 0;
			msg.setContentObject(new AggiornaRisorse(collectedWood, collectedFood));
			
			send(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
		knapsack = 0;
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
}
;