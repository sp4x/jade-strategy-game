package com.jrts.environment;


public enum Cell {
	FREE,
	WOOD,
	FOOD,
	UNIT,
	BUILDING,
	UNKNOWN;
	
	String id;
	int energy = Integer.MAX_VALUE;
	
	public String getId() {
		return id;
	}
	public int getEnergy() {
		return energy;
	}
}
