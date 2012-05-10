package com.jrts.behaviours;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import com.jrts.common.AgentStatus;

public class ReceiveOrders extends CyclicBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ReceiveOrders(Agent a) {
		super(a);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void action() {
		MessageTemplate pattern = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
		ACLMessage msg = myAgent.receive(pattern);
		if (msg != null) {
			parseOrder(msg.getContent());
		}
		else {
			block();
		}

	}

	private void parseOrder(String order) {
		// TODO Auto-generated method stub
		if (order.equals(AgentStatus.WOOD_CUTTING)) {
			myAgent.addBehaviour(new WoodCut(myAgent));
		}
		
	}

}
