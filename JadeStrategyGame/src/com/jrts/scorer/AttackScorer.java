package com.jrts.scorer;

import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import com.jrts.agents.MasterAI;
import com.jrts.agents.Soldier;
import com.jrts.common.AgentStatus;

public class AttackScorer extends GoalScorer {

	public AttackScorer(final MasterAI masterAI) {
		super(masterAI);
		
		addRule(new Rule() {
			
			@Override
			public double value() {
				DFAgentDescription desc = new DFAgentDescription();
				ServiceDescription unitType = new ServiceDescription();
				unitType.setType(Soldier.class.getSimpleName());
				ServiceDescription status = new ServiceDescription();
				status.setType(AgentStatus.FREE);
				desc.addServices(unitType);
				desc.addServices(status);
				int freeSoldiers = masterAI.search(desc).length;
				return freeSoldiers*10;
			}
		});
	}

	

}
