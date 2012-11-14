package com.jrts.agents;

import jade.core.AID;

import com.jrts.agents.MasterAI.Nature;
import com.jrts.common.ResourcesContainer;
import com.jrts.common.UnitFactory;
import com.jrts.common.UnitTable;
import com.jrts.environment.Position;
import com.jrts.environment.WorldMap;
import com.jrts.messages.GoalLevels;
import com.jrts.messages.Notification;
import com.jrts.messages.Order;

public abstract class GoalBasedAI extends JrtsAgent {

	private static final long serialVersionUID = 8548442850146189200L;

	long unitCounter = 0;
	
	GoalLevels goalLevels;
	UnitFactory unitFactory;
	ResourcesContainer resourcesContainer;
	WorldMap worldMap;
	Position cityCenter;
	Nature nature;
	
	UnitTable unitTable = new UnitTable();
	
	public GoalBasedAI() {
		
	}
	
	protected void setup(){
		super.setup();
		
		Object[] args = getArguments();
		if (args != null) {
			setTeamName((String) args[0]);
			unitFactory = (UnitFactory) args[1];
			resourcesContainer = (ResourcesContainer) args[2];
			worldMap = (WorldMap) args[3];
			cityCenter = (Position) args[4];
			nature = (Nature) args[5];
		} else {
			logger.severe("Needs team's name");
			System.exit(1);
		}
	}
	
	public abstract void onGoalsChanged();

	public void giveOrder(AID target, Order order) {
		sendNotification(Notification.ORDER, order, target);
	}

	public UnitTable getUnitTable() {
		return unitTable;
	}
	
	public AID getMasterAID() {
		return new AID(getTeamName(), AID.ISLOCALNAME);
	}
	
	@Override
	protected void handleNotification(Notification n) {
		if (n.getSubject().equals(Notification.GOAL_LEVELS)) {
			this.goalLevels = (GoalLevels) n.getContentObject();
			onGoalsChanged();
		}
	}
}
