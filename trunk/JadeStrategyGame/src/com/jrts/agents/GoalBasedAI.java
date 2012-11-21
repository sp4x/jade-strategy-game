package com.jrts.agents;

import jade.core.AID;

import java.util.Collection;

import com.jrts.agents.MasterAI.Nature;
import com.jrts.common.ResourcesContainer;
import com.jrts.common.UnitFactory;
import com.jrts.common.UnitTable;
import com.jrts.environment.Position;
import com.jrts.environment.WorldMap;
import com.jrts.messages.GoalLevels;
import com.jrts.messages.MessageSubject;
import com.jrts.messages.Notification;
import com.jrts.messages.Order;

public abstract class GoalBasedAI extends JrtsAgent {

	private static final long serialVersionUID = 8548442850146189200L;

	long unitCounter = 0;
	
	GoalLevels goalLevels;
	UnitFactory unitFactory;
	ResourcesContainer resourcesContainer;
//	WorldMap worldMap;
	Position myCityCenter;
	Nature nature;
	
	UnitTable unitTable = new UnitTable();
	
	public GoalBasedAI() {
		
	}
	
	protected void setup(){
		super.setup();
		
		Object[] args = getArguments();
		if (args != null) {
			int i = 0;
			setTeamName((String) args[i++]);
			unitFactory = (UnitFactory) args[i++];
			resourcesContainer = (ResourcesContainer) args[i++];
			myCityCenter = (Position) args[i++];
			nature = (Nature) args[i++];
		} else {
			logger.severe("Needs team's name");
			System.exit(1);
		}
	}

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
		} else if (n.getSubject().equals(Notification.TEAM_DECEASED)) {
			Collection<AID> units = unitTable.getAllUnits();
			for (AID unit : units) {
				sendNotification(Notification.TEAM_DECEASED, null, unit);
			}
			this.doDelete();
		}
	}
	
	public WorldMap requestMap() {
		return (WorldMap) sendRequest(MessageSubject.GET_WORLD_MAP, getMasterAID());
	}
}
