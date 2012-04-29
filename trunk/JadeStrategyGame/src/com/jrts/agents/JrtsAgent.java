package com.jrts.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;

import com.jrts.environment.Floor;

public abstract class JrtsAgent extends Agent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	AID masterAID;
	Floor perception;
	
	protected abstract void updatePerception();
	
	protected void updateLocalPerception(Floor info) {
		if (perception == null)
			perception = info;
		else
			perception.mergeWith(info);
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
	
	protected void setTeam(String team) {
		masterAID = new AID(team, AID.ISLOCALNAME);
	}

	public Floor getPerception() {
		return perception;
	}

}
