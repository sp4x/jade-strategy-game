package com.jrts.environment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.jrts.common.ThreadMonitor;

public class WorldTest {

	boolean movement1complete;
	boolean movement2complete;

	@Test
	public void concurrency() {
		World.create(2, 2, 0);
		World w = World.getInstance();
		final Position p1 = new Position(0, 0);
		final Position p2 = new Position(1, 0);
		w.floor.set(p1, new Cell(CellType.SOLDIER));
		w.floor.set(p2, new Cell(CellType.SOLDIER));

		Thread moveW1 = new Thread() {

			@Override
			public void run() {
				movement1complete = World.getInstance().move(p1,
						Direction.RIGHT);
			}
		};
		Thread moveW2 = new Thread() {

			@Override
			public void run() {
				movement2complete = World.getInstance().move(p2,
						Direction.RIGHT_UP);
			}
		};
		moveW1.start();
		moveW2.start();
		while (moveW1.isAlive() || moveW2.isAlive())
			try {
				Thread.sleep(10);
				ThreadMonitor.getInstance().sendNotifyAll();
			} catch (InterruptedException e) {
				e.printStackTrace();
				fail();
			}
		boolean success = (movement1complete && !movement2complete)
				|| (!movement1complete && movement2complete);
		assertTrue(success);
	}

	@Test
	public void perception() {
		World.create(10, 10, 0);
		Position center = new Position(5, 5);
		Perception perception = World.getInstance().getPerception(center, 1);
		int seen = 0;
		for (int row = 0; row < World.getInstance().getRows(); row++) {
			for (int col = 0; col < World.getInstance().getCols(); col++) {
				seen += perception.get(row, col).isUnknown() ? 0 : 1;
			}
		}
		assertEquals(9, seen);
	}

	@Test
	public void near() {
		World.create(10, 10, 0);
		Position center = new Position(5, 5);
		for (int i = 0; i < 10; i++) {
			Position p = World.getInstance().near(center, 2, 4);
			boolean success = (p.row >= 5 + 2 || p.row <= 5 - 2)
					&& (p.col >= 5 + 2 || p.col <= 5 - 2);
			assertTrue(success);
		}
	}

}
