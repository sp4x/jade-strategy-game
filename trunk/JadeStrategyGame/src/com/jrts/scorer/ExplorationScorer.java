package com.jrts.scorer;

import com.jrts.agents.Soldier;
import com.jrts.agents.MasterAI.Nature;
import com.jrts.common.AgentStatus;
import com.jrts.common.TeamDF;
import com.jrts.environment.MasterPerception;
import com.jrts.scorer.GoalScorer.Rule;

public class ExplorationScorer extends GoalScorer {

	public ExplorationScorer(final Nature nature,
			final MasterPerception perception) {
		super(nature, perception);

		addRule(new Rule() {

			@Override
			public double value() {
				double exploredPercentage = perception.getWorldMap().exploredPercentage();
				return MAX_SCORE*(100.0-exploredPercentage)/100.0;
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
		
		//higher when there are many free soldiers
		addRule(new Rule() {
			@Override
			public double value() {
				TeamDF teamDF = perception.getTeamDF();
				int freeSoldiers = teamDF.searchByUnitStatus(Soldier.class,
						AgentStatus.FREE).length;
				// int watingSoldiers = teamDF.searchByUnitStatus(Soldier.class,
				// AgentStatus.WAIT_TO_FIGHT).length;

				// return (freeSoldiers + watingSoldiers) * 10;
				return (freeSoldiers) * 10;
			}
		});

		
	}

}
