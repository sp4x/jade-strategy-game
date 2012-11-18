package com.jrts.scorer;

import com.jrts.common.ResourcesContainer;
import com.jrts.environment.WorldMap;

public class ExplorationScorer extends GoalScorer {

	public ExplorationScorer(final WorldMap worldMap,
			ResourcesContainer resourcesContainer) {
		super(worldMap, resourcesContainer);
		
		
		addRule(new Rule() {
			
			@Override
			public double value() {
				return 100.0 - worldMap.exploredPercentage();
			}
		});
		
		
	}

}
