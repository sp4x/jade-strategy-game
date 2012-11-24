package com.jrts.scorer;

import com.jrts.agents.MasterAI.Nature;
import com.jrts.environment.MasterPerception;
import com.jrts.environment.Position;
import com.jrts.messages.EnemySighting;

public class DefenceScorer extends GoalScorer {

	public DefenceScorer(final Nature nature, final MasterPerception perception) {
		super(nature, perception);

		addRule(new Rule() {

			@Override
			public double value() {
				int numEnemySighted = 0;
				int numThreats = 0;
						
				int bound = 10;
				if(nature == Nature.DEFENSIVE)
					bound = 15;
				else if(nature == Nature.AGGRESSIVE)
					bound = 5;
				
				for (EnemySighting es : perception.getEnemySightings())
					if(perception.getCityCenter().distance(es.getSightingPosition()) < bound)
							numEnemySighted += es.getSoldierNumber();
				
				for (Position pos : perception.getThreats())
					if(perception.getCityCenter().distance(pos) < bound)
							numThreats += 2;
				
				return (numEnemySighted + numThreats) * 10;
			}
		});

		addRule(new Rule() {

			@Override
			public double value() {

				if(nature == Nature.DEFENSIVE)
					return MAX_SCORE;
				if(nature == Nature.AVERAGE)
					return MAX_SCORE / 3 * 2;

				return MAX_SCORE /  2;
			}
		});
	}

}
