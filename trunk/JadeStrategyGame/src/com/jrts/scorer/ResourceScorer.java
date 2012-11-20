package com.jrts.scorer;

import com.jrts.agents.MasterAI.Nature;
import com.jrts.common.ResourcesContainer;
import com.jrts.environment.MasterPerception;

public class ResourceScorer extends GoalScorer {

	public ResourceScorer(final Nature nature, final MasterPerception perception) {
		super(nature, perception);
		addRule(new Rule() {
			
			@Override
			public double value() {
				ResourcesContainer resourcesContainer = perception.getResourcesContainer();
				double sum = resourcesContainer.getFood() + resourcesContainer.getWood();
				double avg = sum/2;
				return GoalScorer.MAX_SCORE*100/avg;
			}
		});
	}
	
	
	
	

}
