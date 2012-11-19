package com.jrts.scorer;

import com.jrts.agents.MasterAI;

public class ExplorationScorer extends GoalScorer {

	public ExplorationScorer(final MasterAI masterAI) {
		super(masterAI);
		
		addRule(new Rule() {
			
			@Override
			public double value() {
				return 100.0 - masterAI.getWorldMap().exploredPercentage();
			}
		});
	}
	
	

}
