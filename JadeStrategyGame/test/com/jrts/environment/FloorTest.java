package com.jrts.environment;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FloorTest {

	@Test
	//+---
	//-*--
	//--x-
	//----
	public void test1() {
		Floor f = new Floor(4, 4);
		Position p = f.nextTo(new Position(0, 0), new Position(2, 2), CellType.FREE, 3);
		assertEquals(new Position(1, 1), p);
	}
	
	public void test2() {
		Floor f = new Floor(40, 40);
		Position p = f.nextTo(new Position(0, 0), new Position(38, 38), CellType.FREE, 5);
		assertEquals(new Position(37, 37), p);
	}

}
