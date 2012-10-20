package com.jrts.behaviours;

import jade.core.behaviours.Behaviour;

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
	

	private static final int STATUS_NONE = 0;
	private static final int STATUS_GOING = 1;
	private static final int STATUS_CARRYING = 2;

	Worker worker;
	String agentStatus;
	int status = STATUS_NONE;

	Position resource;
	Position cityCenter;
	Position pickUpPoint;
	Position leavePoint;

	public CollectResources(Worker worker, String agentStatus, CellType resource) {
		super(worker);
		this.worker = worker;
		this.agentStatus = agentStatus;
		cityCenter = World.getInstance().getBuilding(worker.getTeamName());
	}

	@Override
	public void action() {
		switch (status) {
		case STATUS_NONE:
			go();
			break;
		case STATUS_GOING:
			pick();
			break;
		case STATUS_CARRYING:
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
		Position p = worker.getPosition();
		if (p.equals(pickUpPoint)) {
			worker.spendTime();
			worker.takeResources(resource);
			if (worker.knapsackIsFull()) {
				leavePoint = World.getInstance().nextTo(p, cityCenter);
				worker.goThere(leavePoint);
				status = STATUS_CARRYING;
			}
		}
	}

	private void go() {
//		resource = worker.findNearest(worker.getPosition(), worker.getSight(), CellType.WOOD);
		resource = worker.findNearest(CellType.WOOD);
		if (resource != null)
			pickUpPoint = World.getInstance().nextTo(worker.getPosition(), resource);
		if (resource != null && pickUpPoint != null) {
			worker.goThere(pickUpPoint);
			status = STATUS_GOING;
		}
		else
			status = STATUS_NONE;
	}
	
	@Override
	public boolean done() {
		if (status == STATUS_NONE) {
			worker.switchStatus(AgentStatus.FREE);
			return true;
		}
		if (!worker.getStatus().equals(agentStatus)) {
			return true;
		}
		return false;
	}

}
