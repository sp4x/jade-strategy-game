package com.jrts.scorer;

import com.jrts.agents.MasterAI;
import com.jrts.agents.Soldier;
import com.jrts.common.AgentStatus;
import com.jrts.common.TeamDF;

public class AttackScorer extends GoalScorer {

	public AttackScorer(final MasterAI masterAI) {
		super(masterAI);
		
		addRule(new Rule() {
			
			@Override
			public double value() {
				TeamDF teamDF = masterAI.getTeamDF();
				int freeSoldiers = 
						teamDF.searchByUnitStatus(Soldier.class, AgentStatus.FREE).length;
				return freeSoldiers*10;
			}
		});
		
		//TODO valutare avvistamenti nemici
	}

	

}
