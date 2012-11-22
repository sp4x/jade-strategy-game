package com.jrts.behaviours;
import junit.framework.Assert;

import org.junit.Test;

import com.jrts.agents.Unit;
import com.jrts.agents.Worker;
import com.jrts.environment.Cell;
import com.jrts.environment.CellType;
import com.jrts.environment.Floor;
import com.jrts.environment.Position;
import com.jrts.environment.World;


public class FollowPathTest {
	
	
	@Test
	// -++++--
	// -------
	// ---x---
	// -------
	// -++++--
	public void test1() {
		Unit[] agents = {
			new Worker("a1", new Position(0, 1)),
			new Worker("a2", new Position(0, 2)),
			new Worker("a3", new Position(0, 3)),
			new Worker("a4", new Position(0, 4)),
			new Worker("a5", new Position(4, 1)),
			new Worker("a6", new Position(4, 2)),
			new Worker("a7", new Position(4, 3)),
			new Worker("a8", new Position(4, 4)),
		};
		Position target = new Position(2, 3);
		simulate(5, 7, agents, target);
	}
	
	@Test
	// -------
	// ----+--
	// ----++-
	// -x-----
	// -------
	public void test2() {
		Unit[] agents = {
			new Worker("a1", new Position(1, 4)),
			new Worker("a2", new Position(2, 4)),
			new Worker("a3", new Position(2, 5)),
		};
		simulate(5, 7, agents, new Position(3, 1));
	}
	

	@Test
	// -------
	// ---x---
	// --+----
	// -+-----
	// x------
	public void test3() {
		Unit[] agents = {
			new Worker("a1", new Position(3, 1)),
			new Worker("a2", new Position(2, 2)),
		};
		Position[] targets = {
			new Position(1, 3),
			new Position(4, 0)
		};
		simulate(5, 7, agents, targets);
	}
	
	public void simulate(int rows, int cols, Unit[] agents, Position target) {
		Position[] targets = new Position[agents.length];
		for (int i = 0; i < targets.length; i++) {
			targets[i] = target;
		}
		simulate(rows, cols, agents, targets);
	}
	
	public void simulate(int rows, int cols, Unit[] agents, Position[] targets) {
		World.create(rows, cols, 0);
		Floor map = World.getInstance().getSnapshot();
		
		FollowPathBehaviour[] list = new FollowPathBehaviour[agents.length];
		for (int i = 0; i < agents.length; i++) {
			Unit unit = agents[i];
			World.getInstance().addUnit(unit.getPosition(), unit.getId(), unit);
			map.set(targets[i], new Cell(CellType.OBSTACLE));
			list[i] = new FollowPathBehaviour(unit, targets[i], 5, map);
		}
		
		simulate(list);
		
		boolean success = true;
		for (int i = 0; i < agents.length; i++) {
			Position p = agents[i].getPosition();
			if (!p.isNextTo(targets[i])) {
				agents[i].logger.severe("wrong position of agent " + i + ": " + p);
				success = false;
			}
		}
		Assert.assertTrue(success);

	}
	
	public void simulate(UnitBehaviour[] list) {
		boolean allDone = false;
		while (!allDone) {
			allDone = true;
			for (UnitBehaviour b : list) {
				if (!b.done()) {
					allDone = false;
					b.myAction();
				}
			}
		}
	}

}
