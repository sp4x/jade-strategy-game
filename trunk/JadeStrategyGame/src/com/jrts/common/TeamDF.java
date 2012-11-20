package com.jrts.common;

import com.jrts.agents.Soldier;
import com.jrts.agents.Unit;
import com.jrts.agents.Worker;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class TeamDF {

	Agent agent;
	AID aid;

	public TeamDF(Agent agent, String teamName) {
		this.agent = agent;
		this.aid = new AID(teamName + "-df", AID.ISLOCALNAME);

	}

	public DFAgentDescription[] search(DFAgentDescription desc) {
		try {
			return DFService.search(agent, aid, desc);
		} catch (FIPAException e) {
			e.printStackTrace();
		}
		return new DFAgentDescription[0];
	}

	public DFAgentDescription[] searchByUnitType(Class<? extends Unit> clazz) {
		DFAgentDescription desc = new DFAgentDescription();
		ServiceDescription unitType = new ServiceDescription();
		unitType.setType(clazz.getSimpleName());
		desc.addServices(unitType);
		return search(desc);
	}

	public DFAgentDescription[] searchByUnitStatus(Class<? extends Unit> clazz,
			String status) {
		DFAgentDescription desc = new DFAgentDescription();
		ServiceDescription unitType = new ServiceDescription();
		unitType.setType(clazz.getSimpleName());
		desc.addServices(unitType);
		ServiceDescription statusDesc = new ServiceDescription();
		statusDesc.setType(status);
		desc.addServices(statusDesc);
		return search(desc);
	}

	public void registerUnit(Unit unit) {
		registerUnit(unit, null);
	}

	public void registerUnit(Unit unit, String status) {
		DFAgentDescription agentDescription = new DFAgentDescription();
		agentDescription.setName(unit.getAID());
		ServiceDescription basicService = new ServiceDescription();
		basicService.setName(unit.getAID().getName());
		basicService.setType(unit.getClass().getSimpleName());
		agentDescription.addServices(basicService);
		if (status != null) {
			ServiceDescription statusDesc = new ServiceDescription();
			statusDesc.setName(unit.getAID().getName());
			statusDesc.setType(status);
			agentDescription.addServices(statusDesc);
		}

		try {
			DFService.deregister(unit, aid);
		} catch (FIPAException e) {
		}

		try {
			DFService.register(unit, aid, agentDescription);
		} catch (FIPAException e) {
			unit.logger.severe("error registering unit");
		}
	}

	public int countUnits() {
		return searchByUnitType(Worker.class).length
				+ searchByUnitType(Soldier.class).length;
	}

	public String getLocalName() {
		return aid.getLocalName();
	}

}
