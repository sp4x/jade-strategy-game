package com.jrts.messages;

import java.io.Serializable;
import java.util.HashMap;

import com.jrts.environment.Direction;

public class Order implements Serializable {

	private static final long serialVersionUID = 1L;

	private String order;
	private HashMap<String, Object> parameters;
	
	public static final String PATROL_DIRECTION = "patrol_direction";
	public static final String PATROL_DISTANCE = "patrol_distance";
	
	public Order() {}
	
	public Order(String order) {

		this.order = order;
		this.parameters = new HashMap<String, Object>();
	}

	/*
	public Order(String order, HashMap<String, Object> parameters) {
		this.order = order;
		this.parameters = parameters;
	}
	*/
	
	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public Direction getPatrolDirection() {
		return (Direction)parameters.get(PATROL_DIRECTION);
	}

	public void setPatrolDirection(Direction direction) {
		this.parameters.put(PATROL_DIRECTION, direction);
	}
	
	public int getPatrolDistance() {
		return (Integer)parameters.get(PATROL_DISTANCE);
	}

	public void setPatrolDistance(int distance) {
		this.parameters.put(PATROL_DISTANCE, distance);
	}
}
