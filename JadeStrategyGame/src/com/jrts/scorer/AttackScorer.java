package com.jrts.scorer;

import com.jrts.agents.MasterAI.Nature;
import com.jrts.agents.Soldier;
import com.jrts.common.AgentStatus;
import com.jrts.common.TeamDF;
import com.jrts.environment.MasterPerception;

public class AttackScorer extends GoalScorer {

	public AttackScorer(final Nature nature, final MasterPerception perception) {
		super(nature, perception);
		
		addRule(new Rule() {
			
			@Override
			public double value() {
				TeamDF teamDF = perception.getTeamDF();
				int freeSoldiers = 
						teamDF.searchByUnitStatus(Soldier.class, AgentStatus.FREE).length;
				return freeSoldiers*10;
			}
		});
		
		//TODO valutare avvistamenti nemici
	}

	

}
