package com.jrts.agents;

import jade.core.AID;
import jade.lang.acl.ACLMessage;

import java.io.IOException;

import com.jrts.behaviours.CheckGoals;
import com.jrts.common.AgentStatus;
import com.jrts.common.ResourcesContainer;
import com.jrts.common.UnitFactory;
import com.jrts.common.UnitTable;
import com.jrts.messages.GoalLevels;
import com.jrts.messages.Order;

public abstract class GoalBasedAI extends JrtsAgent {

	private static final long serialVersionUID = 8548442850146189200L;

	long unitCounter = 0;
	
	GoalLevels goalLevels;
	UnitFactory unitFactory;
	ResourcesContainer resourcesContainer;
	
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
		} else {
			logger.severe("Needs team's name");
			System.exit(1);
		}
		
		addBehaviour(new CheckGoals());
	}
	
//	public AID trainUnit(Class<? extends Unit> claz)  {
//		World world = World.getInstance();
//		String unitName = getTeamName() + "-" + claz.getSimpleName() + unitCounter++;
//		Position unitPosition = world.neighPosition(world.getCityCenter(getTeamName()));
//		if(unitPosition != null){
//			//Instantiate the unit
//			PlatformController container = getContainerController();
//			AgentController agentController;
//			try {
//				Object[] args = {unitPosition, getTeamName()};
//				agentController = container.createNewAgent(unitName, claz.getName(), args);
//				agentController.start();
//				IUnit o2a = agentController.getO2AInterface(IUnit.class);
//				World.getInstance().addUnit(unitPosition, unitName, o2a);
//				
//				AID unitAID = new AID(unitName, AID.ISLOCALNAME);
//				return unitAID;
//			} catch (ControllerException e) {
//				e.printStackTrace();
//			}
//		}
//		else{
//			logger.severe(getTeamName() + ":Cannot instantiate the unit");
//		}
//		return null;
//	}
	
	public void updateGoalLevels(GoalLevels goals) {
		this.goalLevels = goals;
		onGoalsChanged();
	}
	
	public abstract void onGoalsChanged();

	//public void changeAgentStatus(AID target, String newStatus) {
	public void changeAgentStatus(AID target, Order order) {
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.setConversationId(AgentStatus.class.getSimpleName());
		msg.addReceiver(target);
		//msg.setContent(newStatus);
		
		try {
			msg.setContentObject(order);
		} catch (IOException e) { e.printStackTrace(); }
		
		send(msg);
	}

	public UnitTable getUnitTable() {
		return unitTable;
	}
	
	
}
