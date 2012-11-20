package com.jrts.scorer;

import com.jrts.environment.MasterPerception;

public class ExplorationScorer extends GoalScorer {

	public ExplorationScorer(final MasterPerception perception) {
		super(perception);
		
		addRule(new Rule() {
			
			@Override
			public double value() {
				return 100.0 - perception.getWorldMap().exploredPercentage();
			}
		});
	}
	
	

}
