package com.jrts.agents;

import jade.core.AID;
import jade.core.behaviours.WakerBehaviour;

import java.util.ArrayList;


public class MilitaryAI extends GoalBasedAI {
	private static final long serialVersionUID = 9114684864072759345L;

	ArrayList<AID> soldierList = new ArrayList<AID>();
	
	@Override
	protected void setup() {
		super.setup();
		
		// TODO che cazzo deve fare la milaryAI?
		
		addBehaviour(new WakerBehaviour(this, 15000) {
			private static final long serialVersionUID = 1746608629262055814L;

			@Override
			protected void handleElapsedTimeout() {
				unitFactory.trainUnit(Soldier.class);
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
