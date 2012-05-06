package com.jrts.environment;

import java.io.Serializable;

public class Cell implements Serializable {
	CellType type;
	String id;
	int resourceEnergy = Integer.MAX_VALUE;
	
	public Cell(CellType t) {
		type = t;
	}

	public String getId() {
		return id;
	}
	
	public CellType getType() {
		return type;
	}

	public int getResourceEnergy() {
		return resourceEnergy;
	}
}
