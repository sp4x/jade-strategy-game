package com.jrts.messages;

import java.io.Serializable;
import java.util.HashMap;

import com.jrts.environment.Direction;
import com.jrts.environment.Position;

public class Order implements Serializable {

	private static final long serialVersionUID = 1L;

	private String nextStatus;
	private HashMap<String, Object> parameters;
	
	public static final String POSITION = "position";
	public static final String DIRECTION = "direction";
	public static final String DISTANCE = "_distance";
	
	public Order() {}
	
	public Order(String nextStatus) {

		this.nextStatus = nextStatus;
		this.parameters = new HashMap<String, Object>();
	}

	public String getNextStatus() {
		return nextStatus;
	}

	public void setOrder(String order) {
		this.nextStatus = order;
	}

	public Direction getDirection() {
		return (Direction)parameters.get(DIRECTION);
	}

	public void setDirection(Direction direction) {
		this.parameters.put(DIRECTION, direction);
	}
	
	public int getDistance() {
		return (Integer)parameters.get(DISTANCE);
	}

	public void setDistance(int distance) {
		this.parameters.put(DISTANCE, distance);
	}

	public Position getPosition() {
		return (Position)parameters.get(POSITION);
	}

	public void setPosition(Position position) {
		this.parameters.put(POSITION, position);
	}
}