package com.jrts.behaviours;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

import com.jrts.agents.GoalBasedAI;
import com.jrts.messages.GoalLevels;

public class CheckGoals extends CyclicBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8832334971270701493L;

	@Override
	public void action() {
		ACLMessage msg = myAgent.receive(MessageTemplate.MatchConversationId(GoalLevels.class.getSimpleName()));
		if (msg != null) {
			try {
				GoalLevels goals = (GoalLevels) msg.getContentObject();
				((GoalBasedAI) myAgent).updateGoalLevels(goals);
				
			} catch (UnreadableException e) {
				e.printStackTrace();
			}
		} else {
			block();
		}
	}
}
