package com.jrts.agents;

import jade.core.behaviours.WakerBehaviour;

import com.jrts.common.GameConfig;


public class MilitaryAI extends GoalBasedAI {
	private static final long serialVersionUID = 9114684864072759345L;

	int soldierCounter = 0;
	
	@Override
	protected void setup() {
		super.setup();
		
		// TODO che cazzo deve fare la milaryAI?
		
		addBehaviour(new WakerBehaviour(this, 15000) {
			private static final long serialVersionUID = 1746608629262055814L;

			@Override
			protected void handleElapsedTimeout() {
				if (resourcesContainer.isThereEnoughFood(GameConfig.SOLDIER_FOOD_COST) && 
						resourcesContainer.isThereEnoughWood(GameConfig.SOLDIER_WOOD_COST) ) {
					
					resourcesContainer.removeFood(GameConfig.SOLDIER_FOOD_COST);
					resourcesContainer.removeWood(GameConfig.SOLDIER_WOOD_COST);
					unitFactory.trainUnit(Soldier.class);
					soldierCounter++;
				}
			}
		});
	}

	@Override
	protected void updatePerception() {
	}

	@Override
	public void onGoalsChanged() {
		// TODO Auto-generated method stub
		
	}
}
