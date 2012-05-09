package com.jrts.environment;

import java.io.Serializable;

import com.jrts.api.IUnit;

public class Cell implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	CellType type;
	String id;
	IUnit unit;
	int resourceEnergy = 0;
	
	public Cell(CellType t) {
		type = t;
	}

	public String getId() {
		return id;
	}
	
	public CellType getType() {
		return type;
	}

	public IUnit getUnit() {
		return unit;
	}

	public int getResourceEnergy() {
		return resourceEnergy;
	}
}
