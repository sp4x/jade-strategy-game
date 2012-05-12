package com.jrts.agents;

import com.jrts.behaviours.CollectResources;
import com.jrts.common.AgentStatus;
import com.jrts.common.GameConfig;
import com.jrts.environment.Cell;
import com.jrts.environment.CellType;
import com.jrts.environment.Direction;
import com.jrts.environment.Position;
import com.jrts.environment.World;
import com.jrts.gui.AttacksManager;

@SuppressWarnings("serial")
public class Worker extends Unit {
	
	int knapsack = 0;
	CellType resourceCarried;

	public Worker() {
		super();
	}

	public Worker(Position position) {
		super(position);
	}

	@Override
	protected void setup(){
		super.setup();
		setLife(GameConfig.WORKER_LIFE);
		setSpeed(GameConfig.WORKER_SPEED);
		setForceOfAttack(GameConfig.WORKER_DAMAGES);
		setSight(GameConfig.WORKER_SIGHT);
		
		setStatus(AgentStatus.FREE);
		
	}
	
	private void sendHit(Direction direction) {
		AttacksManager.addHit(getPosition().clone(), direction, GameConfig.WORKER_DAMAGES);
	}
	
	public void takeResources(Position pickUpPosition) {
		CellType resource = World.getInstance().getCell(pickUpPosition).getType();
		boolean validResource = (resource == CellType.WOOD || resource == CellType.FOOD);
		if (validResource)
			World.getInstance().takeEnergy(pickUpPosition, 1);
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
		knapsack = 0;
		//TODO send resources
	}

	public void cutWood() {
		addBehaviour(new CollectResources(this, AgentStatus.WOOD_CUTTING, CellType.WOOD));
	}
}
;