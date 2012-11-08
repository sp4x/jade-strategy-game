package com.jrts.common;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.jrts.environment.Cell;
import com.jrts.environment.CellType;
import com.jrts.environment.Direction;
import com.jrts.environment.Floor;
import com.jrts.environment.Position;

public class PathfindingTest {
	
	//+ start
	//X obstacle
	//* end pos
	//- free pos

	@Test
	//+--*x
	public void test1() {
		Floor f = new Floor(1, 5);
		f.set(0, 4, new Cell(CellType.OBSTACLE));
		Position start = new Position(0, 0);
		Position target = new Position(0, 4);
		List<Direction> path = Utils.calculatePath(f, start, target, true);
		Position end = start.followPath(path);
		assertEquals(new Position(0, 3), end);
	}
	
	@Test
	//----*
	//+--xx
	//----*
	public void test2() {
		Floor f = new Floor(3, 5);
		f.set(1, 4, new Cell(CellType.OBSTACLE));
		f.set(1, 3, new Cell(CellType.OBSTACLE));
		Position start = new Position(1, 0);
		Position target = new Position(1, 4);
		List<Direction> path = Utils.calculatePath(f, start, target, true);
		Position end = start.followPath(path);
		assertTrue(end.equals(new Position(0, 4)) || end.equals(new Position(2, 4)));
	}

}
