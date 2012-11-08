package com.jrts.behaviours;

import java.util.ArrayList;

import com.jrts.agents.Unit;
import com.jrts.behaviours.structure.BaseBehaviour;
import com.jrts.common.GameConfig;
import com.jrts.common.Utils;
import com.jrts.environment.Cell;
import com.jrts.environment.CellType;
import com.jrts.environment.Direction;
import com.jrts.environment.Floor;
import com.jrts.environment.Position;

@SuppressWarnings("serial")
public class FollowPathBehaviour extends BaseBehaviour {

	ArrayList<Direction> list;
	Unit unit;
	private int goalRow, goalCol;
	int remainingAttempts;
	boolean tolerance;

	Floor worldCachedCopy = null;

	public FollowPathBehaviour(Unit unit, int goalRow, int goalCol,
			int remainingAttempts, Floor cachedCopy) {
		super(true);// high priority
		this.unit = unit;
		this.goalRow = goalRow;
		this.goalCol = goalCol;
		this.remainingAttempts = remainingAttempts;
		this.list = new ArrayList<Direction>();
		this.worldCachedCopy = cachedCopy;

		// se prima invocazione del behaviour
		if (remainingAttempts == GameConfig.UNIT_MOVING_ATTEMPTS
				|| worldCachedCopy == null) {
			setWorldCachedCopy(unit.requestMap());
		}

		Position start = unit.getPosition();
		this.list = Utils.calculatePath(getWorldCachedCopy(),
				start, new Position(goalRow, goalCol), true);
		
		unit.logger.info("path: " + list);
	}

	public FollowPathBehaviour(Unit unit, int goalRow, int goalCol,
			int remainingAttempts) {
		this(unit, goalRow, goalCol, remainingAttempts, null);
	}

	public void baseAction() {
		unit.spendTime();

		// se non riesco a spostarmi ricalcolo il path
		if (!list.isEmpty()) {
			Direction d = list.remove(0);
			if (!unit.move(d)) {
				Position destination = unit.getPosition().step(d);
				worldCachedCopy.set(destination.getRow(), destination.getCol(),
						new Cell(CellType.OBSTACLE));
				unit.logger.info(unit.getLocalName()
						+ ":Need path recalculation");
				list.clear();
				if (remainingAttempts > 0) // solo se ho ancora tentativi a
											// disposizione
					unit.addBehaviour(new FollowPathBehaviour(unit, goalRow,
							goalCol, remainingAttempts - 1,
							worldCachedCopy));
			} else
				remainingAttempts = GameConfig.UNIT_MOVING_ATTEMPTS;
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

	public int getGoalRow() {
		return goalRow;
	}

	public void setGoalRow(int goalRow) {
		this.goalRow = goalRow;
	}

	public int getGoalCol() {
		return goalCol;
	}

	public void setGoalCol(int goalCol) {
		this.goalCol = goalCol;
	}

	public Floor getWorldCachedCopy() {
		return worldCachedCopy;
	}

	public void setWorldCachedCopy(Floor cachedCopy) {
		this.worldCachedCopy = cachedCopy;
	}
}
