package com.jrts.behaviours;

import jade.core.behaviours.TickerBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.util.Iterator;

import com.jrts.agents.MilitaryAI;
import com.jrts.agents.Soldier;
import com.jrts.common.GameConfig;

public class UpdateSoldiersTable extends TickerBehaviour {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	MilitaryAI agent;
	
	public UpdateSoldiersTable(MilitaryAI agent) {
		super(agent, GameConfig.SOLDIERS_TABLE_REFRESH_TIME);
		this.agent = agent;
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected void onTick() {
		agent.getSoldierMap().clear();
		DFAgentDescription desc = new DFAgentDescription();
		ServiceDescription unitType = new ServiceDescription();
		unitType.setType(Soldier.class.getSimpleName());
		DFAgentDescription[] results = agent.search(desc);
		for (int i = 0; i < results.length; i++) {
			desc = results[i];
			Iterator it = desc.getAllServices();
			it.next(); //basic service
			if (it.hasNext()) {
				ServiceDescription currentStatus = (ServiceDescription) it.next();
				agent.getSoldierMap().put(desc.getName(), currentStatus.getType());
			}
		}
	}

}
