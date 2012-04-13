package com.jrts.environment;

import static org.junit.Assert.*;
import logic.Direction;

import org.junit.Before;
import org.junit.Test;

import agents.Worker;

public class WorldTest {
	
	Worker w1;
	Worker w2;
	
	boolean w1complete;
	boolean w2complete;

	@Test
	public void test() {
		World.create(2, 2, 0);
		World w = World.getInstance();
		w.addObject(Cell.UNIT, 0, 0);
		w.addObject(Cell.UNIT, 1, 0);
		w1 = new Worker();
		w1.setPosition(0, 0);
		w2 = new Worker();
		w2.setPosition(1, 0);
		
		
		Thread moveW1 = new Thread() {
			
			@Override
			public void run() {
				w1complete = World.getInstance().move(w1, Direction.RIGHT );
			}
		};
		Thread moveW2 = new Thread() {
			
			@Override
			public void run() {
				w2complete = World.getInstance().move(w2, Direction.RIGHT_UP);
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

}
