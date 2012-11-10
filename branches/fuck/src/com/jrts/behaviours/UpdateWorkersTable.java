package com.jrts.behaviours;

import jade.core.behaviours.TickerBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.util.Iterator;

import com.jrts.agents.ResourceAI;
import com.jrts.agents.Worker;
import com.jrts.common.GameConfig;

public class UpdateWorkersTable extends TickerBehaviour {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	ResourceAI agent;

	public UpdateWorkersTable(ResourceAI agent) {
		super(agent, GameConfig.WORKERS_TABLE_REFRESH_TIME);
		this.agent = agent;
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected void onTick() {
		agent.getWorkersMap().clear();
		DFAgentDescription desc = new DFAgentDescription();
		ServiceDescription unitType = new ServiceDescription();
		unitType.setType(Worker.class.getSimpleName());
		DFAgentDescription[] results = agent.search(desc);
		for (int i = 0; i < results.length; i++) {
			desc = results[i];
			Iterator it = desc.getAllServices();
			it.next(); //basic service
			if (it.hasNext()) {
				ServiceDescription currentStatus = (ServiceDescription) it.next();
				agent.getWorkersMap().put(desc.getName(), currentStatus.getType());
			}
		}
	}

}
