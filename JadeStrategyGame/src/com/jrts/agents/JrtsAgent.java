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
	
	
	String team;
	Floor perception;
	
	protected abstract void updatePerception();
	
	protected void updateLocalPerception(Floor info) {
		if (perception == null)
			perception = info;
		else
			perception.mergeWith(info);
	}
	
	public String getTeam() {
		return team;
	}
	
	protected AID getMasterAID() {
		return new AID(team, AID.ISLOCALNAME);
	}
	
	protected AID getTeamDF() {
		return new AID(team+"-df", AID.ISLOCALNAME);
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
		this.team = team;
	}

	public Floor getPerception() {
		return perception;
	}

}
