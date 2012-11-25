package com.jrts.behaviours;

import com.jrts.agents.Worker;
import com.jrts.common.AgentStatus;
import com.jrts.environment.Position;
import com.jrts.messages.Notification;

@SuppressWarnings("serial")
public class GoUpgradingBehaviour extends UnitBehaviour {

	Worker worker;
	Position cityCenter;

	boolean done = false;
	/**
	 * 
	 * @param soldier
	 */
	public GoUpgradingBehaviour(Worker soldier) {
		super(AgentStatus.GO_UPGRADING, false);
		this.worker = soldier;
		this.cityCenter = soldier.requestCityCenterPosition();
	}

	@Override
	public void myAction() {
		if(!this.worker.getPosition().isNextTo(cityCenter)){
			this.worker.goThere(cityCenter);
		} else {
			this.worker.sendNotification(Notification.READY_TO_BE_UPGRADED, null, this.worker.getMilitaryAID());
			this.worker.sendNotification(Notification.READY_TO_BE_UPGRADED, null, this.worker.getResourceAID());
			
			this.done = true;
			this.worker.terminate();
		}
	}

	@Override
	public boolean done() {
		return this.done;
	}
}
