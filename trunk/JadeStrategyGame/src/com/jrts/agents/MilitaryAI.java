package com.jrts.agents;

import jade.core.AID;

import java.util.ArrayList;


public class MilitaryAI extends GoalBasedAI {
	private static final long serialVersionUID = 9114684864072759345L;

	ArrayList<AID> soldierList = new ArrayList<AID>();
	
	@Override
	protected void setup() {
		super.setup();
		
		// TODO che cazzo deve fare la milaryAI?
	}

	@Override
	protected void updatePerception() {
	}
}
