package com.jrts.behaviours;

import jade.core.behaviours.TickerBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.util.Iterator;

import com.jrts.agents.GoalBasedAI;
import com.jrts.agents.Unit;
import com.jrts.common.GameConfig;
import com.jrts.common.UnitTable;

public class UpdateUnitTable extends TickerBehaviour {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	UnitTable table;

	String unitTypeName;

	public UpdateUnitTable(GoalBasedAI agent, Class<? extends Unit> clazz) {
		super(agent, GameConfig.UNIT_TABLE_REFRESH_TIME);
		this.table = agent.getUnitTable();
		this.unitTypeName = clazz.getSimpleName();
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected void onTick() {
		table.clear();
		DFAgentDescription desc = new DFAgentDescription();
		ServiceDescription unitType = new ServiceDescription();
		unitType.setType(unitTypeName);
		DFAgentDescription[] results = ((GoalBasedAI) myAgent).search(desc);
		for (int i = 0; i < results.length; i++) {
			desc = results[i];
			Iterator it = desc.getAllServices();
			it.next(); //basic service
			if (it.hasNext()) {
				ServiceDescription currentStatus = (ServiceDescription) it.next();
				table.put(desc.getName(), currentStatus.getType());
			}
		}
	}

}
