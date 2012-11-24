package com.jrts.messages;

import jade.util.leap.Serializable;

public class Death implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public static final int SOLDIER = 0;
	public static final int WORKER = 1;
	
	int unitType;

	public Death(int unitType) {
		super();
		this.unitType = unitType;
	}

	public int getUnitType() {
		return unitType;
	}

	public void setUnitType(int unitType) {
		this.unitType = unitType;
	}
	
	

}
