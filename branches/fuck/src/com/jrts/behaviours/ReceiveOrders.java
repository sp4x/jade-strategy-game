package com.jrts.behaviours;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import com.jrts.agents.Soldier;
import com.jrts.agents.Worker;
import com.jrts.common.AgentStatus;

public class ReceiveOrders extends CyclicBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ReceiveOrders(Agent a) {
		super(a);
	}

	@Override
	public void action() {
		MessageTemplate pattern = MessageTemplate.MatchConversationId(AgentStatus.class.getSimpleName());
		ACLMessage msg = myAgent.receive(pattern);
		if (msg != null) {
			parseOrder(msg.getContent());
		}
		else {
			block();
		}

	}

	private void parseOrder(String order) {
		System.out.println(myAgent + ": RECEIVED ORDER: " + order);
		
		if (order.equals(AgentStatus.WOOD_CUTTING)) {
			if (myAgent instanceof Worker)
				((Worker) myAgent).collectWood();
		} else if (order.equals(AgentStatus.FOOD_COLLECTING)) {
			if (myAgent instanceof Worker)
				((Worker) myAgent).collectFood();
		} else if (order.equals(AgentStatus.PATROLING)) {
			if (myAgent instanceof Soldier)
				((Soldier) myAgent).patrol();
		} else if (order.equals(AgentStatus.EXPLORING)) {
			if (myAgent instanceof Soldier)
				((Soldier) myAgent).explore();
		}
	}

}
