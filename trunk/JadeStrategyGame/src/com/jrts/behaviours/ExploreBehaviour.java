package com.jrts.behaviours;

import java.util.LinkedList;

import com.jrts.agents.Soldier;
import com.jrts.common.Utils;
import com.jrts.environment.Direction;
import com.jrts.environment.Position;

public class ExploreBehaviour extends UnitBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Soldier soldier;
	Position cityCenter;

	/**
	 * 
	 * @param soldier
	 */
	public ExploreBehaviour(Soldier soldier) {
		super(false);

		this.soldier = soldier;
		this.cityCenter = soldier.requestCityCenterPosition();
	}

	@Override
	public void myAction() {

		LinkedList<Direction> dir = new LinkedList<Direction>();
		dir.add(Direction.LEFT_UP);
		dir.add(Direction.LEFT_DOWN);
		dir.add(Direction.RIGHT_UP);
		dir.add(Direction.RIGHT_DOWN);

		dir.remove(Utils.getMapAnglePosition(this.soldier.getPosition()));

		boolean going = false;
		for (int i = 0; i < 10 && !going; i++) {
			Direction dirToGo = dir.get(Utils.random.nextInt(3));
			Position posToGo = Utils.getRandomUnknownCellPosition(this.soldier.requestMap(), dirToGo);

			if (posToGo != null) {
				this.soldier.goThere(posToGo);
				going = true;
			}
		}

	}

	@Override
	public boolean done() {
		return false;
	}
}
