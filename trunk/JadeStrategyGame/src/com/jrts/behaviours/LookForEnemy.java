package com.jrts.behaviours;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;

import com.jrts.agents.Unit;
import com.jrts.environment.Cell;
import com.jrts.environment.Floor;

public class LookForEnemy extends TickerBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LookForEnemy(Agent a, long period) {
		super(a, period);
	}

	@Override
	protected void onTick() {
		Floor perception = ((Unit) myAgent).getPerception();
		for (int i = 0; i < perception.getRows(); i++) {
			for (int j = 0; j < perception.getCols(); j++) {
				check(perception.get(i, j));
			}
		}
	}

	private void check(Cell cell) {
		Unit unit = (Unit) myAgent;
		if (cell == Cell.UNIT && !unit.isFriend(cell.getId()))
			doSomething();
	}

	private void doSomething() {
		System.out.println("found enemy!");
	}
}
