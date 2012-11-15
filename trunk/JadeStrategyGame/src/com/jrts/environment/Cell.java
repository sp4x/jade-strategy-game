package com.jrts.environment;

import java.io.Serializable;

import com.jrts.O2Ainterfaces.IUnit;

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
		id = null;
		unit = null;
	}
	
	public Cell(CellType t, int energy) {
		type = t;
		id = null;
		unit = null;
		resourceEnergy = energy;
	}
	
	public Cell(CellType type, String id) {
		super();
		this.type = type;
		this.id = id;
		this.unit = null;
	}

	public Cell(String id, IUnit unit, CellType type) {
		super();
		this.type = type;
		this.id = id;
		this.unit = unit;
	}
	
	public Cell(Cell cell) {
		this.type = cell.type;
		this.id = cell.id;
		this.unit = cell.unit;
		this.resourceEnergy = cell.resourceEnergy;
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

	public void setType(CellType type) {
		this.type = type;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setUnit(IUnit unit) {
		this.unit = unit;
	}

	public void setResourceEnergy(int resourceEnergy) {
		this.resourceEnergy = resourceEnergy;
	}
}
