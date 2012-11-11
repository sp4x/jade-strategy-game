package com.jrts.behaviours;

import java.util.ArrayList;
import java.util.List;

import com.jrts.agents.Unit;
import com.jrts.common.GameConfig;
import com.jrts.common.Utils;
import com.jrts.environment.Cell;
import com.jrts.environment.CellType;
import com.jrts.environment.Direction;
import com.jrts.environment.Floor;
import com.jrts.environment.Position;

@SuppressWarnings("serial")
public class FollowPathBehaviour extends UnitBehaviour {

	List<Direction> list;
	Unit unit;
	private Position goal;
	int remainingAttempts;
	boolean tolerance;

	Floor worldCachedCopy = null;

	public FollowPathBehaviour(Unit unit, Position goal, int remainingAttempts, Floor cachedCopy) {
		super(true);// high priority
		this.unit = unit;
		this.goal = goal;
		this.remainingAttempts = remainingAttempts;
		this.list = new ArrayList<Direction>();
		this.worldCachedCopy = cachedCopy;

		// se prima invocazione del behaviour
		if (remainingAttempts == GameConfig.UNIT_MOVING_ATTEMPTS || worldCachedCopy == null) {
			setWorldCachedCopy(unit.requestMap());
		}

		calculatePath();
	}

	public FollowPathBehaviour(Unit unit, Position goal, int remainingAttempts) {
		this(unit, goal, remainingAttempts, null);
	}

	private void calculatePath() {
		Position start = unit.getPosition();
		this.list = Utils.calculatePath(getWorldCachedCopy(),
				start, goal);
		
		unit.logger.info(unit.getId() + ":path: " + list);

	}

	public void action() {
		unit.spendTime();

		// se non riesco a spostarmi ricalcolo il path
		if (!list.isEmpty()) {
			Direction d = list.remove(0);
			if (!unit.move(d)) {
				Position destination = unit.getPosition().step(d);
				worldCachedCopy.set(destination, new Cell(CellType.OBSTACLE));
//				unit.logger.info(unit.getId() + ":Need path recalculation");
				if (remainingAttempts > 0) { // solo se ho ancora tentativi a disposizione
					// unit.addBehaviour(new FollowPathBehaviour(unit, goalRow, goalCol, remainingAttempts - 1, worldCachedCopy));
					calculatePath();
				} else {
//					unit.logger.severe(unit.getId() + " no remaining attempts, failed");
					list.clear();
				}

			} else {
				remainingAttempts = GameConfig.UNIT_MOVING_ATTEMPTS;
			}
		}
	}

	@Override
	public boolean done() {
		// Se il path � stato eseguito correttamente ma la posizione raggiunta
		// non � quella giusta
		// significa che � stata fatta un'approssimazione della posizione
		// obiettivo
		// if(list.isEmpty() && !unit.getPosition().equals(new Position(goalRow,
		// goalCol)))
		// unit.goThere(new Position(goalRow, goalCol));
		return list.isEmpty();
	}

	public Floor getWorldCachedCopy() {
		return worldCachedCopy;
	}

	public void setWorldCachedCopy(Floor cachedCopy) {
		this.worldCachedCopy = cachedCopy;
	}
}
