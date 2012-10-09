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
import com.jrts.environment.CellType;
import com.jrts.environment.Floor;
import com.jrts.environment.World;

@SuppressWarnings("serial")
public abstract class JrtsAgent extends Agent {
	
	private String team;
	private Floor perception;
	
	@Override
	protected void setup() {
		super.setup();
		World w = World.getInstance();
		if (w != null) {
			perception = new Floor(w.getRows(), w.getCols(), CellType.UNKNOWN);
			addBehaviour(new TickerBehaviour(this, GameConfig.PERCEPTION_REFRESH) {
	
				@Override
				protected void onTick() {
					perception = updatePerception();
				}
			});
		}
	}
	
	/**
	 * updates the agent perception
	 * @return the updated perception
	 */
	protected abstract Floor updatePerception();
	
	
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
	
	public Floor receivePerception() {
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
		return perception;
	}
	
	public String getTeam() {
		return team;
	}
	
	
	/**
	 * gets the AID of the team directory facilitator
	 */
	protected AID getTeamDF() {
		return new AID(getTeam()+"-df", AID.ISLOCALNAME);
	}

	
	/**
	 * register this agent description to the team directory facilitator
	 * @param desc the agent description
	 * @param deletePrevious deregister any previous description associated to the agent
	 */
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
		this.team = team;
	}

	public Floor getPerception() {
		return perception;
	}

}
