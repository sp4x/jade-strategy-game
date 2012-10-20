package com.jrts.behaviours;

import jade.core.behaviours.Behaviour;

import com.jrts.agents.Unit;
import com.jrts.agents.Worker;
import com.jrts.common.AgentStatus;
import com.jrts.environment.CellType;
import com.jrts.environment.Position;
import com.jrts.environment.World;

public class CollectResources extends Behaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final int STATUS_IDLE = 0;
	private static final int STATUS_GOING_WORK = 1;
	private static final int STATUS_BACK_HOME = 2;

	Worker worker;
	CellType resourceToCollect;
	int behaviourStatus = STATUS_IDLE;

	Position resourcePosition;
	Position cityCenter;
	Position pickUpPoint;
	Position leavePoint;

	public CollectResources(Worker worker, CellType resource) {
		super(worker);
		this.worker = worker;
		this.resourceToCollect = resource;
		this.cityCenter = World.getInstance().getCityCenter(worker.getTeamName());
	}

	@Override
	public void action() {
		switch (behaviourStatus) {
		case STATUS_IDLE:
			go();
			break;
		case STATUS_GOING_WORK:
			pick();
			break;
		case STATUS_BACK_HOME:
			carry();
			break;
		default:
			break;
		}
	}
	
	private void carry() {
		if (worker.getPosition().equals(leavePoint)) {
			worker.dropResources();
			go();
		}
	}

	private void pick() {
		Position currentPosition = worker.getPosition();
		if (currentPosition.equals(pickUpPoint)) {
			worker.spendTime();
			worker.takeResources(resourcePosition);
			if (worker.knapsackIsFull()) {
				leavePoint = World.getInstance().nextTo(currentPosition, cityCenter);
				worker.goThere(leavePoint);
				behaviourStatus = STATUS_BACK_HOME;
			}
		}
	}

	/**
	 * Find a near resource and start to go there
	 */
	private void go() {
		resourcePosition = worker.findNearest(resourceToCollect);
		if (resourcePosition != null)
			pickUpPoint = World.getInstance().nextTo(worker.getPosition(), resourcePosition);
		if (resourcePosition != null && pickUpPoint != null) {
			worker.goThere(pickUpPoint);
			behaviourStatus = STATUS_GOING_WORK;
		}
		else
			behaviourStatus = STATUS_IDLE;
	}
	
	@Override
	public boolean done() {
		String agentStatus = ((Unit) myAgent).getStatus();
		if (!agentStatus.equals(AgentStatus.FOOD_COLLECTING) && resourceToCollect == CellType.FOOD)
			return true;
		if (!agentStatus.equals(AgentStatus.WOOD_CUTTING) && resourceToCollect == CellType.WOOD)
			return true;
		return false;
	}

}
