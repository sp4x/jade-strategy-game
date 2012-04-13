package com.jrts.environment;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import agents.Worker;

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
	public void nextTo() {
		
	}

}
