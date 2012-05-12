package com.jrts.behaviours;

import jade.core.behaviours.Behaviour;

import com.jrts.agents.Worker;
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
	Position pickUpPoint;
	Position leavePoint;

	public CollectResources(Worker worker, String agentStatus, CellType resource) {
		super(worker);
		this.worker = worker;
		this.agentStatus = agentStatus;
		Position cityCenter = World.getInstance().getBuilding(worker.getTeam());
		this.leavePoint = World.getInstance().nextTo(cityCenter);
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
		System.out.println("worker in "+worker.getPosition()+" leave in "+leavePoint);
		if (worker.getPosition().equals(leavePoint)) {
			System.out.println("leaving");
			worker.dropResources();
			go();
		}
	}

	private void pick() {
		if (worker.getPosition().equals(pickUpPoint)) {
			worker.takeResources(resource);
			if (worker.knapsackIsFull()) {
				worker.goThere(leavePoint);
				status = STATUS_CARRYING;
			}
		}
	}

	private void go() {
		resource = worker.findNearest(CellType.WOOD);
		pickUpPoint = World.getInstance().nextTo(resource);
		if (resource != null) {
			worker.goThere(pickUpPoint);
			status = STATUS_GOING;
		}
		else
			status = STATUS_NONE;
	}
	
	@Override
	public boolean done() {
		if (status == STATUS_NONE) {
			return true;
		}
		if (!worker.getStatus().equals(agentStatus)) {
			System.out.println("done, exiting");
			return true;
		}
		return false;
	}

}
