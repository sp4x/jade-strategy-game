package com.jrts.environment;

public enum CellType {
	FREE("Cell"),
	WOOD("Wood"),
	FOOD("Food"),
	WORKER("Worker"), 
	SOLDIER("Soldier"),
	CITY_CENTER("Town center"),
	MEETING_POINT("Meeting Point"),
	UNKNOWN("Unknown"),
	OBSTACLE("Obstacle"),
	EXPLOSION("Explosion");
	
	String text;
	
	private CellType(String text) {
		this.text = text;
	}
	
	public String toString() {
		return text;
	};
}
