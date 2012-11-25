package com.jrts.scorer;

import com.jrts.agents.MasterAI.Nature;
import com.jrts.agents.Soldier;
import com.jrts.common.AgentStatus;
import com.jrts.common.GameConfig;
import com.jrts.common.TeamDF;
import com.jrts.environment.MasterPerception;

public class AttackScorer extends GoalScorer {

	public AttackScorer(final Nature nature, final MasterPerception perception) {
		super(nature, perception);
		
		addRule(new Rule() {
			@Override
			public double value() {
				TeamDF teamDF = perception.getTeamDF();
				int freeSoldiers = teamDF.searchByUnitStatus(Soldier.class, AgentStatus.FREE).length;
				int watingSoldiers = teamDF.searchByUnitStatus(Soldier.class, AgentStatus.WAIT_TO_FIGHT).length;
				
				return (freeSoldiers + watingSoldiers) * 10;
			}
		});
		

		addRule(new Rule() {
			@Override
			public double value() {

				int food = perception.getResourcesContainer().getFood();
				int wood = perception.getResourcesContainer().getWood();
				
				double foodPercentage = (food / GameConfig.SOLDIER_FOOD_COST*10) * 100;
				double woodPercentage = (wood / GameConfig.SOLDIER_WOOD_COST*10) * 100;
				
				return (foodPercentage + woodPercentage) / 2;
			}
		});
		
		//TODO valutare avvistamenti nemici
		addRule(new Rule() {
			@Override
			public double value() {

				if(nature == Nature.AGGRESSIVE)
					return MAX_SCORE;
				if(nature == Nature.AVERAGE)
					return MAX_SCORE / 3 * 2;

				return MAX_SCORE /  2;
			}
		});
	}

	

}
