package com.jrts.scorer;

import com.jrts.agents.MasterAI.Nature;
import com.jrts.agents.Soldier;
import com.jrts.common.AgentStatus;
import com.jrts.environment.MasterPerception;

public class ExplorationScorer extends GoalScorer {

	public ExplorationScorer(final Nature nature,
			final MasterPerception perception) {
		super(nature, perception);

		addRule(new Rule() {

			@Override
			public double value() {
				return (MAX_SCORE * 0.75)
						- perception.getWorldMap().exploredPercentage();
			}
		});

		addRule(new Rule() {

			@Override
			public double value() {
				if (perception.isAlertNoMoreResources())
					return MAX_SCORE * 0.75;
				return MIN_SCORE;
			}
		});

		addRule(new Rule() {

			@Override
			public double value() {
				double score = MAX_SCORE / (double) perception.getNumTeams();
				int knownCityCenters = perception.getWorldMap()
						.getKnownCityCenters().size();
				int difference = perception.getNumTeams() - knownCityCenters;
				return score * difference;
			}
		});

		addRule(new Rule() {

			@Override
			public double value() {
				switch (nature) {
				case AGGRESSIVE:
					return MAX_SCORE * 0.75;
				case AVERAGE:
					return MAX_SCORE * 0.25;
				default:
					return 0.0;
				}
			}
		});

		addRule(new Rule() {

			@Override
			public double value() {
				return perception.getTeamDF().searchByUnitStatus(Soldier.class,
						AgentStatus.FREE).length * (MAX_SCORE/4);
			}
		});
	}

}
