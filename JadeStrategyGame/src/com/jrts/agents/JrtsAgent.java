package com.jrts.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;

import java.util.logging.Logger;

import com.jrts.common.GameConfig;

@SuppressWarnings("serial")
public abstract class JrtsAgent extends Agent {

	public final Logger logger = Logger.getLogger(Agent.class.getName());
	
	private String team;

	public JrtsAgent() {
		//this.logger.setLevel(Level.WARNING);
	}
	
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

	/**
	 * updates the agent perception
	 * 
	 */
	protected abstract void updatePerception();


	public String getTeamName() {
		return team;
	}

	/**
	 * gets the AID of the team directory facilitator
	 */
	protected AID getTeamDF() {
		return new AID(getTeamName() + "-df", AID.ISLOCALNAME);
	}

	/**
	 * register this agent description to the team directory facilitator
	 * 
	 * @param desc
	 *            the agent description
	 * @param deletePrevious
	 *            deregister any previous description associated to the agent
	 */
	protected void register(DFAgentDescription desc, boolean deletePrevious) {
		try {
			if (deletePrevious)
				DFService.deregister(this, getTeamDF());
			DFService.register(this, getTeamDF(), desc);
		} catch (FIPAException e) {
			e.printStackTrace();
		}
	}

	public DFAgentDescription[] search(DFAgentDescription desc) {
		try {
			return DFService.search(this, getTeamDF(), desc);
		} catch (FIPAException e) {
			e.printStackTrace();
		}
		return new DFAgentDescription[0];
	}

	protected void setTeamName(String team) {
		this.team = team;
	}

}
