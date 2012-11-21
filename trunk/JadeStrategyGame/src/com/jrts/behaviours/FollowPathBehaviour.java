package com.jrts.behaviours;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

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
	
	public Level logLevel = Level.FINE;

	List<Direction> list;
	Unit unit;
	private Position goal;
	int remainingAttempts;

	Floor floor = null;

	public FollowPathBehaviour(Unit unit, Position goal, int remainingAttempts, Floor worldMap) {
		super(true);// high priority
		this.unit = unit;
		this.goal = goal;
		this.remainingAttempts = remainingAttempts;
		this.list = new ArrayList<Direction>();
		this.floor = worldMap;

		calculatePath();
	}

	
	public FollowPathBehaviour(Unit unit, Position goal) {
		this(unit, goal, GameConfig.UNIT_MOVING_ATTEMPTS, unit.requestMap());
	}

	private void calculatePath() {
		Position start = unit.getPosition();
		this.list = Utils.calculatePath(floor, start, goal);
	}
	
	@Override
	public void myAction() {
		unit.spendTime();

		// se non riesco a spostarmi ricalcolo il path
		if (!list.isEmpty()) {
			Direction d = list.remove(0);
			if (!unit.move(d)) {
				Position destination = unit.getPosition().step(d);
				if (remainingAttempts > 0) { // solo se ho ancora tentativi a disposizione
					floor.set(destination, new Cell(CellType.OBSTACLE));
					unit.logger.log(logLevel, unit.getId() + ":Need path recalculation");
					remainingAttempts--;
					calculatePath();
				} else {
					list.clear();
				}

			} else {
				remainingAttempts = GameConfig.UNIT_MOVING_ATTEMPTS;
			}
		}
	}

	@Override
	public boolean done() {
		return list.isEmpty();
	}

}
