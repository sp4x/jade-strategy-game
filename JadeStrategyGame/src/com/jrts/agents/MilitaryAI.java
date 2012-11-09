package com.jrts.agents;

import jade.core.AID;
import jade.core.behaviours.WakerBehaviour;

import com.jrts.behaviours.UpdateSoldiersTable;
import com.jrts.common.AgentStatus;
import com.jrts.common.GameConfig;
import com.jrts.common.SoldiersMap;


public class MilitaryAI extends GoalBasedAI {
	private static final long serialVersionUID = 9114684864072759345L;

	SoldiersMap soldierMap = new SoldiersMap();
	int soldierCounter = 0;
	
	@Override
	protected void setup() {
		super.setup();
		
		// TODO che cazzo deve fare la milaryAI?
		
		addBehaviour(new UpdateSoldiersTable(this));

		addBehaviour(new WakerBehaviour(this, 5000) {
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
		
		addBehaviour(new WakerBehaviour(this, 10000) {
			private static final long serialVersionUID = 1746608629262055814L;

			@Override
			protected void handleElapsedTimeout() {

				MilitaryAI mAI = (MilitaryAI)this.myAgent;
				AID soldier = mAI.getSoldierMap().getFreeSoldier();
				if(soldier != null){
					changeAgentStatus(soldier, AgentStatus.PATROLING);
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

	public SoldiersMap getSoldierMap() {
		return soldierMap;
	}
}
