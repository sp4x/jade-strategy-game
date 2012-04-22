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
	int damage = 0;
	
	public String getId() {
		return id;
	}
	public int getEnergy() {
		return energy;
	}
	public int getDamage() {
		return damage;
	}
}
