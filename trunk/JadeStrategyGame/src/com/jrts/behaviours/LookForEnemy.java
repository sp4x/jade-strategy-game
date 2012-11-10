package com.jrts.behaviours;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.util.Logger;

import com.jrts.agents.Unit;
import com.jrts.environment.Cell;
import com.jrts.environment.CellType;

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
		Unit unit = (Unit) myAgent;
		int row = unit.getPosition().getRow();
		int col = unit.getPerception().getCols();
		int sight = unit.getSight();
		for (int i = row - sight; i < row + sight; i++) {
			for (int j = col - sight; j < col + sight; j++) {
				check(unit.getPerception().get(i, j));
			}
		}
	}

	private void check(Cell cell) {
		Unit unit = (Unit) myAgent;
		// TODO: E se celltype è WORKER?
		if (cell.getType() == CellType.SOLDIER && !unit.isFriend(cell.getId()))
			doSomething();
	}

	private void doSomething() {
		
	}
}
