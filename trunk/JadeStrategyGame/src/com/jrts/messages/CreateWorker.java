package com.jrts.messages;

import jade.util.leap.Serializable;

public class CreateWorker implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1475481700008094468L;
	int x;
	int y;
	
	public CreateWorker(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
}
