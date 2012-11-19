package com.jrts.behaviours;

import jade.core.behaviours.TickerBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.util.Iterator;

import com.jrts.agents.GoalBasedAI;
import com.jrts.agents.Unit;
import com.jrts.common.GameConfig;
import com.jrts.common.TeamDF;
import com.jrts.common.UnitTable;

public class UpdateUnitTable extends TickerBehaviour {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	UnitTable table;

	Class<? extends Unit> unitType;

	public UpdateUnitTable(GoalBasedAI agent, Class<? extends Unit> clazz) {
		super(agent, GameConfig.UNIT_TABLE_REFRESH_TIME);
		this.table = agent.getUnitTable();
		this.unitType = clazz;
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected void onTick() {
		table.clear();
		TeamDF teamDF = ((GoalBasedAI) myAgent).getTeamDF();
		DFAgentDescription[] results = teamDF.searchByUnitType(unitType);
		for (int i = 0; i < results.length; i++) {
			DFAgentDescription desc = results[i];
			Iterator it = desc.getAllServices();
			it.next(); //basic service
			if (it.hasNext()) {
				ServiceDescription currentStatus = (ServiceDescription) it.next();
				table.put(desc.getName(), currentStatus.getType());
			}
		}
	}

}
