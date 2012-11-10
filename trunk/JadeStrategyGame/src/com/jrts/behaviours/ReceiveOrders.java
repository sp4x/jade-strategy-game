package com.jrts.behaviours;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

import com.jrts.agents.Soldier;
import com.jrts.agents.Worker;
import com.jrts.common.AgentStatus;
import com.jrts.common.Order;
import com.jrts.environment.Direction;

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
			//parseOrder(msg.getContent());
			try {
				parseOrder((Order)msg.getContentObject());
			} catch (UnreadableException e) { e.printStackTrace(); }
		}
		else {
			block();
		}

	}

	//private void parseOrder(String order) {
	private void parseOrder(Order order) {
		
		
		/*
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
		*/

		if (order.getOrder().equals(AgentStatus.WOOD_CUTTING)) {
			if (myAgent instanceof Worker)
				((Worker) myAgent).collectWood();
		} else if (order.getOrder().equals(AgentStatus.FOOD_COLLECTING)) {
			if (myAgent instanceof Worker)
				((Worker) myAgent).collectFood();
		} else if (order.getOrder().equals(AgentStatus.PATROLING)) {
			if (myAgent instanceof Soldier){
				Direction dir = order.getPatrolDirection();
				int distance = order.getPatrolDistance();
				((Soldier) myAgent).patrol(dir, distance);
			}
		} else if (order.getOrder().equals(AgentStatus.EXPLORING)) {
			if (myAgent instanceof Soldier)
				((Soldier) myAgent).explore();
		}

	}

}
