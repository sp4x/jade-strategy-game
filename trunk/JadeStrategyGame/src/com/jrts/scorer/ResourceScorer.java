package com.jrts.scorer;

import com.jrts.agents.MasterAI;
import com.jrts.common.ResourcesContainer;

public class ResourceScorer extends GoalScorer {

	public ResourceScorer(final MasterAI masterAI) {
		super(masterAI);
		addRule(new Rule() {
			
			@Override
			public double value() {
				ResourcesContainer resourcesContainer = masterAI.getResourcesContainer();
				double sum = resourcesContainer.getFood() + resourcesContainer.getWood();
				double avg = sum/2;
				return GoalScorer.MAX_SCORE*100/avg;
			}
		});
	}
	
	
	
	

}
