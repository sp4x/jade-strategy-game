package com.jrts.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

import java.io.IOException;

import com.jrts.common.GameConfig;
import com.jrts.environment.Floor;

@SuppressWarnings("serial")
public abstract class JrtsAgent extends Agent {
	
	AID masterAID;
	Floor perception;
	
	@Override
	protected void setup() {
		super.setup();
		addBehaviour(new TickerBehaviour(this, GameConfig.PERCEPTION_REFRESH) {

			@Override
			protected void onTick() {
				updatePerception();
			}
		});
	}
	
	protected abstract void updatePerception();
	
	protected void updateLocalPerception(Floor info) {
		if (perception == null)
			perception = info;
		else
			perception.mergeWith(info);
	}
	
	public void sendPerception(Floor perception, AID receiver) {
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.setContent("perception");
		msg.addReceiver(receiver);
		try {
			msg.setContentObject(perception);
			send(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void receivePerception() {
		MessageTemplate mt = MessageTemplate.MatchContent("perception");
		ACLMessage msg = receive(mt);
		if (msg != null) {
			try {
				Object info = msg.getContentObject();
				if (info instanceof Floor) 
					perception.mergeWith((Floor) info);
			} catch (UnreadableException e) {
				e.printStackTrace();
			}
		}
	}
	
	public String getTeam() {
		if (masterAID == null)
			return "NONE";
		return masterAID.getLocalName();
	}
	
	protected AID getTeamDF() {
		return new AID(getTeam()+"-df", AID.ISLOCALNAME);
	}

	protected void register(DFAgentDescription desc, boolean deletePrevious) {
		try {
			if (deletePrevious)
				DFService.deregister(this, getTeamDF());
			DFService.register(this, getTeamDF(), desc);
		} catch (FIPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected DFAgentDescription[] search(ServiceDescription sd) {
		DFAgentDescription desc = new DFAgentDescription();
		desc.addServices(sd);
		try {
			return DFService.search(this, getTeamDF(), desc);
		} catch (FIPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new DFAgentDescription[1];
	}
	
	protected void setTeam(String team) {
		masterAID = new AID(team, AID.ISLOCALNAME);
	}

	public Floor getPerception() {
		return perception;
	}

}
