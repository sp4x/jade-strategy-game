package com.jrts.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.jrts.environment.Cell;
import com.jrts.environment.CellType;
import com.jrts.environment.Direction;
import com.jrts.environment.Floor;
import com.jrts.environment.Position;

public class PathfindingTest {

	// + start
	// X obstacle
	// * end pos
	// - free pos

	@Test
	// +--*x
	public void test1() {
		Floor f = new Floor(1, 5);
		f.set(0, 4, new Cell(CellType.OBSTACLE));
		Position start = new Position(0, 0);
		Position target = new Position(0, 4);
		List<Direction> path = Utils.calculatePath(f, start, target);
		Position end = start.followPath(path);
		assertEquals(new Position(0, 3), end);
	}

	@Test
	// ----*
	// +--xx
	// ----*
	public void test2() {
		Floor f = new Floor(3, 5);
		f.set(1, 4, new Cell(CellType.OBSTACLE));
		f.set(1, 3, new Cell(CellType.OBSTACLE));
		Position start = new Position(1, 0);
		Position target = new Position(1, 4);
		List<Direction> path = Utils.calculatePath(f, start, target);
		Position end = start.followPath(path);
		assertTrue(end.isNextTo(target));
	}

	@Test
	// -----
	// --xx-
	// +-x*-
	// --xx-
	// -----
	public void test3() {
		Floor f = new Floor(5, 5);
		Position[] obstacles = { new Position(1, 2), new Position(1, 3),
				new Position(2, 2), new Position(3, 2), new Position(3, 3) };
		for (int i = 0; i < obstacles.length; i++) {
			f.set(obstacles[i], new Cell(CellType.OBSTACLE));
		}
		Position start = new Position(2, 0);
		Position target = new Position(2, 3);
		List<Direction> path = Utils.calculatePath(f, start, target);
		Position end = start.followPath(path);
		assertEquals(target, end);
	}

}
