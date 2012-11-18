package com.jrts.scorer;

import com.jrts.common.ResourcesContainer;
import com.jrts.environment.WorldMap;

public class ResourceScorer extends GoalScorer {

	public ResourceScorer(final WorldMap worldMap,
			final ResourcesContainer resourcesContainer) {
		super(worldMap, resourcesContainer);
		
		addRule(new Rule() {
			
			@Override
			public double value() {
				double sum = resourcesContainer.getFood() + resourcesContainer.getWood();
				double avg = sum/2;
				return GoalScorer.MAX_SCORE*100/avg;
			}
		});
	}
	
	

}
