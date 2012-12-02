package com.jrts.scorer;

import com.jrts.agents.MasterAI.Nature;
import com.jrts.agents.Soldier;
import com.jrts.common.AgentStatus;
import com.jrts.common.TeamDF;
import com.jrts.environment.MasterPerception;

public class ExplorationScorer extends GoalScorer {

	public ExplorationScorer(final Nature nature,
			final MasterPerception perception) {
		super(nature, perception);

		addRule(new Rule() {
			@Override
			public double value() {
				double exploredPercentage = perception.getWorldMap().exploredPercentage();
				return 100.0-exploredPercentage;
			}
		});

		addRule(new Rule() {
			@Override
			public double value() {
				if (perception.isAlertNoMoreResources())
					return MAX_SCORE;
				
				return MAX_SCORE / 2;
			}
		});

		addRule(new Rule() {
			@Override
			public double value() {
				double score = MAX_SCORE / (double) perception.getNumTeams();
				int knownCityCenters = perception.getWorldMap()
						.getKnownCityCenters().size();

				int difference = perception.getNumTeams() - knownCityCenters;
				return (score * (difference + 1));
			}
		});
		
		addRule(new Rule() {
			@Override
			public double value() {
				switch (nature) {
					case AGGRESSIVE:
						return MAX_SCORE;
					case AVERAGE:
						return MAX_SCORE * 0.75;
					default:
						return MAX_SCORE /2;
				}
			}
		});
		
		//higher when there are many free soldiers
		addRule(new Rule() {
			@Override
			public double value() {
				TeamDF teamDF = perception.getTeamDF();
				int freeSoldiers = teamDF.searchByUnitStatus(Soldier.class,
						AgentStatus.FREE).length;

				return freeSoldiers * 15;
			}
		});
	}
}