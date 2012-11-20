package com.jrts.scorer;

import com.jrts.agents.MasterAI.Nature;
import com.jrts.environment.MasterPerception;

public class ExplorationScorer extends GoalScorer {

	public ExplorationScorer(final Nature nature, final MasterPerception perception) {
		super(nature, perception);
		
		addRule(new Rule() {
			
			@Override
			public double value() {
				return 100.0 - perception.getWorldMap().exploredPercentage();
			}
		});
	}
	
	

}
