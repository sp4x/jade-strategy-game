package com.jrts.agents;

import com.jrts.environment.Floor;

import jade.core.Agent;

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

	protected void setTeam(String team) {
		this.team = team;
	}

	public Floor getPerception() {
		return perception;
	}

}
