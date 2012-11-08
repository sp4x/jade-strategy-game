package com.jrts.environment;

import static org.junit.Assert.*;

import org.junit.Test;

import com.jrts.O2Ainterfaces.IUnit;
import com.jrts.agents.Worker;
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
		w.getFloor().set(p1.row, p2.col, new Cell(CellType.SOLDIER));
		w.getFloor().set(p1.row, p2.col, new Cell(CellType.SOLDIER));
		
		Thread moveW1 = new Thread() {
			
			@Override
			public void run() {
				movement1complete = World.getInstance().move(p1, Direction.RIGHT );
			}
		};
		Thread moveW2 = new Thread() {
			
			@Override
			public void run() {
				movement2complete = World.getInstance().move(p2, Direction.RIGHT_UP);
			}
		};
		moveW1.start();
		moveW2.start();
		while (moveW1.isAlive() || moveW2.isAlive())
			try {
				Thread.sleep(1000);
				ThreadMonitor.getInstance().sendNotifyAll();
			} catch (InterruptedException e) {
				e.printStackTrace();
				fail();
			}
		boolean success = (movement1complete && !movement2complete) || (!movement1complete && movement2complete);
		assertTrue(success);
	}
	
	
	@Test
	public void perception() {
		World.create(10, 10, 0);
		Position center = new Position(5, 5);
		Floor floor = World.getInstance().getPerception(center, 1);
		int seen = 0;
		for (int row = 0; row < floor.rows; row++) {
			for (int col = 0; col < floor.cols; col++) {
				seen += floor.get(row, col).getType() != CellType.UNKNOWN ? 1 : 0;
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
			boolean success = (p.row>=5+2 || p.row<=5-2) && (p.col>=5+2 || p.col<=5-2);
			assertTrue(success);
		}
	}

}
