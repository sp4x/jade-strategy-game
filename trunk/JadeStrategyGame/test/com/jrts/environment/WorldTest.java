package com.jrts.environment;

import static org.junit.Assert.*;

import org.junit.Test;

import com.jrts.agents.Worker;


public class WorldTest {
	
	Worker w1;
	Worker w2;
	
	boolean w1complete;
	boolean w2complete;

	@Test
	public void concurrency() {
		World.create(2, 2, 0);
		World w = World.getInstance();
		w1 = new Worker(new Position(0, 0));
		w2 = new Worker(new Position(1, 0));
		w.addUnit("w1", w1.getPosition());
		w.addUnit("w2", w2.getPosition());
		
		
		Thread moveW1 = new Thread() {
			
			@Override
			public void run() {
				w1complete = World.getInstance().move(w1.getPosition(), Direction.RIGHT );
			}
		};
		Thread moveW2 = new Thread() {
			
			@Override
			public void run() {
				w2complete = World.getInstance().move(w2.getPosition(), Direction.RIGHT_UP);
			}
		};
		moveW1.start();
		moveW2.start();
		while (moveW1.isAlive() || moveW2.isAlive())
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				fail();
			}
		boolean success = (w1complete && !w2complete) || (!w1complete && w2complete);
		assertTrue(success);
	}
	
	
	@Test
	public void perception() {
		World.create(10, 10, 0);
		Position centre = new Position(5, 5);
		Floor floor = World.getInstance().getPerception(centre, 1);
		int seen = 0;
		for (int row = 0; row < floor.rows; row++) {
			for (int col = 0; col < floor.cols; col++) {
				seen += floor.get(row, col) == Cell.UNKNOWN ? 0 : 1;
			}
		}
		assertEquals(5, seen);
	}

}
